package agents;

import behaviours.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Main agent of this system
 * Capable of selling and buying according to its needs
 * Has a type of resources that it consumes and another that it produces
 */
public class ProducerConsumer extends Agent {
    public static final int MAX_STOCK = 200;
    public static final double CONSUMED_STOCK_PER_TICK = 2;
    public static final int PRODUCTION_RATE = 3;

    private double money;
    private double sellingStock;
    private double consumingStock;
    private double baseSellingPrice;
    private String sellingType;
    private String consumingType;
    private double happiness;
    private boolean isTryingToBuy;

    /**
     * Register to the yellow page and had all the behaviours needed for its lifetime
     */
    @Override
    protected void setup() {
        super.setup();
        this.money = 100;
        this.sellingStock = 10;
        this.consumingStock = 10;
        this.happiness = 1.0;
        this.baseSellingPrice = 2;
        this.isTryingToBuy = false;

        Object[] args = getArguments();
        if (args != null && args.length >= 2) {
            this.sellingType = (String) args[0];
            this.consumingType = (String) args[1];
        } else {
            System.err.println("__ProducerConsumer__ Missing arguments");
        }
        this.agentPrint("Created, Producing : " + this.getSellingType() + "\tConsuming : " + this.getConsumingType());

        // Registering Agent as a producer for produced product
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(this.getName());
        serviceDescription.setType(this.sellingType);
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, dfAgentDescription);
            this.agentPrint("Agent registered");
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        this.addBehaviour(new ProducerBehaviour());
        this.addBehaviour(new ConsumingBehaviour(this));
        this.addBehaviour(new SellingBehaviour(this));
        this.addBehaviour(new BuyingDecisionBehaviour(this));

    }

    /**
     * Compute the appropriate price to sell a unit of its stock
     * When happiness decreases
     * @return price to sell a unit of product
     */
    public double computePriceToSell(){
        return this.happiness * this.baseSellingPrice;
    }

    /**
     * Simple print function to allow all the agent to automatically print their name and class with the desired message
     *
     * @param message String to print
     */
    public void agentPrint(String message) {
        System.out.println("__" +
                this.getClass() +
                "__ : " +
                this.getName() +
                " : " +
                message);
    }

    /**
     * Simple print error function to allow all the agent to automatically print their name and class with the desired message
     *
     * @param message String to print to error chanel
     */
    public void agentPrintError(String message) {
        System.err.println("__" +
                this.getClass() +
                "__ : " +
                this.getName() +
                " : " +
                message);
    }

    /**
     * Clean deregister from yellow page before takeDown
     */
    @Override
    protected void takeDown() {
        this.agentPrint("AGENT REMOVED FROM SYSTEM");
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        super.takeDown();
    }

    public double getMoney() {
        return money;
    }


    public void setMoney(double money) {
        this.money = money;
    }

    public double getSellingStock() {
        return sellingStock;
    }

    public void setSellingStock(double sellingStock) {
        this.sellingStock = sellingStock;
    }

    public double getConsumingStock() {
        return consumingStock;
    }

    public void setConsumingStock(double consumingStock) {
        this.consumingStock = consumingStock;
    }

    public double getBaseSellingPrice() {
        return baseSellingPrice;
    }


    public String getSellingType() {
        return sellingType;
    }


    public String getConsumingType() {
        return consumingType;
    }


    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public boolean isTryingToBuy() {
        return isTryingToBuy;
    }

    public void setTryingToBuy(boolean tryingToBuy) {
        isTryingToBuy = tryingToBuy;
    }

    @Override
    public String toString() {
        return "ProducerConsumer{" +
                "money=" + money +
                ", sellingStock=" + sellingStock +
                ", consumingStock=" + consumingStock +
                ", baseSellingPrice=" + baseSellingPrice +
                ", sellingType='" + sellingType + '\'' +
                ", consumingType='" + consumingType + '\'' +
                ", happiness=" + happiness +
                '}';
    }
}
