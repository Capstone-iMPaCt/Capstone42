package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView errorTextMessage;
    private EditText username;
    private TextInputEditText password;
    private TextView signUpLink, forgotPasswordLink;
    private Button logInButton;
    private ImageView facebookIcon, twitterIcon, googleIcon;

    @Override
    protected void onStart() {
        super.onStart();
        Connection.firebaseAuth.addAuthStateListener(Connection.authStateListener);
        Connection.currentUser = Connection.firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorTextMessage = (TextView)findViewById(R.id.textinput_error) ;
        username = (EditText)findViewById(R.id.usernameEditText);
        password = (TextInputEditText)findViewById(R.id.passwordTextInput);
        signUpLink = (TextView)findViewById(R.id.signUpLink);
        forgotPasswordLink = (TextView)findViewById(R.id.forgotPasswordLink);
        logInButton = (Button)findViewById(R.id.logInButton);
        facebookIcon = (ImageView)findViewById(R.id.facebookIconLink);
        twitterIcon = (ImageView)findViewById(R.id.twitterIconLink);
        googleIcon = (ImageView)findViewById(R.id.googleIconLink);

        checkCurrentUser();

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
//                startActivity(new Intent(Login.this, UserPages.class));
                startActivity(new Intent(Login.this, AccountTypeSelection.class));
                break;
            case R.id.forgotPasswordLink:
                break;
            case R.id.logInButton:
                setLogInButton();
//                startActivity(new Intent(Login.this, UserPages.class));
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

    private void checkCurrentUser(){
//        if (Connection.currentUser != null){
//            startActivity(new Intent(this, UserPages.class));
//        }
        Connection.firebaseAuth = FirebaseAuth.getInstance();
        Connection.authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                else {
//                    Toast.makeText(getApplicationContext(), "Logging in unsuccessful. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setLogInButton(){
        if (!Connection.hasInternetAccess()){
            Toast.makeText(Login.this, "No internet access", Toast.LENGTH_SHORT).show();
        }
        else if (username.getText().toString().isEmpty()){
            username.setError("Enter username");
            username.requestFocus();
        }
        else if (password.getText().toString().isEmpty()){
            password.setError("Enter password");
            password.requestFocus();
        }
        else if (!(username.getText().toString().isEmpty() && password.getText().toString().isEmpty())){
            logIn();
        }
        else {
            Toast.makeText(getApplicationContext(), "Error Logging In", Toast.LENGTH_SHORT).show();
        }
    }

    private void logIn(){
        Connection.setUsername(username.getText().toString() + "@mailinator.com");
        Connection.setPassword(password.getText().toString());
        // [START sign_in_with_email]
        Connection.firebaseAuth.signInWithEmailAndPassword(Connection.getUsername(), Connection.getPassword())
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                            Connection.currentUser = Connection.firebaseAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), UserPages.class));
                        } else {
                            // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            Connection.logOut();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
}
