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
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.R;

import java.util.List;

public class AddUpdateResumeSingleListAdapter extends RecyclerView.Adapter<AddUpdateResumeSingleListAdapter.AddUpdateResumeSingleListViewHolder> {

    private Context context;
    private List<ResumeItem> data;
    private String rowDetailHint;

    public AddUpdateResumeSingleListAdapter(Context context, List<ResumeItem> data, String rowDetailHint) {
        this.context = context;
        this.data = data;
        this.rowDetailHint = rowDetailHint;
    }

    @NonNull
    @Override
    public AddUpdateResumeSingleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_add_update_resume_singlelist_row, parent, false);
        return new AddUpdateResumeSingleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddUpdateResumeSingleListViewHolder holder, final int position) {
        holder.rowDetailLayout.setHint(this.rowDetailHint);

        holder.rowDetail.setText(data.get(position).getDetail());
        holder.rowDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(position).setDetail(charSequence.toString());
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

    public class AddUpdateResumeSingleListViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout rowDetailLayout;
        private TextInputEditText rowDetail;

        AddUpdateResumeSingleListViewHolder(View itemView) {
            super(itemView);
            rowDetailLayout = itemView.findViewById(R.id.add_update_resume_singlelist_row_detail_layout);
            rowDetail = itemView.findViewById(R.id.add_update_resume_singlelist_row_detail);
        }
    }
}
