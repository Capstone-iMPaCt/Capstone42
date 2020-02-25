package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobPostSingleListAdapter extends RecyclerView.Adapter<JobPostSingleListAdapter.JobPostSingleListViewHolder> {

    private Context context;
    private List<String> data;
    private String rowDetailHint;
    private boolean isView;

    public JobPostSingleListAdapter(Context context, List<String> data, String rowDetailHint, boolean isView) {
        this.context = context;
        this.data = data;
        this.rowDetailHint = rowDetailHint;
        this.isView = isView;
    }

    @NonNull
    @Override
    public JobPostSingleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_add_update_resume_singlelist_row, parent, false);
        return new JobPostSingleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JobPostSingleListViewHolder holder, final int position) {
        holder.rowDetailLayout.setHint(this.rowDetailHint);

        holder.rowDetail.setText(data.get(position));
        holder.rowDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowDetail.setFocusable(!isView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<String> getData() {
        return data;
    }

    public class JobPostSingleListViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout rowDetailLayout;
        private TextInputEditText rowDetail;

        JobPostSingleListViewHolder(View itemView) {
            super(itemView);
            rowDetailLayout = itemView.findViewById(R.id.add_update_resume_singlelist_row_detail_layout);
            rowDetail = itemView.findViewById(R.id.add_update_resume_singlelist_row_detail);
            rowDetail.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        }
    }
}
