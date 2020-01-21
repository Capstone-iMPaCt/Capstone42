package com.project.ilearncentral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    TextInputEditText password;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    TextView signUpLink, forgotPasswordLink;
    Button logInButton;
    ImageView facebookIcon, twitterIcon, googleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (TextInputEditText)findViewById(R.id.passwordTextInput);
        signUpLink = (TextView)findViewById(R.id.signUpLink);
        forgotPasswordLink = (TextView)findViewById(R.id.forgotPasswordLink);
        logInButton = (Button)findViewById(R.id.logInButton);
        facebookIcon = (ImageView)findViewById(R.id.facebookIconLink);
        twitterIcon = (ImageView)findViewById(R.id.twitterIconLink);
        googleIcon = (ImageView)findViewById(R.id.googleIconLink);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseAuth.getCurrentUser() != null) {
                    Toast.makeText(getApplicationContext(), "You are logged in.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPages.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Logging in unsuccessful. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        };

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
                setLogInButton();
//                startActivity(new Intent(Login.this, ActivityPages.class));
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

    private void setLogInButton(){
        String uname = username.getText().toString();
        String pwd = password.getText().toString();
        if (!(uname.isEmpty() && pwd.isEmpty())) {
            firebaseAuth.signInWithEmailAndPassword(uname, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), ActivityPages.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Logging in unsuccessful. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Log In error. Try again", Toast.LENGTH_SHORT).show();
        }
    }
}
