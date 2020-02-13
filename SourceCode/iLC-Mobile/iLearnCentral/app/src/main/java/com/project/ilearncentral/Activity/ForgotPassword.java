package com.project.ilearncentral.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    private String TAG = "FORGOT_PASSWORD";
    private Spinner question;
    private TextInputEditText username, answer;
    private TextView title;
    private Button button;
    private String email, answerValue;
    private boolean hasSecurity;


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        hasSecurity = false;
        email = "";
        answerValue = "";
        question = findViewById(R.id.forgot_security_question);
        username = findViewById(R.id.forgot_username);
        answer = findViewById(R.id.forgot_security_answer);
        title = findViewById(R.id.forgot_security_title);
        db = FirebaseFirestore.getInstance();
    }

    public void forgotButtonClick(View v) {
        button = (Button)v;
        String cur = button.getText().toString();
        if (!username.getText().toString().isEmpty()) {
            if (cur.equals("Continue")) {
                button.setEnabled(false);
                button.setText("Please wait...");
                db.collection("User").whereEqualTo("Username", username.getText().toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String[] questions = getResources()
                                                .getStringArray(R.array.my_security_questions);
                                        hasSecurity = false;
                                        for (int i = 0; i < questions.length; i++) {
                                            if (questions[i]
                                                    .equals(document.getString("Question"))) {
                                                question.setSelection(i);
                                                question.setEnabled(false);
                                                hasSecurity = true;
                                                answerValue = document.getString("Answer");
                                                break;
                                            }
                                        }
                                        if (hasSecurity)
                                            showSecurityQuestion(document.getString("Email"));
                                        else {
                                            button.setText("Send Reset Link");
                                            button.setEnabled(true);
                                            email = document.getString("Email");
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username does not exist!", Toast.LENGTH_SHORT)
                                            .show();
                                    hideSecurityQuestion();
                                }
                            }
                        });
            } else if (cur.equals("Send Reset Link")) {
                if (hasSecurity) {
                    if (answer.getText().toString().toLowerCase().trim()
                            .equals(answerValue.toLowerCase().trim())) {
                        sendLink();
                    } else {
                        answer.setError("Incorrect answer");
                    }
                } else {
                    sendLink();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Button pressed", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            username.setError("Username is empty.");
        }
    }

    private void sendLink() {
        button.setEnabled(false);
        button.setText("Sending link...");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showAlert("Email link sent","Success");
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    public void showAlert(String Message,String label)
    {
        //set alert for executing the task
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(""+label);
        alert.setMessage(""+Message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int id){
                dialog.cancel();
                Intent intent = new Intent();
                intent.putExtra
                        ("username", username.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Dialog dialog = alert.create();
        dialog.show();
    }

    private void showSecurityQuestion(String email) {
        title.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        answer.setVisibility(View.VISIBLE);
        button.setText("Send Reset Link");
        button.setEnabled(true);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()),
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addRule(RelativeLayout.BELOW, R.id.forgot_security_answer_wrap);
        button.setLayoutParams(layout);
        this.email = email;
    }

    private void hideSecurityQuestion() {
        title.setVisibility(View.GONE);
        question.setVisibility(View.GONE);
        answer.setVisibility(View.GONE);
        button.setText("Continue");
        button.setEnabled(true);
        this.email = "";
    }


}
