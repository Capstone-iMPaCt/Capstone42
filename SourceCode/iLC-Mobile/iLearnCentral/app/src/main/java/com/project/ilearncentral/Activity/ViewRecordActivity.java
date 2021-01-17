package com.project.ilearncentral.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Attendance;
import com.project.ilearncentral.Model.ClassActivity;
import com.project.ilearncentral.Model.Student;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.StudentRecord;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class ViewRecordActivity extends AppCompatActivity {

    private final String TAG = "ViewRecord";
    private List<Student> students;
    private List<String> studentIDs;
    private List<Attendance> attendances;
    private List<ClassActivity> activities;
    private String classID, action;
    private Class aClass;
    private StudentRecord record;
    private ObservableString actionAttendance;
    private ObservableBoolean studentsRetrieved, attendanceRetrieved, activityRetrieved;
    private AttendanceAdapter attendanceAdapter;
    private ClassActivityAdapter activityAdapter;

    private RecyclerView attendanceRecycler, activitiesRecycler;
    private TextView lessonPlan, lessonPlanLabel, attendanceLabel, attLoading, activitiesLabel, actLoading;
    private TextInputEditText lessonPlanInput;
    private TextInputLayout lessonPlanLayout;
    private Button lessonPlanEdit, attendanceEdit, activitiesAdd, lessonPlanReset, attendanceReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        bindViews();

        attendances = new ArrayList<>();
        activities = new ArrayList<>();
        studentIDs = new ArrayList<>();
        students = new ArrayList<>();
        actionAttendance = new ObservableString();
        studentsRetrieved = new ObservableBoolean();
        attendanceRetrieved = new ObservableBoolean();
        activityRetrieved = new ObservableBoolean();
        actLoading.setVisibility(View.VISIBLE);
        attLoading.setVisibility(View.VISIBLE);

        Intent i = getIntent();
        if (i.hasExtra("classID") && Account.getType()!= Account.Type.Student) {
            classID = i.getStringExtra("classID");
            action = i.getStringExtra("action");
            aClass = Class.getClassById(classID);
            if (aClass == null) {
                exitCancelled();
            }
        }

        lessonPlan.setText(aClass.getLessonPlan());
        lessonPlanInput.setText(aClass.getLessonPlan());
        if (aClass.getLessonPlan().isEmpty())
            lessonPlan.setVisibility(View.GONE);

        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        attendanceAdapter = new AttendanceAdapter(this, attendances, aClass, actionAttendance);
        attendanceRecycler.setAdapter(attendanceAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        activitiesRecycler.setLayoutManager(linearLayoutManager);
        activityAdapter = new ClassActivityAdapter(this, activities, aClass);
        activitiesRecycler.setAdapter(activityAdapter);

        setStudent();

        attendanceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (attendanceEdit.getText().toString()) {
                    case "Edit":
                        attendanceEdit.setText("Save");
                        actionAttendance.set("edit");
                        attendanceReset.setVisibility(View.VISIBLE);
                        break;
                    case "Save":
                        attendanceEdit.setText("Edit");
                        actionAttendance.set("save");
                        attendanceReset.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
        attendanceReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceReset.setVisibility(View.VISIBLE);
                actionAttendance.set("reset");
            }
        });

        lessonPlanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lessonPlanEdit.getText().toString().equalsIgnoreCase("Edit")) {
                    lessonPlanEdit.setText("Save");
                    lessonPlanLayout.setVisibility(View.VISIBLE);
                    lessonPlan.setVisibility(View.GONE);
                    lessonPlanReset.setVisibility(View.VISIBLE);
                } else {
                    String lesson = lessonPlanInput.getText().toString();
                    lessonPlanEdit.setText("Edit");
                    lessonPlanLayout.setVisibility(View.GONE);
                    lessonPlan.setVisibility(View.VISIBLE);
                    lessonPlan.setText(lesson);
                    lessonPlanReset.setVisibility(View.GONE);
                    aClass.setLessonPlan(lesson);
                    FirebaseFirestore.getInstance().collection("Class").document(classID)
                            .update("LessonPlan", lesson);
                }
            }
        });
        lessonPlanReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonPlan.setText(aClass.getLessonPlan());
                lessonPlanInput.setText(aClass.getLessonPlan());
            }
        });

        studentsRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    for (Student student: students) {

                    }
                }
            }
        });

        attendanceRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    if (students.size()==0) {
                        attLoading.setText("- No Students");
                    } else {
                        attLoading.setVisibility(View.GONE);
                        for (Student student : students) {
                            if (!Class.hasStudentInAttendance(aClass, student.getUsername())) {
                                Attendance a = new Attendance();
                                a.setStudentID(student.getUsername());
                                a.setStudent(student);
                                a.setClass(aClass);
                                a.setClassID(aClass.getClassID());
                                a.setAttendance("not set");
                                a.setRemarks("");
                                aClass.addAttendance(a);
                            }
                        }
                        attendances.addAll(aClass.getAttendances());
                        attendanceAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        activityRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    if (aClass.getActivities().size()==0) {
                        actLoading.setText("- None");
                    } else {
                        actLoading.setVisibility(View.GONE);
                        for (ClassActivity activity:aClass.getActivities()) {
                            for (Student student:students) {
                                if (!Class.hasStudentInActivities(aClass, activity.getActivityID(), student
                                        .getUsername())) {
                                    activity.addScore(student.getUsername(), 0);
                                }
                                if (!activity.getStudents().contains(student))
                                    activity.addStudents(student);
                            }
                        }
                        activities.addAll(aClass.getActivities());
                        activityAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        activitiesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClassActivity.isAdding(activities)) {
                    ClassActivity a = new ClassActivity();
                    a.setaClass(aClass);
                    a.setClassID(aClass.getClassID());
                    a.setActivityDescription("");
                    a.setActivityTitle("");
                    a.setPerfectScore(0);
                    a.setStudents(students);
                    for (Student student : students) {
                        a.addScore(student.getUsername(), 0);
                    }
                    a.setStudentRecord(false);
                    activities.add(a);
                    aClass.addActivity(a);
                    actLoading.setVisibility(View.GONE);
                    System.out.println("Add new Activity " + activities.size());
                    activityAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setStudent() {
        FirebaseFirestore.getInstance().collection("Enrolment").whereEqualTo("courseID", aClass.getCourseID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String studentID = document.getString("studentID");
                                studentIDs.add(studentID);
                                Student student = Student.getStuByUsername(studentID);
                                if (student!=null)
                                    students.add(student);
                                Log.d(TAG, document.getId());
                            }
                            setAttendance();
                            setActivity();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setAttendance() {
        if (Account.getType() == Account.Type.Educator) {
            attLoading.setVisibility(View.VISIBLE);
            Attendance.retrieveAttendanceFromDB("Class", classID, aClass, null, attendanceRetrieved);
        }
    }

    private void setActivity() {
        if (Account.getType() == Account.Type.Educator) {
            actLoading.setVisibility(View.VISIBLE);
            ClassActivity.retrieveActivityFromDB("Class", classID, aClass, null, activityRetrieved);
        }
    }

    private void exitCancelled() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void bindViews() {
        lessonPlan = findViewById(R.id.view_record_lesson_plan);
        lessonPlanEdit = findViewById(R.id.view_record_lesson_button);
        lessonPlanInput = findViewById(R.id.view_record_lesson_edit_input);
        lessonPlanLayout = findViewById(R.id.view_record_lesson_edit_layout);
        lessonPlanLabel = findViewById(R.id.view_record_lesson_label);
        lessonPlanLabel = findViewById(R.id.view_record_lesson_label);
        lessonPlanReset = findViewById(R.id.view_record_lesson_reset);
        attendanceReset = findViewById(R.id.view_record_attendance_reset);
        activitiesAdd = findViewById(R.id.view_record_activities_add);
        activitiesRecycler = findViewById(R.id.view_record_activities_recycler);
        activitiesLabel = findViewById(R.id.view_record_activities_label);
        actLoading = findViewById(R.id.view_record_activities_loading);
        attendanceEdit = findViewById(R.id.view_record_attendance_edit);
        attendanceRecycler = findViewById(R.id.view_record_attendance_recycler);
        attendanceLabel = findViewById(R.id.view_record_attendance_label);
        attLoading = findViewById(R.id.view_record_attendance_loading);
    }
}