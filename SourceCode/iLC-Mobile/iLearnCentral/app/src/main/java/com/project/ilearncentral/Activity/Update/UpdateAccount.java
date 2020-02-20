package com.project.ilearncentral.Activity.Update;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateAccount extends AppCompatActivity {

    private String TAG = "UpdateAccount";
    private TextInputLayout oldPasswordLayout;
    private TextInputEditText usernameInput, passwordInput, oldPasswordInput, confirmInput, emailInput, contactInput, answerInput;
    private String username, newPassword, oldPassword, confirm, email, contact, answer, question;
    private Spinner questions;
    private TextView title;
    private Button UpdateBtn;
    boolean valid, updated;
    private FirebaseFirestore db;
    FirebaseUser user;
    private boolean emailChangeFlag, passChangeFlag, securityChangeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_users);

        usernameInput = findViewById(R.id.sign_up_username);
        oldPasswordLayout = findViewById(R.id.sign_up_old_password_layout);
        oldPasswordInput = findViewById(R.id.sign_up_old_password);
        passwordInput = findViewById(R.id.sign_up_password);
        confirmInput = findViewById(R.id.sign_up_confirm_password);
        emailInput = findViewById(R.id.sign_up_email);
        contactInput = findViewById(R.id.sign_up_contact_no);
        answerInput = findViewById(R.id.sign_up_security_answer);
        questions = findViewById(R.id.sign_up_security_question);
        title = findViewById(R.id.sign_up_user_title);
        UpdateBtn = findViewById(R.id.sign_up_users_continue_button);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailChangeFlag = passChangeFlag = securityChangeFlag = false;
        updated = false;

        setValues();

        usernameInput.setEnabled(false);
        oldPasswordLayout.setVisibility(View.VISIBLE);
        title.setText("Update User Account");
        UpdateBtn.setText("Update");
        UpdateBtn.setOnClickListener(updateAccount);
    }

    private View.OnClickListener updateAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkErrors()) {
                update();
            } else {
                Toast.makeText(getApplicationContext(), "Please correct errors", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    private void update() {
        Utility.buttonWait(UpdateBtn, true);
        if (emailChangeFlag) {
            changeEmail();
        } else if (passChangeFlag) {
            changePassword();
        } else if (securityChangeFlag) {
            updateUser();
        } else {
            Utility.buttonWait(UpdateBtn,false, "Update");
        }
    }

    private void updateDone() {
        updated = true;
        Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT)
                .show();
        Utility.buttonWait(UpdateBtn, false, "Update");
    }

    private void changeEmail() {
        AuthCredential credential = EmailAuthProvider.getCredential(Account.getStringData("email"), oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Account.addData("email", email);
                                                if (passChangeFlag) {
                                                    changePassword();
                                                } else if (securityChangeFlag || emailChangeFlag) {
                                                    updateUser();
                                                } else {
                                                    updateDone();
                                                }
                                                Log.d(TAG, "User email address updated.");
                                            } else {
                                                Toast.makeText(getApplicationContext(), "User email address NOT updated", Toast.LENGTH_SHORT)
                                                        .show();
                                                Utility.buttonWait(UpdateBtn, false, "Update");
                                                Log.d(TAG, "User email address NOT updated.");
                                            }
                                        }
                                    });
                            Log.d(TAG, "User re-authenticated.");
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Current Password", Toast.LENGTH_SHORT)
                                    .show();
                            Utility.buttonWait(UpdateBtn, false, "Update");
                        }
                    }
                });
    }

    private void changePassword() {
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Account.addData("password", newPassword);
                                            oldPasswordInput.setText(newPassword);
                                            passwordInput.setText("");
                                            confirmInput.setText("");
                                            oldPassword = newPassword;
                                            newPassword = "";
                                            confirm = "";
                                            if (securityChangeFlag) {
                                                updateUser();
                                            } else {
                                                updateDone();
                                            }
                                            Log.d(TAG, "User password updated.");
                                        } else {
                                            Utility.buttonWait(UpdateBtn,false, "Update");
                                            oldPasswordInput.setError("Wrong password");
                                            Log.d(TAG, "Password NOT updated.");
                                        }
                                    }
                                });
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Current Password", Toast.LENGTH_SHORT);
                            Utility.buttonWait(UpdateBtn, false, "Update");
                        }
                    }
                });
    }

    private void updateUser() {
        if(emailChangeFlag) Account.addData("email", email);
        Account.addData("contactNo", contact);
        Account.addData("answer", answer);
        Account.addData("question", question);
        DocumentReference ref = db.collection("User").document(user.getUid());
        ref
                .update("Email", Account.getStringData("email"),
                        "ContactNo", contact,
                        "Answer", answer,
                        "Question", question)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateDone();
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    private boolean checkErrors() {
        valid = true;

        username = usernameInput.getText().toString();
        oldPassword = oldPasswordInput.getText().toString();
        newPassword = passwordInput.getText().toString();
        confirm = confirmInput.getText().toString();
        email = emailInput.getText().toString();
        contact = contactInput.getText().toString();
        answer = answerInput.getText().toString().toLowerCase();
        question = questions.getSelectedItem().toString();

        if (contact.isEmpty()) {
            contactInput.setError("Contact number is empty");
            valid = false;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email is empty");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email invalid format");
            valid = false;
        }

        emailChangeFlag = !email.equals(Account.getStringData("email"));
        passChangeFlag = !oldPassword.isEmpty() && !newPassword.isEmpty();

        if((emailChangeFlag || passChangeFlag) && oldPassword.isEmpty()) {
            oldPasswordInput.setError("Please input old password");
            valid = false;
        } else if ((emailChangeFlag || passChangeFlag) && oldPassword.length() < 6) {
            oldPasswordInput.setError("Password is too short");
            valid = false;
        }

        if (passChangeFlag && newPassword.length() < 6) {
            passwordInput.setError("Password is too short");
            valid = false;
        }

        if (!confirm.equals(newPassword)) {
            confirmInput.setError("Password does not match");
            valid = false;
        }

        if (answer.isEmpty()) {
            answerInput.setError("Please provide an answer.");
            valid = false;
        }
        securityChangeFlag = !answer.equals(Account.getStringData("answer")) || !question.equals(Account.getStringData("question")) || !contact.equals(Account.getStringData("contactNo"));

        return valid;
    }

    private void setValues() {
        usernameInput.setText(Account.getStringData("username"));
        emailInput.setText(Account.getStringData("email"));
        contactInput.setText(Account.getStringData("contactNo"));
        List<String> list = new ArrayList<>();
        for(String s:getResources().getStringArray(R.array.my_security_questions)) {
            list.add(s);
        }
        questions.setSelection(list.indexOf(Account.getStringData("question")));
        answerInput.setText(Account.getStringData("answer"));
    }

    @Override
    public void onBackPressed() {
        if (updated)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        //retrieveData();
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
