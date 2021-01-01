package com.project.ilearncentral.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Enrolment {
    private static final String TAG = "Enrolment";

    private String learningCenterName, courseEnrolled, dateEnrolled;

    private String centerID;
    private String courseID;
    private Date dateCourseStarts;
    private Date dateCourseEnds;
    private Date processedDate;
    private Date enrolledDate;
    private Double enrolmentFee;
    private String enrolmentStatus;
    private String studentID;

    private String studentName;

    public Enrolment() {
    }

    public Enrolment(String learningCenterName, String courseEnrolled, String dateEnrolled) {
        this.learningCenterName = learningCenterName;
        this.courseEnrolled = courseEnrolled;
        this.dateEnrolled = dateEnrolled;
    }

    public Enrolment(String courseEnrolled, String dateEnrolled, String centerID, String courseID, Date dateCourseStarts, Date dateCourseEnds, Date processedDate, Date enrolledDate, Double enrolmentFee, String enrolmentStatus, String studentID, String studentName) {
        this.courseEnrolled = courseEnrolled;
        this.dateEnrolled = dateEnrolled;
        this.centerID = centerID;
        this.courseID = courseID;
        this.dateCourseStarts = dateCourseStarts;
        this.dateCourseEnds = dateCourseEnds;
        this.processedDate = processedDate;
        this.enrolledDate = enrolledDate;
        this.enrolmentFee = enrolmentFee;
        this.enrolmentStatus = enrolmentStatus;
        this.studentID = studentID;
        this.studentName = studentName;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setEnrolment(DocumentSnapshot document) {
        setCenterID(document.get("centerID").toString());
        setCourseID(document.get("courseID").toString());
//        setCourseEnrolled(document.get());
        setStudentID(document.get("studentID").toString());
        setDateCourseStarts(document.getTimestamp("dateCourseStarts").toDate());
        setDateCourseEnds(document.getTimestamp("dateCourseEnds").toDate());
        try {
            setProcessedDate(document.getTimestamp("processedDate").toDate());
            setEnrolledDate(document.getTimestamp("enrolledDate").toDate());
        } catch (Exception e) {
        }
        setEnrolmentFee(document.getDouble("enrolmentFee"));
        setEnrolmentStatus(document.get("enrolmentStatus").toString());
    }

    public static void getDocumentReferenceByID(String idType, String id, final List<Enrolment> enrolmentsList, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("Enrolment")
                .whereEqualTo(idType, id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Enrolment enrolment = new Enrolment();
//                                enrolment.setEnrolment(document);
//                                enrolmentsList.add(enrolment);
                                enrolmentsList.add(document.toObject(Enrolment.class));
                            }
                            listener.set(true);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void getByDocumentRef(String documentRef, final Enrolment enrolment, final ObservableBoolean listener) {
        FirebaseFirestore.getInstance().collection("Enrolment")
                .document(documentRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
//                            enrolment.setCenterID(task.getResult().get("centerID").toString());
//                            enrolment.setCourseID(task.getResult().get("courseID").toString());
//                            enrolment.setStudentID(task.getResult().get("studentID").toString());
//                            enrolment.setProcessedDate(task.getResult().getTimestamp("processedDate").toDate());
//                            enrolment.setEnrolmentStatus(task.getResult().get("enrolmentStatus").toString());
//                            enrolment.setEnrolmentFee(task.getResult().getDouble("enrolmentFee"));
                            enrolment.setEnrolment(task.getResult());
                            listener.set(true);
                        }
                    }
                });
    }

    public static void getDataByID(String idType, String id, final List<Enrolment> enrolmentsList, final ObservableBoolean listener) {
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
                                enrolmentsList.add(document.toObject(Enrolment.class));
                            }
                            listener.set(true);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
                                enrolment.setProcessedDate(document.getTimestamp("processedDate").toDate());
                                enrolment.setEnrolledDate(document.getTimestamp("enrolledDate").toDate());
                                enrolment.setEnrolmentFee(document.getDouble("enrolmentFee"));
                                enrolment.setEnrolmentStatus(document.get("enrolmentStatus").toString());
                            }
                            listener.set(true);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
                                enrolment.setProcessedDate(document.getTimestamp("processedDate").toDate());
                                enrolment.setEnrolledDate(document.getTimestamp("enrolledDate").toDate());
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
