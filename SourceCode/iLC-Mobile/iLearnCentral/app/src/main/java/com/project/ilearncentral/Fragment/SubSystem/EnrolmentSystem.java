package com.project.ilearncentral.Fragment.SubSystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.project.ilearncentral.Model.Enrolment;
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
    private List<Course> courseList;
    private List<Course> pendingCourseList;
    private List<Course> enrolledCourseList;
    private List<Course> retrievedList;
    private List<Enrolment> enrolmentList;

    private ObservableBoolean show, enrolmentStatusListener, courseListener;
    private boolean isSubscribed;

    private FloatingActionButton addNewCourseBtn;
    private final int NEW_COURSE = 1, UPDATE_COURSE = 2;

    private SearchView searchView;
    private Dialog dialog;
    private TextView all, enrolled, pending;
    private TextView noCoursesText, subscriptionExpiry;
    private Button enrolees, pendingEnrolees, paymentRecords;
    private ImageButton enrolmentViewOption;

    private FirebaseFirestore db;

    public EnrolmentSystem() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setTextViewFocusEffect(all);
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

        if (Account.isType("Student")) {
            subscriptionExpiry.setVisibility(View.GONE);
            view.findViewById(R.id.enrolment_app_bar_vertical_line_divider).setVisibility(View.GONE);
            view.findViewById(R.id.enrolment_app_bar_option_horizontal_divider).setVisibility(View.VISIBLE);
            view.findViewById(R.id.enrolment_app_bar_option_button).setVisibility(View.GONE);
            view.findViewById(R.id.enrolment_app_bar_options_layout).setVisibility(View.VISIBLE);
            addNewCourseBtn.setVisibility(View.GONE);
            all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTextViewFocusEffect(all);
                    courseList.clear();
                    courseList.addAll(retrievedList);
                    adapter.notifyDataSetChanged();
                }
            });
            enrolled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTextViewFocusEffect(enrolled);
                    courseList.clear();
                    courseList.addAll(enrolledCourseList);
                    if (enrolledCourseList.size() == 0) {
                        noCoursesText.setVisibility(View.VISIBLE);
                        noCoursesText.setText("No Course/s Enrolled");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            pending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTextViewFocusEffect(pending);
                    courseList.clear();
                    courseList.addAll(pendingCourseList);
                    if (pendingCourseList.size() == 0) {
                        noCoursesText.setVisibility(View.VISIBLE);
                        noCoursesText.setText("No Pending Enrolment/s");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
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
                    enrolees.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), Enrollees.class);
                            intent.putExtra("option", "enrolees");
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    pendingEnrolees.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), Enrollees.class);
                            intent.putExtra("option", "pendings");
                            startActivity(intent);
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
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                List<Course> queryCourse = new ArrayList<>();
                for (Course course : courseList) {
                    if (course.getCenterName().toLowerCase().contains(query)
                            || course.getCourseName().toLowerCase().contains(query)
                            || course.getCourseStatus().toLowerCase().contains(query)
                            || course.getCourseType().toLowerCase().contains(query)
                            || course.getCourseDescription().toLowerCase().contains(query)
                            || (course.getCourseFee() + "").toLowerCase().contains(query)
                            || course.getScheduleFrom().toString().toLowerCase().contains(query)
                            || Utility.getDateStringFromTimestamp(course.getScheduleFrom()).toLowerCase()
                            .contains(query)
                            || course.getScheduleTo().toString().toLowerCase().contains(query)
                            || Utility.getDateStringFromTimestamp(course.getScheduleTo()).toLowerCase()
                            .contains(query)
                            || (Utility.getDateStringFromTimestamp(course.getScheduleFrom())
                            + "-" + Utility.getDateStringFromTimestamp(course.getScheduleTo()))
                            .toLowerCase().contains(query.replaceAll("\\s+", ""))
                            || course.getProcessedDate().toString().toLowerCase().contains(query)
                            || course.getEnrolledDate().toString().toLowerCase().contains(query)) {
                        queryCourse.add(course);
                    }
                }
                courseList.clear();
                courseList.addAll(queryCourse);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                show.set(true);
                return false;
            }
        });
        show = new ObservableBoolean();
        show.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    courseList.clear();
                    courseList.addAll(retrievedList);
                    if (courseList.isEmpty()) {
                        noCoursesText.setVisibility(View.VISIBLE);
                        noCoursesText.setText("Sorry, No Courses Available.");
                    } else {
                        noCoursesText.setVisibility(View.GONE);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        courseListener = new ObservableBoolean();
        enrolmentStatusListener = new ObservableBoolean();
        enrolmentStatusListener.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    for (Course course : retrievedList) {
                        for (Enrolment enrolment : enrolmentList) {
                            if (course.getCourseId().equals(enrolment.getCourseID())
                                    && enrolment.getEnrolmentStatus().equals("pending")
                                    && enrolment.getStudentID().equals(Account.getUsername())
                                    && !pendingCourseList.contains(course)) {
                                course.setStatus(enrolment.getEnrolmentStatus());
                                course.setProcessedDate(enrolment.getProcessedDate());
                                pendingCourseList.add(course);
                                adapter.notifyDataSetChanged();
                            } else if (course.getCourseId().equals(enrolment.getCourseID())
                                    && enrolment.getEnrolmentStatus().equals("enrolled")
                                    && enrolment.getStudentID().equals(Account.getUsername())
                                    && !enrolledCourseList.contains(course)) {
                                course.setStatus(enrolment.getEnrolmentStatus());
                                course.setEnrolledDate(enrolment.getEnrolledDate());
                                enrolledCourseList.add(course);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
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
                searchView.setQuery("", true);
                setTextViewFocusEffect(all);
                retrieveCourses();
                pullToRefresh.setRefreshing(false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CourseAdapter(getContext(), courseList);
        recyclerView.setAdapter(adapter);
        retrieveCourses();

        return view;
    }

    private void bindLayout(View view) {
        subscriptionExpiry = view.findViewById(R.id.enrolment_subscription_status);
        searchView = view.findViewById(R.id.enrolment_app_bar_searchview);
        enrolmentViewOption = view.findViewById(R.id.enrolment_app_bar_option_button);
        all = view.findViewById(R.id.enrolment_app_bar_option_all);
        enrolled = view.findViewById(R.id.enrolment_app_bar_option_enrolled);
        pending = view.findViewById(R.id.enrolment_app_bar_option_pending);
        noCoursesText = view.findViewById(R.id.enrolment_courses_none);
        addNewCourseBtn = view.findViewById(R.id.enrolment_add_fab);
        recyclerView = view.findViewById(R.id.enrolment_recylerview);

        // Search Menu
        enrolees = dialog.findViewById(R.id.enrolment_search_option_enrollees);
        pendingEnrolees = dialog.findViewById(R.id.enrolment_search_option_pending_enrolees);
        paymentRecords = dialog.findViewById(R.id.enrolment_search_option_payment_records);

        db = FirebaseFirestore.getInstance();
        courseList = new ArrayList<>();
        enrolmentList = new ArrayList<>();
        pendingCourseList = new ArrayList<>();
        enrolledCourseList = new ArrayList<>();
    }

    private void retrieveCourses() {
        retrievedList = Course.getRetrieved();
        if (Account.getType() == Account.Type.Student) {
            retrievedList = Course.filterCourses(retrievedList, "status", "open");
            Enrolment.getDataByID("studentID", Account.getUsername(), enrolmentList, enrolmentStatusListener);
        } else if (Account.getType() == Account.Type.Educator) {
            retrievedList = Course.filterCourses(retrievedList, "instructor", Account.getName());
        } else {
            retrievedList = Course.getCoursesByCenterId(Account.getCenterId());
        }
        show.set(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveCourses();
        if (Account.isType("learningcenter"))
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
                                int subscriptionLevel = document.getDouble("SubscriptionLevel").intValue();
                                if (subscriptionLevel > 0) {
                                    Timestamp timestamp = (com.google.firebase.Timestamp) document.get("SubscriptionExpiry");
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

    private void setTextViewFocusEffect(TextView view) {
        retrieveCourses();
        all.setTextColor(Color.GRAY);
        enrolled.setTextColor(Color.GRAY);
        pending.setTextColor(Color.GRAY);
        view.setTextColor(Color.CYAN);
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
