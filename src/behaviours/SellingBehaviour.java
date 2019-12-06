package behaviours;

import agents.ProducerConsumer;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class SellingBehaviour extends CyclicBehaviour {
    @Override
    public void action() {
        sellingRoutine();
    }

    private void sellingRoutine() {
        ACLMessage receivedProposal = this.getAgent().receive();
        if (receivedProposal != null) {
            // Check the type of response
            if (receivedProposal.getPerformative() == ACLMessage.CFP) {
                // Seller try to sell all their stock at once
                double proposedQuantity = ((ProducerConsumer) this.getAgent()).getSellingStock();
                double sellingPrice = ((ProducerConsumer) this.getAgent()).getSellingPrice() * proposedQuantity;
                ACLMessage reply = receivedProposal.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(receivedProposal.getContent() + "," +
                        proposedQuantity + "," +
                        sellingPrice + ',' +
                        this.getAgent().getName());
                this.getAgent().send(reply);
                ((ProducerConsumer) this.getAgent()).agentPrint("Proposition sent to : " + receivedProposal.getContent());
            } else if (receivedProposal.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                ACLMessage reply = receivedProposal.createReply();
                reply.setPerformative(ACLMessage.CONFIRM);
                ((ProducerConsumer) this.getAgent()).setSellingStock(((ProducerConsumer) this.getAgent()).getSellingStock()
                        - Double.parseDouble(receivedProposal.getContent().split(",")[1]));

                ((ProducerConsumer) this.getAgent()).setMoney(((ProducerConsumer) this.getAgent()).getMoney()
                        + Double.parseDouble(receivedProposal.getContent().split(",")[2]));
                reply.setContent(this.getAgent().getName());
                this.getAgent().send(reply);
                ((ProducerConsumer) this.getAgent()).agentPrint("Confirmation sent to : " + receivedProposal.getContent());

            }
        }
    }
}
