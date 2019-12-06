package behaviours;

import agents.ProducerConsumer;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class ProducerBehaviour extends CyclicBehaviour {
    private static final int PRODUCTION_RATE = 3;

    @Override
    public void action() {
        if (((ProducerConsumer) this.getAgent()).getSellingStock() < ProducerConsumer.MAX_STOCK)
            ((ProducerConsumer) this.getAgent()).setSellingStock(((ProducerConsumer) this.getAgent()).getSellingStock() + PRODUCTION_RATE);
    }
}
