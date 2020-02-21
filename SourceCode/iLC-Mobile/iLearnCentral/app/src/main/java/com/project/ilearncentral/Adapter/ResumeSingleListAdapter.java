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

public class ResumeSingleListAdapter extends RecyclerView.Adapter<ResumeSingleListAdapter.ResumeSingleListViewHolder> {

    Context context;
    List<Resume> data;

    public ResumeSingleListAdapter(Context context, List<Resume> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ResumeSingleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_resume_single_list_row, parent, false);
        return new ResumeSingleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeSingleListViewHolder holder, int position) {
        holder.rowBullet.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));
        holder.rowData.setAnimation(AnimationUtils.loadAnimation(context, R.anim.move_left));

        holder.rowData.setText(data.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ResumeSingleListViewHolder extends RecyclerView.ViewHolder {

        private ImageView rowBullet;
        private TextView rowData;

        ResumeSingleListViewHolder(View itemView) {
            super(itemView);
            rowBullet = (ImageView) itemView.findViewById(R.id.resume_single_list_row_bullet);
            rowData = (TextView)itemView.findViewById(R.id.resume_single_list_row_data);
        }
    }
}
