package edu.uga.cs.costsettler;

import java.util.Date;
import java.util.List;

public class Purchase {
    private List<Item> itemsPurchased;
    private double cost;
    private String user;
    private String datePurchased;
    private String key;

    public Purchase(List<Item> itemsPurchased, double cost, String user, String datePurchased) {
        this.itemsPurchased = itemsPurchased;
        this.cost = cost;
        this.user = user;
        this.datePurchased = datePurchased;
    }

    public Purchase() {
        cost = 0;
        user = "";
        datePurchased = "";
        key = "";
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
        if (itemsPurchased != null && itemsPurchased.size() > 0) {
            return "Purchase of " + itemsPurchased + " item(s) made on " + datePurchased +
                    " for " + cost + " dollars by " + user;
        }
        return "";


    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
