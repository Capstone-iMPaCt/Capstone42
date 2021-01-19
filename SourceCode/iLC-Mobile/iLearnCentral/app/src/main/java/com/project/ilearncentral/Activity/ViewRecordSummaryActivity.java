package com.project.ilearncentral.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Adapter.AttendanceAdapter;
import com.project.ilearncentral.Adapter.ClassActivityAdapter;
import com.project.ilearncentral.Adapter.ViewRecordSummaryAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Attendance;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.ClassActivity;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.Model.Student;
import com.project.ilearncentral.Model.StudentRecord;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewRecordSummaryActivity extends AppCompatActivity {

    private final String TAG = "ViewRecordSummary";
    private String courseID;
    private Course course;
    private Map<String, Student> studentMap;
    private Map<String, Class> classMap;
    private Map<String, StudentRecord> recordMap;
    private List<Student> students;
    private List<Class> classes;
    private List<StudentRecord> records;
    private List<Attendance> attendances;
    private List<ClassActivity> activities;
    private ObservableString actionAttendance, actionActivity;
    private ObservableBoolean studentsRetrieved, attendanceRetrieved, activityRetrieved;
    private ViewRecordSummaryAdapter attendanceSummaryAdapter, activitiesSummaryAdapter;
    private Context context;

    private RecyclerView attendanceRecycler, activitiesRecycler;
    private TextView attendanceLabel, attLoading, activitiesLabel, actLoading;
    private TextInputEditText studentNameInput, courseIDInput, courseTitleInput;
    private TextInputLayout studentNameLayout, courseIDLayout, courseTitleLayout;
    private Switch studentFocusedSwitch;
    private Button viewAttendanceBtn, viewActivityBtn;
    private ConstraintLayout attendanceHeader, activityHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record_summary);

        bindViews();

        context = this;
        attendances = new ArrayList<>();
        activities = new ArrayList<>();
        students = new ArrayList<>();
        classes = new ArrayList<>();
        records = new ArrayList<>();
        studentMap = new HashMap<>();
        classMap = new HashMap<>();
        recordMap = new HashMap<>();
        studentsRetrieved = new ObservableBoolean();
        actionAttendance = new ObservableString();
        actionActivity = new ObservableString();
        attendanceRetrieved = new ObservableBoolean();
        activityRetrieved = new ObservableBoolean();
        actLoading.setVisibility(View.VISIBLE);
        attLoading.setVisibility(View.VISIBLE);

        Intent i = getIntent();
        if (i.hasExtra("courseID")) {
            courseID = i.getStringExtra("courseID");
            course = Course.getCourseById(courseID);
            if (course == null) {
                exitCancelled();
            }
        }

        courseIDInput.setText(courseID);
        courseTitleInput.setText(course.getCourseName());
        if (Account.getType()== Account.Type.Student) {
            studentNameInput.setText(Student.getFullnameByUsername(Account.getUsername()));
            studentNameLayout.setVisibility(View.VISIBLE);
            studentFocusedSwitch.setVisibility(View.GONE);
            studentFocusedSwitch.setChecked(true);
        } else {
            studentNameLayout.setVisibility(View.GONE);
        }
        setClassesAndStudent();

        activityHeader.setVisibility(View.GONE);
        activitiesRecycler.setVisibility(View.GONE);

        viewAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceHeader.setVisibility(View.VISIBLE);
                attendanceRecycler.setVisibility(View.VISIBLE);
                activityHeader.setVisibility(View.GONE);
                activitiesRecycler.setVisibility(View.GONE);
                viewAttendanceBtn.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
                viewActivityBtn.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        });
        viewActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceHeader.setVisibility(View.GONE);
                attendanceRecycler.setVisibility(View.GONE);
                activityHeader.setVisibility(View.VISIBLE);
                activitiesRecycler.setVisibility(View.VISIBLE);
                viewActivityBtn.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
                viewAttendanceBtn.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        });

        attendanceRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    if (studentMap.size()==0) {
                        attLoading.setText("- No Students");
                    }
                    for (Class aClass: classList()) {
                        attLoading.setVisibility(View.GONE);
                        if (Account.getType()== Account.Type.Student) {
                            Student student = Student.getStuByUsername(Account.getUsername());
                            if (!Class.hasStudentInAttendance(aClass, student.getUsername())) {
                                Attendance a = new Attendance();
                                a.setStudentID(student.getUsername());
                                a.setStudent(student);
                                a.setClass(aClass);
                                a.setClassID(aClass.getClassID());
                                a.setAttendance("not set");
                                a.setRemarks("");
                                aClass.addAttendanceWithCheck(a);
                            }
                            if (!recordMap.containsKey(student.getUsername())) {
                                StudentRecord record = new StudentRecord();
                                record.setRecordID("toSearch");
                                record.setStudentID(student.getUsername());
                                record.setStudent(student);
                                record.setCourseID(courseID);
                                record.setCourse(course);
                                recordMap.put(student.getUsername(), record);
                            }
                            recordMap.get(student.getUsername()).addAttendanceWithCheck(
                                    Attendance.getAttendancesByStudentID(
                                            aClass.getAttendances(), student.getUsername()));
                        } else {
                            for (Student student : studentMap.values()) {
                                if (!Class.hasStudentInAttendance(aClass, student.getUsername())) {
                                    Attendance a = new Attendance();
                                    a.setStudentID(student.getUsername());
                                    a.setStudent(student);
                                    a.setClass(aClass);
                                    a.setClassID(aClass.getClassID());
                                    a.setAttendance("not set");
                                    a.setRemarks("");
                                    aClass.addAttendanceWithCheck(a);
                                }
                                if (!recordMap.containsKey(student.getUsername())) {
                                    StudentRecord record = new StudentRecord();
                                    record.setRecordID("toSearch");
                                    record.setStudentID(student.getUsername());
                                    record.setStudent(student);
                                    record.setCourseID(courseID);
                                    record.setCourse(course);
                                    recordMap.put(student.getUsername(), record);
                                }
                                recordMap.get(student.getUsername()).addAttendanceWithCheck(
                                        Attendance.getAttendancesByStudentID(
                                                aClass.getAttendances(), student.getUsername()));
                            }
                        }
                    }
                    classList();
                    recordList();
                    studentList();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    attendanceRecycler.setLayoutManager(linearLayoutManager);
                    attendanceSummaryAdapter = new ViewRecordSummaryAdapter(context, classList(), recordList(), studentFocusedSwitch.isChecked(), true, actionAttendance);
                    attendanceRecycler.setAdapter(attendanceSummaryAdapter);
                }
            }
        });
        activityRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    if (studentMap.size()==0) {
                        actLoading.setText("- No Students");
                    } else {
                        actLoading.setVisibility(View.GONE);
                        for (Class aClass : classList()) {
                            for (ClassActivity activity : aClass.getActivities()) {
                                if (Account.getType() == Account.Type.Student) {
                                    Student student = Student
                                            .getStuByUsername(Account.getUsername());
                                    if (!aClass.hasStudentInActivities(aClass, activity
                                            .getActivityID(), student
                                            .getUsername())) {
                                        activity.addScore(student.getUsername(), 0);
                                    }
                                    if (!activity.getStudents().contains(student))
                                        activity.addStudents(student);
                                    recordMap.get(student.getUsername()).addClassActivityWithCheck(activity);
                                } else {
                                    for (Student student : students) {
                                        if (!aClass.hasStudentInActivities(aClass, activity
                                                .getActivityID(), student
                                                .getUsername())) {
                                            activity.addScore(student.getUsername(), 0);
                                        }
                                        if (!activity.getStudents().contains(student))
                                            activity.addStudents(student);
                                        recordMap.get(student.getUsername())
                                                    .addClassActivityWithCheck(activity);
                                    }
                                }
                            }
                        }
                        classList();
                        recordList();
                        studentList();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        activitiesRecycler.setLayoutManager(linearLayoutManager);
                        activitiesSummaryAdapter = new ViewRecordSummaryAdapter(context, classList(), recordList(), studentFocusedSwitch.isChecked(), false, actionActivity);
                        activitiesRecycler.setAdapter(activitiesSummaryAdapter);
                    }
                }
            }
        });

        studentFocusedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                attendanceSummaryAdapter = new ViewRecordSummaryAdapter(context, classList(), recordList(), studentFocusedSwitch.isChecked(), true, actionAttendance);
                attendanceRecycler.setAdapter(attendanceSummaryAdapter);

                activitiesSummaryAdapter = new ViewRecordSummaryAdapter(context, classList(), recordList(), studentFocusedSwitch.isChecked(), false, actionActivity);
                activitiesRecycler.setAdapter(activitiesSummaryAdapter);
            }
        });

    }

    private void setClassesAndStudent() {
        final ObservableBoolean done = new ObservableBoolean();
        done.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    for (StudentRecord record: StudentRecord.getRetrieved()) {
                        if (!recordMap.containsKey(record.getRecordID()))
                            recordMap.put(record.getRecordID(), record);
                    }
                    setAttendance();
                    setActivities();
                }
            }
        });
        FirebaseFirestore.getInstance().collection("Enrolment").whereEqualTo("courseID", courseID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Student student = Student.getStuByUsername(document.getString("studentID"));
                                if (student!=null && !studentMap.containsKey(student.getUsername()))
                                    studentMap.put(student.getUsername(), student);
                                Log.d(TAG,"Student - " + document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        FirebaseFirestore.getInstance().collection("Class").whereEqualTo("CourseID", courseID).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            Class aClass = Class.getClassById(doc.getId());
                                            if (aClass!=null) {
                                                aClass.addStudents(studentList());
                                                if (!classMap.containsKey(aClass.getClassID())) {
                                                    classMap.put(aClass.getClassID(), aClass);
                                                }
                                            }
                                            Log.d(TAG, "Class - " + doc.getId());
                                        }
                                        StudentRecord.retrieveStatusRecordFromDB(courseID, done);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    }
                });
    }

    private List<Student> studentList() {
        students.clear();
        students.addAll(studentMap.values());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            students.sort(new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return s1.getFullname().compareTo(s2.getFullname());
                }
            });
        }
        return students;
    }
    private List<Class> classList() {
        classes.clear();
        classes.addAll(classMap.values());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            classes.sort(new Comparator<Class>() {
                @Override
                public int compare(Class c1, Class c2) {
                    return c1.getClassStart().compareTo(c2.getClassStart());
                }
            });
        }
        return classes;
    }
    private List<StudentRecord> recordList() {
        records.clear();
        records.addAll(recordMap.values());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            records.sort(new Comparator<StudentRecord>() {
                @Override
                public int compare(StudentRecord r1, StudentRecord r2) {
                    return r1.getStudent().getFullname().compareTo(r2.getStudent().getFullname());
                }
            });
        }
        return records;
    }

    private void setAttendance() {
        attLoading.setVisibility(View.VISIBLE);
        for (Class aClass : classList()) {
            Attendance.retrieveAttendanceFromDB("Class", aClass
                    .getClassID(), aClass, null, attendanceRetrieved);
        }
    }
    private void setActivities() {
        attLoading.setVisibility(View.VISIBLE);
        for (Class aClass: classList()) {
            ClassActivity.retrieveActivityFromDB("Class", aClass.getClassID(), aClass, null, activityRetrieved);
        }
    }

    private void exitCancelled() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void bindViews() {
        courseIDInput = findViewById(R.id.view_record_summary_course_id);
        courseIDLayout = findViewById(R.id.view_record_summary_course_id_layout);
        courseTitleInput = findViewById(R.id.view_record_summary_course_title);
        courseIDLayout = findViewById(R.id.view_record_summary_course_title_layout);
        studentNameInput = findViewById(R.id.view_record_summary_student_name);
        studentNameLayout = findViewById(R.id.view_record_summary_student_name_layout);
        activitiesRecycler = findViewById(R.id.view_record_summary_activities_recycler);
        activitiesLabel = findViewById(R.id.view_record_summary_activities_label);
        actLoading = findViewById(R.id.view_record_summary_activities_loading);
        attendanceRecycler = findViewById(R.id.view_record_summary_attendance_recycler);
        attendanceLabel = findViewById(R.id.view_record_summary_attendance_label);
        attLoading = findViewById(R.id.view_record_summary_attendance_loading);
        studentFocusedSwitch = findViewById(R.id.view_record_summary_focused_student);
        viewAttendanceBtn = findViewById(R.id.view_record_summary_attendance_toggle);
        viewActivityBtn = findViewById(R.id.view_record_summary_activity_toggle);
        attendanceHeader = findViewById(R.id.view_record_summary_attendance_header);
        activityHeader = findViewById(R.id.view_record_summary_activity_header);
    }
}