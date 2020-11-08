package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haozhang.lib.SlantedTextView;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private ObservableString edit;
    private List<Course> courses;

    public CourseAdapter(Context context, ObservableString edit, List<Course> courses) {
        this.context = context;
        this.edit = edit;
        this.courses = courses;
    }

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

        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        lastPosition = position;
//        holder.courseFeeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_270));

        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getCourseDescription());
        holder.classScheduleFrom.setText(course.getClassScheduleFrom());
        holder.classScheduleTo.setText(course.getClassScheduleTo());
        holder.instructor.setText(course.getCourseInstructor());
        holder.courseFee.setText(Utility.showPriceInPHP(course.getCourseFee()));
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
        private TextView courseStatus, courseType, courseName, courseDescription, classScheduleFrom, classScheduleTo, instructor;

        CourseViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.course_container);
            courseFeeLayout = itemView.findViewById(R.id.course_fee_layout);
            userImage = itemView.findViewById(R.id.course_user_display_image);
            courseName = itemView.findViewById(R.id.course_title);
            courseDescription = itemView.findViewById(R.id.course_description);
            classScheduleFrom = itemView.findViewById(R.id.course_schedule_from);
            classScheduleTo = itemView.findViewById(R.id.course_schedule_to);
            instructor = itemView.findViewById(R.id.course_instructor);
//            courseFeeLabel = itemView.findViewById(R.id.course_fee_label);
            courseFee = itemView.findViewById(R.id.course_fee);
        }
    }
}
