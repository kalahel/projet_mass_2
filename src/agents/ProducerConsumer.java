package agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class ProducerConsumer extends Agent {
    public static final int MAX_STOCK = 200;

    private double money;
    private double sellingStock;
    private double consumingStock;
    private double sellingPrice;
    private double buyingPrice;
    private String sellingType;
    private String consumingType;
    private double happiness;


    @Override
    protected void setup() {
        super.setup();
        this.money = 100;
        this.sellingStock = 10;
        this.consumingStock = 10;
        this.happiness = 1.0;
        this.buyingPrice = 10;
        this.sellingPrice = 12;

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

    }


    public void agentPrint(String message) {
        System.out.println("__" +
                this.getClass() +
                "__ : " +
                this.getName() +
                " : " +
                message);
    }

    public void agentPrintError(String message) {
        System.err.println("__" +
                this.getClass() +
                "__ : " +
                this.getName() +
                " : " +
                message);
    }

    public double getMoney() {
        return money;
    }

    public static int getMaxStock() {
        return MAX_STOCK;
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

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getSellingType() {
        return sellingType;
    }

    public void setSellingType(String sellingType) {
        this.sellingType = sellingType;
    }

    public String getConsumingType() {
        return consumingType;
    }

    public void setConsumingType(String consumingType) {
        this.consumingType = consumingType;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }
}
