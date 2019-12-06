package example.behaviours;

import jade.core.behaviours.Behaviour;

public class SumBehaviors extends Behaviour {
    private int numberOfState;

    public SumBehaviors(int numberOfState) {
        this.numberOfState = numberOfState;
    }

    @Override
    public void action() {

    }

    @Override
    public boolean done() {
        return false;
    }
}
