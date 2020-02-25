package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobPostEducListAdapter extends RecyclerView.Adapter<JobPostEducListAdapter.JobPostEducListViewHolder> {

    private Context context;
    private List<Map<String, Object>> data;
    private boolean isView;

    public JobPostEducListAdapter(Context context, List<Map<String, Object>> data, boolean isview) {
        this.context = context;
        this.data = data;
        this.isView = isview;
    }

    @NonNull
    @Override
    public JobPostEducListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_nve_jobpost_educ_row, parent, false);
        return new JobPostEducListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JobPostEducListViewHolder holder, final int position) {
        final Map<String, Object> row = data.get(position);
        if (isView) {
            holder.rowGraduated.setClickable(false);
            if ((boolean)row.get("Graduated"))
                holder.rowGraduated.setText("Must be a Graduate");
            else
                holder.rowGraduated.setVisibility(View.GONE);
            holder.rowMinUnits.setFocusable(false);
            holder.rowDegree.setFocusable(false);
            holder.rowEducLevel.setFocusable(false);
            holder.rowMajor.setFocusable(false);
        }
        holder.rowGraduated.setChecked((boolean)row.get("Graduated"));
        holder.rowGraduated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                row.put("Graduated", isChecked);
                    if (isChecked) {
                    holder.rowMinUnitsLayout.setVisibility(View.GONE);
                } else {
                    holder.rowMinUnitsLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.rowEducLevel.setText(row.get("EducationalLevel").toString());
        holder.rowEducLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                row.put("EducationalLevel", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowDegree.setText(row.get("Degree").toString());
        holder.rowDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                row.put("Degree", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowMajor.setText(row.get("Major").toString());
        holder.rowMajor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                row.put("Major", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowMinUnits.setText(row.get("MinimumUnits").toString());
        holder.rowMinUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                row.put("MinimumUnits", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public class JobPostEducListViewHolder extends RecyclerView.ViewHolder {

        private CheckBox rowGraduated;
        private TextInputLayout rowMinUnitsLayout;
        private TextInputEditText rowEducLevel, rowDegree, rowMajor, rowMinUnits;

        JobPostEducListViewHolder(View itemView) {
            super(itemView);
            rowGraduated = itemView.findViewById(R.id.job_post_nve_educ_graduate);
            rowEducLevel = itemView.findViewById(R.id.job_post_nve_educ_level);
            rowDegree = itemView.findViewById(R.id.job_post_nve_educ_degree);
            rowMajor = itemView.findViewById(R.id.job_post_nve_educ_major);
            rowMinUnitsLayout = itemView.findViewById(R.id.job_post_nve_educ_unit_layout);
            rowMinUnits = itemView.findViewById(R.id.job_post_nve_educ_unit);
        }
    }
}
