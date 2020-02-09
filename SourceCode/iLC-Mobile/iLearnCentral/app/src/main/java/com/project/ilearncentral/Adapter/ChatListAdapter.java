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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.MessagesActivity;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {

    private Context context;
    private List<Map<String, Object>> chatList;

    public ChatListAdapter(Context context, List<Map<String, Object>> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_chat, parent, false);

        return new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListHolder holder, int position) {
        holder.parent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final Map<String, Object> chat = chatList.get(position);
        holder.username.setText(chat.get("To").toString());
        holder.lastMessage.setText(chat.get("Message").toString());
        holder.time.setText(chat.get("elapseTime").toString());

        new Thread(new Runnable() {
            public void run() {
                try
                {
                    storageRef.child("images").child(chat.get("image").toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            Picasso.get().load(uri.toString()).error(R.drawable.user).into(holder.image);
                        }
                    });
                } catch (Exception e) {}
            }
        }).start();
        holder.parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra("TYPE", "both");
                intent.putExtra("USER_NAME", chat.get("To").toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println(chatList.size());
        return chatList.size();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder {

        private RelativeLayout parent;
        private TextView username, lastMessage, time;
        private ImageView image;

        ChatListHolder(@NonNull View itemView) {
            super(itemView);

            parent = (RelativeLayout)itemView.findViewById(R.id.chat_item_parent);
            username = (TextView)itemView.findViewById(R.id.chat_item_username);
            lastMessage = (TextView)itemView.findViewById(R.id.chat_item_message);
            time = (TextView)itemView.findViewById(R.id.chat_item_time);
            image = (ImageView) itemView.findViewById(R.id.chat_item_image);
        }
    }
}
