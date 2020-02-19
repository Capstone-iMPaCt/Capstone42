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
import com.project.ilearncentral.Model.Post;
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
        getImage(holder.userImageView, "images/" , posts.get(position).getNewsUserImageView());
        holder.contentImageView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        getImage(holder.contentImageView, "posts/" , posts.get(position).getNewsContentImageView());

        lastPosition = position;

        holder.titleTextView.setText(posts.get(position).getTitleTextView());
        holder.dateTextView.setText(posts.get(position).getDateTextView());
        holder.timeTextView.setText(posts.get(position).getTimeTextView());
        holder.contentTextView.setText(posts.get(position).getContentTextView());

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
                Toast.makeText(context, posts.get(position).getTitleTextView(), Toast.LENGTH_SHORT).show();
//                System.out.print(posts.get(position).getTitleTextView());
            }
        });

//        holder.userImageView.setOnClickListener(this);
//        holder.titleTextView.setOnClickListener(this);
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
        private TextView titleTextView, dateTextView, timeTextView, contentTextView;

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
        }
    }
}

