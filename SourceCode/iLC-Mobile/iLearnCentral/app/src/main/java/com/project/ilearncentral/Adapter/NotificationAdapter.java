package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.Model.Message;
import com.project.ilearncentral.Model.Notification;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_notification_row, parent, false);

        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder holder, int position) {
        final Notification notification = notifications.get(position);
        holder.subject.setText(notification.getSubject());
        holder.message.setText(notification.getMessage());
        holder.time.setText(Utility.getDateTimeStringFromTimestamp(notification.getDate()));
        if (notification.getStatus().equalsIgnoreCase("unread")) {
            holder.parent.setEnabled(true);
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.bt_black_12));
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore.getInstance().collection("Notification").document(notification.getNotifId()).update("Status", "read");
                    holder.parent.setEnabled(false);
                    holder.parent.setBackgroundColor(context.getResources().getColor(R.color.bt_white));
                    switch (notification.getLink()) {
                        case "none":
                        default:
                            break;
                    }
                }
            });
        } else {
            holder.parent.setEnabled(false);
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.bt_white));
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("Notification size: " + notifications.size());
        return notifications.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        private RelativeLayout parent;
        private TextView subject, message, time;

        NotificationHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.notif_row_layout);
            subject = itemView.findViewById(R.id.notif_item_subject);
            message = itemView.findViewById(R.id.notif_item_message);
            time = itemView.findViewById(R.id.notif_item_time);
        }
    }
}
