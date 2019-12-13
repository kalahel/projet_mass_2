package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Cyclic behaviour allowing an agent to be ready to respond to potentials buyers
 */
public class SellingBehaviour extends CyclicBehaviour {
    private ProducerConsumer producerConsumerAgent;

    public SellingBehaviour(Agent a) {
        super(a);
        this.producerConsumerAgent = (ProducerConsumer) a;
    }

    /**
     * Call sellingRoutine()
     */
    @Override
    public void action() {
        sellingRoutine();
    }

    /**
     * Checks for CFP or ACCEPT_PROPOSAL from buyer and respond accordingly
     * It will always try to sell a tenth of its stock, the price for a unit varies depending of the agent's happiness
     */
    private void sellingRoutine() {
        // Template is used to avoid loosing messages
        MessageTemplate messageTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
        ACLMessage receivedProposal = this.getAgent().receive(messageTemplate);
        if (receivedProposal != null) {
            // Check the type of response
            if (receivedProposal.getPerformative() == ACLMessage.CFP) {
                // Seller try to sell a portion of its stock
                double proposedQuantity = this.producerConsumerAgent.getSellingStock() / 10;
                double sellingPrice = this.producerConsumerAgent.computePriceToSell() * proposedQuantity;
                if (proposedQuantity > ProducerConsumer.MAX_STOCK)
                    this.producerConsumerAgent.agentPrintError("Proposed more than theoretical max stock, agent state : \n" + this.getAgent().toString());
                ACLMessage reply = receivedProposal.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(receivedProposal.getContent() + "," +
                        proposedQuantity + "," +
                        sellingPrice + ',' +
                        this.getAgent().getName());
                this.getAgent().send(reply);
                this.producerConsumerAgent.agentPrint("Proposition sent to : " + receivedProposal.getContent() +
                        "\toffers : " + proposedQuantity + " " + this.producerConsumerAgent.getConsumingType() +
                        "\tfor : " + sellingPrice + "€");
            } else if (receivedProposal.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                ACLMessage reply = receivedProposal.createReply();
                reply.setPerformative(ACLMessage.CONFIRM);
                this.producerConsumerAgent.setSellingStock(this.producerConsumerAgent.getSellingStock()
                        - Double.parseDouble(receivedProposal.getContent().split(",")[1]));

                this.producerConsumerAgent.setMoney(this.producerConsumerAgent.getMoney()
                        + Double.parseDouble(receivedProposal.getContent().split(",")[2]));
                reply.setContent(receivedProposal.getContent());
                this.getAgent().send(reply);
                this.producerConsumerAgent.agentPrint("Confirmation sent to : " + receivedProposal.getContent());
            }

        }

    }
}
