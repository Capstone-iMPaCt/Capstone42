package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.R;

import java.util.List;

public class AddUpdateResumeReferenceAdapter extends RecyclerView.Adapter<AddUpdateResumeReferenceAdapter.AddUpdateResumeReferenceViewHolder> {

    private Context context;
    private List<ResumeItem> data;

    public AddUpdateResumeReferenceAdapter(Context context, List<ResumeItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public AddUpdateResumeReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_add_update_resume_references_row, parent, false);
        return new AddUpdateResumeReferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddUpdateResumeReferenceViewHolder holder, final int position) {
        holder.rowPersonName.setText(data.get(position).getPersonName());
        holder.rowPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setPersonName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowJobTitle.setText(data.get(position).getJobTitle());
        holder.rowJobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setJobTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowCompanyName.setText(data.get(position).getCompanyName());
        holder.rowCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setCompanyName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowContactNumber.setText(data.get(position).getContactNumber());
        holder.rowContactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setContactNumber(charSequence.toString());
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

    public List<ResumeItem> getData() {
        return data;
    }

    public class AddUpdateResumeReferenceViewHolder extends RecyclerView.ViewHolder {

        private TextInputEditText rowPersonName, rowJobTitle, rowCompanyName, rowContactNumber;

        AddUpdateResumeReferenceViewHolder(View itemView) {
            super(itemView);
            rowPersonName = itemView.findViewById(R.id.add_update_resume_reference_row_personname);
            rowJobTitle = itemView.findViewById(R.id.add_update_resume_reference_row_jobtitle);
            rowCompanyName = itemView.findViewById(R.id.add_update_resume_reference_row_companyname);
            rowContactNumber = itemView.findViewById(R.id.add_update_resume_reference_row_contactnumber);
        }
    }
}
