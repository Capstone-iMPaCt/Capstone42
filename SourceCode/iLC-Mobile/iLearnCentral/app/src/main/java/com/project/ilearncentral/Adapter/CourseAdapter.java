package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.CustomBehavior.ObservableObject;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnObjectChangeListener;
import com.project.ilearncentral.Model.CourseOffered;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;
import java.util.Map;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private ObservableString edit;
    private List<CourseOffered> courses;

    public CourseAdapter(Context context, ObservableString edit, List<CourseOffered> courses) {
        this.context = context;
        this.edit = edit;
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

        final CourseOffered course = courses.get(position);

        holder.containerLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        holder.courseName.setText(course.getCourseName());
        holder.courseDescription.setText(course.getCourseDescription());
        holder.classScheduleFrom.setText(course.getClassScheduleFrom());
        holder.classScheduleTo.setText(course.getClassScheduleTo());
        holder.instructor.setText(course.getCourseName());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout containerLayout;
        private TextView courseName, courseDescription, classScheduleFrom, classScheduleTo, instructor;

        CourseViewHolder(View itemView) {
            super(itemView);

            containerLayout = itemView.findViewById(R.id.course_container);
            courseName = itemView.findViewById(R.id.course_name);
            courseDescription = itemView.findViewById(R.id.course_description);
            classScheduleFrom = itemView.findViewById(R.id.course_schedule_from);
            classScheduleTo = itemView.findViewById(R.id.course_schedule_to);
            instructor = itemView.findViewById(R.id.course_instructor);
        }
    }
}
