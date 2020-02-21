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
import com.google.android.material.textfield.TextInputLayout;
import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.List;

public class AddUpdateResumeGroupListAdapter extends RecyclerView.Adapter<AddUpdateResumeGroupListAdapter.AddUpdateResumeGroupListViewHolder> {

    private Context context;
    private List<Resume> data;
    private String rowNameHint, rowAddressHint, rowDatePeriodHint;

    public AddUpdateResumeGroupListAdapter(Context context, List<Resume> data) {
        this.context = context;
        this.data = data;
    }

    public AddUpdateResumeGroupListAdapter(Context context, List<Resume> data, String rowNameHint, String rowAddressHint, String rowDatePeriodHint) {
        this.context = context;
        this.data = data;
        this.rowNameHint = rowNameHint;
        this.rowAddressHint = rowAddressHint;
        this.rowDatePeriodHint = rowDatePeriodHint;
    }

    @NonNull
    @Override
    public AddUpdateResumeGroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_add_update_resume_grouplist_row, parent, false);
        return new AddUpdateResumeGroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddUpdateResumeGroupListViewHolder holder, final int position) {
        holder.rowNameLayout.setHint(this.rowNameHint);
        holder.rowAddressLayout.setHint(this.rowAddressHint);
        holder.rowDatePeriodLayout.setHint(this.rowDatePeriodHint);

        holder.rowName.setText(data.get(position).getAddress());
        holder.rowName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setHeader(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowAddress.setText(data.get(position).getAddress());
        holder.rowAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setAddress(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowDatePeriod.setText(data.get(position).getDatePeriod());
        holder.rowDatePeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setDatePeriod(charSequence.toString());
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

    public List<Resume> getData() {
        return data;
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
