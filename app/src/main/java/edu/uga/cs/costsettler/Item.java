package edu.uga.cs.costsettler;

public class Item {
    private String itemName;
    private int quantity;
    private String key;

    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Item() {
        this.itemName = "";
        this.quantity = 0;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        return "Item: " + itemName + ", "+ "quantity: " + quantity;
    }
}
