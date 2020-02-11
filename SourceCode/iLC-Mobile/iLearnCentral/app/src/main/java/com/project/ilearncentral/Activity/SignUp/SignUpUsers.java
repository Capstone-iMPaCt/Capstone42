package com.project.ilearncentral.Activity.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

public class SignUpUsers extends AppCompatActivity {

    private Button btnContinue;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_users);


        String[] items = new String[] {"-- Select a question --","Question 1: ","Question 2: ","Question 3: "};
        Spinner spinner = (Spinner) findViewById(R.id.spinnerQuestion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

        btnContinue = findViewById(R.id.buttonContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Account.getType()){
                    case LearningCenter:
                        intent = new Intent(getApplicationContext(), SignUpLearningCenter.class);
                        break;
                    case Educator:
                        intent = new Intent(getApplicationContext(), SignUpEducator.class);
                        break;
                    case Student:
                        intent = new Intent(getApplicationContext(), SIgnUpStudent.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}