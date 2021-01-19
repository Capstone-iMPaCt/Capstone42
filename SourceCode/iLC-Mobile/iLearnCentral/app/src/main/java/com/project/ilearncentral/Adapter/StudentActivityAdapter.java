package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.ClassActivity;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.Student;
import com.project.ilearncentral.Model.StudentRecord;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentActivityAdapter extends RecyclerView.Adapter<StudentActivityAdapter.StudentActivityViewHolder> {
    private static final CharSequence TAG = "StudentActivityAdapter";
    private final Context context;
    private final List<ClassActivity> activities;
    private final StudentRecord record;
    private final ObservableString actionActivity;

    public StudentActivityAdapter(Context context, List<ClassActivity> activity, StudentRecord record, ObservableString actionActivity) {
        this.context = context;
        this.activities = activity;
        this.record = record;
        this.actionActivity = actionActivity;
    }

    @NonNull
    @Override
    public StudentActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_activity_row_student, parent, false);
        return new StudentActivityViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final StudentActivityViewHolder holder, final int position) {
        final ClassActivity activity = activities.get(position);
        final Class aClass = Class.getClassById(activity.getClassID());
        if (aClass!=null)
            holder.classSchedInput.setText(Utility.getDateTimeStringFromTimestamp(aClass.getClassStart()) +
                    " - " + Utility.getTimeStringFromTimestamp(aClass.getClassEnd()));
        holder.titleInput.setText(activity.getActivityTitle());
        holder.perfectScoreInput.setText(Utility.numberFormat(activity.getPerfectScore()));
        holder.descriptionInput.setText(activity.getActivityDescription());
        if (activity.getActivityDescription().isEmpty() && !activity.getActivityID().isEmpty()) {
            holder.descriptionLayout.setVisibility(View.GONE);
        } else {
            holder.descriptionLayout.setVisibility(View.VISIBLE);
        }

        if (activity.getScores().containsKey(record.getStudentID())) {
            holder.actualScoreInput.setText(Utility.numberFormat(activity.getScores().get(record.getStudentID())));
        } else actionActivity.set("Hide");

    }

    private String tempID() {
        return "row-";
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class StudentActivityViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout parent;
        private final TextInputLayout classSchedLayout, titleLayout, descriptionLayout, perfectScoreLayout, actualScoreLayout;
        private final TextInputEditText classSchedInput, titleInput, descriptionInput, perfectScoreInput, actualScoreInput;

        StudentActivityViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.view_record_activity_student_parent);
            classSchedLayout = itemView.findViewById(R.id.view_record_activity_student_class_layout);
            classSchedInput = itemView.findViewById(R.id.view_record_activity_student_class);
            titleLayout = itemView.findViewById(R.id.view_record_activity_student_title_layout);
            titleInput = itemView.findViewById(R.id.view_record_activity_student_title);
            descriptionLayout = itemView.findViewById(R.id.view_record_activity_student_desc_layout);
            descriptionInput = itemView.findViewById(R.id.view_record_activity_student_desc);
            perfectScoreLayout = itemView.findViewById(R.id.view_record_activity_student_full_score_layout);
            perfectScoreInput = itemView.findViewById(R.id.view_record_activity_student_full_score);
            actualScoreLayout = itemView.findViewById(R.id.view_record_activity_student_score_layout);
            actualScoreInput = itemView.findViewById(R.id.view_record_activity_student_score);
        }
    }
}
