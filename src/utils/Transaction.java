package utils;

public class Transaction {
    private String sellerName;
    private double buyingQuantity;
    private double buyingPrice;

    public Transaction(String sellerName, double buyingQuantity, double buyingPrice) {
        this.sellerName = sellerName;
        this.buyingQuantity = buyingQuantity;
        this.buyingPrice = buyingPrice;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public double getBuyingQuantity() {
        return buyingQuantity;
    }

    public void setBuyingQuantity(double buyingQuantity) {
        this.buyingQuantity = buyingQuantity;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }
}
