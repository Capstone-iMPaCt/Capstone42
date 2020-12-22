package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.databinding.ActivityEnrolmentPaymentBinding;

public class EnrolmentPayment extends AppCompatActivity {

    private ActivityEnrolmentPaymentBinding layoutBinding;

    private double fee;
    private String courseID;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        layoutBinding.enrolmentPaymentCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialize(){
        layoutBinding = ActivityEnrolmentPaymentBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        intent = getIntent();
        fee = Double.parseDouble(String.valueOf(intent.getDoubleExtra("Fee", 0)));
        courseID = intent.getStringExtra("CourseID");
        layoutBinding.enrolmentPaymentCourseTitle.setText(intent.getStringExtra("Title"));
        layoutBinding.enrolmentPaymentCourseFee.setText(Utility.showPriceInPHP(fee));
    }
}