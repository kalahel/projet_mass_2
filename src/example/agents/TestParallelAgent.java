package example.agents;

import example.behaviours.SumBehaviors;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import utils.Function;
import utils.MyFunction;
import utils.Normalizer;

import java.io.IOException;

public class TestParallelAgent extends Agent {
    private double min = 1.0;
    private double max = Math.exp(1);
    private double delta = 0.00000001;
    private double result = 0.0;

    @Override
    protected void setup() {
        super.setup();
        long start = System.currentTimeMillis();
        Function function = new MyFunction(min, max, delta);
        double result = function.eval();
        long finish = System.currentTimeMillis();

        long timeElapsed = finish - start;
        Normalizer.normalizePrint(this.getClass().toString(), this.getName(),
                "Result : " + result +
                        " Time elapsed : " + timeElapsed + "ms");
        // Service search
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("compute");
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] foundAgents = DFService.search(this, dfAgentDescription);
            Normalizer.normalizePrint(this.getClass().toString(), this.getName(),
                    "Found " + foundAgents.length +
                            " Capable of computing function");
            if (foundAgents.length > 0) {
                double step = (max - min) / foundAgents.length;
                for (int i = 0; i < foundAgents.length; i++) {
                    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                    message.addReceiver(foundAgents[i].getName());
                    message.setContentObject(new MyFunction(min + i * step, min + (i + 1) * step, delta));
                    send(message);
                }
                // FIXME THIS not finished
                addBehaviour(new SumBehaviors(4));
            }


        } catch (FIPAException | IOException e) {
            e.printStackTrace();
        }
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
