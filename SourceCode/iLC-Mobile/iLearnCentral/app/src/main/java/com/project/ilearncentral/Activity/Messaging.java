package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_activity_pages);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
