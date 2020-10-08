package com.project.ilearncentral.Model;

public class Course {
    private String courseStatus;
    private String courseType;
    private double courseFee;
    private String courseName;
    private String courseDescription;
    private String classScheduleFrom;
    private String classScheduleTo;
    private String classScheduleDays;

    public Course(String courseStatus, String courseType, double courseFee, String courseName, String courseDescription, String classFromSchedule, String classToSchedule, String classDaysSchedule) {
        this.courseStatus = courseStatus;
        this.courseType = courseType;
        this.courseFee = courseFee;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.classScheduleFrom = classFromSchedule;
        this.classScheduleTo = classToSchedule;
        this.classScheduleDays = classDaysSchedule;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public double getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getClassScheduleFrom() {
        return classScheduleFrom;
    }

    public void setClassScheduleFrom(String classScheduleFrom) {
        this.classScheduleFrom = classScheduleFrom;
    }

    public String getClassScheduleTo() {
        return classScheduleTo;
    }

    public void setClassScheduleTo(String classScheduleTo) {
        this.classScheduleTo = classScheduleTo;
    }

    public String getClassScheduleDays() {
        return classScheduleDays;
    }

    public void setClassScheduleDays(String classScheduleDays) {
        this.classScheduleDays = classScheduleDays;
    }
}