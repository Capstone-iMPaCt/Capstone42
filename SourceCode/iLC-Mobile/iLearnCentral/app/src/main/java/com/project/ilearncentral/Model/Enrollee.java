package com.project.ilearncentral.Model;

public class Enrollee {
    private int enrolleeImage;
    private String enrolleeName, enrolleeCourseEnrolled;

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
}
