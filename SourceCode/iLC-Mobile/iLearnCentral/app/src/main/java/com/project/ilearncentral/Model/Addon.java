package com.project.ilearncentral.Model;

public class Addon {

    private String title, description, countdown;

    public Addon(String title, String description, String countdown) {
        this.title = title;
        this.description = description;
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

}
