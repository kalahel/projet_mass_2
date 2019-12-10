package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class ConsumingBehaviour extends CyclicBehaviour {
    private ProducerConsumer producerConsumerAgent;

    public ConsumingBehaviour(Agent a) {
        super(a);
        producerConsumerAgent = (ProducerConsumer) a;
    }

    @Override
    public void action() {
        if (this.producerConsumerAgent.getConsumingStock() <= ProducerConsumer.CONSUMED_STOCK_PER_TICK) {
            this.producerConsumerAgent.setConsumingStock(0.0);

        }
        else
            this.producerConsumerAgent.setConsumingStock(this.producerConsumerAgent.getConsumingStock() - ProducerConsumer.CONSUMED_STOCK_PER_TICK);
    }
}
