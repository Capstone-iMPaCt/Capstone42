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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.ilearncentral.Model.News;
import com.project.ilearncentral.R;

import java.util.List;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.newsViewHolder> {

<<<<<<< HEAD:SourceCode/iLC-Mobile/iLearnCentral/app/src/main/java/com/project/ilearncentral/Adapter/PostAdapter.java
    private Context context;
    private List<Post> posts;
    private OnPostTitleListener onPostTitleListener;
=======
    Context context;
    List<News> news;
>>>>>>> b698c1d5c5e5273ea2d047a9046c9baf9dc765ca:SourceCode/iLC-Mobile/iLearnCentral/app/src/main/java/com/project/ilearncentral/Adapter/NewsFeedAdapter.java

    public NewsFeedAdapter(Context context, List<News> news) {
        this.context = context;
        this.news = news;
    }

    public PostAdapter(Context context, List<Post> posts, OnPostTitleListener onPostTitleListener) {
        this.context = context;
        this.posts = posts;
        this.onPostTitleListener = onPostTitleListener;
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD:SourceCode/iLC-Mobile/iLearnCentral/app/src/main/java/com/project/ilearncentral/Adapter/PostAdapter.java
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new newsViewHolder(view, onPostTitleListener);
=======
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new newsViewHolder(view);
>>>>>>> b698c1d5c5e5273ea2d047a9046c9baf9dc765ca:SourceCode/iLC-Mobile/iLearnCentral/app/src/main/java/com/project/ilearncentral/Adapter/NewsFeedAdapter.java
    }

    // On bind/display animation
    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.timestampLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        
        holder.newsUserImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.newsContentImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        
        holder.titleTextView.setText(news.get(position).getTitleTextView());
        holder.dateTextView.setText(news.get(position).getDateTextView());
        holder.timeTextView.setText(news.get(position).getTimeTextView());
        holder.contentTextView.setText(news.get(position).getContentTextView());

        getImage(holder.newsContentImageView,news.get(position).getNewsContentImageView(),400);
        getImage(holder.newsUserImageView,news.get(position).getNewsUserImageView(),600);

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RelativeLayout containerLayout;
        LinearLayout timestampLayout;
        ImageView newsUserImageView, newsContentImageView;
        TextView titleTextView, dateTextView, timeTextView, contentTextView;
        OnPostTitleListener onPostTitleListener;

        public newsViewHolder(View itemView, OnPostTitleListener onPostTitleListener) {
            super(itemView);
            containerLayout = (RelativeLayout)itemView.findViewById(R.id.container_layout);
            newsUserImageView = (ImageView)itemView.findViewById(R.id.news_user_imageview);
            titleTextView = (TextView)itemView.findViewById(R.id.news_title_textview);
            timestampLayout = (LinearLayout)itemView.findViewById(R.id.timestamp_layout);            
<<<<<<< HEAD:SourceCode/iLC-Mobile/iLearnCentral/app/src/main/java/com/project/ilearncentral/Adapter/PostAdapter.java
            dateTextView = (TextView)itemView.findViewById(R.id.post_date);
            timeTextView = (TextView)itemView.findViewById(R.id.post_time);
            newsContentImageView = (ImageView)itemView.findViewById(R.id.post_content_image);
            contentTextView = (TextView)itemView.findViewById(R.id.post_description);

            titleTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostTitleListener.onTitleClick(getAdapterPosition());
=======
            dateTextView = (TextView)itemView.findViewById(R.id.news_date_textview);
            timeTextView = (TextView)itemView.findViewById(R.id.news_time_textview);
            newsContentImageView = (ImageView)itemView.findViewById(R.id.news_content_imageview);
            contentTextView = (TextView)itemView.findViewById(R.id.content_textview);
>>>>>>> b698c1d5c5e5273ea2d047a9046c9baf9dc765ca:SourceCode/iLC-Mobile/iLearnCentral/app/src/main/java/com/project/ilearncentral/Adapter/NewsFeedAdapter.java
        }
    }
    private void getImage(ImageView newsUserImageView, int newsContentImageView, int height) {
        Glide
                .with(context)
                .load(newsContentImageView)
                .centerCrop()
                .apply(new RequestOptions().override(height))
                .into(newsUserImageView);
    }

    public interface OnPostTitleListener{
        void onTitleClick(int position);
    }
}

