package com.project.ilearncentral.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.haozhang.lib.SlantedTextView;
import com.project.ilearncentral.Activity.NveClass;
import com.project.ilearncentral.Activity.RequestSchedChange;
import com.project.ilearncentral.Activity.ViewRecordActivity;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Subscription;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private final Context context;
    private final List<Class> classes;
    private final ObservableString statusChange;

    public ClassAdapter(Context context, List<Class> classes, ObservableString statusChange) {
        this.context = context;
        this.classes = classes;
        this.statusChange = statusChange;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_class_row, parent, false); //to change
        return new ClassAdapter.ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassViewHolder holder, final int position) {
        final Class aClass = classes.get(position);

        if (aClass.getClassStart()!=null)
            holder.day.setText(Utility.getDayStringFromTimestamp(aClass.getClassStart()));
        else holder.day.setVisibility(View.INVISIBLE);

        if (aClass.getClassStart()!=null)
            holder.date.setText(Utility.getStringFromDateFull(aClass.getClassStart()));
        else holder.date.setVisibility(View.INVISIBLE);

        if (aClass.getClassStart()!=null)
            holder.timeStart.setText(Utility.getTimeStringFromTimestamp(aClass.getClassStart()));
        else holder.timeStart.setVisibility(View.INVISIBLE);

        if (aClass.getClassEnd()!=null)
            holder.timeEnd.setText(Utility.getTimeStringFromTimestamp(aClass.getClassEnd()));
        else holder.timeEnd.setVisibility(View.INVISIBLE);

        if (aClass.getEducator()!=null)
            holder.educator.setText(aClass.getEducator().getFullname());
        else holder.educator.setVisibility(View.INVISIBLE);

        if (!aClass.getRoomNo().isEmpty())
            holder.roomNo.setText(aClass.getRoomNo());
        else holder.roomNo.setVisibility(View.INVISIBLE);

        switch (aClass.getStatus()) {
            case "Open":
                holder.classStatus.setText("Open");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.light_blue));
                break;
            case "Close":
                holder.classStatus.setText("Close");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.hint_color));
                if (Account.getType() == Account.Type.Educator)
                    holder.otherButton.setVisibility(View.INVISIBLE);
                break;
            case "Cancelled":
                holder.classStatus.setText("Cancelled");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.holo_red));
                if (Account.getType() == Account.Type.Educator)
                    holder.otherButton.setVisibility(View.INVISIBLE);
                break;
            case "Ongoing":
                holder.classStatus.setText("Ongoing");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.mint_blue));
                break;
            case "Pending":
                holder.classStatus.setText("Pending");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.text_color));
                break;
            default:
                holder.classStatus.setText(aClass.getStatus());
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.light_blue));
                break;
        }

        if (Account.getType() == Account.Type.LearningCenter) {
            holder.viewButton.setVisibility(View.VISIBLE);
            holder.otherButton.setText("UPDATE CLASS");
        } else if (Account.getType() == Account.Type.Educator) {
            holder.viewButton.setVisibility(View.VISIBLE);
            holder.delButton.setVisibility(View.GONE);
            holder.otherButton.setText("REQUEST RESCHEDULE");
            if(!aClass.getEduID().equals(Account.getUsername())) {
                holder.containerLayout.setVisibility(View.GONE);
            }
            else {
                holder.containerLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.viewButton.setVisibility(View.GONE);
            holder.otherButton.setVisibility(View.GONE);
            holder.delButton.setVisibility(View.GONE);
        }

        statusChange.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String value) {
                switch (value) {
                    case "Open":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Open"))
                            holder.containerLayout.setVisibility(View.GONE);
                        else
                            holder.containerLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Close":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Close"))
                            holder.containerLayout.setVisibility(View.GONE);
                        else
                            holder.containerLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Cancelled":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Cancelled"))
                            holder.containerLayout.setVisibility(View.GONE);
                        else
                            holder.containerLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Ongoing":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Ongoing"))
                            holder.containerLayout.setVisibility(View.GONE);
                        else
                            holder.containerLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Pending":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Pending"))
                            holder.containerLayout.setVisibility(View.GONE);
                        else
                            holder.containerLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Account.getType() == Account.Type.LearningCenter) {
                    if (!Subscription.isSchedulingSubscribed()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Please subscribe");
                        alertDialog.setCancelable(true);
                        alertDialog
                                .setMessage("You do not have access to this feature.\nPlease subscribe.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        return;
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    Intent intent = new Intent(context, ViewRecordActivity.class);
                    intent.putExtra("classID", aClass.getClassID());
                    intent.putExtra("action", "view");
                    context.startActivity(intent);
                } else if (Account.getType() == Account.Type.Educator) {
                    Intent intent = new Intent(context, ViewRecordActivity.class);
                    intent.putExtra("classID", aClass.getClassID());
                    if (aClass.getEduID().equalsIgnoreCase(Account.getUsername()))
                        intent.putExtra("action", "edit");
                    else
                        intent.putExtra("action", "view");
                    context.startActivity(intent);
                }
            }
        });

        holder.otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Account.getType() == Account.Type.LearningCenter) {
                    if (!Subscription.isSchedulingSubscribed()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Please subscribe");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        return;
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    Intent intent = new Intent(context, NveClass.class);
                    intent.putExtra("classID", aClass.getClassID());
                    intent.putExtra("courseID", aClass.getCourseID());
                    intent.putExtra("action", "edit");
                    context.startActivity(intent);
                } else if (Account.getType() == Account.Type.Educator) {
                    Intent intent = new Intent(context, RequestSchedChange.class);
                    intent.putExtra("classID", aClass.getClassID());
                    context.startActivity(intent);
                }
            }
        });

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Subscription.isSchedulingSubscribed()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Please subscribe");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    return;
                                }
                            });
                    alertDialog.show();
                    return;
                }
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseFirestore.getInstance().collection("Class").document(aClass.getClassID()).delete();
                                holder.containerLayout.setVisibility(View.GONE);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure in deleting class?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout containerLayout;
        private final LinearLayout classStatusLayout;
        private final SlantedTextView classStatus;
        private final TextView day;
        private final TextView date;
        private final TextView timeStart;
        private final TextView timeEnd;
        private final TextView educator;
        private final TextView roomNo;
        private final Button viewButton;
        private final Button otherButton;
        private final ImageButton delButton;

        ClassViewHolder(View itemView) {
            super(itemView);
            containerLayout = itemView.findViewById(R.id.class_container);
            classStatusLayout = itemView.findViewById(R.id.class_status_layout);
            classStatus = itemView.findViewById(R.id.class_status);
            day = itemView.findViewById(R.id.class_day);
            date = itemView.findViewById(R.id.class_date);
            timeStart = itemView.findViewById(R.id.class_schedule_time_start);
            timeEnd = itemView.findViewById(R.id.class_schedule_time_end);
            educator = itemView.findViewById(R.id.class_educator_name);
            roomNo = itemView.findViewById(R.id.class_room_no);
            otherButton = itemView.findViewById(R.id.class_update_button);
            viewButton = itemView.findViewById(R.id.class_view_button);
            delButton = itemView.findViewById(R.id.class_delete_button);
        }
    }
}
