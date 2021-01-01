package com.project.ilearncentral.Model;

public class Enrollee {
    private int enrolleeImage;
    private String enrolleeName, enrolleeCourseEnrolled;

    private String courseID;
    private String studentID;

    public Enrollee() {
    }

    public Enrollee(int enrolleeImage, String enrolleeName, String enrolleeCourseEnrolled) {
        this.enrolleeImage = enrolleeImage;
        this.enrolleeName = enrolleeName;
        this.enrolleeCourseEnrolled = enrolleeCourseEnrolled;
    }

    public Enrollee(String enrolleeName, String enrolleeCourseEnrolled) {
        this.enrolleeName = enrolleeName;
        this.enrolleeCourseEnrolled = enrolleeCourseEnrolled;
    }

    public int getEnrolleeImage() {
        return enrolleeImage;
    }

    public void setEnrolleeImage(int enrolleeImage) {
        this.enrolleeImage = enrolleeImage;
    }

    public String getEnrolleeName() {
        return enrolleeName;
    }

    public void setEnrolleeName(String enrolleeName) {
        this.enrolleeName = enrolleeName;
    }

    public String getEnrolleeCourseEnrolled() {
        return enrolleeCourseEnrolled;
    }

    public void setEnrolleeCourseEnrolled(String enrolleeCourseEnrolled) {
        this.enrolleeCourseEnrolled = enrolleeCourseEnrolled;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
