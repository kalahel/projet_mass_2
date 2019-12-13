package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Consumes resources and reset happiness to 1
 * If their is nothing to consume, decrease the happiness of the agent
 * Happiness is decreased by 10% each time
 */
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
            this.producerConsumerAgent.setHappiness(this.producerConsumerAgent.getHappiness() - (this.producerConsumerAgent.getHappiness() * 0.1));

        } else {
            this.producerConsumerAgent.setConsumingStock(this.producerConsumerAgent.getConsumingStock() - ProducerConsumer.CONSUMED_STOCK_PER_TICK);
            this.producerConsumerAgent.setHappiness(1.0);
        }
    }
}
