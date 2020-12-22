package com.project.ilearncentral.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private String courseId;
    private String centerId;
    private String courseStatus;
    private String courseType;
    private double courseFee;
    private String courseName;
    private String courseDescription;
    private Timestamp scheduleFrom;
    private Timestamp scheduleTo;
    private List<String> scheduleDays;
    private String courseInstructor;
    private static List<Class> retrieved = new ArrayList<>();

    public Class() {
        this.courseId = "";
        this.courseStatus = "open";
        this.courseType = "";
        this.courseFee = 0.0;
        this.courseName = "";
        this.courseDescription = "";
        this.scheduleFrom = Timestamp.now();
        this.scheduleTo = Timestamp.now();
        this.scheduleDays = new ArrayList<String>();
        this.courseInstructor = "";
    }

    public Class(String courseId, String centerId, String courseStatus, String courseType, double courseFee, String courseName, String courseDescription, Timestamp ScheduleFrom, Timestamp ScheduleTo, List<String> ScheduleDays, String courseInstructor) {
        this.courseId = courseId;
        this.centerId = centerId;
        this.courseStatus = courseStatus;
        this.courseType = courseType;
        this.courseFee = courseFee;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.scheduleFrom = ScheduleFrom;
        this.scheduleTo = ScheduleTo;
        this.scheduleDays = ScheduleDays;
        this.courseInstructor = courseInstructor;
    }

    public void setCourse(Class otherCourse) {
        this.courseId = otherCourse.getCourseId();
        this.centerId = otherCourse.getCenterId();
        this.courseStatus = otherCourse.getCourseStatus();
        this.courseType = otherCourse.getCourseType();
        this.courseFee = otherCourse.getCourseFee();
        this.courseName = otherCourse.getCourseName();
        this.courseDescription = otherCourse.getCourseDescription();
        this.scheduleFrom = otherCourse.getScheduleFrom();
        this.scheduleTo = otherCourse.getScheduleTo();
        this.scheduleDays = otherCourse.getScheduleDays();
        this.courseInstructor = otherCourse.getCourseInstructor();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
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

    public Timestamp getScheduleFrom() {
        return scheduleFrom;
    }

    public void setScheduleFrom(Timestamp scheduleFrom) {
        this.scheduleFrom = scheduleFrom;
    }

    public Timestamp getScheduleTo() {
        return scheduleTo;
    }

    public void setScheduleTo(Timestamp scheduleTo) {
        this.scheduleTo = scheduleTo;
    }

    public List<String> getScheduleDays() {
        return scheduleDays;
    }

    public void setScheduleDays(List<String> scheduleDays) {
        this.scheduleDays = scheduleDays;
    }

    public void addScheduleDay(String value) {
        scheduleDays.add(value);
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public static List<Class> getRetrieved() {
        return retrieved;
    }

    public static void setRetrieved(List<Class> otherRetrieved) {
        retrieved = otherRetrieved;
    }

    public static void retrieveCoursesFromDB(final ObservableBoolean done) {
        FirebaseFirestore.getInstance().collection("Course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    Class course = new Class();
                                    course.setCourseId(document.getId());
                                    course.setCenterId(document.getString("CenterID"));
                                    course.setCourseStatus(document.getString("CourseStatus"));
                                    course.setCourseType(document.getString("CourseType"));
                                    course.setCourseFee(document.getDouble("CourseFee"));
                                    course.setCourseName(document.getString("CourseName"));
                                    course.setCourseDescription(document
                                            .getString("CourseDescription"));
                                    course.setScheduleFrom(document
                                            .getTimestamp("ScheduleFrom"));
                                    course.setScheduleTo(document
                                            .getTimestamp("ScheduleTo"));
                                    course.setCourseInstructor(document
                                            .getString("CourseInstructor"));

                                    List<String> op = (List<String>) document
                                            .get("ScheduleDays");
                                    for (int i = 0; i < op.size(); i++) {
                                        course.addScheduleDay(op.get(i));
                                    }

                                    int pos = getCoursePositionById(document.getId());
                                    if (pos == -1) {
                                        retrieved.add(course);
                                    } else {
                                        retrieved.get(pos).setCourse(course);
                                    }
                                    Log.d("getCourses", "Course item retrieved: " + document
                                            .getId());
                                } catch (Exception e) {
                                    Log.d("getCourses", "Course item not retrieved: " + e
                                            .getMessage());
                                }
                            }
                            if (done!=null) done.set(true);
                        } else {
                            if (done!=null) done.set(false);
                            Log.d("getCourses", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static int getCoursePositionById(String courseId) {
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getCourseId().equals(courseId))
                return i;
        }
        return -1;
    }

    public static Class getCourseById(String courseId) {
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getCourseId().equals(courseId))
                return retrieved.get(i);
        }
        return null;
    }

    public static List<Class> getCoursesByCenterId(String centerId) {
        List<Class> courses = new ArrayList<Class>();
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getCenterId().equals(centerId))
                courses.add(retrieved.get(i));
        }
        return courses;
    }

    public static List<Class> getCoursesByCenterIdWithStatus(String centerId, String status) {
        List<Class> courses = getCoursesByCenterId(centerId);
        for (int i = 0; i < retrieved.size(); i++) {
            if (!retrieved.get(i).getCourseStatus().equalsIgnoreCase(status))
                courses.remove(retrieved.get(i));
        }
        return courses;
    }

    public static List<Class> filterCourses(List<Class> courses, String filterBy, String filterValue) {
        filterValue = filterValue.toLowerCase().trim();
        List<Class> filteredCourse = new ArrayList<Class>();
        for (Class course:courses) {
            switch (filterBy.toLowerCase()) {
                case "status":
                    if (filterValue.equalsIgnoreCase(course.getCourseStatus())) {
                        filteredCourse.add(course);
                    }
                    break;
                case "type":
                    if (course.getCourseType().contains(filterValue)) {
                        filteredCourse.add(course);
                    }
                    break;
                case "name":
                    if (course.getCourseName().contains(filterValue)) {
                        filteredCourse.add(course);
                    }
                    break;
                case "description":
                    if (course.getCourseDescription().contains(filterValue)) {
                        filteredCourse.add(course);
                    }
                    break;
                case "instructor":
                    if (course.getCourseInstructor().contains(filterValue)) {
                        filteredCourse.add(course);
                    }
                    break;
                case "days":
                    if (course.getScheduleDays().contains(filterValue)) {
                        filteredCourse.add(course);
                    }
                    break;
                default:
                    break;
            }
        }
        return filteredCourse;
    }
}