package behaviours;

import agents.ProducerConsumer;
import jade.core.behaviours.CyclicBehaviour;

public class ConsumingBehaviour extends CyclicBehaviour {
    @Override
    public void action() {
        if (((ProducerConsumer) this.getAgent()).getConsumingStock() <= ProducerConsumer.CONSUMED_STOCK_PER_TICK)
            ((ProducerConsumer) this.getAgent()).setConsumingStock(0.0);
        else
            ((ProducerConsumer) this.getAgent()).setConsumingStock(((ProducerConsumer) this.getAgent()).getConsumingStock() - ProducerConsumer.CONSUMED_STOCK_PER_TICK);
    }
}
