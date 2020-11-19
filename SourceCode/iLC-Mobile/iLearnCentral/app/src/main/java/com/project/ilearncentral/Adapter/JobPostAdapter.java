package com.project.ilearncentral.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.Messages;
import com.project.ilearncentral.Activity.NveJobPost;
import com.project.ilearncentral.CustomBehavior.ObservableObject;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnObjectChangeListener;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Model.JobApplication;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;
import java.util.Map;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {

    private static final String TAG = "Job Post Adapter Log: ";
    private Context context;
    private ObservableString edit;
    private List<JobVacancy> jobs;
    private StorageReference storageRef;

    public JobPostAdapter(Context context, ObservableString edit, List<JobVacancy> jobs) {
        this.context = context;
        this.edit = edit;
        this.jobs = jobs;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public JobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_jobpost_row, parent, false);
        return new JobPostAdapter.JobPostViewHolder(view);
    }

    private int lastPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull final JobPostViewHolder holder, final int position) {

        final JobVacancy job = jobs.get(position);

        holder.editTextView.setVisibility(View.GONE);
        holder.closePostButton.setVisibility(View.GONE);
        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        getImage(holder.logoImageView, "images/", job.getCenterId());
        ObservableObject businessDetails = new ObservableObject();
        businessDetails.setOnObjectChangeListener(new OnObjectChangeListener() {
            @Override
            public void onChanged(Object value) {
                Map<String, String> businessDetails = (Map<String, String>) value;
                holder.businessNameTextView.setText(businessDetails.get("BusinessName"));
                job.setBusinessData(businessDetails);
            }
        });
        if (job.getBusinessData().isEmpty()) {
            Utility.getBusinessName(job.getCenterId(), businessDetails);
        } else {
            holder.businessNameTextView.setText(job.getBusinessData().get("BusinessName"));
        }
        holder.positionTextView.setText(job.getPosition());
        holder.dateTextView.setText(Utility.getStringFromDate(job.getDate()));
        holder.timeTextView.setText(Utility.getStringFromTime(job.getDate()));
        holder.descriptionTextView.setText(job.getJobDescription());

        holder.logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //view learning center
            }
        });
        if (Account.isType("LearningCenter")) {
            holder.applyButton.setVisibility(View.INVISIBLE);
            if (Account.getStringData("centerId")
                    .equals(job.getCenterId())){
                holder.editTextView.setVisibility(View.VISIBLE);
                holder.closePostButton.setVisibility(View.VISIBLE);
                holder.chatButton.setVisibility(View.GONE);
            }
        } else if (Account.isType("Educator")) {
            if (JobApplication.isApplicant(job.getJobId(), Account.getUsername())) {
                holder.applyButton.setVisibility(View.GONE);
            }
        }
        if (job.getStatus().equalsIgnoreCase("close")) {
            holder.closePostButton.setBackgroundColor(Color.GRAY);
            holder.closePostButton.setClickable(false);
            holder.closePostButton.setText("CLOSED");
        }
        holder.editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobPosts.setCurPost(JobPosts.getJobPostById(job.getJobId()));
                if (JobPosts.hasCurrent()) {
                    edit.set("y" + job.getJobId());
                }
            }
        });
//        holder.containerLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JobPosts.setCurPost(JobPosts.getJobPostById(job.getJobId()));
//                if (JobPosts.hasCurrent()) {
//                    edit.set("n" + job.getJobId());
//                }
//            }
//        });
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (JobPosts.hasCurrent()) {
                    Intent intent = new Intent(context, Messages.class);
                    intent.putExtra("USER_NAME", job.getUsername());
                    intent.putExtra("FULL_NAME", job.getBusinessData().get("BusinessName"));
                    context.startActivity(intent);
                }
            }
        });
        holder.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NveJobPost.class);
                intent.putExtra("jobId", job.getJobId());
                intent.putExtra("View", true);
                context.startActivity(intent);
                JobPost.applying = true;
            }
        });
        holder.closePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Closing Job Post");
                builder.setMessage("Are you sure you want to close this Job Post?\nNote: All ongoing applications will be closed.");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        JobPosts.closeVacancy(job.getJobId());
                        holder.closePostButton.setBackgroundColor(Color.GRAY);
                        holder.closePostButton.setClickable(false);
                        holder.closePostButton.setText("CLOSED");

                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    private void getImage(final ImageView imageView, String folderName, String filename) {
        storageRef.child(folderName).child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).error(R.drawable.white).fitCenter().into(imageView);
//                Picasso.get().load(uri.toString()).centerCrop().fit().into(imageView);
            }
        });
    }

    public class JobPostViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout containerLayout;
        private RelativeLayout headerLayout;
        private ImageView logoImageView;
        private TextView businessNameTextView, positionTextView, dateTextView, timeTextView, descriptionTextView, editTextView;
        private Button closePostButton, applyButton, chatButton;

        JobPostViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.job_post_container);
            logoImageView = itemView.findViewById(R.id.job_post_center_logo);
            businessNameTextView = itemView.findViewById(R.id.job_post_business_name);
            positionTextView = itemView.findViewById(R.id.job_post_position_title);
            headerLayout = itemView.findViewById(R.id.job_post_header);
            dateTextView = itemView.findViewById(R.id.job_post_date);
            timeTextView = itemView.findViewById(R.id.job_post_time);
            descriptionTextView = itemView.findViewById(R.id.job_post_description);
            editTextView = itemView.findViewById(R.id.job_post_edit_icon);
            closePostButton = itemView.findViewById(R.id.job_post_close_button);
            applyButton = itemView.findViewById(R.id.job_post_apply_button);
            chatButton = itemView.findViewById(R.id.job_post_chat_button);
        }
    }
}
