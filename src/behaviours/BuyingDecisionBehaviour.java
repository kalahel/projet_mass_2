package behaviours;

import agents.ProducerConsumer;
import jade.core.behaviours.CyclicBehaviour;

public class BuyingDecisionBehaviour extends CyclicBehaviour {
    @Override
    public void action() {
        if (((ProducerConsumer) this.getAgent()).getConsumingStock() <= 5) {
            this.getAgent().addBehaviour(new BuyingBehaviour(this.getAgent()));
        }
    }
}
