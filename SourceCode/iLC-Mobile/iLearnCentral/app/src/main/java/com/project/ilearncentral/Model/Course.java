package com.project.ilearncentral.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    private final String TAG = "Course";
    private String courseId;
    private String centerId;
    private String centerName;
    private String courseStatus;
    private String courseType;
    private double courseFee;
    private String courseName;
    private String courseDescription;
    private Timestamp scheduleFrom;
    private Timestamp scheduleTo;
    private Map<String, Object> course;
    private static List<Course> retrieved = new ArrayList<>();

    // just for course view purposes
    private Date processedDate = new Date();
    private Date enrolledDate = new Date();
    private String status = "";

    public Course() {
        this.course = new HashMap<>();
        this.courseId = "";
        this.centerId = "";
        this.centerName = "";
        this.courseStatus = "open";
        this.courseType = "";
        this.courseFee = 0.0;
        this.courseName = "";
        this.courseDescription = "";
        this.scheduleFrom = Timestamp.now();
        this.scheduleTo = Timestamp.now();
    }

    public Course(String courseId, String centerId, String courseStatus, String courseType, double courseFee, String courseName, String courseDescription, Timestamp ScheduleFrom, Timestamp ScheduleTo) {
        this.courseId = courseId;
        this.centerId = centerId;
        this.courseStatus = courseStatus;
        this.courseType = courseType;
        this.courseFee = courseFee;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.scheduleFrom = ScheduleFrom;
        this.scheduleTo = ScheduleTo;
    }

    public void setCourse(Course otherCourse) {
        this.courseId = otherCourse.getCourseId();
        this.centerId = otherCourse.getCenterId();
        this.courseStatus = otherCourse.getCourseStatus();
        this.courseType = otherCourse.getCourseType();
        this.courseFee = otherCourse.getCourseFee();
        this.courseName = otherCourse.getCourseName();
        this.courseDescription = otherCourse.getCourseDescription();
        this.scheduleFrom = otherCourse.getScheduleFrom();
        this.scheduleTo = otherCourse.getScheduleTo();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCenterName() {
        return centerName;
    }

    private void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
        this.course.put("CenterID", centerId);
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
        this.course.put("CourseStatus", courseStatus);
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
        this.course.put("CourseType", courseType);
    }

    public double getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
        this.course.put("CourseFee", courseFee);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
        this.course.put("CourseName", courseName);
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
        this.course.put("CourseDescription", courseDescription);
    }

    public Timestamp getScheduleFrom() {
        return scheduleFrom;
    }

    public void setScheduleFrom(Timestamp scheduleFrom) {
        this.scheduleFrom = scheduleFrom;
        this.course.put("ScheduleFrom", scheduleFrom);
    }

    public Timestamp getScheduleTo() {
        return scheduleTo;
    }

    public void setScheduleTo(Timestamp scheduleTo) {
        this.scheduleTo = scheduleTo;
        this.course.put("ScheduleTo", scheduleTo);
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<Course> getRetrieved() {
        return retrieved;
    }

    public void setToDB(){
        setCourseStatus(this.courseStatus);
        setCenterId(Account.getCenterId());
        FirebaseFirestore.getInstance().collection("Course")
                .add(course)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, task.getResult().toString());
                        }
                    }
                });
    }

    public static void setRetrieved(List<Course> otherRetrieved) {
        retrieved = otherRetrieved;
    }

    public static void retrieveCoursesFromDB(final ObservableBoolean done) {
        final ObservableBoolean centerData = new ObservableBoolean();
        FirebaseFirestore.getInstance().collection("Course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    final Course course = new Course();
                                    course.setCourseId(document.getId());
                                    course.setCenterId(document.getString("CenterID"));
                                    FirebaseFirestore.getInstance()
                                            .collection("LearningCenter")
                                            .document(course.getCenterId())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        course.setCenterName(document.get("BusinessName").toString());
                                                    }
                                                }
                                            });
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
                            if (done != null) done.set(true);
                        } else {
                            if (done != null) done.set(false);
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

    public static Course getCourseById(String courseId) {
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getCourseId().equals(courseId))
                return retrieved.get(i);
        }
        return null;
    }

    public static List<Course> getCoursesByCenterId(String centerId) {
        List<Course> courses = new ArrayList<Course>();
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getCenterId().equals(centerId))
                courses.add(retrieved.get(i));
        }
        return courses;
    }

    public static List<Course> getCoursesByCenterIdWithStatus(String centerId, String status) {
        List<Course> courses = getCoursesByCenterId(centerId);
        for (int i = 0; i < retrieved.size(); i++) {
            if (!retrieved.get(i).getCourseStatus().equalsIgnoreCase(status))
                courses.remove(retrieved.get(i));
        }
        return courses;
    }

    public static List<Course> filterCourses(List<Course> courses, String filterBy, String filterValue) {
        filterValue = filterValue.toLowerCase().trim();
        List<Course> filteredCourse = new ArrayList<Course>();
        for (Course course : courses) {
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
                default:
                    break;
            }
        }
        return filteredCourse;
    }
}