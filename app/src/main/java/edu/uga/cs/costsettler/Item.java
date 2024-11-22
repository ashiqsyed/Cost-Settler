package edu.uga.cs.costsettler;

public class Item {
    private double price;
    private String itemName;
    private int quantity;
    private boolean isNeeded;
    private boolean isPurchased;

    public Item(double price, String itemName, int quantity) {
        this.price = price;
        this.itemName = itemName;
        this.quantity = quantity;
        this.isNeeded = false;
        this.isPurchased = false;
    }
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

    public String toString() {
        return "Item: " + itemName + ", price: " + price + ", quantity: " + quantity;
    }
}
