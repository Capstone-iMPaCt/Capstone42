package com.project.ilearncentral.Model;

public class Resume {

    private String detail;
    private String companyName, companyAddress, durationPeriod;

    public Resume(String detail) {
        this.detail = detail;
    }

    public Resume(String companyName, String companyAddress, String durationPeriod) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.durationPeriod = durationPeriod;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getDurationPeriod() {
        return durationPeriod;
    }

    public void setDurationPeriod(String durationPeriod) {
        this.durationPeriod = durationPeriod;
    }
}
