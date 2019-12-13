package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import utils.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyingBehaviour extends Behaviour {
    private ProducerConsumer producerConsumerAgent;
    private Map<String, DFAgentDescription> pendingReaching;
    private Map<String, Transaction> pendingTransactions;
    private List<ACLMessage> sellerResponses;
    private String bestSellingAgentName;
    private double bestSellingPrice;
    private double bestSellerQuantity;
    private boolean reachingHasBeenDone;
    private boolean isTransactionFinished;
    private int currentState;

    public BuyingBehaviour(Agent a) {
        super(a);
        this.producerConsumerAgent = (ProducerConsumer) a;
        this.pendingReaching = new HashMap<>();
        this.pendingTransactions = new HashMap<>();
        this.sellerResponses = new ArrayList<>();
        this.bestSellingAgentName = "";
        this.bestSellingPrice = 10000;
        this.bestSellerQuantity = 1;
        this.reachingHasBeenDone = false;
        this.isTransactionFinished = false;
        this.currentState = 0;
    }

    @Override
    public void action() {
        if (!this.reachingHasBeenDone) {
            try {
                this.reachForSellers();
//                this.currentState = 1;
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        } else {
            if (!pendingReaching.isEmpty()) {
                this.receiveSellersProposals();
            }
            if (!bestSellingAgentName.equals("") && this.pendingReaching.isEmpty()) {
                this.respondToProposals();
//                this.currentState = 2;
            }
            if (!this.pendingTransactions.isEmpty()) {
                this.receiveConfirmation();
            }
        }

    }

    /**
     * Reach for sellers by looking into the yellow pages.
     * Send Call For Proposal to each of them
     *
     * @throws FIPAException
     */
    private void reachForSellers() throws FIPAException {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(this.producerConsumerAgent.getConsumingType());
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        DFAgentDescription[] foundAgents = DFService.search(this.getAgent(), dfAgentDescription);
        this.producerConsumerAgent.agentPrint("Found " + foundAgents.length +
                " Selling " + this.producerConsumerAgent.getSellingType());
        if (foundAgents.length > 0) {
            ACLMessage message = new ACLMessage(ACLMessage.CFP);

            for (DFAgentDescription foundAgent : foundAgents) {
                message.addReceiver(foundAgent.getName());
                message.setContent(this.getAgent().getName());
                this.pendingReaching.put(foundAgent.getName().getName(), foundAgent);
            }
            this.getAgent().send(message);
        }
        this.reachingHasBeenDone = true;
    }

    /**
     * This method checks for responses from sellers
     * This will be called until all the potential sellers has responded
     * Compute the best offer based on the Selling Price/Quantity ratio
     */
    private void receiveSellersProposals() {
        ACLMessage receivedProposal = this.getAgent().receive();
        if (receivedProposal != null) {
            if (receivedProposal.getPerformative() == ACLMessage.PROPOSE) {
                // Take only responses addressed to this agent
                if (this.getAgent().getName().equals(receivedProposal.getContent().split(",")[0])) {
                    sellerResponses.add(receivedProposal);
                    String buyingQuantity = receivedProposal.getContent().split(",")[1];
                    String buyingPrice = receivedProposal.getContent().split(",")[2];
                    String sellerName = receivedProposal.getContent().split(",")[3];
                    this.producerConsumerAgent.agentPrint("Received proposition from : " + sellerName +
                            "\toffers : " + buyingQuantity + " " + this.producerConsumerAgent.getConsumingType() +
                            "\tfor : " + buyingPrice + "€");
                    // Finding best selling ratio and agent must have enough money
                    if (((Double.parseDouble(buyingPrice) / Double.parseDouble(buyingQuantity)) < (bestSellingPrice / bestSellerQuantity)) &&
                            Double.parseDouble(buyingPrice) < this.producerConsumerAgent.getMoney()) {

                        bestSellingPrice = Double.parseDouble(buyingPrice);
                        bestSellerQuantity = Double.parseDouble(buyingQuantity);
                        bestSellingAgentName = sellerName;
                    }
                    this.pendingReaching.remove(sellerName);
                }
            }
        }
    }

    /**
     * Sends responses to all sellers with Accept or Reject
     * Accept only the best transaction
     * Put the best transaction in a map to save state
     */
    private void respondToProposals() {
        // Responding to all agents
        for (ACLMessage sellerResponse :
                sellerResponses) {
            // Sending accept to best seller agent
            if (sellerResponse.getContent().split(",")[3].equals(bestSellingAgentName)) {
                ACLMessage reply = sellerResponse.createReply();
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reply.setContent(this.getAgent().getName() + "," + bestSellerQuantity + "," + bestSellingPrice + ',' + bestSellingAgentName);
                this.getAgent().send(reply);
                this.pendingTransactions.put(bestSellingAgentName,
                        new Transaction(bestSellingAgentName, bestSellerQuantity, bestSellingPrice));
            }
            // Send refusals to others seller
            else {
                ACLMessage reply = sellerResponse.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                reply.setContent(this.getAgent().getName());
                this.getAgent().send(reply);
            }
        }

    }

    /**
     * Handles the reception of the confirmation from the best seller
     * Update agent money and stock accordingly
     */
    private void receiveConfirmation() {
        // TODO USE SAME PROTOCOL TO AVOID HAVING TO PERSIST INFORMATION
        // TODO USE THIS LIKE A PING/PONG (DO NOT FORGET TO CHANGE SellingBehaviour IN THIS CASE)
        ACLMessage potentialConfirmation = this.getAgent().receive();
        if (potentialConfirmation != null) {
            {
                // Seller sending it's own name
                if (this.pendingTransactions.containsKey(potentialConfirmation.getSender().getName())) {
                    if (potentialConfirmation.getPerformative() == ACLMessage.CONFIRM) {
//                        this.producerConsumerAgent.setMoney(this.producerConsumerAgent.getMoney() - this.pendingTransactions.get(potentialConfirmation.getContent()).getBuyingPrice());
//                        this.producerConsumerAgent.setConsumingStock(this.producerConsumerAgent.getConsumingStock() + this.pendingTransactions.get(potentialConfirmation.getContent()).getBuyingQuantity());
//                        this.producerConsumerAgent.agentPrint("Confirmation received, new state " + this.getAgent().toString());
//                        this.pendingTransactions.remove(potentialConfirmation.getSender().getName());
//                        this.isTransactionFinished = true;

                        this.producerConsumerAgent.setConsumingStock(this.producerConsumerAgent.getConsumingStock() + Double.parseDouble(potentialConfirmation.getContent().split(",")[1]));
                        this.producerConsumerAgent.setMoney(this.producerConsumerAgent.getMoney() - Double.parseDouble(potentialConfirmation.getContent().split(",")[2]));
                        this.producerConsumerAgent.agentPrint("Confirmation received, new state " + this.getAgent().toString());
                        this.pendingTransactions.remove(potentialConfirmation.getSender().getName());
                        this.isTransactionFinished = true;

                    }

                } else {
                    this.producerConsumerAgent.agentPrintError("Confirmation received from agent" + potentialConfirmation.getContent() + " not listed in transaction map");
                }
            }
        }
    }

    /**
     * This behaviour ends when there no more sellers to responds to and no more pending responses
     *
     * @return
     */
    @Override
    public boolean done() {
        if ((this.pendingTransactions.isEmpty() && this.pendingReaching.isEmpty()) || this.isTransactionFinished) {
            this.producerConsumerAgent.setTryingToBuy(false);
            return true;
        } else
            return false;
    }
}
