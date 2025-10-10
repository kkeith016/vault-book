package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions {
    //date|time|description|vendor|amount
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transactions(LocalDate date, LocalTime time, String description, double amount, String vendor) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.amount = amount;
        this.vendor = vendor;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public String toString() {
        String formattedTime = String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
        return date + "|" + formattedTime + "|" + description + "|" + vendor + "|" + amount;
    }
}
