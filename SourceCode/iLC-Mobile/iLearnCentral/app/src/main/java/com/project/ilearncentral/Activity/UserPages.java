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
import android.widget.RelativeLayout;
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
import com.project.ilearncentral.Activity.SignUp.CreateUser;
import com.project.ilearncentral.Activity.Update.UpdateAccount;
import com.project.ilearncentral.Activity.Update.UpdateLearningCenter;
import com.project.ilearncentral.Activity.Update.UpdateProfile;
import com.project.ilearncentral.Adapter.UserPagesAdapter;
import com.project.ilearncentral.CustomBehavior.CustomAppBarLayoutBehavior;
import com.project.ilearncentral.Fragment.Feed;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Fragment.Management;
import com.project.ilearncentral.Fragment.Profile.EducatorProfile;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserPages extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "USER_PAGES";
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private ObservableBoolean accountSet, authProfileSet,
            userSet, profileSet, centerSet;

    private Toolbar toolbar;
    private CircleImageView userImage;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button featuresButton, notificationButton, messageButton;
    private AppBarLayout appBarLayout;
    private RelativeLayout loadingPage;
    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout.LayoutParams clLayoutParams;
    private TextView usernameDisplay, fieldDisplay;
    private LinearLayout profileView;
    private final int UPDATE_PROFILE = 11, UPDATE_ACCOUNT = 12, UPDATE_CENTER = 13, CREATE_USER = 14;

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
        loadingPage = findViewById(R.id.user_loading_panel);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        clLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(true);
        usernameDisplay = findViewById(R.id.user_full_name);
        fieldDisplay = findViewById(R.id.user_expertise);

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
        setObservableListeners();
    }

    private void setObservableListeners() {
        userSet = new ObservableBoolean();
        profileSet = new ObservableBoolean();
        centerSet = new ObservableBoolean();
        accountSet = new ObservableBoolean();
        accountSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    if (user.getDisplayName()==null || user.getDisplayName().equals("")) {
                        Utility.updateProfileWithImage(TAG, authProfileSet);
                    } else {
                        authProfileSet.set(true);
                        //SETUP all methods that need Account static to be completely loaded here.
                    }
                }
            }
        });
        authProfileSet = new ObservableBoolean();
        authProfileSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    changeProfileImage();
                    usernameDisplay
                            .setText(Account.getName() + " | " + Account.getStringData("username"));
                    if (Account.getType() == Account.Type.LearningCenter) {
                        fieldDisplay.setText(Account.getType().toString() + " | " + Utility
                                .caps(Account.getStringData("accessLevel")));
                    } else
                        fieldDisplay.setText(Account.getType().toString());
                } else {

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
                        if (Account.getType() == Account.Type.LearningCenter)
                            collection = "LearningCenterStaff";
                        else if (Account.getType() == Account.Type.Educator)
                            collection = "Educator";
                        else if (Account.getType() == Account.Type.Student)
                            collection = "Student";
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
                                                DocumentReference docRef = db.collection("LearningCenter")
                                                        .document(Account.getStringData("centerId"));
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Account.setBusinessData(document.getData());
                                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                            } else {
                                                                Log.d(TAG, "No such document");
                                                            }
                                                        } else {
                                                            Log.d(TAG, "get failed with ", task.getException());
                                                        }
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

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
//        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
//        menu
//        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem updateBusiness = menu.findItem(R.id.menu_update_business);
        if (Account.getType() == Account.Type.LearningCenter) {
            updateBusiness.setVisible(true);
            MenuItem createUser = menu.findItem(R.id.menu_create_user);
            if (Account.getStringData("accessLevel").equals("administrator"))
                createUser.setVisible(true);
            else
                createUser.setVisible(false);
        } else {
            updateBusiness.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account_settings:
                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                return true;
            case R.id.menu_update_account:
                startActivityForResult(new Intent(getApplicationContext(), UpdateAccount.class), UPDATE_ACCOUNT);
                return true;
            case R.id.menu_update_profile:
                startActivityForResult(new Intent(getApplicationContext(), UpdateProfile.class), UPDATE_PROFILE);
                return true;
            case R.id.menu_update_business:
                startActivityForResult(new Intent(getApplicationContext(), UpdateLearningCenter.class), UPDATE_CENTER);
                return true;
            case R.id.menu_create_user:
                startActivity(new Intent(getApplicationContext(), CreateUser.class));
                return true;
            case R.id.menu_logout:
                Connection.logOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeProfileImage() {
        runOnUiThread(new Runnable() {
            public void run() {
            if (user.getPhotoUrl() != null) {
                storageRef.child("images").child(Account.getStringData("username")).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).error(R.drawable.user)
                                    .into(userImage);

                            loadingPage.setVisibility(View.GONE);
                            appBarLayout.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.VISIBLE);
                        }
                    });
            } else {
                loadingPage.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        if(requestCode == UPDATE_ACCOUNT && resultCode == RESULT_OK) {
            setAccount();
            setObservableListeners();
        } else if(requestCode == UPDATE_PROFILE && resultCode == RESULT_OK) {
            setAccount();
            setObservableListeners();
        } else if(requestCode == UPDATE_CENTER && resultCode == RESULT_OK) {
            setAccount();
            setObservableListeners();
        }
    }
}
