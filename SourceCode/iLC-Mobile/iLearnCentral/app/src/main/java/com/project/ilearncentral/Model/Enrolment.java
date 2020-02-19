package com.project.ilearncentral.Model;

public class Enrolment {
    private String learningCenterName, courseEnrolled, dateEnrolled;

    public Enrolment(String learningCenterName, String courseEnrolled, String dateEnrolled) {
        this.learningCenterName = learningCenterName;
        this.courseEnrolled = courseEnrolled;
        this.dateEnrolled = dateEnrolled;
    }

    public String getLearningCenterName() {
        return learningCenterName;
    }

    public String getCourseEnrolled() {
        return courseEnrolled;
    }

    public String getDateEnrolled() {
        return dateEnrolled;
    }
}
