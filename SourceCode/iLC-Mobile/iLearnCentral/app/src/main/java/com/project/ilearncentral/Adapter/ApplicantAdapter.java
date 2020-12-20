package com.project.ilearncentral.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.Activity.ViewResume;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.Model.JobApplication;
import com.project.ilearncentral.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicantAdapter extends BaseAdapter {

    private Context context;
    private List<JobApplication> applicants;
    private ObservableBoolean updateList;

    private boolean starred = false;

    public ApplicantAdapter(Context context, List<JobApplication> applicants, ObservableBoolean updateList) {
        this.context = context;
        this.applicants = applicants;
        this.updateList = updateList;
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

        final JobApplication applicant = applicants.get(i);

        CircleImageView viewProfile = view.findViewById(R.id.applicant_view_user_profile);
        CircleImageView applicantImage = view.findViewById(R.id.applicant_user_image);
        TextView applicantName = view.findViewById(R.id.applicant_user_name);
        TextView applicantCourseEnrolled = view.findViewById(R.id.applicant_position_applied);
        Button hireButton = view.findViewById(R.id.applicant_hire_button);
        Button rejectButton = view.findViewById(R.id.applicant_reject_button);

        String name = "";
        if (applicant.getEducator()!=null) {
            applicantName.setText(applicant.getEducator().getFullname());
            name = applicant.getEducator().getFullname();
            Glide.with(context).load(applicant.getEducator().getImage()).error(R.drawable.user).fitCenter().into(applicantImage);
        }
        applicantCourseEnrolled.setText(applicant.getJobVacancy().getPosition());

        switch (applicant.getApplicationStatus()) {
            case JobApplication.OPEN:
                break;
            case JobApplication.HIRED:
                hireButton.setText(JobApplication.HIRED.toUpperCase());
                hireButton.setEnabled(false);
                rejectButton.setEnabled(false);
                rejectButton.setVisibility(View.INVISIBLE);
                hireButton.setVisibility(View.VISIBLE);
                break;
            case JobApplication.REJECTED:
                rejectButton.setText(JobApplication.REJECTED.toUpperCase());
                rejectButton.setEnabled(false);
                hireButton.setEnabled(false);
                hireButton.setVisibility(View.INVISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Position: " + i, Toast.LENGTH_SHORT).show();
//            }
//        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewResume.class);
                intent.putExtra("educator", applicant.getEduUsername());
                intent.putExtra("jobApplication", applicant.getJobApplicationId());
                context.startActivity(intent);
            }
        });

        final String finalName = name;
        hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if (!applicant.getApplicationStatus().equalsIgnoreCase(JobApplication.HIRED)) {
                                applicant.setApplicationStatus(JobApplication.HIRED);
                                FirebaseFirestore.getInstance().collection("JobApplication").document(applicant.getJobApplicationId())
                                        .update("ApplicationStatus", JobApplication.HIRED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateList.set(true);
                                    }
                                });
                            }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure in hiring " + finalName + "?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if (!applicant.getApplicationStatus().equalsIgnoreCase(JobApplication.REJECTED)) {
                                    applicant.setApplicationStatus(JobApplication.REJECTED);
                                    FirebaseFirestore.getInstance().collection("JobApplication").document(applicant.getJobApplicationId())
                                            .update("ApplicationStatus", JobApplication.REJECTED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            updateList.set(true);
                                        }
                                    });
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure in rejecting " + finalName + "?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });



        return view;
    }
}
