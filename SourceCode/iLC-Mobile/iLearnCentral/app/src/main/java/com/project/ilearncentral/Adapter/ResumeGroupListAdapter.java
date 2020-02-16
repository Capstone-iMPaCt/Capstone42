package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.Resume;
import com.project.ilearncentral.R;

import java.util.List;

public class ResumeGroupListAdapter extends RecyclerView.Adapter<ResumeGroupListAdapter.EducatorBackgroundViewHolder> {

    Context context;
    List<Resume> data;

    public ResumeGroupListAdapter(Context context, List<Resume> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EducatorBackgroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_educator_row_item_group_list, parent, false);
        return new EducatorBackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducatorBackgroundViewHolder holder, int position) {
        holder.rowLine.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_up));
        holder.rowBullet.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_up));
        if (position == data.size()-1)
            holder.rowLine.setVisibility(View.INVISIBLE);

        holder.rowTitle.setText(data.get(position).getHeader());
        holder.rowAddress.setText(data.get(position).getAddress());
        holder.rowDatePeriod.setText(data.get(position).getDatePeriod());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class EducatorBackgroundViewHolder extends RecyclerView.ViewHolder {

        private View rowLine;
        private ImageView rowBullet;
        private TextView rowTitle, rowAddress, rowDatePeriod;

        EducatorBackgroundViewHolder(View itemView) {
            super(itemView);
            rowLine = (View) itemView.findViewById(R.id.resume_group_list_row_line);
            rowBullet = (ImageView) itemView.findViewById(R.id.resume_group_list_row_bullet);
            rowTitle = (TextView) itemView.findViewById(R.id.resume_group_list_row_company_name);
            rowAddress = (TextView) itemView.findViewById(R.id.resume_group_list_row_company_address);
            rowDatePeriod = (TextView) itemView.findViewById(R.id.resume_group_list_row_date_period);
        }
    }
}
