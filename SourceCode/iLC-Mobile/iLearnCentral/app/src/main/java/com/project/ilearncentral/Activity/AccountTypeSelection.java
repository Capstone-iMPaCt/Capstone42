package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.project.ilearncentral.Activity.SignUp.SignUpLearningCenter;
import com.project.ilearncentral.Activity.SignUp.SignUpUsers;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccountTypeSelection extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout learningCenterAccount, educatorAccount, studentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type_selection);

        learningCenterAccount = (LinearLayout) findViewById(R.id.LearningCenterAccountPanel);
        educatorAccount = (LinearLayout) findViewById(R.id.EducatorAccountPanel);
        studentAccount = (LinearLayout) findViewById(R.id.StudentAccountPanel);

        learningCenterAccount.setOnClickListener(this);
        educatorAccount.setOnClickListener(this);
        studentAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == learningCenterAccount.getId()) {
            Account.setType(Account.Type.LearningCenter);
            startActivityForResult(new Intent(this, SignUpLearningCenter.class), 2);
        }
        else {
            if (v.getId() == educatorAccount.getId())
                Account.setType(Account.Type.Educator);
            else if (v.getId() == studentAccount.getId())
                Account.setType(Account.Type.Student);
            startActivityForResult(new Intent(this, SignUpUsers.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        Log.d("ACCOUNT_TYPE", "onBackPressed Called");
        super.onBackPressed();
    }
}
