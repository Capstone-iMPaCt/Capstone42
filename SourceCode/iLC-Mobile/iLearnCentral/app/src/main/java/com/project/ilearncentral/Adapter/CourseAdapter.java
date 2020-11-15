package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haozhang.lib.SlantedTextView;
import com.project.ilearncentral.Activity.NveCourse;
import com.project.ilearncentral.Activity.SignUp.SignUpOthers;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courses;

    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
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
        System.out.println("!!!!!!Course size " + position);

        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        lastPosition = position;
//        holder.courseFeeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_270));

        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getCourseDescription());
        holder.scheduleFrom.setText(Utility.getStringFromDate(course.getScheduleFrom()));
        holder.scheduleTo.setText(Utility.getStringFromDate(course.getScheduleTo()));
        if (course.getCourseInstructor().isEmpty()) {
            holder.instructorLabel.setVisibility(View.GONE);
        } else {
            holder.instructor.setText(course.getCourseInstructor());
        }
        checkDay(course,"Mon", holder.monday);
        checkDay(course,"Tue", holder.tuesday);
        checkDay(course,"Wed", holder.wednesday);
        checkDay(course,"Thu", holder.thursday);
        checkDay(course,"Fri", holder.friday);
        checkDay(course,"Sat", holder.saturday);
        checkDay(course,"Sun", holder.sunday);
        holder.courseFee.setText(Utility.showPriceInPHP(course.getCourseFee()));
        holder.userImage.setVisibility(View.INVISIBLE);
        if (Account.getCenterId().equalsIgnoreCase(course.getCenterId())) {
            holder.editLink.setVisibility(View.VISIBLE);
            holder.editLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NveCourse.class);
                    intent.putExtra("courseId", course.getCourseId());
                    intent.putExtra("type", "edit");
                    context.startActivity(intent);
                }
            });
        } else {
            holder.editLink.setVisibility(View.GONE);
        }
        if (Account.getType() == Account.Type.Student) {
            holder.enrollButton.setVisibility(View.VISIBLE);
            holder.enrollButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent for opening enrol payment
                    /*Intent intent = new Intent(context, NveCourse.class);
                    intent.putExtra("courseId", course.getCourseId());
                    intent.putExtra("type", "edit");
                    context.startActivity(intent);*/
                }
            });
        } else {
            holder.enrollButton.setVisibility(View.GONE);
        }
    }

    private void checkDay(Course c, String text, View v) {
        if (c.getScheduleDays().contains(text))
            v.setBackgroundResource(R.drawable.bg_selected_day_rounded);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout containerLayout;
        private LinearLayout courseFeeLayout;
        private CircleImageView userImage;
        private SlantedTextView courseFeeLabel, courseFee;
        private TextView editLink, courseStatus, courseType, courseName, courseDescription, scheduleFrom, scheduleTo, instructor, instructorLabel;
        private TextView monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        private Button enrollButton;

        CourseViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.course_container);
            courseFeeLayout = itemView.findViewById(R.id.course_fee_layout);
            userImage = itemView.findViewById(R.id.course_user_display_image);
            editLink = itemView.findViewById(R.id.course_edit_link);
            courseName = itemView.findViewById(R.id.course_title);
            courseDescription = itemView.findViewById(R.id.course_description);
            scheduleFrom = itemView.findViewById(R.id.course_schedule_from);
            scheduleTo = itemView.findViewById(R.id.course_schedule_to);
            instructor = itemView.findViewById(R.id.course_instructor);
            instructorLabel = itemView.findViewById(R.id.course_instructor_label);
//            courseFeeLabel = itemView.findViewById(R.id.course_fee_label);
            courseFee = itemView.findViewById(R.id.course_fee);
            monday = itemView.findViewById(R.id.course_sched_day_mon);
            tuesday = itemView.findViewById(R.id.course_sched_day_tue);
            wednesday = itemView.findViewById(R.id.course_sched_day_wed);
            thursday = itemView.findViewById(R.id.course_sched_day_thu);
            friday = itemView.findViewById(R.id.course_sched_day_fri);
            saturday = itemView.findViewById(R.id.course_sched_day_sat);
            sunday = itemView.findViewById(R.id.course_sched_day_sun);
            enrollButton = itemView.findViewById(R.id.course_enrol_button);
        }
    }
}
