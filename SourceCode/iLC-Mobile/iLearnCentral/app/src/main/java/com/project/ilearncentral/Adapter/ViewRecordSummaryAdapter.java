package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.ClassActivity;
import com.project.ilearncentral.Model.StudentRecord;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewRecordSummaryAdapter extends RecyclerView.Adapter<ViewRecordSummaryAdapter.ViewRecordSummaryViewHolder> {
    private static final CharSequence TAG = "ViewRecordSummaryAdapter";
    private final Context context;
    private final List<Class> classes;
    private final List<StudentRecord> records;
    private final boolean isAttendance;
    private boolean studentFocus;
    private final ObservableString actionState;
    private final List<String> added;

    public ViewRecordSummaryAdapter(Context context, List<Class> classes, List<StudentRecord> records, boolean focus, boolean isAttendance, ObservableString actionState) {
        this.context = context;
        this.classes = classes;
        this.records = records;
        this.studentFocus = focus;
        this.isAttendance = isAttendance;
        this.actionState = actionState;
        this.added = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewRecordSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_summary_row, parent, false);
        return new ViewRecordSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewRecordSummaryViewHolder holder, final int position) {
        ObservableString attendanceAction = new ObservableString();
        ObservableString activityAction = new ObservableString();

        if (studentFocus) {
            holder.nameInput.setHint("Student Name");
            holder.headerName.setText("Class Dates");
            final StudentRecord record = records.get(position);
            if (!added.contains(record.getRecordID())) {
                added.add(record.getRecordID());
                if (record.getStudent() != null) {
                    holder.nameInput.setText(record.getStudent().getFullname());
                } else {
                    holder.nameInput.setText(record.getStudentID());
                }

                holder.recycler.setLayoutManager(new LinearLayoutManager(context));
                if (isAttendance) {
                    if (record.getAttendances().size()<classes.size()) {
                        holder.parent.setVisibility(View.GONE);
                    }
                    AttendanceAdapter attendanceAdapter;
                    attendanceAdapter = new AttendanceAdapter(context, record
                            .getAttendances(), null, attendanceAction, studentFocus);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    holder.recycler.setLayoutManager(linearLayoutManager);
                    holder.recycler.setAdapter(attendanceAdapter);
                } else {
                    holder.tableHeader.setVisibility(View.GONE);
                    StudentActivityAdapter activityAdapter;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            record.getActivities().sort(new Comparator<ClassActivity>() {
                                @Override
                                public int compare(ClassActivity a1, ClassActivity a2) {
                                    return a1.getaClass().getClassStart()
                                            .compareTo(a2.getaClass().getClassStart());
                                }
                            });
                        }
                    }catch (Exception e) {}
                    activityAdapter = new StudentActivityAdapter(context, record
                            .getActivities(), record, activityAction);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    holder.recycler.setLayoutManager(linearLayoutManager);
                    holder.recycler.setAdapter(activityAdapter);
                }
            } else {
                holder.parent.setVisibility(View.GONE);
            }
        } else {
            holder.nameInput.setHint("Class Date");
            holder.headerName.setText("Student Names");
            final Class aClass = classes.get(position);
            if (!added.contains(aClass.getClassID())) {
                added.add(aClass.getClassID());
                holder.nameInput
                        .setText(Utility.getDateTimeStringFromTimestamp(aClass.getClassStart()) +
                                "-" + Utility.getTimeStringFromTimestamp(aClass.getClassEnd()));
                holder.recycler.setLayoutManager(new LinearLayoutManager(context));
                if (isAttendance) {
                    AttendanceAdapter attendanceAdapter;
                    attendanceAdapter = new AttendanceAdapter(context, aClass
                            .getAttendances(), aClass, attendanceAction, studentFocus);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    holder.recycler.setLayoutManager(linearLayoutManager);
                    holder.recycler.setAdapter(attendanceAdapter);
                } else {
                    holder.tableHeader.setVisibility(View.GONE);
                    ClassActivityAdapter activityAdapter;
                    activityAdapter = new ClassActivityAdapter(context, aClass
                            .getActivities(), aClass, "view");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    holder.recycler.setLayoutManager(linearLayoutManager);
                    holder.recycler.setAdapter(activityAdapter);
                }
            } else {
                holder.parent.setVisibility(View.GONE);
            }
        }

        activityAction.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String value) {
                if (value.equalsIgnoreCase("Hide")) {
                    holder.parent.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (studentFocus)
            return records.size();
        else
            return classes.size();
    }

    public class ViewRecordSummaryViewHolder extends RecyclerView.ViewHolder {

        private final TextInputLayout nameLayout;
        private final TextInputEditText nameInput;
        private final LinearLayout tableHeader, parent;
        private final TextView headerName;
        private final RecyclerView recycler;

        ViewRecordSummaryViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.view_record_summary_row_parent);
            nameLayout = itemView.findViewById(R.id.view_record_summary_row_name_layout);
            nameInput = itemView.findViewById(R.id.view_record_summary_row_name);
            tableHeader = itemView.findViewById(R.id.view_record_summary_row_table_header);
            headerName = itemView.findViewById(R.id.view_record_summary_row_header_name);
            recycler = itemView.findViewById(R.id.view_record_summary_row_recycler);
        }
    }
}
