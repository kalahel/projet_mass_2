package behaviours;

import agents.ProducerConsumer;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class ProducerBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        if (((ProducerConsumer) this.getAgent()).getSellingStock() < ProducerConsumer.MAX_STOCK)
            ((ProducerConsumer) this.getAgent()).setSellingStock(((ProducerConsumer) this.getAgent()).getSellingStock() + ProducerConsumer.PRODUCTION_RATE);
    }
}
