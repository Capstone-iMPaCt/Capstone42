package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.BankAccountDetail;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.databinding.ActivityEnrolmentPaymentBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnrolmentPayment extends AppCompatActivity {

    private ActivityEnrolmentPaymentBinding binding;

    private double fee;
    private String centerID, businessName, courseID, title;

    private Intent intent;
    private List<BankAccountDetail> bankAccountList;
    private OnBooleanChangeListener bankDataListener;
    private ObservableString imageDone, confirmDone;
    private ImageHandler imageHandler;
    private boolean withImage;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        binding.enrolmentPaymentCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.enrolmentPaymentChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageHandler.selectImage();
            }
        });
        binding.enrolmentPaymentConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (withImage) {
                    Utility.buttonWait(binding.enrolmentPaymentConfirmButton, true, "Saving...");
                }
            }
        });
        confirmDone.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String value) {
                Date date = new Date();
                Timestamp fileName = new Timestamp(date);
                if (withImage) {
                    imageHandler.uploadImage("proofOfPayment/" + Account.getUsername()
                            , fileName.toString()
                            , binding.enrolmentPaymentImage
                            , imageDone);
                } else {
                    finishPayment();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        withImage = imageHandler
                .onActivityResult(requestCode, resultCode, data, null, binding.enrolmentPaymentImage, "");
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        imageHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initialize() {
        binding = ActivityEnrolmentPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageHandler = new ImageHandler(this, EnrolmentPayment.this);
        imageDone = new ObservableString();
        confirmDone = new ObservableString();

        intent = getIntent();
        centerID = intent.getStringExtra("CenterID");
        businessName = intent.getStringExtra("BusinessName");
        title = intent.getStringExtra("Title");
        fee = Double.parseDouble(String.valueOf(intent.getDoubleExtra("Fee", 0)));
        courseID = intent.getStringExtra("CourseID");

        bankAccountList = new ArrayList<>();
        bankDataListener = new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    setBankDetails();
                }
            }
        };
        BankAccountDetail.getDataByCenterID(centerID, bankAccountList, bankDataListener);

        binding.enrolmentPaymentCenterName.setText(businessName);
        binding.enrolmentPaymentCourseTitle.setText(title);
        binding.enrolmentPaymentCourseFee.setText(Utility.showPriceInPHP(fee));
    }

    private void finishPayment() {
        Utility.buttonWait(binding.enrolmentPaymentConfirmButton, false, "CONFIRM");
        Intent i = new Intent();
//        i.putExtra("proofOfPayment", proofOfPayment);
        setResult(RESULT_OK, i);
        finish();
    }

    private void setBankDetails() {
        for (BankAccountDetail details : bankAccountList) {
            binding.enrolmentPaymentBankDetails.append("* " + details.getBankName()
                    + ": " + details.getBankAccountNumber() + "\n");
        }
    }
}