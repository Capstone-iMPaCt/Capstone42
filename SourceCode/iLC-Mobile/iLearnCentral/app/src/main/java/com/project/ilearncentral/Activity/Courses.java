package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;

import com.project.ilearncentral.Adapter.CourseAdapter;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class Courses extends AppCompatActivity {

    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Course> course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subsystem_enrolment);

        disableFunctions();

        course = new ArrayList<>();
        course.add(new Course("Open", "Type",5000.00,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name Instructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor Name"));
        course.add(new Course("Open", "Type",4000.0,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",3000.25,"Course Title","Description Description Description Description Description Description Description Description Description ","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",2000,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",500,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",5000.00,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name Instructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor Name"));
        course.add(new Course("Open", "Type",4000.0,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",3000.25,"Course Title","Description Description Description Description Description Description Description Description Description ","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",2000,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",500,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",5000.00,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name Instructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor Name"));
        course.add(new Course("Open", "Type",4000.0,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",3000.25,"Course Title","Description Description Description Description Description Description Description Description Description ","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",2000,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",500,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",5000.00,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name Instructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor Name"));
        course.add(new Course("Open", "Type",4000.0,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",3000.25,"Course Title","Description Description Description Description Description Description Description Description Description ","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",2000,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",500,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",5000.00,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name Instructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor NameInstructor Name"));
        course.add(new Course("Open", "Type",4000.0,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",3000.25,"Course Title","Description Description Description Description Description Description Description Description Description ","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",2000,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        course.add(new Course("Open", "Type",500,"Course Title","Description ...","8:00 AM","5:00 PM","", "Instructor Name"));
        recyclerView = findViewById(R.id.enrolment_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this, course);
        recyclerView.setAdapter(adapter);
    }

    private void disableFunctions() {
        findViewById(R.id.enrolment_app_bar_vertical_line_divider).setVisibility(View.GONE);
        findViewById(R.id.enrolment_toggle_view).setVisibility(View.GONE);
        findViewById(R.id.enrolment_app_bar_horizontal_line_divider).setVisibility(View.GONE);
        findViewById(R.id.enrolment_options_layout).setVisibility(View.GONE);
        findViewById(R.id.enrolment_add_fab).setVisibility(View.GONE);
    }
}