package example.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class EchoAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" - " + myAgent.getLocalName() + " <- " + msg.getContent());
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(" Pong ");
                    send(reply);
                }
            }
        });
    }
}
