package com.project.ilearncentral.Activity.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpUsers extends AppCompatActivity {

    private String TAG = "SIGNUP";
    private TextInputEditText usernameInput, passwordInput, confirmInput, emailInput, answerInput;
    private Spinner questions;
    private Intent intent;
    private TextView title;
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
        questions = findViewById(R.id.sign_up_security_question);
        title = findViewById(R.id.sign_up_user_title);

        setValues();

        if (Account.getType() == Account.Type.LearningCenter)
            title.setText("Center's Administrator Sign Up");
        db = FirebaseFirestore.getInstance();
    }

    public void continueSignUp(View v) {
        if (checkErrors()) {
            intent = new Intent(getApplicationContext(), SignUpOthers.class);
            startActivityForResult(intent,1);
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
        String question = questions.getSelectedItem().toString();

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
            retrieveData();
        }
        return valid;
    }

    private void retrieveData() {
        Account.addData("username", usernameInput.getText().toString().toLowerCase());
        Account.addData("password", passwordInput.getText().toString());
        Account.addData("email", emailInput.getText().toString());
        Account.addData("answer", answerInput.getText().toString().toLowerCase());
        Account.addData("question", questions.getSelectedItem().toString());
    }

    private void setValues() {
        usernameInput.setText(Account.getStringData("username"));
        passwordInput.setText(Account.getStringData("password"));
        emailInput.setText(Account.getStringData("email"));
        List<String> list = new ArrayList<>();
        for(String s:getResources().getStringArray(R.array.my_security_questions)) {
            list.add(s);
        }
        questions.setSelection(list.indexOf(Account.getStringData("question")));
        answerInput.setText(Account.getStringData("answer"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        retrieveData();
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
