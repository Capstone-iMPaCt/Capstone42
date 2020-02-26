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

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.AddEditFeed;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Posts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {

        final Post post = posts.get(position);

        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
//        holder.headerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
//        holder.userImageView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
//        holder.contentImageView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        getImage(holder.userImageView, "images/" , post.getPostSender());

        if (post.isWithImage())
            getImage(holder.contentImageView, "posts/" , post.getPostId());
        else
            holder.contentImageView.setVisibility(View.GONE);

        lastPosition = position;
        ObservableString fullname = new ObservableString();
        fullname.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String fullName) {
                holder.fullnameTextView.setText(fullName);
                post.setFullname(fullName);
                String postID = Posts.getIdOfPost(post.getPostSender(), post.getContent());
                Posts.getPostById(postID).put("FullName", fullName);
            }
        });
        if (post.getFullname().isEmpty()) {
            Utility.getFullName(post.getPostSender(), fullname);
        } else {
            holder.fullnameTextView.setText(post.getFullname());
        }

        holder.titleTextView.setText(post.getPostTitle());
        holder.dateTextView.setText(post.getDatePosted());
        holder.timeTextView.setText(post.getTimePosted());
        holder.contentTextView.setText(post.getContent());
        holder.editTextView.setVisibility(View.VISIBLE);

        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view user
            }
        });
        if (!post.getPostSender().equals(Account.getStringData("username"))) {
            holder.editTextView.setVisibility(View.GONE);
        }

        holder.editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts.setCurPost(Posts.getIdOfPost(post.getPostSender(), post.getContent()));
                if (Posts.hasCurrent()) {
                    Intent i = new Intent(context, AddEditFeed.class);
                    i.putExtra("postId", post.getPostId());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void getImage(final ImageView imageView, String folderName, String filename) {
        storageRef.child(folderName).child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(uri.toString()).fitCenter().into(imageView);
//                Picasso.get().load(uri.toString()).centerCrop().fit().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setVisibility(View.GONE);
            }
        });
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout containerLayout;
        private RelativeLayout headerLayout;
        private CircleImageView userImageView;
        private ImageView contentImageView;
        private TextView fullnameTextView, titleTextView, dateTextView, timeTextView, contentTextView, editTextView;

        PostViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.item_post_container);
            userImageView = itemView.findViewById(R.id.item_post_user_display_image);
            fullnameTextView = itemView.findViewById(R.id.item_post_user_fullname);
            titleTextView = itemView.findViewById(R.id.item_post_title);
            headerLayout = itemView.findViewById(R.id.item_post_header_container);
            dateTextView = itemView.findViewById(R.id.item_post_date);
            timeTextView = itemView.findViewById(R.id.item_post_time);
            contentImageView = itemView.findViewById(R.id.item_post_content_image);
            contentTextView = itemView.findViewById(R.id.item_post_content_text);
            editTextView = itemView.findViewById(R.id.item_post_edit_link);
        }
    }
}

