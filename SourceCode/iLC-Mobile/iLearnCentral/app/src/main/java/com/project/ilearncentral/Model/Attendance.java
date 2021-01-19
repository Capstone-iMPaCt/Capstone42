package com.project.ilearncentral.Model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Attendance {
    private Student student;
    private Class aClass;
    private Course course;
    private String studentID, classID, courseID;
    private String remarks;
    private String attendance;
    private boolean isSaved;

    public static List<Attendance> retrieved = new ArrayList<>();

    public Attendance() {
        studentID = classID = courseID = remarks = attendance = "";
        student = null;
        aClass = null;
        course = null;
        isSaved = false;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
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

    public void setAttendance(Attendance attendance) {
        this.studentID = attendance.getStudentID();
        this.classID = attendance.getClassID();
        this.courseID = attendance.getCourseID();
        this.remarks = attendance.getRemarks();
        this.attendance = attendance.getAttendance();
        this.student = attendance.getStudent();
        this.aClass = attendance.getaClass();
        this.course = attendance.getCourse();
        this.isSaved = attendance.isSaved();
    }

    public void retrieveModels() {
        if (!studentID.isEmpty()) {
            student = Student.getStuByUsername(studentID);
        }
        if (!classID.isEmpty()) {
            aClass = Class.getClassById(classID);
        }
    }

    public Map<String, String> getAttendanceClassModel() {
        Map<String, String> attend = new HashMap<>();
        attend.put("Attendance", this.getAttendance());
        attend.put("Remarks", this.getRemarks());
        attend.put("StudentID", this.getStudentID());
        return attend;
    }

    public Map<String, String> getAttendanceStudentRecordModel() {
        Map<String, String> attend = new HashMap<>();
        attend.put("Attendance", this.getAttendance());
        attend.put("Remarks", this.getRemarks());
        attend.put("ClassID", this.getClassID());
        return attend;
    }

    public static List<Map<String, String>> getAttendanceClassModel(List<Attendance> attendances) {
        List<Map<String, String >> attendanceList = new ArrayList<>();
        List<String> done = new ArrayList<>();
        for (Attendance attendance:attendances) {
            if (!done.contains(attendance.getStudentID())) {
                done.add(attendance.getStudentID());
                Map<String, String> attend = new HashMap<>();
                attend.put("Attendance", attendance.getAttendance());
                attend.put("Remarks", attendance.getRemarks());
                attend.put("StudentID", attendance.getStudentID());
                attendanceList.add(attend);
            }
        }
        return attendanceList;
    }

    public static List<Map<String, String>> getAttendanceStudentRecordModel(List<Attendance> attendances) {
        List<Map<String, String >> attendanceList = new ArrayList<>();
        List<String> done = new ArrayList<>();
        for (Attendance attendance:attendances) {
            if (!done.contains(attendance.getClassID())) {
                done.add(attendance.getStudentID());
                Map<String, String> attend = new HashMap<>();
                attend.put("Attendance", attendance.getAttendance());
                attend.put("Remarks", attendance.getRemarks());
                attend.put("ClassID", attendance.getClassID());
                attendanceList.add(attend);
            }
        }
        return attendanceList;
    }

    public static void clearRedundant(List<Attendance> attendances, String key) {
        List<String> done = new ArrayList<>();
        for (Attendance a: attendances) {
            if (key.equalsIgnoreCase("StudentID")) {
                if (done.contains(a.getStudentID())) {
                    attendances.remove(a);
                } else {
                    done.add(a.getStudentID());
                }
            } else if (key.equalsIgnoreCase("ClassID")) {
                if (done.contains(a.getClassID())) {
                    attendances.remove(a);
                } else {
                    done.add(a.getClassID());
                }
            }
        }
    }

    public static void retrieveAttendanceFromDB(String source, String id, final Class aClass, final StudentRecord studentRecord, final ObservableBoolean done) {
        retrieved.clear();
        if (source.equalsIgnoreCase("Class") && aClass != null) {
            FirebaseFirestore.getInstance().collection("Class").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<Object> attendInput = (List<Object>) document.get("Attendance");
                        if (attendInput != null) {
                            for (int i=0;i<attendInput.size();i++) {
                                Map<String, String> data = new HashMap<>();
                                for (Map.Entry entry : ((Map<String, Object>) attendInput.get(i)).entrySet()) {
                                    data.put(entry.getKey().toString(), entry.getValue().toString());
                                }
                                Attendance attendance = new Attendance();
                                attendance.setClassID(aClass.getClassID());
                                attendance.setClass(Class.getClassById(aClass.getClassID()));
                                attendance.setStudentID(data.get("StudentID"));
                                attendance.setStudent(Student.getStuByUsername(attendance.getStudentID()));
                                attendance.setRemarks(data.get("Remarks"));
                                attendance.setAttendance(data.get("Attendance"));
                                attendance.setSaved(true);
                                retrieved.add(attendance);
                            }
                        }
                        if(done!=null) done.set(true);
                        if (aClass!=null) aClass.addAttendance(retrieved);
                    } else {
                        if(done!=null) done.set(false);
                    }
                }
            });
        } else if (source.equalsIgnoreCase("Student") && studentRecord != null) {
            FirebaseFirestore.getInstance().collection("StudentRecord")
                    .whereEqualTo("StudentID", id.substring(0, id.indexOf("-")))
                    .whereEqualTo("CourseID", id.substring(id.indexOf("-")+1))
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                        List<Object> attendInput = (List<Object>) document.get("Attendance");
                        if (attendInput != null) {
                            for (int i=0;i<attendInput.size();i++) {
                                Map<String, String> data = new HashMap<>();
                                for (Map.Entry entry : ((Map<String, Object>) attendInput.get(i)).entrySet()) {
                                    data.put(entry.getKey().toString(), entry.getValue().toString());
                                }
                                Attendance attendance = new Attendance();
                                attendance.setClassID(aClass.getClassID());
                                attendance.setClass(Class.getClassById(aClass.getClassID()));
                                attendance.setStudentID(data.get("StudentID"));
                                attendance.setStudent(Student.getStuByUsername(attendance.getStudentID()));
                                attendance.setRemarks(data.get("Remarks"));
                                attendance.setAttendance(data.get("Attendance"));
                                retrieved.add(attendance);
                            }}
                        }
                        if(done!=null) done.set(true);
                        if (studentRecord!=null) studentRecord.addAttendance(retrieved);
                    } else {
                        if(done!=null) done.set(false);
                    }
                }
            });
        }
    }

    public static void saveAttendance(final Class aClass, final StudentRecord studentRecord) {
        if (aClass!=null) {
            FirebaseFirestore.getInstance().collection("Class").document(aClass.getClassID())
                    .update("Attendance", Attendance.getAttendanceClassModel(aClass.getAttendances()));
        }
        if (studentRecord != null) {
            FirebaseFirestore.getInstance().collection("StudentRecord").document(studentRecord.getRecordID())
                    .update("Classes", Attendance.getAttendanceStudentRecordModel(studentRecord.getAttendances()));
        }
    }

    public static Attendance getAttendancesByStudentID(List<Attendance> attendances, String studentID) {
        for (Attendance attendance:attendances) {
            if (attendance.getStudentID().equalsIgnoreCase(studentID))
                return attendance;
        }
        return null;
    }

    public static Attendance getAttendancesByClassID(List<Attendance> attendances, String classID) {
        for (Attendance attendance:attendances) {
            if (attendance.getClassID().equalsIgnoreCase(classID))
                return attendance;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Attendance{" + "\n" +
                "\t" + "student=" + student + ",\n" +
                "\t" + "aClass=" + aClass + ",\n" +
                "\t" + "course=" + course + ",\n" +
                "\t" + "studentID='" + studentID + '\'' + ",\n" +
                "\t" + "classID='" + classID + '\'' + ",\n" +
                "\t" + "courseID='" + courseID + '\'' + ",\n" +
                "\t" + "remarks='" + remarks + '\'' + ",\n" +
                "\t" + "attendance='" + attendance + '\'' + ",\n" +
                "\t" + "isSaved=" + isSaved + "\n" +
                '}';
    }
}
