package com.example.uyashop.Helper;

import com.example.uyashop.Domain.ItemsDomain;

import java.util.ArrayList;
import java.util.Calendar;

public class Order {
    private ArrayList<ItemsDomain> items;
    private double total;
    private String timestamp;
    private String userName;
    private String userEmail;

    public Order(ArrayList<ItemsDomain> items, double total, String timestamp) {
        this.items = items;
        this.total = total;
        this.timestamp = timestamp;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public ArrayList<ItemsDomain> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public static String getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month dimulai dari 0, maka tambahkan 1
        int year = calendar.get(Calendar.YEAR);

        return day + "-" + month + "-" + year;
    }
}
