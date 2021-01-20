package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.Adapter.AddonAdapter;
import com.project.ilearncentral.Model.Addon;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;
import com.project.ilearncentral.databinding.ActivitySubscriptionBinding;

import java.util.ArrayList;

public class Subscription extends AppCompatActivity implements View.OnClickListener {

    private ActivitySubscriptionBinding layoutBinding;
    private AddonAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Addon> addon;
    private Intent intent;

    private String userPhotoUrl;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_subscription);
        setContentView(layoutBinding.getRoot());

        initialize();
    }

    private void initialize() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.inflateMenu(R.menu.menu_activity_pages);

        intent = getIntent();
        userPhotoUrl = intent.getStringExtra("userPhotoUrl");
        changeProfileImage();
        layoutBinding.userName.setText(Account.getName());

        layoutBinding.userPageLink.setOnClickListener(this);

        addon = new ArrayList<>();
        if (Account.isType("learningcenter")) {
            addon.add(new Addon("Subscription Level 0", "This is a lifetime free subscription which includes Job Hiring and Profiling systems. These systems handles the job post management, and potential hire recommendation features of iLearnCentral.", 0));
            addon.add(new Addon("Subscription Level 1", "This subscription level includes the Enrolment System. This system handles the course and enrolment management features of iLearnCentral that include the posting, updating, and closing of courses; processing student enrolment and enrolment fees; and viewing students enrolled and payments.", 199));
            addon.add(new Addon("Subscription Level 2", "This subscription level includes the Enrolment System extended with a Scheduling System. Scheduling System features are the adding of classes, the updating of class details, the closing of classes, and the viewing of class records.", 399));
        } else if (Account.isType("educator")) {
            addon.add(new Addon("Subscription Level 0", "This is a lifetime free subscription which includes Job Seeking and Profiling systems. These systems handles the job searching, job applying, job vacancy recommendation features of iLearnCentral.", 0));
        }
        recyclerView = findViewById(R.id.addon_container_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddonAdapter(this, addon);
        recyclerView.setAdapter(adapter);
    }

    public void changeProfileImage() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (userPhotoUrl != null) {
                    FirebaseStorage.getInstance().getReference().child("images").child(Account.getUsername()).getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getApplicationContext()).load(uri.toString()).fitCenter().into(layoutBinding.userProfilePicture);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_page_link:
                onBackPressed();
                finish();
            default:
                return;
        }
    }
}
