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

public class EducatorBackgroundAdapter extends RecyclerView.Adapter<EducatorBackgroundAdapter.EducatorBackgroundViewHolder> {

    Context context;
    List<Resume> details;

    public EducatorBackgroundAdapter(Context context, List<Resume> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public EducatorBackgroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_educator_profile_background, parent, false);
        return new EducatorBackgroundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducatorBackgroundViewHolder holder, int position) {
        holder.listLine.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));
        holder.bullet.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));
        if (position == details.size()-1)
            holder.listLine.setVisibility(View.INVISIBLE);

        holder.companyName.setText(details.get(position).getCompanyName());
        holder.companyAddress.setText(details.get(position).getCompanyAddress());
        holder.durationPeriod.setText(details.get(position).getDurationPeriod());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class EducatorBackgroundViewHolder extends RecyclerView.ViewHolder {

        private View listLine;
        private ImageView bullet;
        private TextView companyName, companyAddress, durationPeriod;

        EducatorBackgroundViewHolder(View itemView) {
            super(itemView);
            listLine = (View) itemView.findViewById(R.id.educator_bg_item_line);
            bullet = (ImageView) itemView.findViewById(R.id.educator_bg_item_bullet);
            companyName = (TextView) itemView.findViewById(R.id.educator_bg_item_company_name);
            companyAddress = (TextView) itemView.findViewById(R.id.educator_bg_item_company_address);
            durationPeriod = (TextView) itemView.findViewById(R.id.educator_bg_item_duration_period);
        }
    }
}
