package com.project.ilearncentral.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobVacancy {
    private String jobId;
    private String centerId;
    private Map<String, String> businessData;
    private String username;
    private String jobDescription;
    private String position;
    private String status;
    private Timestamp date;
    private List<String> applicationMethods;
    private List<String> qualification;
    private List<String> responsibilities;
    private List<String> skills;
    private List<String> jobTypes;
    private List<Map<String, Object>> educationalRequirements;

    public JobVacancy() {
        jobId = "";
        centerId = "";
        businessData = new HashMap<>();
        username = "";
        jobDescription = "";
        position = "";
        status = "";
        date = Timestamp.now();
        applicationMethods = new ArrayList<>();
        qualification = new ArrayList<>();
        responsibilities = new ArrayList<>();
        skills = new ArrayList<>();
        educationalRequirements = new ArrayList<>();
        jobTypes = new ArrayList<>();
    }

    public JobVacancy(String jobId, Map<String, Object> data) {
        setJobVacancy(jobId, data);
    }

    public void setJobVacancy(String jobId, Map<String, Object> data) {
        this.jobId = jobId;
        businessData = new HashMap<>();
        centerId = data.get("CenterID").toString();
        username = data.get("Username").toString();
        jobDescription = data.get("JobDescription").toString();
        position = data.get("Position").toString();
        status = data.get("Status").toString();
        date = (Timestamp) data.get("Date");
        applicationMethods = ((List<String >) data.get("ApplicationMethods"));
        qualification = ((List<String >) data.get("Qualification"));
        responsibilities = ((List<String >) data.get("Responsibilities"));
        skills = ((List<String>) data.get("Skills"));
        jobTypes = ((List<String>) data.get("JobType"));
        educationalRequirements = ((List<Map<String, Object>>) data.get("EducationalRequirements"));
    }

    public Map<String, Object> getJobVacancyData() {
        Map<String, Object> data = new HashMap<>();
        data.put("CenterID", centerId);
        data.put("Username", username);
        data.put("JobDescription", jobDescription);
        data.put("Position", position);
        data.put("Status", status);
        data.put("Date", date);
        data.put("ApplicationMethods", applicationMethods);
        data.put("Qualification", qualification);
        data.put("Responsibilities", responsibilities);
        data.put("Skills", skills);
        data.put("JobType", jobTypes);
        data.put("EducationalRequirements", educationalRequirements);
        return data;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Map<String, String> getBusinessData() {
        return businessData;
    }

    public void setBusinessData(Map<String, String> businessData) {
        this.businessData = businessData;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getApplicationMethods() {
        return applicationMethods;
    }

    public void setApplicationMethods(List<String> applicationMethods) {
        this.applicationMethods = applicationMethods;
    }

    public List<String> getQualification() {
        return qualification;
    }

    public void setQualification(List<String> qualification) {
        this.qualification = qualification;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(List<String> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public List<Map<String, Object>> getEducationalRequirements() {
        return educationalRequirements;
    }

    public void setEducationalRequirements(List<Map<String, Object>> educationalRequirements) {
        this.educationalRequirements = educationalRequirements;
    }
}
