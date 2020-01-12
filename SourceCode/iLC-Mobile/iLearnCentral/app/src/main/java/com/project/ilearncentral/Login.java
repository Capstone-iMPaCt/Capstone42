package com.project.ilearncentral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView signUpLink, forgotPasswordLink;
    Button logInButton;
    ImageView facebookIcon, twitterIcon, googleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpLink = (TextView)findViewById(R.id.signUpLink);
        forgotPasswordLink = (TextView)findViewById(R.id.forgotPasswordLink);
        logInButton = (Button)findViewById(R.id.logInButton);
        facebookIcon = (ImageView)findViewById(R.id.facebookIconLink);
        twitterIcon = (ImageView)findViewById(R.id.twitterIconLink);
        googleIcon = (ImageView)findViewById(R.id.googleIconLink);

        signUpLink.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        facebookIcon.setOnClickListener(this);
        twitterIcon.setOnClickListener(this);
        googleIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.signUpLink:
                startActivity(new Intent(Login.this, AccountTypeSelection.class));
                break;
            case R.id.forgotPasswordLink:
                break;
            case R.id.logInButton:
                startActivity(new Intent(Login.this, ActivityPages.class));
                break;
            case R.id.facebookIconLink:
                break;
            case R.id.twitterIconLink:
                break;
            case R.id.googleIconLink:
                break;
            default:
                break;
        }
    }
}
