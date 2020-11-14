package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.project.ilearncentral.Model.Enrollee;
import com.project.ilearncentral.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EnrolleeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Enrollee> enrollees;

    public EnrolleeAdapter(Context context, ArrayList<Enrollee> enrollees) {
        this.context = context;
        this.enrollees = enrollees;
    }

    @Override
    public int getCount() {
        return enrollees.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.recyclerview_enrollee_item, viewGroup, false);

        Enrollee enrollee = enrollees.get(i);

        CircleImageView enrolleeImage = view.findViewById(R.id.enrollee_user_image);
        TextView enrolleeName = view.findViewById(R.id.enrollee_user_name);
        TextView enrolleeCourseEnrolled = view.findViewById(R.id.enrollee_course_enrolled);

        enrolleeName.setText(enrollee.getEnrolleeName());
        enrolleeCourseEnrolled.setText(enrollee.getEnrolleeCourseEnrolled());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Position: " + i, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
