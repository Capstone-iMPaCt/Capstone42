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

public class ResumeSingleListAdapter extends RecyclerView.Adapter<ResumeSingleListAdapter.EducatorDetailsViewHolder> {

    Context context;
    List<Resume> data;

    public ResumeSingleListAdapter(Context context, List<Resume> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EducatorDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row_item_single_list, parent, false);
        return new EducatorDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducatorDetailsViewHolder holder, int position) {
        holder.rowBullet.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));
        holder.rowData.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));

        holder.rowData.setText(data.get(position).getDataList());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class EducatorDetailsViewHolder extends RecyclerView.ViewHolder {

        private ImageView rowBullet;
        private TextView rowData;

        EducatorDetailsViewHolder(View itemView) {
            super(itemView);
            rowBullet = (ImageView) itemView.findViewById(R.id.resume_single_list_row_bullet);
            rowData = (TextView)itemView.findViewById(R.id.resume_single_list_row_data);
        }
    }
}
