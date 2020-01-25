package com.project.ilearncentral.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.R;

public class Messaging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

//        FirebaseInstanceId.getInstance().getInstanceId()
//            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                if (!task.isSuccessful()) {
//                    Log.w(TAG, "getInstanceId failed", task.getException());
//                    return;
//                }
//
//                // Get new Instance ID token
//                String token = task.getResult().getToken();
//
//                // Log and toast
//                String msg = getString(R.string.msg_token_fmt, token);
//                Log.d(TAG, msg);
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
