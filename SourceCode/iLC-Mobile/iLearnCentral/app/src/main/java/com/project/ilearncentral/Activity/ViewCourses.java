package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.project.ilearncentral.Adapter.CourseAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCourses extends AppCompatActivity {

    private List<Course> retrieved;
    private List<Course> courses;
    private LearningCenter lc;

    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ObservableBoolean show;
    private TextView noCoursesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses);

        lc = LearningCenter.getLCById(Account.getStringData("openLC"));
        courses = new ArrayList<Course>();
        recyclerView = findViewById(R.id.courses_recyclerview);
        searchView = findViewById(R.id.courses_search_view);
        noCoursesText = findViewById(R.id.view_courses_none);
        show = new ObservableBoolean();
        show.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    courses.clear();
                    courses.addAll(retrieved);
                    if (courses.isEmpty()) {
                        noCoursesText.setVisibility(View.VISIBLE);
                    } else {
                        noCoursesText.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchBusinessName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                show.set(true);
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this, courses);
        recyclerView.setAdapter(adapter);
        retrieved = Course.getCoursesByCenterId(lc.getCenterId());
        show.set(true);
    }
}