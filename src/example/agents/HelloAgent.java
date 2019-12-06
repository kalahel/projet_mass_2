package example.agents;

import example.behaviours.HelloBehaviour;
import jade.core.Agent;

public class HelloAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
        int n = 0;
        Object[] args = getArguments();
        // To handle empty arguments for GUI and command line
        if (args != null && args.length != 0) {
            n = Integer.parseInt((String) args[0]);
        }
        System.out.println("Hi, I'm a new agent, this is working !");
        this.addBehaviour(new HelloBehaviour(n));
    }
}
