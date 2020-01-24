package com.project.ilearncentral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView errorTextMessage;
    private EditText username;
    private TextInputEditText password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView signUpLink, forgotPasswordLink;
    private Button logInButton;
    private ImageView facebookIcon, twitterIcon, googleIcon;

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

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPages.class));
                }
                else {
//                    Toast.makeText(getApplicationContext(), "Logging in unsuccessful. Try again.", Toast.LENGTH_SHORT).show();
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
//                startActivity(new Intent(Login.this, ActivityPages.class));
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
        final String uname = username.getText().toString();
        final String pwd = password.getText().toString();
        if (!hasInternetAccess()){
            Toast.makeText(Login.this, "No internet access", Toast.LENGTH_SHORT).show();
        }
        else if (uname.isEmpty()){
            username.setError("Enter username");
            username.requestFocus();
        }
        else if (pwd.isEmpty()){
            password.setError("Enter password");
            password.requestFocus();
        }
        else if (!(uname.isEmpty() && pwd.isEmpty())){
            firebaseAuth.signInWithEmailAndPassword(uname, pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Login.this, "Email or Password is incorrect. Try again.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), ActivityPages.class));
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Error Logging In", Toast.LENGTH_SHORT).show();
        }
    }

//    private static Socket socket = new Socket();
    public static boolean hasInternetAccess(){
        /*ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else {
            return false;
        }*/
        /*ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();*/

        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }

        /*try {
            socket.connect(new InetSocketAddress("www.google.com", 80), 2000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            System.out.println(e);
            return false;
        }*/
    }

    private void setErrorMessage(String errorMessage){
        errorTextMessage.setText(errorMessage);
        errorTextMessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        FirebaseUser currenUser = firebaseAuth.getCurrentUser();
    }
}
