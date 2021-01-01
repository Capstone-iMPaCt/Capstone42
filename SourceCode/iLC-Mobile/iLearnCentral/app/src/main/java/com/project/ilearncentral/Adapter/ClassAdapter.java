package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haozhang.lib.SlantedTextView;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Class;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Context context;
    private List<Class> classes;
    private ObservableString statusChange;

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
            holder.day.setText(Utility.getStringFromDay(aClass.getClassStart()));
        else holder.day.setVisibility(View.INVISIBLE);

        if (aClass.getClassStart()!=null)
            holder.date.setText(Utility.getStringFromDateFull(aClass.getClassStart()));
        else holder.date.setVisibility(View.INVISIBLE);

        if (aClass.getClassStart()!=null)
            holder.timeStart.setText(Utility.getStringFromTime(aClass.getClassStart()));
        else holder.timeStart.setVisibility(View.INVISIBLE);

        if (aClass.getClassEnd()!=null)
            holder.timeEnd.setText(Utility.getStringFromTime(aClass.getClassEnd()));
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
                break;
            case "Cancelled":
                holder.classStatus.setText("Cancelled");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.holo_red));
                break;
            case "Ongoing":
                holder.classStatus.setText("Ongoing");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.mint_blue));
                break;
            case "Requesting":
                holder.classStatus.setText("Requesting");
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.text_color));
                break;
            default:
                holder.classStatus.setText(aClass.getStatus());
                holder.classStatus.setSlantedBackgroundColor(context.getResources().getColor(R.color.light_blue));
                break;
        }

        if (Account.getType() == Account.Type.LearningCenter) {
            holder.viewButton.setVisibility(View.VISIBLE);
        } else if (Account.getType() == Account.Type.Educator) {
            holder.viewButton.setVisibility(View.VISIBLE);
        } else if (Account.getType() == Account.Type.Student) {
            holder.viewButton.setVisibility(View.GONE);
        }

        statusChange.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String value) {
                switch (value) {
                    case "Open":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Open"))
                            holder.containerLayout.setVisibility(View.GONE);
                        break;
                    case "Close":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Close"))
                            holder.containerLayout.setVisibility(View.GONE);
                        break;
                    case "Cancelled":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Cancelled"))
                            holder.containerLayout.setVisibility(View.GONE);
                        break;
                    case "Ongoing":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Ongoing"))
                            holder.containerLayout.setVisibility(View.GONE);
                        break;
                    case "Requested":
                        if (!holder.classStatus.getText().equalsIgnoreCase("Requested"))
                            holder.containerLayout.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to do
            }
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout containerLayout;
        private LinearLayout classStatusLayout;
        private SlantedTextView classStatus;
        private TextView day, date, timeStart, timeEnd, educator, roomNo;
        private Button viewButton;

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
            viewButton = itemView.findViewById(R.id.class_button);
        }
    }
}
