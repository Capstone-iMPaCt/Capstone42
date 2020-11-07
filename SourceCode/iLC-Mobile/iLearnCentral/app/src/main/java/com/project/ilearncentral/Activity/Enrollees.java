package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.GridView;

import com.project.ilearncentral.Adapter.CourseAdapter;
import com.project.ilearncentral.Adapter.EnrolleeAdapter;
import com.project.ilearncentral.Model.Enrollee;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class Enrollees extends AppCompatActivity {

    private ArrayList<Enrollee> enrollee;
    private GridView gridView;
    EnrolleeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollees);

        enrollee = new ArrayList<Enrollee>();
        gridView = findViewById(R.id.enrollees_gridview);
        enrollee.add(new Enrollee("User NameUser NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        enrollee.add(new Enrollee("User NameUser Name","Course Title"));
        enrollee.add(new Enrollee("User Name User Name User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title Course Title"));
        enrollee.add(new Enrollee("User Name","Course Title CourseTitle Course Title"));
        adapter = new EnrolleeAdapter(this, enrollee);
        gridView.setAdapter(adapter);
    }
}