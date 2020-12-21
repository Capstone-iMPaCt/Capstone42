package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;

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


    }

    private void initialize(){
        layoutBinding = ActivityEnrolmentPaymentBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        intent = getIntent();
        fee = Double.parseDouble(intent.getStringExtra("Fee"));
        courseID = intent.getStringExtra("CourseID");
        layoutBinding.enrolmentPaymentCourseTitle.setText(intent.getStringExtra("Title"));
        layoutBinding.enrolmentPaymentCourseFee.setText(Utility.showPriceInPHP(fee));
        //setContentView(R.layout.activity_enrolment_payment);
    }
}