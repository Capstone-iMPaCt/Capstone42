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
import com.project.ilearncentral.Activity.Messages;
import com.project.ilearncentral.Model.Message;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {

    private Context context;
    private List<Message> chatList;

    public ChatListAdapter(Context context, List<Message> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_chat_row, parent, false);

        return new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListHolder holder, int position) {
        holder.parent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        final Message chat = chatList.get(position);
        final boolean to = (chat.getType().equals("to"));
        if (to)
            holder.username.setText(chat.getFrom());
        else
            holder.username.setText(chat.getTo());
        holder.lastMessage.setText(chat.getMessage());
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        holder.time.setText(dateFormat.getInstance().format(chat.getDateSent().toDate()));

        new Thread(new Runnable() {
            public void run() {
            try
            {
                storageRef.child("images").child(holder.username.getText().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
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
                Intent intent = new Intent(context, Messages.class);
                intent.putExtra("TYPE", "both");
                if (to) {
                    intent.putExtra("USER_NAME", chat.getFrom());
                    intent.putExtra("TYPE", "From");
                }
                else {
                    intent.putExtra("USER_NAME", chat.getTo());
                    intent.putExtra("TYPE", "To");
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
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
