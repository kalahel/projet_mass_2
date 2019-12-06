package example.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Normalizer;

public class PongAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage message = receive();
                if (message != null) {
                    Normalizer.normalizePrint(this.getClass().toString(), this.getAgent().getName(), message.getContent());
                    ACLMessage messageReply = message.createReply();
                    messageReply.setContent("PONG");
                    messageReply.setPerformative(ACLMessage.INFORM);
                    send(messageReply);
                }
            }
        });
    }
}
