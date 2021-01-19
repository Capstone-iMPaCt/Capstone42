package com.project.ilearncentral.Fragment.SubSystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Activity.NveClass;
import com.project.ilearncentral.Activity.ViewRecordSummaryActivity;
import com.project.ilearncentral.Adapter.ClassAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.Model.Student;
import com.project.ilearncentral.Model.StudentRecord;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Subscription;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulingSystem extends Fragment {

    private static final String TAG = "SchedulingSystem";
    private ClassAdapter adapter;
    private RecyclerView recyclerView;
    private List<Class> classes;
    private Map<String, String> courses;

    private Dialog dialog;
    private Spinner coursesSpinner;
    private TextView subscriptionExpiry, noClass;
    private FloatingActionButton addNewClassBtn, viewSummaryBtn;
    private Button okDialog, clearDialog;
    private ImageButton viewOption;
    private RadioGroup status;
    private RadioButton openStatus, closeStatus, cancelledStatus, ongoingStatus, pendingStatus;

    private ObservableBoolean loadedClass, isLoading, subscriptionStatus;
    private ObservableString statusChange;
    private String courseID;
    private String statusCurrent;

    public SchedulingSystem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_subsystem_scheduling, container, false);
        super.onCreate(savedInstanceState);
        initialize(view);

        classes = new ArrayList<>();
        courses = new HashMap<>();
        courseID = "";
        statusCurrent = "";
        setCourseSpinner();
        statusChange = new ObservableString();
        loadedClass = new ObservableBoolean();
        loadedClass.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    isLoading.set(false);
                    classes.addAll(Class.getRetrieved());
                    adapter.notifyDataSetChanged();
                    if (classes.isEmpty()) {
                        noClass.setVisibility(View.VISIBLE);
                        noClass.setText("No Classes Found");
                        viewSummaryBtn.setVisibility(View.GONE);
                    } else {
                        noClass.setVisibility(View.GONE);
                        viewSummaryBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        isLoading = new ObservableBoolean();
        isLoading.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean loading) {
                if (loading) {
                    coursesSpinner.setEnabled(false);
                    viewOption.setEnabled(false);
                } else {
                    coursesSpinner.setEnabled(true);
                    viewOption.setEnabled(true);
                }
            }
        });
        addNewClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!Subscription.isSchedulingSubscribed()) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Please subscribe");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
                alertDialog.show();
                return;
            }
            Intent intent = new Intent(getActivity(), NveClass.class);
            intent.putExtra("classID", "");
            intent.putExtra("courseID", courseID);
            intent.putExtra("action", "add");
            startActivity(intent);
            }
        });

        viewSummaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewRecordSummaryActivity.class);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });

        coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = coursesSpinner.getSelectedItem().toString();
                courseID = selected.substring(selected.indexOf("- ") + 2);
                classesLoading();
                Class.retrieveClassesFromDB(courseID, "", loadedClass);
                StudentRecord.retrieveStatusRecordFromDB(courseID, null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                noClass.setText("Please Choose Course");
            }

        });

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.scheduling_pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);
                if (!courseID.isEmpty()) {
                    classesLoading();
                    Class.retrieveClassesFromDB(courseID, statusCurrent, loadedClass);
                    StudentRecord.retrieveStatusRecordFromDB(courseID, null);
                }
            }
        });

        viewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setCancelable(true);
                dialog.show();
                okDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        classesLoading();
                        String msg = "";
                        if (status.getCheckedRadioButtonId() == openStatus.getId()) {
                            msg += "OPEN: ";
                            statusCurrent = "Open";
                        } else if (status.getCheckedRadioButtonId() == closeStatus.getId()) {
                            msg += "CLOSE: ";
                            statusCurrent = "Close";
                        } else if (status.getCheckedRadioButtonId() == cancelledStatus.getId()) {
                            msg += "CANCELLED: ";
                            statusCurrent = "Cancelled";
                        } else if (status.getCheckedRadioButtonId() == ongoingStatus.getId()) {
                            msg += "ONGOING: ";
                            statusCurrent = "Ongoing";
                        } else if (status.getCheckedRadioButtonId() == pendingStatus.getId()) {
                            msg += "PENDING: ";
                            statusCurrent = "Pending";
                        }
                        Class.retrieveClassesFromDB(courseID, statusCurrent, loadedClass);
//                        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0,0);
//                        toast.show();
                        dialog.dismiss();
                    }
                });
                clearDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        status.clearCheck();
                        statusCurrent = "";
                        Class.retrieveClassesFromDB(courseID, statusCurrent, loadedClass);
                        classesLoading();
