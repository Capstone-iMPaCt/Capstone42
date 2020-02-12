package com.project.ilearncentral.Activity;

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
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView errorTextMessage;
    private EditText username;
    private TextInputEditText password;
    private TextView signUpLink, forgotPasswordLink;
    private Button logInButton;
    private ImageView facebookIcon, twitterIcon, googleIcon;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkCurrentUser();

        errorTextMessage = (TextView) findViewById(R.id.textinput_error);
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (TextInputEditText) findViewById(R.id.passwordTextInput);
        signUpLink = (TextView) findViewById(R.id.signUpLink);
        forgotPasswordLink = (TextView) findViewById(R.id.forgotPasswordLink);
        logInButton = (Button) findViewById(R.id.logInButton);
        facebookIcon = (ImageView) findViewById(R.id.facebookIconLink);
        twitterIcon = (ImageView) findViewById(R.id.twitterIconLink);
        googleIcon = (ImageView) findViewById(R.id.googleIconLink);

        signUpLink.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        facebookIcon.setOnClickListener(this);
        twitterIcon.setOnClickListener(this);
        googleIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpLink:
                startActivityForResult(new Intent(Login.this, AccountTypeSelection.class),1);
                break;
            case R.id.forgotPasswordLink:
                break;
            case R.id.logInButton:
                setLogInButton();
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

    private void checkCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, UserPages.class));
            Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setLogInButton() {
        if (!Connection.hasInternetAccess()) {
            Toast.makeText(Login.this, "No internet access", Toast.LENGTH_SHORT).show();
        } else if (username.getText().toString().isEmpty()) {
            username.setError("Enter username");
            username.requestFocus();
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Enter password");
            password.requestFocus();
        } else if (!(username.getText().toString().isEmpty() && password.getText().toString().isEmpty())) {
            logIn();
        } else {
            Toast.makeText(getApplicationContext(), "Error Logging In", Toast.LENGTH_SHORT).show();
        }
    }

    private void logIn(){
        String usernameValue = username.getText().toString() + getResources().getString(R.string.emailSuffix);
        String passwordValue = password.getText().toString();
        // [START sign_in_with_email]
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usernameValue, passwordValue)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(getApplicationContext(), UserPages.class));
                            Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Username or Password is incorrect.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Error logging in. Try again.", Toast.LENGTH_SHORT).show();
                            Connection.logOut(Login.this);
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }
}
