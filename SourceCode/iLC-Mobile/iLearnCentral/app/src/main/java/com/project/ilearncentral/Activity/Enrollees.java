package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.project.ilearncentral.Adapter.EnrolleeAdapter;
import com.project.ilearncentral.Model.Enrollee;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class Enrollees extends AppCompatActivity {

    private ArrayList<Enrollee> enrollees;
    private GridView gridView;
    private EnrolleeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollees);

        enrollees = new ArrayList<>();
        gridView = findViewById(R.id.enrollees_gridview);
        enrollees.add(new Enrollee("User NameUser NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollees.add(new Enrollee("User NameUser Name","Course Title"));
        enrollees.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title Course Title"));
        enrollees.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        adapter = new EnrolleeAdapter(this, enrollees);
        gridView.setAdapter(adapter);
    }
}