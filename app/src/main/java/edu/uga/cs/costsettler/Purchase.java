package edu.uga.cs.costsettler;

import java.util.Date;
import java.util.List;

public class Purchase {
    private List<Item> itemsPurchased;
    private double cost;
    private String user;
    private String datePurchased;

    public Purchase(List<Item> itemsPurchased, double cost, String user, String datePurchased) {
        this.itemsPurchased = itemsPurchased;
        this.cost = cost;
        this.user = user;
        this.datePurchased = datePurchased;
    }

    public List<Item> getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(List<Item> itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String toString() {
        return "Purchase of size " + itemsPurchased.size() + " made on " + datePurchased;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }
}
