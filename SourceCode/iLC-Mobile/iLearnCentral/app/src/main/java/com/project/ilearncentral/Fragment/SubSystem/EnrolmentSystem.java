package com.project.ilearncentral.Fragment.SubSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ilearncentral.Activity.NveCourse;
import com.project.ilearncentral.Activity.NveJobPost;
import com.project.ilearncentral.Adapter.CourseAdapter;
import com.project.ilearncentral.Adapter.JobPostAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.CourseOffered;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Posts;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class EnrolmentSystem extends Fragment {

    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<CourseOffered> course;

    private ObservableBoolean done;
    private ObservableString editOrView;

    private FloatingActionButton addNewCourseBtn;
    private final int NEW_COURSE = 1, UPDATE_COURSE = 2;

    private SearchView searchView;
    private TextView toggleView, searchOption;
    private ImageButton toggleRecommend;

    private Drawable enableRecommend, disableRecommend;

    public EnrolmentSystem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subsystem_enrolment, container, false);
        super.onCreate(savedInstanceState);

        enableRecommend = getResources().getDrawable(R.drawable.enable_recommend_icon);
        disableRecommend = getResources().getDrawable(R.drawable.disable_recommend_icon);

        searchOption = view.findViewById(R.id.enrolment_toggle_view);

        addNewCourseBtn = view.findViewById(R.id.enrolment_add_fab);
        addNewCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), NveCourse.class), NEW_COURSE);
            }
        });

        toggleView = view.findViewById(R.id.enrolment_toggle_view);
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToggleView();
            }
        });

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.enrolment_pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!searchView.isIconified()) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
                searchView.clearFocus();
                JobPosts.retrievePostsFromDB(done);
                pullToRefresh.setRefreshing(false);
            }
        });

        course = new ArrayList<>();
        course.add(new CourseOffered("Open", "Type",3000,"Name","Description","8:00 AM","5:00 PM",""));
        course.add(new CourseOffered("Open", "Type",3000,"Name","Description","8:00 AM","5:00 PM",""));
        course.add(new CourseOffered("Open", "Type",3000,"Name","Description","8:00 AM","5:00 PM",""));
        recyclerView = view.findViewById(R.id.enrolment_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CourseAdapter(getContext(), editOrView, course);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void setToggleView() {
        if (toggleView.getText().toString().equalsIgnoreCase("Open")) {
            toggleView.setText("Closed");
            toggleView.setBackgroundResource(R.drawable.bg_unselected_day_rounded);
//            course.clear();
//            course.addAll(Posts.myPosts());
//            adapter.notifyDataSetChanged();
        } else {
            toggleView.setBackgroundResource(R.drawable.bg_selected_day_rounded);
            toggleView.setText("Open");
//            course.clear();
//            course.addAll(Posts.searchText(""));
//            adapter.notifyDataSetChanged();
        }
    }
}
