package com.project.ilearncentral.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Enrolment {
    private static final String TAG = "Enrolment";

    private String learningCenterName, courseEnrolled, dateEnrolled;

    private String centerID;
    private String courseID;
    private Date dateCourseStarts;
    private Date dateCourseEnds;
    private Date enrolmentDate;
    private Double enrolmentFee;
    private String enrolmentStatus;
    private String studentID;

    public Enrolment() {
    }

    public Enrolment(String learningCenterName, String courseEnrolled, String dateEnrolled) {
        this.learningCenterName = learningCenterName;
        this.courseEnrolled = courseEnrolled;
        this.dateEnrolled = dateEnrolled;
    }

    public Enrolment(String learningCenterName, String courseEnrolled, String dateEnrolled, String centerID, String courseID, Date dateCourseStarts, Date dateCourseEnds, Date enrolmentDate, Double enrolmentFee, String enrolmentStatus, String studentID) {
        this.learningCenterName = learningCenterName;
        this.courseEnrolled = courseEnrolled;
        this.dateEnrolled = dateEnrolled;
        this.centerID = centerID;
        this.courseID = courseID;
        this.dateCourseStarts = dateCourseStarts;
        this.dateCourseEnds = dateCourseEnds;
        this.enrolmentDate = enrolmentDate;
        this.enrolmentFee = enrolmentFee;
        this.enrolmentStatus = enrolmentStatus;
        this.studentID = studentID;
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

    public void setLearningCenterName(String learningCenterName) {
        this.learningCenterName = learningCenterName;
    }

    public void setCourseEnrolled(String courseEnrolled) {
        this.courseEnrolled = courseEnrolled;
    }

    public void setDateEnrolled(String dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    public String getCenterID() {
        return centerID;
    }

    public void setCenterID(String centerID) {
        this.centerID = centerID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Date getDateCourseStarts() {
        return dateCourseStarts;
    }

    public void setDateCourseStarts(Date dateCourseStarts) {
        this.dateCourseStarts = dateCourseStarts;
    }

    public Date getDateCourseEnds() {
        return dateCourseEnds;
    }

    public void setDateCourseEnds(Date dateCourseEnds) {
        this.dateCourseEnds = dateCourseEnds;
    }

    public Date getEnrolmentDate() {
        return enrolmentDate;
    }

    public void setEnrolmentDate(Date enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public Double getEnrolmentFee() {
        return enrolmentFee;
    }

    public void setEnrolmentFee(Double enrolmentFee) {
        this.enrolmentFee = enrolmentFee;
    }

    public String getEnrolmentStatus() {
        return enrolmentStatus;
    }

    public void setEnrolmentStatus(String enrolmentStatus) {
        this.enrolmentStatus = enrolmentStatus;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public static void getDataByID(String idType, String id, final List<Enrolment> enrolmentList, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("Enrolment")
                .whereEqualTo(idType, id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Enrolment enrolment = new Enrolment();
//                                enrolment.setCenterID(document.get("centerID").toString());
//                                enrolment.setCourseID(document.get("courseID").toString());
//                                enrolment.setStudentID(document.get("studentID").toString());
//                                enrolment.setDateCourseStarts(document.getTimestamp("dateCourseStarts").toDate());
//                                enrolment.setDateCourseEnds(document.getTimestamp("dateCourseEnds").toDate());
//                                enrolment.setEnrolmentDate(document.getTimestamp("enrolmentDate").toDate());
//                                enrolment.setEnrolmentFee(document.getDouble("enrolmentFee"));
//                                enrolment.setEnrolmentStatus(document.get("enrolmentStatus").toString());
                                enrolmentList.add(document.toObject(Enrolment.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        listener.set(true);
                    }
                });
    }

    public static void getDataByStudentIDCourseID(String studentID, String courseID, final Enrolment enrolment, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("Enrolment")
                .whereEqualTo("studentID", studentID)
                .whereEqualTo("courseID", courseID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                enrolment.setCenterID(document.get("centerID").toString());
                                enrolment.setCourseID(document.get("courseID").toString());
                                enrolment.setStudentID(document.get("studentID").toString());
                                enrolment.setDateCourseStarts(document.getTimestamp("dateCourseStarts").toDate());
                                enrolment.setDateCourseEnds(document.getTimestamp("dateCourseEnds").toDate());
                                enrolment.setEnrolmentDate(document.getTimestamp("enrolmentDate").toDate());
                                enrolment.setEnrolmentFee(document.getDouble("enrolmentFee"));
                                enrolment.setEnrolmentStatus(document.get("enrolmentStatus").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        listener.set(true);
                    }
                });
    }

    public void getDataByID(String idType1, String id1, String idType2, String id2, String idType3, String id3) {
        final List<Enrolment> enrolmentList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Enrolment")
                .whereEqualTo(idType1, id1)
                .whereEqualTo(idType2, id2)
                .whereEqualTo(idType3, id3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Enrolment enrolment = new Enrolment();
                                enrolment.setCenterID(document.get("centerID").toString());
                                enrolment.setCourseID(document.get("courseID").toString());
                                enrolment.setStudentID(document.get("studentID").toString());
                                enrolment.setDateCourseStarts(document.getTimestamp("dateCourseStarts").toDate());
                                enrolment.setDateCourseEnds(document.getTimestamp("dateCourseEnds").toDate());
                                enrolment.setEnrolmentDate(document.getTimestamp("enrolmentDate").toDate());
                                enrolment.setEnrolmentFee(document.getDouble("enrolmentFee"));
                                enrolment.setEnrolmentStatus(document.get("enrolmentStatus").toString());
                                enrolmentList.add(enrolment);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
