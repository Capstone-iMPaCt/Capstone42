package com.project.ilearncentral.Model;

public class Resume {

    private String detail;
    private String header, address, datePeriod;
    private String personName, jobTitle, companyName, contactNumber;

    public Resume(String detail) {
        this.detail = detail;
    }

    public Resume(String header, String address, String datePeriod) {
        this.header = header;
        this.address = address;
        this.datePeriod = datePeriod;
    }

    public Resume(String personName, String jobTitle, String companyName, String contactNumber) {
        this.personName = personName;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.contactNumber = contactNumber;
    }

    public String getDetail() {
        return detail;
    }

    public String getHeader() {
        return header;
    }

    public String getAddress() {
        return address;
    }

    public String getDatePeriod() {
        return datePeriod;
    }

    public String getPersonName() {
        return personName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDatePeriod(String datePeriod) {
        this.datePeriod = datePeriod;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
