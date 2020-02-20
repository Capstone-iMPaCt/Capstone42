package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.AddEditFeed;
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.Model.Posts;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostFeedAdapter extends RecyclerView.Adapter<PostFeedAdapter.PostViewHolder> {

    private Context context;
    private List<Post> posts;
    private Intent intent;
    private StorageReference storageRef;

    public PostFeedAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_post, parent, false);
        return new PostViewHolder(view);
    }

    private int lastPosition = -1;

    // On bind/display animation
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        holder.headerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        holder.userImageView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        final Post post = posts.get(position);
        getImage(holder.userImageView, "images/" , post.getNewsUserImageView());
        holder.contentImageView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        getImage(holder.contentImageView, "posts/" , post.getNewsContentImageView());

        lastPosition = position;

        holder.titleTextView.setText(post.getTitleTextView());
        holder.dateTextView.setText(post.getDateTextView());
        holder.timeTextView.setText(post.getTimeTextView());
        holder.contentTextView.setText(post.getContentTextView());

        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent = new Intent(context, Chat.class);
                intent.putExtra("title", posts.get(position).getTitleTextView());
                context.startActivity(intent);*/
                Toast.makeText(context, post.getTitleTextView(), Toast.LENGTH_SHORT).show();
//                System.out.print(posts.get(position).getTitleTextView());
            }
        });

        holder.editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts.setCurPost(Posts.getIdOfPost(post.getNewsUserImageView(), post.getContentTextView()));
                if (Posts.hasCurrent()) {
                    context.startActivity(new Intent(context, AddEditFeed.class));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void getImage(final ImageView newsUserImageView, String folderName, String filename) {
//        Glide.with(context)
//                .load(storageRef.child(folderName).child(filename))
//                .into(newsUserImageView);
        storageRef.child(folderName).child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(newsUserImageView);
            }
        });
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout containerLayout, headerLayout;
        private CircleImageView userImageView;
        private ImageView contentImageView;
        private TextView titleTextView, dateTextView, timeTextView, contentTextView, editTextView;

        PostViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.addon_container_relativelayout);
            userImageView = itemView.findViewById(R.id.user_imageview);
            titleTextView = itemView.findViewById(R.id.post_title_textview);
            headerLayout = itemView.findViewById(R.id.timestamp_layout);
            dateTextView = itemView.findViewById(R.id.date_textview);
            timeTextView = itemView.findViewById(R.id.time_textview);
            contentImageView = itemView.findViewById(R.id.content_imageview);
            contentTextView = itemView.findViewById(R.id.content_textview);
            editTextView = itemView.findViewById(R.id.edit_textview);

        }
    }
}

