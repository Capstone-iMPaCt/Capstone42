package com.project.ilearncentral.Model;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableInteger;

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
    private List<Student> students;
    private List<Attendance> attendances;
    private List<ClassActivity> activities;
    private String lessonPlan;
    private boolean linkedPlan;

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
        this.students = new ArrayList<>();
        this.attendances = new ArrayList();
        this.activities = new ArrayList<>();
    }

    public String getClassID() {
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

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public void addAttendance(List<Attendance> attendances) {
        this.attendances.addAll(attendances);
    }

    public void addAttendance(Attendance attendance) {
        this.attendances.add(attendance);
    }

    public List<ClassActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<ClassActivity> activities) {
        this.activities = activities;
    }
    public void addActivity(ClassActivity activity) {
        this.activities.add(activity);
    }

    public boolean addAttendanceWithCheck(Attendance attendance) {
        for (Attendance a: this.attendances) {
            if(a.getClassID().equalsIgnoreCase(attendance.getClassID())
                    && a.getStudentID().equalsIgnoreCase(attendance.getStudentID())) {
                a.setAttendance(attendance);
                return true;
            }
        }
        attendances.add(attendance);
        return false;
    }

    public boolean addAttendanceWithCheck(List<Attendance> attendances, Attendance attendance) {
        for (Attendance a: attendances) {
            if(a.getClassID().equalsIgnoreCase(attendance.getClassID()) && a.getStudentID().equalsIgnoreCase(attendance.getStudentID())) {
                a.setAttendance(attendance);
                return true;
            }
        }
        attendances.add(attendance);
        return false;
    }

    public boolean addClassActivityWithCheck(ClassActivity activity) {
        for (ClassActivity a: this.activities) {
            if(a.getActivityID().equalsIgnoreCase(activity.getActivityID())) {
                a.setActivity(activity);
                return true;
            }
        }
        activities.add(activity);
        return false;
    }

    public boolean addClassActivityWithCheck(List<ClassActivity> activities, ClassActivity activity) {
        for (ClassActivity a: activities) {
            if(a.getActivityID().equalsIgnoreCase(activity.getActivityID())) {
                a.setActivity(activity);
                return true;
            }
        }
        activities.add(activity);
        return false;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudents(List<Student> students) {
        this.students.addAll(students);
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public String getLessonPlan() {
        return lessonPlan;
    }

    public void setLessonPlan(String lessonPlan) {
        this.lessonPlan = lessonPlan;
    }

    public boolean isLinkedPlan() {
        return linkedPlan;
    }

    public void setLinkedPlan(boolean linkedPlan) {
        this.linkedPlan = linkedPlan;
    }

    public void setClass(Class otherClass) {
        this.classId = otherClass.getClassID();
        this.eduID = otherClass.getEduID();
        this.courseID = otherClass.getCourseID();
        this.educator = otherClass.getEducator();
        this.course = otherClass.getCourse();
        this.classStart = otherClass.getClassStart();
        this.classEnd = otherClass.getClassEnd();
        this.roomNo = otherClass.getRoomNo();
        this.status = otherClass.getStatus();
        this.requestMessage = otherClass.getRequestMessage();
        this.attendances = otherClass.getAttendances();
        this.activities = otherClass.getActivities();
        this.lessonPlan = otherClass.getLessonPlan();
        this.linkedPlan = otherClass.isLinkedPlan();
    }

    public static void retrieveClassesFromDB(String courseID, String classStatus, final ObservableBoolean done) {
        final String TAG = "Class Model";
        retrieved.clear();
        if (classStatus.isEmpty() || classStatus == null) {
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

                List <Object> attendInput = (List<Object>) document.get("Attendance");
                if (attendInput != null) {
                    for (int i=0;i<attendInput.size();i++) {
                        Map <String, String> data = new HashMap<>();
                        for (Map.Entry entry : ((Map<String, Object>) attendInput.get(i)).entrySet()) {
                            data.put(entry.getKey().toString(), entry.getValue().toString());
                        }
                        Attendance attendance = new Attendance();
                        attendance.setClassID(c.getClassID());
                        attendance.setClass(c);
                        attendance.setStudentID(data.get("StudentID"));
                        attendance.setStudent(Student.getStuByUsername(attendance.getStudentID()));
                        attendance.setRemarks(data.get("Remarks"));
                        attendance.setAttendance(data.get("Attendance"));
                        c.addAttendance(attendance);
                    }
                }
                c.setLessonPlan(document.getString("LessonPlan"));
                c.setLinkedPlan(document.getBoolean("LinkedPlan"));

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
        data.put("Message", aClass.getRequestMessage());
        data.put("LessonPlan", "");
        data.put("LinkedPlan", false);
        FirebaseFirestore.getInstance().collection("Class").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        aClass.setClassId(documentReference.getId());
                        if (count!=null) {
                            count.set(count.get()+1);
                        }
                        DocumentReference courseRef = FirebaseFirestore.getInstance().collection("Course").document(aClass.getCourseID());
                        courseRef.update("Educators", FieldValue.arrayUnion(aClass.getEduID()));
                    }
                });
    }

    public static void editClassToDB(final Class aClass) {
        Map<String, Object> data = new HashMap<>();
        data.put("CourseID", aClass.getCourseID());
        data.put("EducatorID", aClass.getEduID());
        data.put("RoomNo", aClass.getRoomNo());
        data.put("Status", aClass.getStatus());
        data.put("ClassStart", aClass.getClassStart());
        data.put("ClassEnd", aClass.getClassEnd());
        data.put("Message", aClass.getRequestMessage());
        FirebaseFirestore.getInstance().collection("Class").document(aClass.getClassID()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference courseRef = FirebaseFirestore.getInstance().collection("Course").document(aClass.getCourseID());
                        courseRef.update("Educators", FieldValue.arrayUnion(aClass.getEduID()));
                    }
                });
    }

    public static void addScheduleRequest(String classId, String requestMessage, final ObservableBoolean done) {
        DocumentReference request = FirebaseFirestore.getInstance().collection("Class").document(classId);
        request.update("Message", requestMessage,
                "Status", "Pending")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (done!=null) done.set(task.isSuccessful());
            }
        });
    }

    public static Class getClassById(String classId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getClassID().equals(classId))
                return retrieved.get(i);
        }
        return null;
    }

    public static boolean hasStudentInAttendance(Class aClass, String studentID) {
        for (Attendance attendance:aClass.attendances) {
           if (studentID.equalsIgnoreCase(attendance.getStudentID()))
               return true;
        }
        return false;
    }
    public static boolean hasStudentInActivities(Class aClass, String activityID, String studentID) {
        for (ClassActivity activity: aClass.getActivities()) {
            if (activity.getActivityID().equalsIgnoreCase(activityID)) {
                if (activity.hasStudent(studentID))
                    return true;
            }
        }
        return false;
    }

    public static boolean hasClass(List<Class> classes, String classID) {
        for (Class aClass:classes) {
            if (aClass.getClassID().equalsIgnoreCase(classID))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Class{" + "\n" +
                "\t" + "classId='" + classId + '\'' + ",\n" +
                "\t" + "eduID='" + eduID + '\'' + ",\n" +
                "\t" + "courseID='" + courseID + '\'' + ",\n" +
//                "\t" + "educator=" + educator + ",\n" +
//                "\t" + "course=" + course + ",\n" +
                "\t" + "classStart=" + classStart + ",\n" +
                "\t" + "classEnd=" + classEnd + ",\n" +
                "\t" + "roomNo='" + roomNo + '\'' + ",\n" +
                "\t" + "status='" + status + '\'' + ",\n" +
                "\t" + "requestMessage='" + requestMessage + '\'' + ",\n" +
//                "\t" + "students=" + students + ",\n" +
//                "\t" + "attendances=" + attendances + ",\n" +
//                "\t" + "activities=" + activities + ",\n" +
                "\t" + "lessonPlan='" + lessonPlan + '\'' + ",\n" +
                "\t" + "linkedPlan=" + linkedPlan + "\n" +
                '}';
    }
}