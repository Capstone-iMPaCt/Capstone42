package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.News;
import com.project.ilearncentral.R;

import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.newsViewHolder> {

    Context context;
    List<News> news;

    public NewsFeedAdapter(Context context, List<News> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new newsViewHolder(view);
    }

    // On bind/display animation
    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.timestampLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        
        holder.newsUserImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.newsUserImageView.setImageResource(news.get(position).getNewsUserImageView());
        holder.newsContentImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.newsContentImageView.setImageResource(news.get(position).getNewsContentImageView());
        
        holder.titleTextView.setText(news.get(position).getTitleTextView());
        holder.dateTextView.setText(news.get(position).getDateTextView());
        holder.timeTextView.setText(news.get(position).getTimeTextView());
        holder.contentTextView.setText(news.get(position).getContentTextView());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout containerLayout;
        LinearLayout timestampLayout;
        ImageView newsUserImageView, newsContentImageView;
        TextView titleTextView, dateTextView, timeTextView, contentTextView;

        newsViewHolder(View itemView) {
            super(itemView);
            containerLayout = (RelativeLayout)itemView.findViewById(R.id.container_layout);
            newsUserImageView = (ImageView)itemView.findViewById(R.id.news_user_imageview);
            titleTextView = (TextView)itemView.findViewById(R.id.news_title_textview);
            timestampLayout = (LinearLayout)itemView.findViewById(R.id.timestamp_layout);            
            dateTextView = (TextView)itemView.findViewById(R.id.news_date_textview);
            timeTextView = (TextView)itemView.findViewById(R.id.news_time_textview);
            newsContentImageView = (ImageView)itemView.findViewById(R.id.news_content_imageview);
            contentTextView = (TextView)itemView.findViewById(R.id.content_textview);
        }
    }
}
