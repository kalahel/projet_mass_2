package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import utils.Normalizer;
import utils.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyingBehaviour extends CyclicBehaviour {
    private Map<String, Transaction> pendingTransactions;

    public BuyingBehaviour(Agent a) {
        super(a);
        this.pendingTransactions = new HashMap<>();
    }

    @Override
    public void action() {
        // TODO make sur the behavior is correct even if there is no sellers
        // Looking for a seller
        // Service search
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(((ProducerConsumer) this.getAgent()).getConsumingType());
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] foundAgents = DFService.search(this.getAgent(), dfAgentDescription);
            ((ProducerConsumer) this.getAgent()).agentPrint("Found " + foundAgents.length +
                    " Selling " + (((ProducerConsumer) this.getAgent())).getSellingType());
            if (foundAgents.length > 0) {
                ACLMessage message = new ACLMessage(ACLMessage.CFP);

                for (DFAgentDescription foundAgent : foundAgents) {
                    message.addReceiver(foundAgent.getName());
                    message.setContent(this.getAgent().getName());
                }
                this.getAgent().send(message);

                // Waiting for responses and determining best seller
                String bestSellingAgentName = "";
                double bestSellingPrice = 10000;
                double bestSellerQuantity = 1;
                int pendingResponses = foundAgents.length;
                List<ACLMessage> sellerResponses = new ArrayList<>();

                // Active wait for all the answers
                // TODO MAKE IT NOT ACTIVE
                while (pendingResponses >= 0) {
                    ACLMessage receivedProposal = this.getAgent().blockingReceive();
                    if (receivedProposal != null) {
                        if (receivedProposal.getPerformative() == ACLMessage.PROPOSE) {

                            // Take only responses addressed to this agent
                            if (this.getAgent().getName().equals(receivedProposal.getContent().split(",")[0])) {
                                sellerResponses.add(receivedProposal);
                                String buyingQuantity = receivedProposal.getContent().split(",")[1];
                                String buyingPrice = receivedProposal.getContent().split(",")[2];
                                String sellerName = receivedProposal.getContent().split(",")[3];
                                ((ProducerConsumer) this.getAgent()).agentPrint("Received proposition from : " + sellerName +
                                        "\toffers : " + buyingQuantity +
                                        "\tfor : " + buyingPrice);
                                // Finding best selling ratio and agent must have enough money
                                if (((Double.parseDouble(buyingPrice) / Double.parseDouble(buyingQuantity)) < (bestSellingPrice / bestSellerQuantity)) &&
                                        Double.parseDouble(buyingPrice) < ((ProducerConsumer) this.getAgent()).getMoney()) {

                                    bestSellingPrice = Double.parseDouble(buyingPrice);
                                    bestSellerQuantity = Double.parseDouble(buyingQuantity);
                                    bestSellingAgentName = sellerName;
                                }
                                pendingResponses--;
                            }
                        }
                    }
                }

                // Responding to all agents
                for (ACLMessage sellerResponse :
                        sellerResponses) {
                    // Sending accept to best seller agent
                    if (sellerResponse.getContent().split(",")[3].equals(bestSellingAgentName)) {
                        ACLMessage reply = sellerResponse.createReply();
                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setContent(this.getAgent().getName());
                        this.getAgent().send(reply);
                    }
                    // Send refusals to others seller
                    else {
                        ACLMessage reply = sellerResponse.createReply();
                        reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        reply.setContent(this.getAgent().getName());
                        this.getAgent().send(reply);
                    }
                }
                this.pendingTransactions.put(bestSellingAgentName,
                        new Transaction(bestSellingAgentName, bestSellerQuantity, bestSellingPrice));

            }
            // Wait for confirmation
            ACLMessage potentialConfirmation = this.getAgent().receive();
            if (potentialConfirmation != null) {
                {
                    // Seller sending it's own name
                    if (this.pendingTransactions.containsKey(potentialConfirmation.getContent())) {
                        if (potentialConfirmation.getPerformative() == ACLMessage.CONFIRM) {
                            ((ProducerConsumer) this.getAgent()).setMoney(((ProducerConsumer) this.getAgent()).getMoney() - this.pendingTransactions.get(potentialConfirmation.getContent()).getBuyingPrice());
                            ((ProducerConsumer) this.getAgent()).setConsumingStock(((ProducerConsumer) this.getAgent()).getConsumingStock() + this.pendingTransactions.get(potentialConfirmation.getContent()).getBuyingQuantity());
                            return;
                        }
                    } else {
                        ((ProducerConsumer) this.getAgent()).agentPrint("Confirmation received from agent" + potentialConfirmation.getContent() + " not listed in the map");
                    }
                }
            }
            ((ProducerConsumer) this.getAgent()).agentPrint("No confirmation received, exiting buying behaviour without any purchase");
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

    }
}
