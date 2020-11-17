package com.project.ilearncentral.Fragment.SubSystem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ilearncentral.Activity.Enrollees;
import com.project.ilearncentral.Activity.NveCourse;
import com.project.ilearncentral.Adapter.CourseAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class EnrolmentSystem extends Fragment {

    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private List<Course> course;
    private List<Course> retrieved;

    private ObservableBoolean show;

    private FloatingActionButton addNewCourseBtn;
    private final int NEW_COURSE = 1, UPDATE_COURSE = 2;

    private SearchView searchView;
    private Dialog dialog;
    private TextView noCoursesText;
    private Button enrollees, enroll;
    private ImageButton enrolmentViewOption;

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
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_dialog_enrolment_search_option);
        Window window = dialog.getWindow();
        window.setLayout(Utility.dpToPx(getContext(),300), LinearLayout.LayoutParams.WRAP_CONTENT);

        bindLayout(view);
        course = new ArrayList<>();

        if (Account.isType("Student")) {
            view.findViewById(R.id.enrolment_app_bar_vertical_line_divider).setVisibility(View.GONE);
            view.findViewById(R.id.enrolment_app_bar_option_button).setVisibility(View.GONE);
            addNewCourseBtn.setVisibility(View.GONE);
            enroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        enrolmentViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                final List<String> options = new ArrayList<>();
//                options.add("Enrollees");
//                options.add("");
//                options.add("");
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i) {
//                            case 0:
//                                startActivity(new Intent(getActivity(), Enrollees.class));
//                                break;
//                            default:
//                                Toast.makeText(getActivity(), options.get(i) + " Clicked!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                builder.create().show();

                dialog.setCancelable(true);
                dialog.show();
                enrollees.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), Enrollees.class));
                        dialog.dismiss();
                    }
                });
            }
        });
        addNewCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), NveCourse.class), NEW_COURSE);
            }
        });
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.enrolment_pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (!searchView.isIconified()) {
//                    searchView.setQuery("", false);
//                    searchView.setIconified(true);
//                }
//                searchView.clearFocus();
//                JobPosts.retrievePostsFromDB(done);
                pullToRefresh.setRefreshing(false);
            }
        });
        show = new ObservableBoolean();
        show.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    course.clear();
                    course.addAll(retrieved);
                    if (course.isEmpty()) {
                        noCoursesText.setVisibility(View.VISIBLE);
                    } else {
                        noCoursesText.setVisibility(View.GONE);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    System.out.println("~~~~~~~~~~~Adapter item count after datachanged " + adapter.getItemCount());
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CourseAdapter(getContext(), course);
        recyclerView.setAdapter(adapter);
        System.out.println("~~~~~~~~~~~Adapter item count " + adapter.getItemCount());
        retrieveCourses();

        return view;
    }

    private void bindLayout(View view) {
        searchView = view.findViewById(R.id.enrolment_app_bar_searchview);
        enrolmentViewOption = view.findViewById(R.id.enrolment_app_bar_option_button);
        noCoursesText = view.findViewById(R.id.enrolment_courses_none);
        addNewCourseBtn = view.findViewById(R.id.enrolment_add_fab);
        recyclerView = view.findViewById(R.id.enrolment_recylerview);
        enroll = view.findViewById(R.id.course_enroll_button);

        // Search Menu
        enrollees = dialog.findViewById(R.id.enrolment_search_option_enrollees);
    }

    private void retrieveCourses() {
        retrieved = Course.getRetrieved();
        if (Account.getType() == Account.Type.Student) {
            retrieved = Course.filterCourses(retrieved, "status", "open");
        } else if (Account.getType() == Account.Type.Educator) {
            retrieved = Course.filterCourses(retrieved, "instructor", Account.getName());
        } else {
            retrieved = Course.getCoursesByCenterId(Account.getCenterId());
        }
        show.set(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveCourses();
    }
}
