package com.project.ilearncentral.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Adapter.UserPagesAdapter;
import com.project.ilearncentral.CustomBehavior.CustomAppBarLayoutBehavior;
import com.project.ilearncentral.Fragment.Feed;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Fragment.Management;
import com.project.ilearncentral.Fragment.Profile.EducatorProfile;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.MyClass.VariableListeners.ObservableBoolean;
import com.project.ilearncentral.MyClass.VariableListeners.OnBooleanChangeListener;
import com.project.ilearncentral.R;
import com.project.ilearncentral.UpdateProfile;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserPages extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "USER_PAGES";
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private ObservableBoolean accountSet;

    private Toolbar toolbar;
    private CircleImageView userImage;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button featuresButton, notificationButton, messageButton;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout.LayoutParams clLayoutParams;
    private TextView usernameDisplay, fieldDisplay;
    private LinearLayout profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pages);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        if (user == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }

        setAccount();

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        userImage = (CircleImageView) findViewById(R.id.user_image);
        featuresButton = (Button) findViewById(R.id.main_features_button);
        notificationButton = (Button) findViewById(R.id.notification_button);
        messageButton = (Button) findViewById(R.id.message_button);
        appBarLayout = (AppBarLayout) findViewById(R.id.home_app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        clLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(true);
        usernameDisplay = findViewById(R.id.user_full_name);
        fieldDisplay = findViewById(R.id.user_expertise);
        profileView = findViewById(R.id.user_pages_profile_view);

        setSupportActionBar(toolbar);

        featuresButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        userImage.setOnClickListener(this);


        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        UserPagesAdapter adapter = new UserPagesAdapter(getSupportFragmentManager());
        adapter.addFragment(new EducatorProfile(), "Profile");
        adapter.addFragment(new Feed(), "Feeds");
        adapter.addFragment(new JobPost(), "Job Posts");
        adapter.addFragment(new Management(), "Recommendations");
        adapter.addFragment(new Management(), "My Activies");

        viewPager.setAdapter(adapter);
//        tabLayout.setBackground();
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL|TabLayout.GRAVITY_CENTER);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        appBarLayout.setExpanded(true);
                        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(true);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        appBarLayout.setExpanded(false);
                        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        profileView.setVisibility(View.INVISIBLE);
        accountSet = new ObservableBoolean();
        accountSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    changeProfileImage();
                    usernameDisplay
                            .setText(Account.getName() + " | " + Account.getStringData("username"));
                    if (Account.getType() == Account.Type.LearningCenter) {
                        fieldDisplay.setText(Account.getType().toString() + " | " + Utility
                                .caps(Account.getStringData("accessLevel")));
                    } else
                        fieldDisplay.setText(Account.getType().toString());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_features_button:
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                break;
            case R.id.notification_button:
                setAccount();
                break;
            case R.id.message_button:
                startActivity(new Intent(getApplicationContext(), Chat.class));
                break;
        }
    }

    private void setAccount() {
        DocumentReference userRef = db.collection("User").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Account.setUserData(document.getData());
                        Log.d(TAG, "User - DocumentSnapshot data: " + document.getData());

                        String collection = "";
                        switch (Account.getType().toString()) {
                            case "LearningCenter": collection = "LearningCenterStaff";
                                break;
                            case "Educator":collection = "Educator";
                                break;
                            case "Student":collection = "Student";
                                break;
                        }
                        db.collection(collection)
                            .whereEqualTo("Username", Account.getStringData("username"))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Account.setProfileData(document.getData());
                                            if (Account.getType()== Account.Type.LearningCenter) {
                                                db.collection("LearningCenter")
                                                    .document(Account.getStringData("centerId"))
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Account.setBusinessData(documentSnapshot.getData());
                                                        accountSet.set(true);
                                                    }
                                                });
                                            } else {
                                                accountSet.set(true);
                                            }
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    } else {
                        Log.d(TAG, "User - No such document");
                    }
                } else {
                    Log.d(TAG, "User - get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account_settings:
                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                return true;
            case R.id.menu_update_account:
                startActivity(new Intent(getApplicationContext(), UpdateAccount.class));
                return true;
            case R.id.menu_update_profile:
                startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
                return true;
            case R.id.menu_logout:
                Connection.logOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeProfileImage() {
        new Thread(new Runnable() {
            public void run() {
            if (user.getPhotoUrl() != null) {
                storageRef.child("images").child(Account.getStringData("username")).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).error(R.drawable.user)
                                    .into(userImage);
                            profileView.setVisibility(View.VISIBLE);
                        }
                    });
            } else {
                profileView.setVisibility(View.VISIBLE);
            }
            }
        }).start();
    }
}
