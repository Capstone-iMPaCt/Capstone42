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

public class EducatorDetailsAdapter extends RecyclerView.Adapter<EducatorDetailsAdapter.EducatorDetailsViewHolder> {

    Context context;
    List<Resume> details;

    public EducatorDetailsAdapter(Context context, List<Resume> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public EducatorDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_educator_details, parent, false);
        return new EducatorDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducatorDetailsViewHolder holder, int position) {
        holder.bullet.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));
        holder.detail.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));

        holder.detail.setText(details.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class EducatorDetailsViewHolder extends RecyclerView.ViewHolder {

        private ImageView bullet;
        private TextView detail;

        EducatorDetailsViewHolder(View itemView) {
            super(itemView);
            bullet = (ImageView) itemView.findViewById(R.id.educator_detail_item_bullet);
            detail = (TextView)itemView.findViewById(R.id.educator_detail_item);
        }
    }
}
