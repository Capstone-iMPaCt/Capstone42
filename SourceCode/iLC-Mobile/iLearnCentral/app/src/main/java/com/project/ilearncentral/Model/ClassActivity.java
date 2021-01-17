package com.project.ilearncentral.Model;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class ClassActivity {
    private String activityID;
    private double perfectScore;
    private String activityTitle;
    private String activityDescription;
    private Class aClass;
    private String classID;

    private List<Student> students;
    private Map<String, Double> scores;

    private Student student;
    private String studentID;
    private double score;

    private boolean studentRecord;

    public static List<ClassActivity> retrieved = new ArrayList<>();

    public ClassActivity() {
        activityID = classID = studentID = activityTitle = activityDescription = "";
        perfectScore = 0.0;
        aClass = null;
        studentID = null;
        students = new ArrayList<>();
        score = 0.0;
        scores = new HashMap<>();
        studentRecord = false;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public boolean hasStudent(String studentID) {
        for (Student student: students) {
            if (student.getUsername().equalsIgnoreCase(studentID))
                return true;
        }
        return false;
    }

    public void addStudents(Student student) {
        this.students.add(student);
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    public void setScores(Map<String, Double> scores) {
        this.scores = scores;
    }

    public void addScore(String studentID, double score) {
        this.scores.put(studentID, score);
    }

    public double getPerfectScore() {
        return perfectScore;
    }

    public void setPerfectScore(double perfectScore) {
        this.perfectScore = perfectScore;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isStudentRecord() {
        return studentRecord;
    }

    public void setStudentRecord(boolean studentRecord) {
        this.studentRecord = studentRecord;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setActivity(ClassActivity classActivity) {
        this.activityID = classActivity.getActivityID();
        this.classID = classActivity.getClassID();
        this.studentID = classActivity.getStudentID();
        this.activityTitle = classActivity.getActivityTitle();
        this.perfectScore = classActivity.getPerfectScore();
        this.aClass = classActivity.getaClass();
        this.student = classActivity.getStudent();
        this.students = classActivity.getStudents();
        this.score = classActivity.getScore();
        this.scores = classActivity.getScores();
        this.activityDescription = classActivity.getActivityDescription();
        this.studentRecord = classActivity.isStudentRecord();
    }

    public static boolean isAdding(List<ClassActivity> activities) {
        for (int i=activities.size()-1;i>=0;i--) {
            if (activities.get(i).getActivityID().isEmpty())
                return true;
        }
        return false;
    }

    public static void retrieveActivityFromDB(String source, final String id, final Class aClass, final StudentRecord studentRecord, final ObservableBoolean done) {
        retrieved.clear();
        if (source.equalsIgnoreCase("Class") && aClass != null) {
            FirebaseFirestore.getInstance().collection("ClassActivity").whereEqualTo("ClassID", id)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ClassActivity activity = new ClassActivity();
                            activity.setActivityID(document.getId());
                            activity.setActivityTitle(document.getString("ActivityTitle"));
                            activity.setActivityDescription(document.getString("ActivityDescription"));
                            activity.setPerfectScore(document.getDouble("PerfectScore"));
                            List<Map<String, Object>> scores = (List<Map<String, Object>>) document.get("Scores");
                            if (scores!=null) {
                                for (Map<String, Object> score : scores) {
                                    activity.addScore(score.get("StudentID").toString(), Double.valueOf(score.get("Score").toString()));
                                    activity.addStudents(Student.getStuByUsername(score.get("StudentID").toString()));
                                }
                            }
                            activity.setClassID(document.getString("ClassID"));
                            activity.setaClass(Class.getClassById(activity.getClassID()));
                            activity.setStudentRecord(false);
                            aClass.addActivity(activity);
                            retrieved.add(activity);
                            Log.d("Activities", document.getId());
                        }
                        if (done!=null) done.set(true);
                    } else {
                        if (done!=null) done.set(false);
                        Log.d("Activities", "Error getting documents: ", task.getException());
                    }
                }
            });
        } else if (source.equalsIgnoreCase("Student")) {
            FirebaseFirestore.getInstance().collection("ClassActivity").whereArrayContains("Students", studentRecord.getStudentID())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equalsIgnoreCase(id)) {
                                ClassActivity activity = new ClassActivity();
                                activity.setActivityID(document.getId());
                                activity.setActivityTitle(document.getString("ActivityTitle"));
                                activity.setActivityDescription(document
                                        .getString("ActivityDescription"));
                                activity.setPerfectScore(document.getDouble("PerfectScore"));
                                List<Map<String, Double>> scores = (List<Map<String, Double>>) document
                                        .get("Scores");
                                if (scores != null) {
                                    for (Map<String, Double> score : scores) {
                                        for (Map.Entry entry : score.entrySet()) {
                                            if (entry.getKey().toString()
                                                    .equalsIgnoreCase(studentRecord
                                                            .getStudentID())) {
                                                activity.setScore((Double) entry.getValue());
                                                activity.setStudentID(studentRecord.getStudentID());
                                                activity.setStudent(studentRecord.getStudent());
                                            }
                                            activity.addStudents(Student
                                                    .getStuByUsername(entry.getKey().toString()));
                                        }
                                    }
                                }
                                activity.setClassID(document.getString("ClassID"));
                                activity.setaClass(Class.getClassById(activity.getClassID()));
                                activity.setStudentRecord(true);
                                studentRecord.addActivity(activity);
                                retrieved.add(activity);
                                Log.d("Activities", document.getId());
                            }
                        }
                        if (done!=null) done.set(true);
                    } else {
                        if (done!=null) done.set(false);
                        Log.d("Activities", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    public static Map<String, Object> getActivityClassModel(ClassActivity activity) {
        Map<String, Object > activityModel = new HashMap<>();
        activityModel.put("ActivityDescription", activity.getActivityDescription());
        activityModel.put("ActivityTitle", activity.getActivityTitle());
        activityModel.put("ClassID", activity.getClassID());
        activityModel.put("PerfectScore", activity.getPerfectScore());
        List<Map<String, Object>> scoresModel = new ArrayList<>();
        for (Map.Entry entry : activity.getScores().entrySet()) {
            Map<String, Object> studentScore = new HashMap<>();
            studentScore.put("StudentID", entry.getKey());
            studentScore.put("Score", entry.getValue());
            if (!studentScore.containsKey(entry.getKey()))
                scoresModel.add(studentScore);
        }
        activityModel.put("Scores", scoresModel);
        List<String> studentsModel = new ArrayList<>();
        for (Student student:activity.getStudents()) {
            studentsModel.add(student.getUsername());
        }
        activityModel.put("Students", studentsModel);
        return activityModel;
    }

    public static void saveActivity(final ClassActivity activity) {
        if (activity.getActivityID().isEmpty() || activity.getActivityID().equalsIgnoreCase("toSet")) {
            FirebaseFirestore.getInstance().collection("ClassActivity")
                    .add(getActivityClassModel(activity)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull final Task<DocumentReference> task) {
                    final String activityID = task.getResult().getId();
                    activity.setActivityID(activityID);
                    FirebaseFirestore.getInstance().collection("Class").document(activity.getClassID()).update("Activity", FieldValue
                            .arrayUnion(activityID));
                    for(Student student:activity.getStudents()) {
                        FirebaseFirestore.getInstance().collection("StudentRecord")
                                .whereEqualTo("StudentID", student.getUsername())
                                .whereEqualTo("CourseID", activity.getaClass().getCourseID()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        FirebaseFirestore.getInstance().collection("StudentRecord").document(document.getId()).update("Activities", FieldValue
                                                .arrayUnion(activityID));
                                    }
                                }
                            }
                        });
                    }
                }
            });
        } else {
            FirebaseFirestore.getInstance().collection("ClassActivity")
                    .document(activity.getActivityID())
                    .set(getActivityClassModel(activity));
        }
    }
}
