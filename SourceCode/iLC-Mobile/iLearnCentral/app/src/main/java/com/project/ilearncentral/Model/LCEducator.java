package com.project.ilearncentral.Model;

public class LCEducator {
    private int educatorImage;
    private String educatorName, educatorDateEmployed, educatorStatus;

    public LCEducator(int educatorImage, String educatorName, String educatorDateEmployed, String educatorStatus) {
        this.educatorImage = educatorImage;
        this.educatorName = educatorName;
        this.educatorDateEmployed = educatorDateEmployed;
        this.educatorStatus = educatorStatus;
    }

    public LCEducator(String educatorName, String educatorDateEmployed, String educatorStatus) {
        this.educatorName = educatorName;
        this.educatorDateEmployed = educatorDateEmployed;
        this.educatorStatus = educatorStatus;
    }

    public int getEducatorImage() {
        return educatorImage;
    }

    public void setEducatorImage(int educatorImage) {
        this.educatorImage = educatorImage;
    }

    public String getEducatorName() {
        return educatorName;
    }

    public void setEducatorName(String educatorName) {
        this.educatorName = educatorName;
    }

    public String getEducatorDateEmployed() {
        return educatorDateEmployed;
    }

    public void setEducatorDateEmployed(String educatorDateEmployed) {
        this.educatorDateEmployed = educatorDateEmployed;
    }

    public String getEducatorStatus() {
        return educatorStatus;
    }

    public void setEducatorStatus(String educatorStatus) {
        this.educatorStatus = educatorStatus;
    }
}