//                        Toast toast = Toast.makeText(getContext(), "Cleared filters", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0,0);
//                        toast.show();
                        dialog.dismiss();
                    }
                });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ClassAdapter(getContext(), classes, statusChange);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void classesLoading() {
        classes.clear();
        isLoading.set(true);
        adapter.notifyDataSetChanged();
        noClass.setVisibility(View.VISIBLE);
        noClass.setText("Please Wait. Class List Loading.");
    }

    private void setCourseSpinner() {
        courses.clear();
        noClass.setText("Loading Courses. Please wait.");
        if (Account.getType() == Account.Type.LearningCenter) {
            addNewClassBtn.setVisibility(View.VISIBLE);
            for (Course course : Course.getCoursesByCenterId(Account.getCenterId())) {
                if (course.getCourseStatus().equalsIgnoreCase("open"))
                    courses.put(course.getCourseId(), course.getCourseName());
            }
            List<String> courseSpin = new ArrayList<>();
            for (Map.Entry<String, String> entry : courses.entrySet()) {
                courseSpin.add(entry.getValue() + " - " + entry.getKey());
            }
            ArrayAdapter<String> aadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courseSpin);
            aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            coursesSpinner.setAdapter(aadapter);
            noClass.setText("Please Choose Course");

            Subscription.getSchedulingSubscriptionDetails(subscriptionExpiry);
        } else if (Account.getType() == Account.Type.Educator) {
            subscriptionExpiry.setVisibility(View.GONE);
            addNewClassBtn.setVisibility(View.GONE);
            FirebaseFirestore.getInstance().collection("Course").whereArrayContains("Educators", Account.getUsername())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Course course = Course.getCourseById(document.getId());
                                    if (course != null) {
                                        if (course.getCourseStatus().equalsIgnoreCase("open"))
                                            courses.put(course.getCourseId(), course.getCourseName());
                                    }
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                List<String> courseSpin = new ArrayList<>();
                                for (Map.Entry<String, String> entry : courses.entrySet()) {
                                    courseSpin.add(entry.getValue() + " - " + entry.getKey());
                                }
                                noClass.setText("Please Choose Course");
                                ArrayAdapter<String> aadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courseSpin);
                                aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                coursesSpinner.setAdapter(aadapter);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else if (Account.getType() == Account.Type.Student) {
            subscriptionExpiry.setVisibility(View.GONE);
            addNewClassBtn.setVisibility(View.GONE);
            FirebaseFirestore.getInstance().collection("Enrolment").whereEqualTo("studentID", Account.getUsername()).whereEqualTo("enrolmentStatus", "enrolled")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Course course = Course.getCourseById(document.getString("courseID"));
                                    System.out.println(document.getId() + " " + course.getCourseStatus());
                                    if (course != null) {
                                        if (course.getCourseStatus().equalsIgnoreCase("open"))
                                            courses.put(course.getCourseId(), course.getCourseName());
                                    }
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                List<String> courseSpin = new ArrayList<>();
                                for (Map.Entry<String, String> entry : courses.entrySet()) {
                                    courseSpin.add(entry.getValue() + " - " + entry.getKey());
                                }
                                noClass.setText("Please Choose Course");
                                ArrayAdapter<String> aadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courseSpin);
                                aadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                coursesSpinner.setAdapter(aadapter);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void initialize(View view) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_dialog_filter_classes);
        Window window = dialog.getWindow();
        window.setLayout(Utility.dpToPx(getContext(), 300), LinearLayout.LayoutParams.WRAP_CONTENT);
        status = dialog.findViewById(R.id.class_status_filter_group);
        openStatus = dialog.findViewById(R.id.class_status_filter_open);
        closeStatus = dialog.findViewById(R.id.class_status_filter_close);
        cancelledStatus = dialog.findViewById(R.id.class_status_filter_cancelled);
        ongoingStatus = dialog.findViewById(R.id.class_status_filter_ongoing);
        pendingStatus = dialog.findViewById(R.id.class_status_filter_pending);
        okDialog = dialog.findViewById(R.id.class_filter_option_ok);
        clearDialog = dialog.findViewById(R.id.class_filter_option_clear);

        coursesSpinner = view.findViewById(R.id.scheduling_app_bar_spinner);
        subscriptionExpiry = view.findViewById(R.id.scheduling_subscription_status);
        noClass = view.findViewById(R.id.scheduling_classes_none);
        addNewClassBtn = view.findViewById(R.id.scheduling_add_fab);
        viewSummaryBtn = view.findViewById(R.id.scheduling_view_summary);
        viewOption = view.findViewById(R.id.scheduling_app_bar_option_button);
        recyclerView = view.findViewById(R.id.scheduling_recylerview);
    }

    @Override
    public void onResume() {
        super.onResume();
        classesLoading();
        Class.retrieveClassesFromDB(courseID, statusCurrent, loadedClass);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (Account.getType() == Account.Type.LearningCenter) {
            Subscription.getSchedulingSubscriptionDetails(subscriptionExpiry);
        }
    }


}
