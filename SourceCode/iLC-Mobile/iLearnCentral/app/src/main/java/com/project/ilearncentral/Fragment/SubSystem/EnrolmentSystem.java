package com.project.ilearncentral.Fragment.SubSystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.Activity.Enrollees;
import com.project.ilearncentral.Activity.EnrolmentPaymentRecords;
import com.project.ilearncentral.Activity.NveCourse;
import com.project.ilearncentral.Adapter.CourseAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Subscription;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EnrolmentSystem extends Fragment {

    private static final String TAG = "EnrolmentSystem";
    private CourseAdapter adapter;
    private RecyclerView recyclerView;
    private List<Course> course;
    private List<Course> retrieved;

    private ObservableBoolean show;
    private boolean isSubscribed;

    private FloatingActionButton addNewCourseBtn;
    private final int NEW_COURSE = 1, UPDATE_COURSE = 2;

    private SearchView searchView;
    private Dialog dialog;
    private TextView noCoursesText, subscriptionExpiry;
    private Button enrollees, pendingEnrolees, paymentRecords;
    private ImageButton enrolmentViewOption;

    private FirebaseFirestore db;

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
        window.setLayout(Utility.dpToPx(getContext(), 300), LinearLayout.LayoutParams.WRAP_CONTENT);

        bindLayout(view);
        course = new ArrayList<>();

        if (Account.isType("Student")) {
            subscriptionExpiry.setVisibility(View.GONE);
            view.findViewById(R.id.enrolment_app_bar_vertical_line_divider).setVisibility(View.GONE);
            view.findViewById(R.id.enrolment_app_bar_option_button).setVisibility(View.GONE);
            addNewCourseBtn.setVisibility(View.GONE);
        } else if (Account.isType("LearningCenter")) {
            enrolmentViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isSubscribed) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Please subscribe");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    dialog.setCancelable(true);
                    dialog.show();
                    enrollees.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), Enrollees.class));
                            dialog.dismiss();
                        }
                    });
                    pendingEnrolees.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), Enrollees.class));
                            dialog.dismiss();
                        }
                    });
                    paymentRecords.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), EnrolmentPaymentRecords.class));
                            dialog.dismiss();
                        }
                    });
                }
            });
            addNewCourseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSubscribed) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Please subscribe");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    startActivityForResult(new Intent(getContext(), NveCourse.class), NEW_COURSE);
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
                    }
                }
            });
        }
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CourseAdapter(getContext(), course);
        recyclerView.setAdapter(adapter);
        retrieveCourses();

        return view;
    }

    private void bindLayout(View view) {
        subscriptionExpiry = view.findViewById(R.id.enrolment_subscription_status);
        searchView = view.findViewById(R.id.enrolment_app_bar_searchview);
        enrolmentViewOption = view.findViewById(R.id.enrolment_app_bar_option_button);
        noCoursesText = view.findViewById(R.id.enrolment_courses_none);
        addNewCourseBtn = view.findViewById(R.id.enrolment_add_fab);
        recyclerView = view.findViewById(R.id.enrolment_recylerview);

        // Search Menu
        enrollees = dialog.findViewById(R.id.enrolment_search_option_enrollees);
        pendingEnrolees = dialog.findViewById(R.id.enrolment_search_option_pending_enrolees);
        paymentRecords = dialog.findViewById(R.id.enrolment_search_option_payment_records);

        db = FirebaseFirestore.getInstance();
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
        setSubscriptionStatus();
    }

    private void setSubscriptionStatus() {
        subscriptionExpiry.setText("Subscribe to enable this feature.");
        db.collection("Subscription")
                .document(Account.getCenterId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.getData().containsKey("EnrolmentSystem")) {
                                    Map<String, Object> data = (Map<String, Object>) document.get("EnrolmentSystem");
                                    Timestamp timestamp = (com.google.firebase.Timestamp) data.get("SubscriptionExpiry");
                                    Date dateNow = new Date();
                                    if (dateNow.compareTo(timestamp.toDate()) < 0) {
                                        // If dateNow occurs before SubscriptionExpiry
                                        subscriptionExpiry.setText("Subscription expires on: " + timestamp.toDate());
                                        isSubscribed = true;
                                        Subscription.setEnrolmentSubscriptionStatus(isSubscribed);
                                    } else {
                                        subscriptionExpiry.setText("Subscribe to enable this feature.");
                                        isSubscribed = false;
                                        Subscription.setEnrolmentSubscriptionStatus(isSubscribed);
                                    }
                                }
                            }
                        }
                    }
                });
    }
}
