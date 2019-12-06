package example.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import utils.Normalizer;

public class PingAgent extends Agent {
    private static String className = "PingAgent";
    @Override
    protected void setup() {
        super.setup();
        try {
            SearchConstraints constraints = new SearchConstraints();
            constraints.setMaxResults((-1L));
            AMSAgentDescription[] agentDescriptions = AMSService.search(this, new AMSAgentDescription(), constraints);
            for (AMSAgentDescription amsAgentDescription : agentDescriptions) {
                Normalizer.normalizePrint(this.getClass().toString(),this.getName(),"Agent(s) found : " + amsAgentDescription.getName());
            }
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            for (AMSAgentDescription description : agentDescriptions) {
                if (!this.getAID().equals(description.getName()))
                    message.addReceiver(description.getName());
            }
            message.setContent("PING");
            send(message);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage message = receive();
                if(message != null) {
                    Normalizer.normalizePrint(this.getClass().toString(),this.getAgent().getName(),"Agent(s) found : " + "Agent name : " + this.myAgent.getLocalName() + ", Received : " + message);

                }

            }
        });
    }
}
