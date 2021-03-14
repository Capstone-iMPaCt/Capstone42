package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.ilearncentral.Activity.ViewResume;
import com.project.ilearncentral.Activity.ViewUser;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.R;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder> {

    private Context context;
    private List<User> users;
    private String jobPostID;
    private int lastPosition = -1;

    public SearchUserAdapter(Context context, List<User> users, String jobPostID) {
        this.context = context;
        this.users = users;
        this.jobPostID = jobPostID;
    }

    @NonNull
    @Override
    public SearchUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_search_user_row, parent, false);

        return new SearchUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchUserHolder holder, int position) {
        final User user = users.get(position);

        holder.root.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        holder.type.setText(user.getStringType());
        holder.username.setText(user.getFullname());
//        if (!user.getImage().isEmpty())
        try {
            if (holder.image.getBackground() == null)
//            Picasso.get().load(Uri.parse(user.getImage())).error(R.drawable.user).fit().into(holder.image);
                Glide.with(context).load(user.getImage()).error(R.drawable.user).fitCenter()
                        .into(holder.image);
        } catch (Exception e) {
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jobPostID != null) {
                    Intent intent = new Intent(context, ViewResume.class);
                    intent.putExtra("educator", user.getUsername());
//                    intent.putExtra("jobApplication", "");
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ViewUser.class);
                    intent.putExtra("USERNAME", user.getUsername());
                    intent.putExtra("TYPE", user.getType());
                    intent.putExtra("FULL_NAME", user.getFullname());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class SearchUserHolder extends RecyclerView.ViewHolder {

        private CardView root;
        private RelativeLayout parent;
        private TextView username, type;
        private ImageView image;

        SearchUserHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.search_user_root);
            parent = itemView.findViewById(R.id.search_user_parent);
            username = itemView.findViewById(R.id.search_username);
            type = itemView.findViewById(R.id.search_user_type);
            image = itemView.findViewById(R.id.search_user_image);
        }
    }
}
