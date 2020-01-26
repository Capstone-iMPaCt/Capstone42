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
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.newsViewHolder> {

    Context context;
    List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new newsViewHolder(view);
    }

    // On bind/display animation
    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.timestampLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        
        holder.newsUserImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        holder.newsContentImageView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        
        holder.titleTextView.setText(posts.get(position).getTitleTextView());
        holder.dateTextView.setText(posts.get(position).getDateTextView());
        holder.timeTextView.setText(posts.get(position).getTimeTextView());
        holder.contentTextView.setText(posts.get(position).getContentTextView());

        getImage(holder.newsContentImageView, posts.get(position).getNewsContentImageView());
        getImage(holder.newsUserImageView, posts.get(position).getNewsUserImageView());

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout containerLayout;
        LinearLayout timestampLayout;
        ImageView newsUserImageView, newsContentImageView;
        TextView titleTextView, dateTextView, timeTextView, contentTextView;

        newsViewHolder(View itemView) {
            super(itemView);
            containerLayout = (RelativeLayout)itemView.findViewById(R.id.container_layout);
            newsUserImageView = (ImageView)itemView.findViewById(R.id.post_user_image);
            titleTextView = (TextView)itemView.findViewById(R.id.post_title);
            timestampLayout = (LinearLayout)itemView.findViewById(R.id.timestamp_layout);            
            dateTextView = (TextView)itemView.findViewById(R.id.post_date);
            timeTextView = (TextView)itemView.findViewById(R.id.post_time);
            newsContentImageView = (ImageView)itemView.findViewById(R.id.post_content_image);
            contentTextView = (TextView)itemView.findViewById(R.id.post_description);
        }
    }

    private void getImage(ImageView newsUserImageView, int newsContentImageView) {
        Glide
                .with(context)
                .load(newsContentImageView)
                .fitCenter()
                .apply(new RequestOptions())
                .into(newsUserImageView);
    }

    private void getImage(ImageView newsUserImageView, int newsContentImageView, int height) {
        Glide
                .with(context)
                .load(newsContentImageView)
                .fitCenter()
                .apply(new RequestOptions().override(height))
                .into(newsUserImageView);
    }
}

