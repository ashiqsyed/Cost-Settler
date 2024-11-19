package edu.uga.cs.costsettler;

public class Item {
    private double price;
    private String itemName;
    private int quantity;
    private boolean isNeeded;
    private boolean isPurchased;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsNeeded() {
        return isNeeded;
    }

    public void setIsNeeded(boolean isNeeded) {
        this.isNeeded = isNeeded;
    }

    public boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }
}
