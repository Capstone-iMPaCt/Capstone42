package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

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
        addon.add(new Addon("Enrolment System","This feature handles the management of courses which includes adding, updating, and closing of course posted. This feature also includes viewing of enrolees and pending enrolees.", 199));
//        addon.add(new Addon("Scheduling System", "This feature handles the adding, updating, closing, setting schedule, and viewing of classes.", 199));
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
