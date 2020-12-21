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
import com.project.ilearncentral.Model.BankAccountDetail;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class AddUpdateBankAccountDetailListAdapter extends RecyclerView.Adapter<AddUpdateBankAccountDetailListAdapter.AddUpdateBankAccountDetailListViewHolder> {

    private Context context;
    private List<BankAccountDetail> data;

    public AddUpdateBankAccountDetailListAdapter(Context context, List<BankAccountDetail> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public AddUpdateBankAccountDetailListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_nve_bank_account_detail_list_row, parent, false);
        return new AddUpdateBankAccountDetailListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddUpdateBankAccountDetailListViewHolder holder, final int position) {
        final BankAccountDetail pos = data.get(position);

        holder.rowBankName.setText(pos.getBankName());
        holder.rowBankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pos.setBankName(charSequence.toString());
                if (holder.rowBankName.getText().toString().isEmpty())
                    holder.rowBankName.setError("Field is empty");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.rowAccountNumber.setText(pos.getBankAccountNumber());
        holder.rowAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pos.setBankAccountNumber(charSequence.toString());
                if (holder.rowAccountNumber.getText().toString().isEmpty())
                    holder.rowAccountNumber.setError("Field is empty");
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

    public List<BankAccountDetail> getData() {
        return data;
    }

    protected class AddUpdateBankAccountDetailListViewHolder extends RecyclerView.ViewHolder {

        private TextInputEditText rowBankName, rowAccountNumber;

        AddUpdateBankAccountDetailListViewHolder(View itemView) {
            super(itemView);
            rowBankName = itemView.findViewById(R.id.nve_bank_account_detail_bank_name);
            rowAccountNumber = itemView.findViewById(R.id.nve_bank_account_detail_account_number);
        }
    }
}
