package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class StudentRecord {
    private String recordID, studentID, courseID;
    private Student student;
    private Course course;
    private List<Attendance> attendances;
    private List<ClassActivity> activities;

    private static List<StudentRecord> retrieved = new ArrayList<>();

    public StudentRecord() {
        recordID = studentID = courseID = "";
        student = null;
        course = null;
        attendances = new ArrayList<>();
        activities = new ArrayList<>();
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public void addAttendance(List<Attendance> attendanceList) {
        this.attendances.addAll(attendanceList);
    }

    public void addAttendance(Attendance attendanceList) {
        this.attendances.add(attendanceList);
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
    public List<ClassActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<ClassActivity> activities) {
        this.activities = activities;
    }

    public void addActivity(List<ClassActivity> activities) {
        this.activities.addAll(activities);
    }

    public void addActivity(ClassActivity activities) {
        this.activities.add(activities);
    }
    
    public static List<StudentRecord> getRetrieved() {
        return retrieved;
    }

    public static void setRetrieved(List<StudentRecord> retrieved) {
        StudentRecord.retrieved = retrieved;
    }

    public void setStudentRecord(StudentRecord record) {
        recordID = record.getRecordID();
        studentID = record.getStudentID();
        student = record.getStudent();
        courseID = record.getCourseID();
        course = record.getCourse();
        attendances = record.getAttendances();
        activities = record.getActivities();
    }

    public Attendance getAttendanceByClassID(String classID) {
        for (Attendance attendance : attendances) {
            if (attendance.getClassID().equalsIgnoreCase(classID))
                return attendance;
        }
        return null;
    }

    public static void retrieveStatusRecordFromDB(final String courseID, final ObservableBoolean done) {
        final String TAG = "Student Record Model";
        retrieved.clear();
        FirebaseFirestore.getInstance().collection("StudentRecord")
            .whereEqualTo("CourseID", courseID)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        StudentRecord record = new StudentRecord();
                        record.setRecordID(document.getId());
                        record.setStudentID(document.getString("StudentID"));
                        record.setStudent(Student.getStuByUsername(record.getStudentID()));
                        record.setCourseID(courseID);
                        record.setCourse(Course.getCourseById(courseID));
                        List <Object> attendInput = (List<Object>) document.get("Classes");
                        if (attendInput != null) {
                            for (int i=0;i<attendInput.size();i++) {
                                Map <String, String> data = new HashMap<>();
                                for (Map.Entry entry : ((Map<String, Object>) attendInput.get(i)).entrySet()) {
                                    data.put(entry.getKey().toString(), entry.getValue().toString());
                                }
                                Attendance attendance = new Attendance();
                                attendance.setClassID(data.get("ClassID"));
                                attendance.setClass(Class.getClassById(attendance.getClassID()));
                                attendance.setStudentID(record.getStudentID());
                                attendance.setStudent(record.getStudent());
                                attendance.setRemarks(data.get("Remarks"));
                                attendance.setAttendance(data.get("Attendance"));
                                record.addAttendance(attendance);
                            }
                        }

                        for (String activityID:(List<String>) document.get("Activities")) {
                            ClassActivity.retrieveActivityFromDB("Student", activityID, null, record, null);
                        }
                        int pos = getRecordPositionById(document.getId());
                        if (pos == -1) {
                            retrieved.add(record);
                        } else {
                            retrieved.get(pos).setStudentRecord(record);
                        }
                        Log.d(TAG, document.getId());
                    }
                    if (done != null) done.set(true);
                } else {
                    if (done != null) done.set(false);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                }
            });
    }

    public static int getRecordPositionById(String courseId) {
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getRecordID().equals(courseId))
                return i;
        }
        return -1;
    }

    public static StudentRecord getRecordById(String recordID) {
        for (int i = 0; i < retrieved.size(); i++) {
            if (retrieved.get(i).getRecordID().equals(recordID))
                return retrieved.get(i);
        }
        return null;
    }

    public static StudentRecord getRecordByStudentIDAndCourseID(String studentID, String courseID) {
        for(StudentRecord record:retrieved) {
            if (record.getCourseID().equalsIgnoreCase(courseID) && record.getStudentID().equalsIgnoreCase(studentID)) {
                return record;
            }
        }
        return null;
    }

    public static Map<String, Object> getStudentRecordModel(StudentRecord record) {
        Map<String, Object> data = new HashMap<>();
        data.put("StudentID", record.getStudentID());
        data.put("CourseID", record.getCourseID());
        List<Map<String, String>> attendances = new ArrayList<>();
        for (Attendance attendance:record.getAttendances()) {
            attendances.add(attendance.getAttendanceStudentRecordModel());
        }
        data.put("Classes", attendances);
        List<String> activities = new ArrayList<>();
        for (ClassActivity activity:record.getActivities()) {
            activities.add(activity.getActivityID());
        }
        data.put("Activities", activities);
        return data;
    }

    public static void saveStudentRecord(final StudentRecord studentRecord) {
        final CollectionReference ref = FirebaseFirestore.getInstance().collection("StudentRecord");
        if (studentRecord.getRecordID().isEmpty() || studentRecord.getRecordID().equalsIgnoreCase("toSet")) {
            ref.add(getStudentRecordModel(studentRecord))
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        studentRecord.setRecordID(task.getResult().getId());
                    }
                }
            });
        } else if (studentRecord.getRecordID().equalsIgnoreCase("toSearch")) {
            ref.whereEqualTo("CourseID", studentRecord.getCourseID())
                    .whereEqualTo("StudentID", studentRecord.getStudentID())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ref.document(document.getId()).set(getStudentRecordModel(studentRecord));
                            studentRecord.setRecordID(document.getId());
                        }
                    }
                }
            });
        } else {
            ref.document(studentRecord.getRecordID())
                .set(getStudentRecordModel(studentRecord));
        }
    }

    public static boolean hasStudentRecord(List<StudentRecord> records, String id) {
        return hasStudentRecord(records, id, "student");
    }

    public static boolean hasStudentRecord(List<StudentRecord> records, String id, String key) {
        for (StudentRecord record: records) {
            switch (key.toLowerCase()) {
                case "student":
                    if (record.getStudentID().equalsIgnoreCase(id))
                        return true;
                case "course":
                    if (record.getCourseID().equalsIgnoreCase(id))
                        return true;
                case "record":
                    if (record.getRecordID().equalsIgnoreCase(id))
                        return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public static StudentRecord getStudentRecord(List<StudentRecord> records, String id, String key) {
        for (StudentRecord record: records) {
            switch (key.toLowerCase()) {
                case "student":
                    if (record.getStudentID().equalsIgnoreCase(id))
                        return record;
                case "course":
                    if (record.getCourseID().equalsIgnoreCase(id))
                        return record;
                case "record":
                    if (record.getRecordID().equalsIgnoreCase(id))
                        return record;
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "StudentRecord{" + "\n" +
                "\t" + "recordID='" + recordID + '\'' + ",\n" +
                "\t" + "studentID='" + studentID + '\'' + ",\n" +
                "\t" + "courseID='" + courseID + '\'' + ",\n" +
                "\t" + "student=" + student + ",\n" +
                "\t" + "course=" + course + ",\n" +
                "\t" + "attendances=" + attendances + ",\n" +
                "\t" + "activities=" + activities + "\n" +
                '}';
    }
}
