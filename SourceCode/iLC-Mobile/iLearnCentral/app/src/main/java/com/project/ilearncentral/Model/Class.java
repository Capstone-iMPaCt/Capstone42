package com.project.ilearncentral.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableInteger;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Class {

    private String classId;
    private String eduID, courseID;
    private Educator educator;
    private Course course;
    private Timestamp classStart, classEnd;
    private String roomNo;
    private String status;
    private String requestMessage;
    public static List<Class> retrieved = new ArrayList<>();

    public Class() {
        this.classId = eduID = courseID = "";
        this.educator = null;
        this.course = null;
        this.classStart = Timestamp.now();
        this.classEnd = Timestamp.now();
        this.roomNo = "";
        this.status = "";
        this.requestMessage ="";
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getEduID() {
        return eduID;
    }

    public void setEduID(String eduID) {
        this.eduID = eduID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Educator getEducator() {
        return educator;
    }

    public void setEducator(Educator educator) {
        this.educator = educator;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Timestamp getClassStart() {
        return classStart;
    }

    public void setClassStart(Timestamp classStart) {
        this.classStart = classStart;
    }

    public Timestamp getClassEnd() {
        return classEnd;
    }

    public void setClassEnd(Timestamp classEnd) {
        this.classEnd = classEnd;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public static List<Class> getRetrieved() {
        return retrieved;
    }

    public void setClass(Class otherClass) {
        this.classId = otherClass.getClassId();
        this.eduID = otherClass.getEduID();
        this.courseID = otherClass.getCourseID();
        this.educator = otherClass.getEducator();
        this.course = otherClass.getCourse();
        this.classStart = otherClass.getClassStart();
        this.classEnd = otherClass.getClassEnd();
        this.roomNo = otherClass.getRoomNo();
        this.status = otherClass.getStatus();
        this.requestMessage = otherClass.getRequestMessage();
    }

    public static void retrieveClassesFromDB(String courseID, String classStatus, final ObservableBoolean done) {
        final String TAG = "Class Model";
        retrieved.clear();
        if (classStatus.isEmpty() ||classStatus == null) {
            FirebaseFirestore.getInstance().collection("Class")
                    .whereEqualTo("CourseID", courseID)
                    .orderBy("ClassStart")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            processDBResult(task, TAG, done);
                        }
                    });
        } else {
            FirebaseFirestore.getInstance().collection("Class")
                    .whereEqualTo("CourseID", courseID)
                    .whereEqualTo("Status", classStatus)
                    .orderBy("ClassStart")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            processDBResult(task, TAG, done);
                        }
                    });
        }
    }

    private static void processDBResult(@NonNull Task<QuerySnapshot> task, String TAG, ObservableBoolean done) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Class c = new Class();
                c.setClassId(document.getId());
                c.setRoomNo(document.getString("RoomNo"));
                c.setEduID(document.getString("EducatorID"));
                c.setCourseID(document.getString("CourseID"));
                c.setClassStart(document.getTimestamp("ClassStart"));
                c.setClassEnd(document.getTimestamp("ClassEnd"));
                c.setEducator(Educator.getEduByUsername(document.getString("EducatorID")));
                c.setCourse(Course.getCourseById(document.getString("CourseID")));
                if (document.contains("Message"))
                    c.setRequestMessage(document.getString("Message"));
                else
                    c.setRequestMessage("");
                c.setStatus(document.getString("Status"));
                if (c.getStatus().equalsIgnoreCase("Open") || c.getStatus().equalsIgnoreCase("Ongoing")) {
                    if (c.getClassStart()!=null) {
                        if (c.getClassStart().compareTo(Timestamp.now()) <= 0 && c.getClassEnd()
                                .compareTo(Timestamp.now()) >= 0) {
                            c.setStatus("Ongoing");
                            FirebaseFirestore.getInstance().collection("Class")
                                    .document(document.getId()).update("Status", "Ongoing");
                        } else if (c.getClassEnd().compareTo(Timestamp.now()) < 0) {
                            c.setStatus("Close");
                            FirebaseFirestore.getInstance().collection("Class")
                                    .document(document.getId()).update("Status", "Close");
                        }
                    }
                }
                if (c.getClassStart()!=null)
                    retrieved.add(c);
                Log.d(TAG, document.getId());
            }
            if (done != null) done.set(true);
        } else {
            if (done != null) done.set(false);
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }

    public static void addNewClassToDB(final Class aClass, final ObservableInteger count) {
        Map<String, Object> data = new HashMap<>();
        data.put("CourseID", aClass.getCourseID());
        data.put("EducatorID", aClass.getEduID());
        data.put("RoomNo", aClass.getRoomNo());
        data.put("Status", aClass.getStatus());
        data.put("ClassStart", aClass.getClassStart());
        data.put("ClassEnd", aClass.getClassEnd());
        FirebaseFirestore.getInstance().collection("Class").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        aClass.setClassId(documentReference.getId());
                        if (count!=null) {
                            count.set(count.get()+1);
                            System.out.println("JSHAKJHFKAS  " + count.get());
                        }
                    }
                });
    }

    public static Class getClassById(String classId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getClassId().equals(classId))
                return retrieved.get(i);
        }
        return null;
    }
}