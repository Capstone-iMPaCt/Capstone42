package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.project.ilearncentral.Model.Message;
import com.project.ilearncentral.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private Context context;
    private List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_message_row, parent, false);

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageHolder holder, int position) {
        //holder.parent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        final Message message = messageList.get(position);
        final boolean to = (message.getType().equals("to"));

        holder.message.setText(message.getMessage());
        if (to) {
            holder.message.setBackground(context.getDrawable(R.drawable.shape_bg_outgoing_message));
            holder.message.setPadding(10,10,20,20);
            holder.time.setPadding(0,0, 30,0);
            holder.time.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.parent);
            constraintSet.connect(holder.time.getId(),ConstraintSet.END,holder.parent.getId(),ConstraintSet.END,0);
            constraintSet.connect(holder.message.getId(),ConstraintSet.END,holder.parent.getId(),ConstraintSet.END,0);
            constraintSet.connect(holder.message.getId(),ConstraintSet.TOP,holder.parent.getId(),ConstraintSet.TOP,0);
            constraintSet.clear(holder.time.getId(),ConstraintSet.START);
            constraintSet.clear(holder.message.getId(),ConstraintSet.START);
            constraintSet.clear(holder.message.getId(),ConstraintSet.TOP);
            constraintSet.applyTo(holder.parent);
            holder.parent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation_right));
        }
        else {
            holder.message.setBackground(context.getDrawable(R.drawable.shape_bg_incoming_message));
            holder.message.setPadding(20,10,10,20);
            holder.time.setPadding(30,0,0,0);
            holder.time.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.parent);
            constraintSet.connect(holder.time.getId(),ConstraintSet.START,holder.parent.getId(),ConstraintSet.START,0);
            constraintSet.connect(holder.message.getId(),ConstraintSet.START,holder.parent.getId(),ConstraintSet.START,0);
            constraintSet.connect(holder.message.getId(),ConstraintSet.TOP,holder.parent.getId(),ConstraintSet.TOP,0);
            constraintSet.clear(holder.time.getId(),ConstraintSet.END);
            constraintSet.clear(holder.message.getId(),ConstraintSet.END);
            constraintSet.clear(holder.message.getId(),ConstraintSet.TOP);
            constraintSet.applyTo(holder.parent);
            holder.parent.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        }
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        holder.time.setText(dateFormat.getInstance().format(message.getDateSent().toDate()));

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout parent;
        private TextView message, time;

        MessageHolder(@NonNull View itemView) {
            super(itemView);

            parent = (ConstraintLayout) itemView.findViewById(R.id.message_parent);
            message = (TextView)itemView.findViewById(R.id.message_message);
            time = (TextView)itemView.findViewById(R.id.message_date);
        }
    }
}
