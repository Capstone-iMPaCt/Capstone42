package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private Context context;
    private List<Enrolment> paymentsList;
    private int lastPosition = -1;

    public PaymentAdapter(Context context, List<Enrolment> paymentsList) {
        this.context = context;
        this.paymentsList = paymentsList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_enrolment_payment_row, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Enrolment payment = paymentsList.get(position);

        holder.rootLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        lastPosition = position;

        holder.enroleeName.setText(payment.getStudentName());
        holder.courseTitle.setText(payment.getCourseEnrolled());
        holder.amountPayed.setText(Utility.showPriceInPHP(payment.getEnrolmentFee()));
        holder.datePayed.setText(Utility.getStringFromDate(new Timestamp(payment.getProcessedDate())));
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }

    protected class PaymentViewHolder extends RecyclerView.ViewHolder {

        private CardView rootLayout;
        private TextView enroleeName, courseTitle, amountPayed, datePayed;

        protected PaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            rootLayout = itemView.findViewById(R.id.repr_root_layout);
            enroleeName = itemView.findViewById(R.id.repr_enrolee_name);
            courseTitle = itemView.findViewById(R.id.repr_course_title);
            amountPayed = itemView.findViewById(R.id.repr_amount);
            datePayed = itemView.findViewById(R.id.repr_date);
        }
    }
}
