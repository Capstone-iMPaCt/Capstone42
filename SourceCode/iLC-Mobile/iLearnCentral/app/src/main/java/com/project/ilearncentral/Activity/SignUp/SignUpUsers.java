package com.project.ilearncentral.Activity.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpUsers extends AppCompatActivity {

    private String TAG = "SIGNUP";
    private TextInputEditText usernameInput, passwordInput, confirmInput, emailInput, answerInput;
    private Spinner spinner;
    private Intent intent;
    boolean valid;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_users);

        usernameInput = findViewById(R.id.sign_up_username);
        passwordInput = findViewById(R.id.sign_up_password);
        confirmInput = findViewById(R.id.sign_up_confirm_password);
        emailInput = findViewById(R.id.sign_up_email);
        answerInput = findViewById(R.id.sign_up_security_answer);
        spinner = findViewById(R.id.sign_up_security_question);

        db = FirebaseFirestore.getInstance();
    }

    public void continueSignUp(View v) {
        if (checkErrors()) {
            switch (Account.getType()) {
                case LearningCenter:
                    intent = new Intent(getApplicationContext(), SignUpLearningCenter.class);
                    startActivityForResult(intent,1);
                    break;
                case Educator:
                    intent = new Intent(getApplicationContext(), SignUpOthers.class);
                    startActivityForResult(intent,2);
                    break;
                case Student:
                    intent = new Intent(getApplicationContext(), SignUpOthers.class);
                    startActivityForResult(intent,3);
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please correct errors", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkErrors() {
        valid = true;

        final String username = usernameInput.getText().toString().toLowerCase();
        String password = passwordInput.getText().toString();
        String confirm = confirmInput.getText().toString();
        String email = emailInput.getText().toString();
        String answer = answerInput.getText().toString().toLowerCase();
        String question = spinner.getSelectedItem().toString();

        if (username.isEmpty()) {
            usernameInput.setError("Username is empty.");
            valid = false;
        } else if(!username.matches("[a-z0-9_]{3,}")) {
            usernameInput.setError("Username invalid format.");
            valid = false;
        } else {
            db.collection("User").whereEqualTo("Username", username).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                usernameInput.setError("Username already exists");
                                valid = false;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is empty");
            valid = false;
        } else if (password.length() < 6) {
            passwordInput.setError("Password is too short");
            valid = false;
        }
        if (!confirm.equals(password)) {
            confirmInput.setError("Password does not match");
            valid = false;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email is empty");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email invalid format");
            valid = false;
        }
        if (answer.isEmpty()) {
            answerInput.setError("Please provide an answer.");
            valid = false;
        }

        if (valid) {
            Account.clearData();
            Account.addData("username", username);
            Account.addData("password", password);
            Account.addData("email", email);
            Account.addData("question", question);
            Account.addData("answer", answer);
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        } else if(requestCode == 2 && resultCode == RESULT_OK) {
            finish();
        } else if(requestCode == 3 && resultCode == RESULT_OK) {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
