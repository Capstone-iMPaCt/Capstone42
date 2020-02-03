package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.ilearncentral.R;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_activity_pages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
        return true;
    }

}
