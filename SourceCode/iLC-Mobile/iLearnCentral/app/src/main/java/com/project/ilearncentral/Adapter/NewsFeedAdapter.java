package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.News;
import com.project.ilearncentral.R;

import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.newsViewHolder> {

    Context context;
    List<News> mNews;
    public NewsFeedAdapter(Context context, List<News> mNews) {
        this.context = context;
        this.mNews = mNews;
    }




    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new newsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
        holder.tv_date.setText(mNews.get(position).getDate());
        holder.tv_title.setText(mNews.get(position).getTitle());
        holder.tv_description.setText(mNews.get(position).getContent());
        holder.img_user.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        holder.img_user.setImageResource(mNews.get(position).getUserPhoto());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_title,tv_description;
        ImageView img_user;
        RelativeLayout container;
        newsViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            img_user = (ImageView) itemView.findViewById(R.id.img_user);
            container = itemView.findViewById(R.id.container);
        }
    }
}

