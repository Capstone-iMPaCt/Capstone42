package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private String TAG ="LOGIN";
    private TextView errorTextMessage;
    private EditText username;
    private TextInputEditText password;
    private TextView signUpLink, forgotPasswordLink;
    private Button logInButton;
    private String email;
    private boolean aUser;
    private String oldId;
    private Map<String, Object> oldData;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        email = oldId = "";
        aUser = false;
        oldData = new HashMap<>();
        checkCurrentUser();

        errorTextMessage = (TextView) findViewById(R.id.textinput_error);
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (TextInputEditText) findViewById(R.id.passwordTextInput);
        signUpLink = (TextView) findViewById(R.id.signUpLink);
        forgotPasswordLink = (TextView) findViewById(R.id.forgotPasswordLink);
        logInButton = (Button) findViewById(R.id.logInButton);

        signUpLink.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
        logInButton.setOnClickListener(this);

        //erase this later;
        password.setText("123456");
        //erase this later
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpLink:
                startActivityForResult(new Intent(Login.this, AccountTypeSelection.class),1);
                break;
            case R.id.forgotPasswordLink:
                startActivityForResult(new Intent(Login.this, ForgotPassword.class),2);
                break;
            case R.id.logInButton:
                setLogInButton();
                break;
            default:
                break;
        }
    }

    private void checkCurrentUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, Main.class));
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
        if (password.getText().toString().length()>=6) {
            Utility.buttonWait(logInButton, true);
            final String usernameValue = username.getText().toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
                db.collection("User").whereEqualTo("Username", usernameValue).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    setAccountType(document.get("AccountType").toString());
                                    email = document.getString("Email");
                                    oldId = document.getId();
                                    oldData = document.getData();
                                    aUser = true;
                                    loginEmail();
                                    Log.d(TAG, document.getId() + " => " + document
                                            .getData());
                                }
                            } else {
                                Utility.buttonWait(logInButton, false, "Log In");
                                Toast.makeText(getApplicationContext(), "Username or Password is incorrect.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        }
                    });
            } else {
                db.collection("User").whereEqualTo("Email", usernameValue).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            setAccountType(document.get("AccountType").toString());
                                            email = document.getString("Email");
                                            oldId = document.getId();
                                            oldData = document.getData();
                                            aUser = true;
                                            loginEmail();
                                            Log.d(TAG, document.getId() + " => " + document
                                                    .getData());
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                if (email.isEmpty()) {
                    email = usernameValue;
                    oldId = "";
                    aUser = false;
                }
                loginEmail();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Username or Password is incorrect.", Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "Short password");
        }
    }

    private void setAccountType(String accountType) {
        switch (accountType) {
            case "student":
                Account.setType(Account.Type.Student);
                break;
            case "educator":
                Account.setType(Account.Type.Educator);
                break;
            case "learningcenter":
                Account.setType(Account.Type.LearningCenter);
                break;
            default:
                Account.setType(Account.Type.Student);
                break;
        }
    }

    private void loginEmail() {
        final String passwordValue = password.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, passwordValue)
            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        loggedIn();
                    } else {
                        if (aUser && task.getException().getMessage().equals(getString(R.string.login_error_message_no_email))) {
                            firebaseAuth.createUserWithEmailAndPassword(email, passwordValue)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            db.collection("User").document(user.getUid()).set(oldData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        db.collection("User").document(oldId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                loggedIn();
                                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                            }
                                                        });
                                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                                    }
                                                });

                                        } else {
                                            Utility.buttonWait(logInButton, false, "Log In");
                                            Toast.makeText(getApplicationContext(), "Username or Password is incorrect.", Toast.LENGTH_SHORT)
                                                    .show();
                                            Log.d(TAG, "Error logging in.");
                                        }
                                    }
                                });
                        } else {
                            Utility.buttonWait(logInButton, false, "Log In");
                            Toast.makeText(getApplicationContext(), "Username or Password is incorrect.", Toast.LENGTH_SHORT)
                                    .show();
                            Log.d(TAG, "Error logging in.");
                        }
                    }
                }
            });
    }

    private void loggedIn() {
        startActivity(new Intent(getApplicationContext(), Main.class));
        Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Logged in.");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        } else if(requestCode == 2 && resultCode == RESULT_OK) {
            username.setText(data.getStringExtra("username"));
        }
    }
}
