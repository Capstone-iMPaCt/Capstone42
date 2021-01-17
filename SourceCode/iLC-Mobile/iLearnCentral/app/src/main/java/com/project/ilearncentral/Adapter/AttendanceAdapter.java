package com.project.ilearncentral.Adapter;

import android.R.color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Attendance;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.StudentRecord;
import com.project.ilearncentral.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {
    private static final CharSequence TAG = "AttendanceAdapter";
    private final Context context;
    private final List<Attendance> attendances;
    private final Class aClass;
    private final ObservableString actionState;

    public AttendanceAdapter(Context context, List<Attendance> attendance, Class aClass, ObservableString actionState) {
        this.context = context;
        this.attendances = attendance;
        this.aClass = aClass;
        this.actionState = actionState;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_attendance_row, parent, false);
        return new AttendanceViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final AttendanceViewHolder holder, final int position) {
        final Attendance attend = attendances.get(position);
        holder.toSet.setVisibility(View.GONE);
        final StudentRecord record = StudentRecord.getRecordByStudentIDAndCourseID(attend.getStudentID(), aClass.getCourseID());
        Attendance.clearRedundant(aClass.getAttendances(), "StudentID");
        Attendance.clearRedundant(record.getAttendanceList(), "ClassID");
        final Attendance attend2 = record.getAttendanceByClassID(aClass.getClassID());
        if (attend.getStudent()!=null)
            holder.name.setText(attend.getStudent().getFullname());
        actionState.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String value) {
                if (value.equalsIgnoreCase("edit")) {
                    holder.toSet.setVisibility(View.VISIBLE);
                    holder.remarksLayout.setVisibility(View.VISIBLE);
                    holder.remarksLayout.setEnabled(true);
                } else if (value.equalsIgnoreCase("save")) {
                    if (!holder.remarks.getText().toString().isEmpty()) {
                        holder.remarksLayout.setVisibility(View.VISIBLE);
                        holder.remarksLayout.setEnabled(false);
                    } else {
                        holder.remarksLayout.setVisibility(View.GONE);
                    }
                    holder.toSet.setVisibility(View.GONE);
                    for(Attendance attendance:aClass.getAttendances()) {
                        if (attendance.getStudentID().equalsIgnoreCase(attend.getStudentID())) {
                            attend.setRemarks(holder.remarks.getText().toString());
                            attend2.setRemarks(holder.remarks.getText().toString());
                            attendance.setRemarks(holder.remarks.getText().toString());
                            attend.setAttendance(holder.presence.getText().toString().toLowerCase());
                            attend2.setAttendance(holder.presence.getText().toString().toLowerCase());
                            attendance.setAttendance(holder.presence.getText().toString().toLowerCase());
                            break;
                        }
                    }
                    Attendance.saveAttendance(aClass, record);
                } else if (value.equalsIgnoreCase("reset")) {
                    holder.remarks.setText(attend.getRemarks());
                    setCurrentValues(holder, attend);
                }
            }
        });
        setCurrentValues(holder, attend);
        if (!holder.remarks.getText().toString().isEmpty()) {
            holder.remarksLayout.setVisibility(View.VISIBLE);
            holder.remarksLayout.setEnabled(false);
        } else {
            holder.remarksLayout.setVisibility(View.GONE);
        }
        holder.present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.presence.setText(holder.present.getText().toString());
                    holder.presence.setBackgroundColor(context.getResources().getColor(color.holo_green_light));
                }
            }
        });
        holder.absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.presence.setText(holder.absent.getText().toString());
                    holder.presence.setBackgroundColor(context.getResources().getColor(color.holo_red_light));
                }
            }
        });
        holder.late.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.presence.setText(holder.late.getText().toString());
                    holder.presence.setBackgroundColor(context.getResources().getColor(color.holo_orange_dark));
                }
            }
        });
    }

    private void setCurrentValues(@NonNull AttendanceViewHolder holder, Attendance attend) {
        switch (attend.getAttendance()) {
            case "present":
                holder.presence.setText(holder.present.getText().toString());
                holder.presence.setBackgroundColor(context.getResources().getColor(color.holo_green_light));
                holder.present.setChecked(true);
                break;
            case "absent":
                holder.presence.setText(holder.absent.getText().toString());
                holder.presence.setBackgroundColor(context.getResources().getColor(color.holo_red_light));
                holder.absent.setChecked(true);
                break;
            case "late":
                holder.presence.setText(holder.late.getText().toString());
                holder.presence.setBackgroundColor(context.getResources().getColor(color.holo_orange_dark));
                holder.late.setChecked(true);
                break;
            default:
                break;
        }
        holder.remarks.setText(attend.getRemarks());
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView presence;
        private final View topDivider;
        private final View bottomDivider;
        private final TextInputLayout remarksLayout;
        private final TextInputEditText remarks;
        private final RadioGroup toSet;
        private final RadioButton present;
        private final RadioButton absent;
        private final RadioButton late;

        AttendanceViewHolder(View itemView) {
            super(itemView);

            topDivider = itemView.findViewById(R.id.view_record_attendance_row_top_line);
            bottomDivider = itemView.findViewById(R.id.view_record_attendance_row_below_line);
            name = itemView.findViewById(R.id.view_record_attendance_row_student_name);
            presence = itemView.findViewById(R.id.view_record_attendance_row_presence);
            toSet = itemView.findViewById(R.id.view_record_attendance_row_radiogroup);
            present = itemView.findViewById(R.id.view_record_attendance_row_present);
            absent = itemView.findViewById(R.id.view_record_attendance_row_absent);
            late = itemView.findViewById(R.id.view_record_attendance_row_late);
            remarksLayout = itemView.findViewById(R.id.view_record_attendance_row_remarks_layout);
            remarks = itemView.findViewById(R.id.view_record_attendance_row_remarks);
        }
    }
}
