package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.project.ilearncentral.Model.Applicant;
import com.project.ilearncentral.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicantAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Applicant> applicants;

    private boolean starred = false;

    public ApplicantAdapter(Context context, ArrayList<Applicant> applicants) {
        this.context = context;
        this.applicants = applicants;
    }

    @Override
    public int getCount() {
        return applicants.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.recyclerview_applicant_item, viewGroup, false);

        Applicant applicant = applicants.get(i);

        CircleImageView viewProfile = view.findViewById(R.id.applicant_view_user_profile);
        CircleImageView applicantImage = view.findViewById(R.id.applicant_user_image);
        final CircleImageView star = view.findViewById(R.id.applicant_star_button);
        TextView applicantName = view.findViewById(R.id.applicant_user_name);
        TextView applicantCourseEnrolled = view.findViewById(R.id.applicant_position_applied);

        applicantName.setText(applicant.getApplicantName());
        applicantCourseEnrolled.setText(applicant.getApplicantPositionApplied());

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Position: " + i, Toast.LENGTH_SHORT).show();
//            }
//        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Position: " + i, Toast.LENGTH_SHORT).show();
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!starred) {
                    star.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                    starred = true;
                } else {
                    star.setBackgroundTintList(null);
                    starred = false;
                }
            }
        });

        return view;
    }
}
