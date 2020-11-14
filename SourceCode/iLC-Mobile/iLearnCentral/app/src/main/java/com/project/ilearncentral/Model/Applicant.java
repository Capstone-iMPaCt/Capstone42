package com.project.ilearncentral.Model;

public class Applicant {
    private int applicantImage;
    private String applicantName, applicantPositionApplied;

    public Applicant(int applicantImage, String applicantName, String applicantPositionApplied) {
        this.applicantImage = applicantImage;
        this.applicantName = applicantName;
        this.applicantPositionApplied = applicantPositionApplied;
    }

    public Applicant(String applicantName, String applicantPositionApplied) {
        this.applicantName = applicantName;
        this.applicantPositionApplied = applicantPositionApplied;
    }

    public int getApplicantImage() {
        return applicantImage;
    }

    public void setApplicantImage(int applicantImage) {
        this.applicantImage = applicantImage;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantPositionApplied() {
        return applicantPositionApplied;
    }

    public void setApplicantPositionApplied(String applicantPositionApplied) {
        this.applicantPositionApplied = applicantPositionApplied;
    }
}
