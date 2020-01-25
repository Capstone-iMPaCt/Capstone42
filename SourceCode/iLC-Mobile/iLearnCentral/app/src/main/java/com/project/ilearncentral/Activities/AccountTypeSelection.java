package com.project.ilearncentral.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.R;

public class AccountTypeSelection extends AppCompatActivity {

    LinearLayout learningCenterAccountPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type_selection);

        learningCenterAccountPanel = findViewById(R.id.LearningCenterAccountPanel);

        learningCenterAccountPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountTypeSelection.this, SignUpUsers.class);
                startActivity(intent);
            }
        });
    }
}
