package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.type.Date;
import com.project.ilearncentral.Activity.EnroleePaymentDetails;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EnrolleeAdapter extends BaseAdapter {
    private Context context;
    private List<Enrolment> enroleeList;
    private String mode;
    private int lastPosition = -1;

    public EnrolleeAdapter(Context context, List<Enrolment> enroleeList, String mode) {
        this.context = context;
        this.enroleeList = enroleeList;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return enroleeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private void getImage(final ImageView imageView, String filename) {
        FirebaseStorage.getInstance().getReference()
                .child("images")
                .child(filename)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(context).load(uri.toString()).fitCenter().into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.recyclerview_enrollee_item, viewGroup, false);

        final Enrolment enrolee = enroleeList.get(i);

        final RelativeLayout layout = view.findViewById(R.id.enrollee_user_layout);
        final CircleImageView enrolleeImage = view.findViewById(R.id.enrollee_user_image);
        final TextView enroleeName = view.findViewById(R.id.enrollee_user_name);
        final TextView enroleeCourseEnrolled = view.findViewById(R.id.enrollee_course_enrolled);
        final TextView enroleeDate = view.findViewById(R.id.enrollee_date);

        layout.setAnimation(AnimationUtils.loadAnimation(context, (i > lastPosition) ? R.anim.move_up : R.anim.move_down));
        lastPosition = i;

        getImage(enrolleeImage, enrolee.getStudentID());
        enroleeName.setText(enrolee.getStudentName());
        enroleeCourseEnrolled.setText(enrolee.getCourseEnrolled());

        if (mode.equals("pendings")) {
            enroleeDate.setText(Utility.getStringFromDate(new Timestamp(enrolee.getProcessedDate())));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EnroleePaymentDetails.class);
                    intent.putExtra("Date", enrolee.getProcessedDate().getTime());
                    intent.putExtra("StudentName", enrolee.getStudentName());
                    intent.putExtra("StudentID", enrolee.getStudentID());
                    intent.putExtra("CourseTitle", enrolee.getCourseEnrolled());
                    intent.putExtra("CourseID", enrolee.getCourseID());
                    intent.putExtra("CenterID", enrolee.getCenterID());
                    intent.putExtra("Fee", enrolee.getEnrolmentFee());
                    context.startActivity(intent);
                }
            });
        } else if (mode.equals("enrolees")) {
            enroleeDate.setText(Utility.getStringFromDate(new Timestamp(enrolee.getEnrolledDate())));
        }

        return view;
    }
}
