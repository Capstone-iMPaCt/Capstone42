package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.databinding.ActivityEnrolmentPaymentBinding;

public class EnrolmentPayment extends AppCompatActivity {

    private ActivityEnrolmentPaymentBinding layoutBinding;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();


    }

    private void initialize(){
        layoutBinding = ActivityEnrolmentPaymentBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

//        intent = ;
        //setContentView(R.layout.activity_enrolment_payment);
    }
}