package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.ClassActivity;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.Model.Student;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassActivityAdapter extends RecyclerView.Adapter<ClassActivityAdapter.ClassActivityViewHolder> {
    private static final CharSequence TAG = "ClassActivityAdapter";
    private final Context context;
    private final List<ClassActivity> activities;
    private final String action;
    private final Class aClass;
    private final ClassActivityAdapter adapter;
    private final Map<String,List<String>> tableLayouts;

    public ClassActivityAdapter(Context context, List<ClassActivity> activity, Class aClass, String action) {
        this.context = context;
        this.activities = activity;
        this.action = action;
        this.aClass = aClass;
        this.adapter = this;
        this.tableLayouts = new HashMap<>();
    }

    @NonNull
    @Override
    public ClassActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_activity_row, parent, false);
        return new ClassActivityViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ClassActivityViewHolder holder, final int position) {
        final ClassActivity activity = activities.get(position);
        holder.titleInput.setText(activity.getActivityTitle());
        holder.perfectScoreInput.setText(Utility.numberFormat(activity.getPerfectScore()));
        if (aClass!=null)
            holder.date.setText(Utility.getDateStringFromTimestamp(aClass.getClassStart()));
        else
            holder.date.setVisibility(View.GONE);
        holder.descriptionInput.setText(activity.getActivityDescription());
        if (action.equalsIgnoreCase("view")) {
            holder.editBtn.setVisibility(View.GONE);
            holder.resetBtn.setVisibility(View.GONE);
        }
        if (activity.getActivityDescription().isEmpty() && !activity.getActivityID().isEmpty()) {
            holder.descriptionLayout.setVisibility(View.GONE);
        } else {
            holder.descriptionLayout.setVisibility(View.VISIBLE);
        }

        final Map<String,EditText> scoreInputs = new HashMap<>();
        final List<String> studentsInRow = new ArrayList<>();
        for (Student student: activity.getStudents()) {
            if (!scoreInputs.containsKey(student.getUsername())) {
                TextView studentName = new TextView(context);
                studentName.setText(student.getFullname());
                studentName
                        .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                EditText score = new EditText(context);
                score.setTextSize(15);
                score.setTextColor(context.getResources().getColor(R.color.bt_black));
                score.setEnabled(false);
                score.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                score.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                score.setBackground(context.getDrawable(R.drawable.input_white_bg_rounded));
                if (activity.getScores().containsKey(student.getUsername())) {
                    score.setText(Utility
                            .numberFormat(activity.getScores().get(student.getUsername())));
                } else {
                    score.setText("0");
                }
                score.setGravity(Gravity.CENTER);

                LinearLayout row = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
                layoutParams.setMargins(10, 5, 10, 5);
                row.setLayoutParams(layoutParams);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(10,0,10,0);
                row.addView(studentName);
                row.addView(score);

                scoreInputs.put(student.getUsername(), score);
                studentsInRow.add(student.getUsername());
                if (tableLayouts.containsKey(tempID() + activity.getActivityID())) {
                    if (!tableLayouts.get(tempID() +activity.getActivityID()).contains(student.getUsername()))
                        holder.tableContent.addView(row);
                } else {
                    holder.tableContent.addView(row);
                }
            }
            tableLayouts.put(tempID() + activity.getActivityID(), studentsInRow);
        }

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editState(holder, scoreInputs, activity);
            }
        });
        holder.resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.resetBtn.getText().toString().equalsIgnoreCase("Reset")) {
                    holder.titleInput.setError(null);
                    holder.perfectScoreInput.setError(null);
                    holder.titleInput.setText(activity.getActivityTitle());
                    holder.perfectScoreInput
                            .setText(Utility.numberFormat(activity.getPerfectScore()));
                    holder.descriptionInput.setText(activity.getActivityDescription());
                    for (String key : scoreInputs.keySet()) {
                        if (activity.getScores().containsKey(key)) {
                            scoreInputs.get(key)
                                    .setText(Utility.numberFormat(activity.getScores().get(key)));
                        } else {
                            scoreInputs.get(key).setText("0");
                        }
                        scoreInputs.get(key).setError(null);
                    }
                } else if (holder.resetBtn.getText().toString().equalsIgnoreCase("Cancel")) {
                    holder.resetBtn.setText("Reset");
                    holder.editBtn.setText("Edit");
                    aClass.getActivities().remove(activity);
                    activities.remove(activity);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        if (activity.getActivityID().isEmpty()) {
            editState(holder, scoreInputs, activity);
            holder.resetBtn.setText("Cancel");
        }
    }

    private String tempID() {
        return "row-";
    }

    private void editState(@NonNull ClassActivityViewHolder holder, Map<String, EditText> scoreInputs, ClassActivity activity) {
        if (holder.editBtn.getText().toString().equalsIgnoreCase("Edit")) {
            holder.editBtn.setText("Save");
            holder.resetBtn.setVisibility(View.VISIBLE);
            holder.titleInput.setEnabled(true);
            holder.perfectScoreInput.setEnabled(true);
            holder.descriptionInput.setEnabled(true);
            holder.descriptionLayout.setVisibility(View.VISIBLE);
            for (EditText score: scoreInputs.values()) {
                score.setEnabled(true);
            }
        } else {
            boolean valid = true;
            if (holder.titleInput.getText().toString().isEmpty()) {
                valid = false;
                holder.titleInput.setError("Title is Empty");
            }
            if (holder.perfectScoreInput.getText().toString().isEmpty()) {
                valid = false;
                holder.titleInput.setError("Title is Empty");
            } else {
                try {
                    double perfect = (Double
                            .parseDouble(holder.perfectScoreInput.getText().toString()));
                    if (perfect<0) {
                        valid = false;
                        holder.perfectScoreInput.setError("Invalid Score");
                    }
                    for (String key : scoreInputs.keySet()) {
                        double score = Double.parseDouble(scoreInputs.get(key).getText().toString());
                        if (score<0 || score>perfect) {
                            valid = false;
                            scoreInputs.get(key).setError("Invalid Score");
                        }
                    }
                } catch (Exception e) {
                    valid = false;
                }
            }
            if (valid) {
                holder.editBtn.setText("Edit");
                holder.resetBtn.setVisibility(View.GONE);
                holder.titleInput.setEnabled(false);
                holder.perfectScoreInput.setEnabled(false);
                holder.descriptionInput.setEnabled(false);
                if (holder.descriptionInput.getText().toString().isEmpty()) {
                    holder.descriptionLayout.setVisibility(View.GONE);
                } else {
                    holder.descriptionLayout.setVisibility(View.VISIBLE);
                }
                activity.setActivityTitle(holder.titleInput.getText().toString());
                activity.setActivityDescription(holder.descriptionInput.getText().toString());
                try {
                    activity.setPerfectScore(Double
                            .parseDouble(holder.perfectScoreInput.getText().toString()));
                    for (String key : scoreInputs.keySet()) {
                        scoreInputs.get(key).setEnabled(false);
                        activity.getScores().put(key, Double
                                .parseDouble(scoreInputs.get(key).getText().toString()));
                    }
                } catch (Exception e) {}
                if (activity.getActivityID().isEmpty())
                    activity.setActivityID("toSet");
                ClassActivity.saveActivity(activity);
            }
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ClassActivityViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout parent, tableContent;
        private final TextInputLayout titleLayout, perfectScoreLayout, descriptionLayout;
        private final TextInputEditText titleInput, perfectScoreInput, descriptionInput;
        private final Button resetBtn, editBtn;
        private final TextView date;

        ClassActivityViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.view_record_activities_row);
            titleLayout = itemView.findViewById(R.id.view_record_activity_row_title_layout);
            titleInput = itemView.findViewById(R.id.view_record_activity_row_title);
            perfectScoreLayout = itemView.findViewById(R.id.view_record_activity_row_score_layout);
            perfectScoreInput = itemView.findViewById(R.id.view_record_activity_row_score);
            resetBtn = itemView.findViewById(R.id.view_record_activities_row_reset);
            editBtn = itemView.findViewById(R.id.view_record_activities_row_button);
            date = itemView.findViewById(R.id.view_record_activity_row_date);
            descriptionLayout = itemView.findViewById(R.id.view_record_activity_row_desc_layout);
            descriptionInput = itemView.findViewById(R.id.view_record_activity_row_desc);
            tableContent = itemView.findViewById(R.id.view_record_activity_row_table_contents);
        }
    }
}
