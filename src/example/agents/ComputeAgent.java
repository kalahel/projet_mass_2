package example.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.Function;
import utils.Normalizer;

import java.io.IOException;

public class ComputeAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(this.getName());
        serviceDescription.setType("compute");
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
            Normalizer.normalizePrint(this.getClass().toString(), this.getName(), "Agent registered");

        } catch (FIPAException e) {
            e.printStackTrace();
        }
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    Normalizer.normalizePrint(this.getClass().toString(), this.getAgent().getName(), "Function received");
                    try {
                        Function f = (Function) msg.getContentObject();
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContentObject("" + f.eval());
                        send(reply);
                    } catch (UnreadableException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
