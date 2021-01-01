package com.project.ilearncentral.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.Activity.EnroleePaymentDetails;
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
        final Enrolment payment = paymentsList.get(position);

        holder.rootLayout.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));
        lastPosition = position;

        holder.enroleeName.setText(payment.getStudentName());
        holder.courseTitle.setText(payment.getCourseEnrolled());
        holder.amountPayed.setText(Utility.showPriceInPHP(payment.getEnrolmentFee()));
        holder.datePayed.setText(Utility.getStringFromDate(new Timestamp(payment.getProcessedDate())));

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View mView = ((Activity)context).getLayoutInflater().inflate(R.layout.fragment_dialog_photoview, null);
                final PhotoView photoView = mView.findViewById(R.id.photoview);
                photoView.setMinimumHeight(Utility.getScreenHeight());
                FirebaseStorage.getInstance().getReference()
                        .child("enrolment_payment_proof")
                        .child(payment.getCenterID())
                        .child(payment.getCourseID())
                        .child(payment.getStudentID())
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context)
                                        .load(uri.toString())
                                        .fitCenter()
                                        .into(photoView);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        photoView.setImageDrawable(context.getDrawable(R.drawable.no_image_available));
                    }
                });
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentsList.size();
    }

    protected class PaymentViewHolder extends RecyclerView.ViewHolder {

        private CardView rootLayout;
        private TextView enroleeName, courseTitle, amountPayed, datePayed;
        private ImageButton imageButton;

        protected PaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            rootLayout = itemView.findViewById(R.id.repr_root_layout);
            enroleeName = itemView.findViewById(R.id.repr_enrolee_name);
            courseTitle = itemView.findViewById(R.id.repr_course_title);
            amountPayed = itemView.findViewById(R.id.repr_amount);
            datePayed = itemView.findViewById(R.id.repr_date);
            imageButton = itemView.findViewById(R.id.repr_image);
        }
    }
}
