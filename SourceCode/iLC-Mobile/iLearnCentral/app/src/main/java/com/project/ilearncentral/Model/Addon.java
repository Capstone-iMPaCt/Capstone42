package com.project.ilearncentral.Model;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Addon {

    private String title;
    private String description;
    private String countdown;
    private double fee;

    public Addon(String title, String description, double fee) {
        this.title = title;
        this.description = description;
        this.fee = fee;
        this.countdown = countdown;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
