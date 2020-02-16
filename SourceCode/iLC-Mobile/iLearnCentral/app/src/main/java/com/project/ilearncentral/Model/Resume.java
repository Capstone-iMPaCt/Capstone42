package com.project.ilearncentral.Model;

public class Resume {

    private String dataList;
    private String header, address, datePeriod;
    private String personName, jobTitle, companyName, contactNumber;

    public Resume(String dataList) {
        this.dataList = dataList;
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

    public String getDataList() {
        return dataList;
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
}
