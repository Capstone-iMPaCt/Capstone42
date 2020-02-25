package com.project.ilearncentral.Model;

import com.project.ilearncentral.MyClass.Resume;

import java.util.HashMap;
import java.util.Map;

public class ResumeItem {

    private String detail;
    private String header, address, datePeriod;
    private String personName, jobTitle, companyName, contactNumber;

    public ResumeItem(String detail) {
        this.detail = detail;
    }

    public ResumeItem(String header, String address, String datePeriod) {
        this.header = header;
        this.address = address;
        this.datePeriod = datePeriod;
    }

    public ResumeItem(String personName, String jobTitle, String companyName, String contactNumber) {
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

    public Object getData(Resume.ResumeItemType type) {
        Map<String, Object> data = new HashMap<>();
        switch (type) {
            case Award:
            case Interest:
            case Qualities:
            case Skill:
                return detail;
            case Education:
                data.put("SchoolName", header);
                data.put("SchoolAddress", address);
                data.put("SchoolYear", datePeriod);
                break;
            case Employment:
                data.put("CompanyName", header);
                data.put("CompanyAddress", address);
                data.put("DatePeriod", datePeriod);
                break;
            case Reference:
                data.put("ReferenceName", personName);
                data.put("Affiliation", companyName);
                data.put("Position", jobTitle);
                data.put("ContactNo", contactNumber);
                break;
        }
        return data;
    }
}
