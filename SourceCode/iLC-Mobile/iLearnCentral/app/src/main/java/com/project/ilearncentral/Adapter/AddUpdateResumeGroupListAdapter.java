package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.List;

public class AddUpdateResumeGroupListAdapter extends RecyclerView.Adapter<AddUpdateResumeGroupListAdapter.AddUpdateResumeGroupListViewHolder> {

    private Context context;
    private List<Resume> data;

    public AddUpdateResumeGroupListAdapter(Context context, List<Resume> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public AddUpdateResumeGroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_add_update_grouplist_row, parent, false);
        return new AddUpdateResumeGroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddUpdateResumeGroupListViewHolder holder, int position) {
        holder.rowName.setText(data.get(position).getHeader());
        holder.rowAddress.setText(data.get(position).getAddress());
        holder.rowDatePeriod.setText(data.get(position).getDatePeriod());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AddUpdateResumeGroupListViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout rowNameLayout, rowAddressLayout, rowDatePeriodLayout;
        private TextInputEditText rowName, rowAddress, rowDatePeriod;

        AddUpdateResumeGroupListViewHolder(View itemView) {
            super(itemView);
            rowNameLayout = itemView.findViewById(R.id.add_update_resume_grouplist_row_name_layout);
            rowName = itemView.findViewById(R.id.add_update_resume_grouplist_row_name);
            rowAddressLayout = itemView.findViewById(R.id.add_update_resume_grouplist_row_address_layout);
            rowAddress = itemView.findViewById(R.id.add_update_resume_grouplist_row_address);
            rowDatePeriodLayout = itemView.findViewById(R.id.add_update_resume_grouplist_row_dateperiod_layout);
            rowDatePeriod = itemView.findViewById(R.id.add_update_resume_grouplist_row_dateperiod);
        }
    }
}
