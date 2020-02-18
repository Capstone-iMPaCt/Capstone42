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

import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.R;

import java.util.List;

public class EnrolmentGroupListAdapter extends RecyclerView.Adapter<EnrolmentGroupListAdapter.EnrolmentBackgroundViewHolder> {

    Context context;
    List<Enrolment> data;

    public EnrolmentGroupListAdapter(Context context, List<Enrolment> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EnrolmentBackgroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row_item_group_list, parent, false);
        return new EnrolmentBackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrolmentBackgroundViewHolder holder, int position) {
        holder.rowLine.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_up));
        holder.rowBullet.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_up));
        if (position == data.size()-1)
            holder.rowLine.setVisibility(View.INVISIBLE);

        holder.rowTitle.setText(data.get(position).getLearningCenterName());
        holder.rowAddress.setText(data.get(position).getCourseEnrolled());
        holder.rowDatePeriod.setText(data.get(position).getDateEnrolled());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class EnrolmentBackgroundViewHolder extends RecyclerView.ViewHolder {

        private View rowLine;
        private ImageView rowBullet;
        private TextView rowTitle, rowAddress, rowDatePeriod;

        EnrolmentBackgroundViewHolder(View itemView) {
            super(itemView);
            rowLine = (View) itemView.findViewById(R.id.resume_group_list_row_line);
            rowBullet = (ImageView) itemView.findViewById(R.id.resume_group_list_row_bullet);
            rowTitle = (TextView) itemView.findViewById(R.id.resume_group_list_row_company_name);
            rowAddress = (TextView) itemView.findViewById(R.id.resume_group_list_row_company_address);
            rowDatePeriod = (TextView) itemView.findViewById(R.id.resume_group_list_row_date_period);
        }
    }
}
