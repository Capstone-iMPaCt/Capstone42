package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.List;

public class ResumeReferenceAdapter extends RecyclerView.Adapter<ResumeReferenceAdapter.ResumeReferenceViewHolder> {

    Context context;
    List<Resume> data;

    public ResumeReferenceAdapter(Context context, List<Resume> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ResumeReferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_educator_row_item_references, parent, false);
        return new ResumeReferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeReferenceViewHolder holder, int position) {
        holder.rowPersonName.setText(data.get(position).getPersonName());
        holder.rowJobTitle.setText(data.get(position).getJobTitle());
        holder.rowCompanyName.setText(data.get(position).getCompanyName());
        holder.rowContactNumber.setText(data.get(position).getContactNumber());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ResumeReferenceViewHolder extends RecyclerView.ViewHolder {

        private TextView rowPersonName, rowJobTitle, rowCompanyName, rowContactNumber;

        ResumeReferenceViewHolder(View itemView) {
            super(itemView);
            rowPersonName = (TextView) itemView.findViewById(R.id.reference_row_person_name);
            rowJobTitle = (TextView) itemView.findViewById(R.id.reference_row_job_title);
            rowCompanyName = (TextView) itemView.findViewById(R.id.reference_row_company_name);
            rowContactNumber = (TextView) itemView.findViewById(R.id.reference_row_contact_number);
        }
    }
}
