package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.Activity.SignUp.SignUpUsers;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

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
        if (v.getId() == learningCenterAccount.getId())
            Account.setType(Account.Type.LearningCenter);
        else if (v.getId() == educatorAccount.getId())
            Account.setType(Account.Type.Educator);
        else if (v.getId() == studentAccount.getId())
            Account.setType(Account.Type.Student);
        startActivity(new Intent(this, SignUpUsers.class));
    }
}
