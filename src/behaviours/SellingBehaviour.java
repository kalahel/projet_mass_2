package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

public class SellingBehaviour extends CyclicBehaviour {
    private ProducerConsumer producerConsumerAgent;
    private int currentState;

    public SellingBehaviour(Agent a) {
        super(a);
        this.producerConsumerAgent = (ProducerConsumer) a;
        this.currentState = 0;
    }

    @Override
    public void action() {
        sellingRoutine();
    }

    private void sellingRoutine() {
        MessageTemplate messageTemplate = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CFP), MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
        ACLMessage receivedProposal = this.getAgent().receive(messageTemplate);
        if (receivedProposal != null) {
            // Check the type of response
            if (receivedProposal.getPerformative() == ACLMessage.CFP) {
                // Seller try to sell a portion of its stock
                double proposedQuantity = this.producerConsumerAgent.getSellingStock() / 100;
                double sellingPrice = this.producerConsumerAgent.getSellingPrice() * proposedQuantity;
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
//                this.currentState = 1;
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
//                this.currentState = 0;
            }
//            else if (receivedProposal.getPerformative() == ACLMessage.REJECT_PROPOSAL && currentState ==1)
//                this.currentState = 0;
        }

    }
}
