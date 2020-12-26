package com.project.ilearncentral.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.haozhang.lib.SlantedTextView;
import com.project.ilearncentral.Activity.EnrolmentPayment;
import com.project.ilearncentral.Activity.NveCourse;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Subscription;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private static final CharSequence TAG = "CourseAdapter";
    private Context context;
    private List<Course> courses;
    private StorageReference storageRef;

    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_course_row, parent, false);
        return new CourseAdapter.CourseViewHolder(view);
    }

    private int lastPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull final CourseViewHolder holder, final int position) {

        final Course course = courses.get(position);

        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        lastPosition = position;

        holder.userName.setText(course.getCenterName());
        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getCourseDescription());
        holder.scheduleFrom.setText(Utility.getStringFromDate(course.getScheduleFrom()));
        holder.scheduleTo.setText(Utility.getStringFromDate(course.getScheduleTo()));
        holder.courseFee.setText(Utility.showPriceInPHP(course.getCourseFee()));

        if (Account.isType("Student")) {
            getImage(holder.userImage, "images/", course.getCenterId());
            holder.userImage.setVisibility(View.VISIBLE);
            holder.userName.setVisibility(View.VISIBLE);
            holder.editLink.setVisibility(View.GONE);
            holder.topDivider.setVisibility(View.VISIBLE);
            holder.bottomDivider.setVisibility(View.VISIBLE);
            holder.enrollButton.setVisibility(View.VISIBLE);
            holder.closeCourseButton.setVisibility(View.GONE);
            holder.enrollButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EnrolmentPayment.class);
                    intent.putExtra("CenterID", course.getCenterId());
                    intent.putExtra("BusinessName", course.getCenterName());
                    intent.putExtra("Title", course.getCourseName());
                    intent.putExtra("Fee", course.getCourseFee());
                    context.startActivity(intent);
                }
            });
        } else if (Account.isType("Educator")) {
            getImage(holder.userImage, "images/", course.getCenterId());
            holder.userImage.setVisibility(View.VISIBLE);
            holder.userName.setVisibility(View.VISIBLE);
            holder.editLink.setVisibility(View.GONE);
            holder.topDivider.setVisibility(View.VISIBLE);
            holder.bottomDivider.setVisibility(View.INVISIBLE);
            holder.enrollButton.setVisibility(View.GONE);
            holder.closeCourseButton.setVisibility(View.GONE);
        } else if (Account.isType("LearningCenter") && Account.getStringData("centerId").equals(course.getCenterId())) {
            holder.userImage.setVisibility(View.GONE);
            holder.userName.setVisibility(View.GONE);
            holder.editLink.setVisibility(View.VISIBLE);
            holder.topDivider.setVisibility(View.VISIBLE);
            holder.bottomDivider.setVisibility(View.VISIBLE);
            holder.enrollButton.setVisibility(View.GONE);
            holder.closeCourseButton.setVisibility(View.VISIBLE);
            holder.editLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Subscription.isEnrolmentSubscribed()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Please subscribe");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    Intent intent = new Intent(context, NveCourse.class);
                    intent.putExtra("courseId", course.getCourseId());
                    intent.putExtra("type", "edit");
                    context.startActivity(intent);
                }
            });
            holder.closeCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Subscription.isEnrolmentSubscribed()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Please subscribe");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("You do not have access to this feature.\nPlease subscribe.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Closing Course");
                    builder.setMessage("Are you sure you want to close this Course?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // TODO: codes here..

                            Toast.makeText(context, "Clicked YES", Toast.LENGTH_SHORT).show();
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
        } else if (Account.isType("LearningCenter") && !Account.getStringData("centerId").equals(course.getCenterId())) {
            getImage(holder.userImage, "images/", course.getCenterId());
            holder.userImage.setVisibility(View.VISIBLE);
            holder.userName.setVisibility(View.VISIBLE);
            holder.editLink.setVisibility(View.GONE);
            holder.topDivider.setVisibility(View.VISIBLE);
            holder.bottomDivider.setVisibility(View.INVISIBLE);
            holder.enrollButton.setVisibility(View.GONE);
            holder.closeCourseButton.setVisibility(View.GONE);
        }
    }

    private void getImage(final ImageView imageView, String folderName, String filename) {
        storageRef.child(folderName).child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(uri.toString()).fitCenter().into(imageView);
//                Picasso.get().load(uri.toString()).centerCrop().fit().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout containerLayout;
        private LinearLayout courseFeeLayout;
        private CircleImageView userImage;
        private SlantedTextView courseFee;
        private TextView userName, editLink, courseStatus, courseType, courseName, courseDescription, scheduleFrom, scheduleTo;
        private Button enrollButton, closeCourseButton;
        private View topDivider, bottomDivider;

        CourseViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.course_container);
            courseFeeLayout = itemView.findViewById(R.id.course_fee_layout);
            userImage = itemView.findViewById(R.id.course_user_display_image);
            userName = itemView.findViewById(R.id.course_user_name);
            topDivider = itemView.findViewById(R.id.course_horizontal_line_top_divider);
            bottomDivider = itemView.findViewById(R.id.course_horizontal_line_bottom_divider);
            editLink = itemView.findViewById(R.id.course_edit_link);
            courseName = itemView.findViewById(R.id.course_title);
            courseDescription = itemView.findViewById(R.id.course_description);
            scheduleFrom = itemView.findViewById(R.id.course_schedule_date_start);
            scheduleTo = itemView.findViewById(R.id.course_schedule_date_end);
            courseFee = itemView.findViewById(R.id.course_fee);
            enrollButton = itemView.findViewById(R.id.course_enroll_button);
            closeCourseButton = itemView.findViewById(R.id.course_close_button);
        }
    }
}
