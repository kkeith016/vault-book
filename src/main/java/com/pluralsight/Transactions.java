package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transactions {
    //date|time|description|vendor|amount
    private LocalDate date;
    private LocalTime time;
    private String description;
    private double amount;

    public Transactions(LocalDate date, LocalTime time, String description, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.amount = amount;
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
        return "Transactions{" +
                "date=" + date +
                ", time=" + time +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
