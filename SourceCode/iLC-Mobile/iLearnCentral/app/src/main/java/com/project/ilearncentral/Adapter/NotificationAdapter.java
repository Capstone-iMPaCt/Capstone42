package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.Model.Notification;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

import androidx.annotation.NonNull;
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
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_notification_row, parent, false);

        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder holder, int position) {
        final Notification notification = notifications.get(position);
        holder.subject.setText(notification.getSubject());
        holder.message.setText(notification.getMessage());
        holder.time.setText(Utility.getDateTimeStringFromTimestamp(notification.getDate()));
        holder.status.setText(notification.getStatus());
        if (notification.getStatus().equalsIgnoreCase("unread")) {
            holder.link.setEnabled(true);
            holder.parent.setEnabled(true);
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.bt_black_12));
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotifToRead(notification, holder);
                }
            });
            holder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotifToRead(notification, holder);
                }
            });
        } else {
            holder.link.setEnabled(false);
            holder.parent.setEnabled(false);
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.bt_white));
        }
    }

    private void setNotifToRead(Notification notification, @NonNull NotificationHolder holder) {
        FirebaseFirestore.getInstance().collection("Notification").document(notification.getNotifId()).update("Status", "read");
        notification.setStatus("read");
        holder.link.setEnabled(false);
        holder.parent.setEnabled(false);
        holder.parent.setBackgroundColor(context.getResources().getColor(R.color.bt_white));
        switch (notification.getLink()) {
            case "none":
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        private RelativeLayout parent;
        private Button link;
        private TextView subject, message, time, status;

        NotificationHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.notif_row_layout);
            subject = itemView.findViewById(R.id.notif_item_subject);
            message = itemView.findViewById(R.id.notif_item_message);
            time = itemView.findViewById(R.id.notif_item_time);
            link = itemView.findViewById(R.id.notif_item_link);
            status = itemView.findViewById(R.id.notif_item_status);
        }
    }
}
