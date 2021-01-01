package com.project.ilearncentral.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.project.ilearncentral.databinding.ActivityEnroleePaymentDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class EnroleePaymentDetails extends AppCompatActivity {

    private ActivityEnroleePaymentDetailsBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private Intent intent;
    private Date dateProcessed;
    private String studentName, studentID, courseTitle, courseID, centerID;
    private double fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        binding.enroleePaymentTransactionSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EnroleePaymentDetails.this);
                View mView = getLayoutInflater().inflate(R.layout.fragment_dialog_photoview, null);
                PhotoView photoView = mView.findViewById(R.id.photoview);
                photoView.setMinimumHeight(Utility.getScreenHeight());
                setPaymentSlipImage(photoView);
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.enroleePaymentConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Enrolment")
                        .whereEqualTo("courseID", courseID)
                        .whereEqualTo("studentID", studentID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    final AlertDialog alertDialog = new AlertDialog
                                            .Builder(EnroleePaymentDetails.this)
                                            .create();
                                    alertDialog.setTitle("Confirm Payment");
                                    alertDialog.setCancelable(true);
                                    alertDialog.setMessage("Confirm student's enrolment and payment?");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    for (DocumentSnapshot document : task.getResult()) {
                                                        FirebaseFirestore.getInstance()
                                                                .collection("Enrolment")
                                                                .document(document.getId())
                                                                .update(
                                                                        "enrolledDate", new Date(),
                                                                        "enrolmentStatus", "enrolled"
                                                                );
                                                        toastMessage("Enrolee successfully enrolled!");
                                                    }
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            }
                        });
            }
        });
    }

    private void initialize() {
        binding = ActivityEnroleePaymentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        intent = getIntent();
        dateProcessed = new Date();
        dateProcessed.setTime(intent.getLongExtra("Date", -1));
        studentName = intent.getStringExtra("StudentName");
        studentID = intent.getStringExtra("StudentID");
        courseTitle = intent.getStringExtra("CourseTitle");
        courseID = intent.getStringExtra("CourseID");
        centerID = intent.getStringExtra("CenterID");
        fee = Double.parseDouble(String.valueOf(intent.getDoubleExtra("Fee", 0)));

        binding.enroleePaymentDate.append(Utility.getStringFromDate(new Timestamp(dateProcessed)));
        binding.enroleePaymentName.append(studentName);
        binding.enroleePaymentCourseTitle.append(courseTitle);
        binding.enroleePaymentFee.append(Utility.showPriceInPHP(fee));
        setPaymentSlipImage(binding.enroleePaymentTransactionSlip);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            binding.enroleePaymentTransactionSlip.setScaleX(mScaleFactor);
            binding.enroleePaymentTransactionSlip.setScaleY(mScaleFactor);
            return true;
        }
    }

    private void setPaymentSlipImage(final ImageView imageView) {
        FirebaseStorage.getInstance().getReference()
                .child("enrolment_payment_proof")
                .child(centerID)
                .child(courseID)
                .child(studentID)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext())
                                .load(uri.toString())
                                .fitCenter()
                                .into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageDrawable(getDrawable(R.drawable.no_image_available));
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(EnroleePaymentDetails.this, message, Toast.LENGTH_SHORT).show();
    }
}