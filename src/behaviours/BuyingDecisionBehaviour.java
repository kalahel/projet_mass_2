package behaviours;

import agents.ProducerConsumer;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Cyclic behaviour, used to add a BuyingBehaviour if the agent needs to restock and is not already trying to buy
 */
public class BuyingDecisionBehaviour extends CyclicBehaviour {
    private ProducerConsumer producerConsumerAgent;
    public BuyingDecisionBehaviour(Agent a) {
        super(a);
        this.producerConsumerAgent = (ProducerConsumer) a;
    }

    @Override
    public void action() {
        if (this.producerConsumerAgent.getConsumingStock() <= 5 && !this.producerConsumerAgent.isTryingToBuy()) {
            this.getAgent().addBehaviour(new BuyingBehaviour(this.producerConsumerAgent));
            this.producerConsumerAgent.setTryingToBuy(true);
        }
    }
}
