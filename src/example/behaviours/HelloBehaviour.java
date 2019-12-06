package example.behaviours;

import jade.core.behaviours.Behaviour;

public class HelloBehaviour extends Behaviour {
    private int n;
    public HelloBehaviour(int n) {
        this.n = n;
    }

    @Override
    public void action() {
        System.out.println("Running behavior, n = " + this.n);
        this.n--;
    }

    @Override
    public boolean done() {
        return n<= 0;
    }
}
