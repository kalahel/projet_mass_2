package example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import utils.Normalizer;

public class PingPongAgent extends Agent {
    private int nMax;
    private String futureAgentName;

    @Override
    protected void setup() {
        super.setup();
        nMax = 10;
        futureAgentName = "noName";

        // Exacting creature name and # of iterations
        Object[] args = getArguments();
        if (args != null && args.length != 0) {
            futureAgentName = args[0].toString();
            nMax = Integer.parseInt(args[1].toString());
        }
        Normalizer.normalizePrint(this.getClass().toString(), this.getName(), "Arg name = " + futureAgentName);

        //creating the agent and starting it
        AID creature = new AID(futureAgentName, AID.ISLOCALNAME);
        AgentContainer agentContainer = getContainerController();

        try {
            AgentController agentController = agentContainer.createNewAgent(futureAgentName, "example.agents.PingAgent", null);
            agentController.start();
            Normalizer.normalizePrint(this.getClass().toString(), this.getName(), "Created : " + creature);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        addBehaviour(new SimpleBehaviour() {
            protected int n = 1;
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Message #" + n);
                msg.addReceiver(creature);
                System.out.println();
            }

            @Override
            public boolean done() {
                return false;
            }
        });


    }
}
