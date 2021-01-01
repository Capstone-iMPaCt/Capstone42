package com.project.ilearncentral.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.Activity.SignUp.CreateUser;
import com.project.ilearncentral.Activity.Update.UpdateAccount;
import com.project.ilearncentral.Activity.Update.UpdateLearningCenter;
import com.project.ilearncentral.Activity.Update.UpdateProfile;
import com.project.ilearncentral.Adapter.MainAdapter;
import com.project.ilearncentral.CustomBehavior.CustomAppBarLayoutBehavior;
import com.project.ilearncentral.Fragment.Feed;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Fragment.LCEducators;
import com.project.ilearncentral.Fragment.Profile.EducatorProfile;
import com.project.ilearncentral.Fragment.Profile.LearningCenterProfile;
import com.project.ilearncentral.Fragment.Profile.StudentProfile;
import com.project.ilearncentral.Fragment.SubSystem.EnrolmentSystem;
import com.project.ilearncentral.Fragment.SubSystem.SchedulingSystem;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.MyClass.Resume;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.project.ilearncentral.Fragment.UserActivitySchedules;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MAIN";
    private FirebaseUser user;
    private boolean tabGenerate, exit;
    private LearningCenter lc;

    private Toolbar toolbar;
    private CircleImageView userImage;
    private ImageView lcLogo;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button featuresButton, findUserButton, findCenterButton, notificationButton, messageButton;
    private AppBarLayout appBarLayout;
    private RelativeLayout loadingPage;
    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout.LayoutParams clLayoutParams;
    private TextView usernameDisplay, fieldDisplay;
    private final int UPDATE_PROFILE = 11, UPDATE_ACCOUNT = 12, UPDATE_CENTER = 13, CREATE_USER = 14, UPDATE_RESUME = 15;

    @Override
    public void onBackPressed() {
//        if (!exit) {
//            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
//            exit = true;
//        } else
//            super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true).setTitle("Exit").setMessage("Do you want to exit iLearnCentral?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        tabGenerate = true;
        exit = false;

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        userImage = (CircleImageView) findViewById(R.id.view_user_image);
        lcLogo = (ImageView) findViewById(R.id.user_learning_center_logo);
        featuresButton = (Button) findViewById(R.id.main_subscription_button);
        findUserButton = (Button) findViewById(R.id.main_find_user_button);
        findCenterButton = (Button) findViewById(R.id.main_center_list_button);
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

//        if (Account.isType("learningcenter")){
//            userImage.getLayoutParams().width = Utility.getScreenWidth();
//            userImage.getLayoutParams().height = Utility.dpToPx(this,256);
//        }

        featuresButton.setOnClickListener(this);
        findUserButton.setOnClickListener(this);
        findCenterButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        userImage.setOnClickListener(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Utility.setScreenHeight(displayMetrics.heightPixels);
        Utility.setScreenWidth(displayMetrics.widthPixels);

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        if (tabGenerate)
            generateTabs();
        setDetails(1);
    }

    private void setDetails(int code) {
        if (code != 3) {
            changeProfileImage();
            usernameDisplay.setText(Account.getName());
            if (Account.getType() == Account.Type.LearningCenter) {
                lc = LearningCenter.getLCById(Account.getCenterId());
                fieldDisplay.setText(Account.getType().toString() + " | " + Utility
                        .caps(Account.getStringData("accessLevel")));
            } else
                fieldDisplay.setText(Account.getType().toString());
        }
    }

    private void generateTabs() {
        tabGenerate = false;
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        if (Account.isType("LearningCenter")) {
            adapter.addFragment(new StudentProfile(), "Profile");
            adapter.addFragment(new LearningCenterProfile(), "Center");
            adapter.addFragment(new Feed(), "Feeds");
            adapter.addFragment(new JobPost(), "Job Posts");
            adapter.addFragment(new LCEducators(), "Educators");
            adapter.addFragment(new EnrolmentSystem(), "Enrolment");
            adapter.addFragment(new SchedulingSystem(), "Classes");
        } else if (Account.isType("Educator")) {
            adapter.addFragment(new EducatorProfile(), "Profile");
            adapter.addFragment(new Feed(), "Feeds");
            adapter.addFragment(new JobPost(), "Job Posts");
//            adapter.addFragment(new SchedulingSystem(), "Classes");
        } else if (Account.isType("Student")) {
            adapter.addFragment(new StudentProfile(), "Profile");
            adapter.addFragment(new Feed(), "Feeds");
            adapter.addFragment(new EnrolmentSystem(), "Courses");
//            adapter.addFragment(new SchedulingSystem(), "Classes");
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setIcon(R.drawable.bell_icon);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL|TabLayout.GRAVITY_CENTER);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        appBarLayout.setExpanded(true);
                        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(true);
                        userImage.setVisibility(View.VISIBLE);
                        lcLogo.setVisibility(View.GONE);
                        break;
                    case 1:
                        if (Account.isType("learningcenter")) {
                            appBarLayout.setExpanded(true);
                            ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(true);
                            userImage.setVisibility(View.GONE);
                            lcLogo.setVisibility(View.VISIBLE);
                            changeLCLogo();
                            break;
                        }
                    default:
                        appBarLayout.setExpanded(false);
                        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(false);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_subscription_button:
                if (!Account.isType("LearningCenter")) {
                    String accountType = "";
                    if (Account.isType("Educator"))
                        accountType = "Educator";
                    else
                        accountType = "Student";
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Access denied!");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("There are no subscription features for " + accountType + " accounts yet.");
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    break;
                }
                if (Account.getProfileData().get("AccessLevel").toString().compareToIgnoreCase("staff") == 0) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Access denied!");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Only administrator accounts have access to this feature.");
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    break;
                }
                Intent intent = new Intent(getApplicationContext(), Subscription.class);
                if (user.getPhotoUrl() != null)
                    intent.putExtra("userPhotoUrl", user.getPhotoUrl().toString());
                startActivity(intent);
                break;
            case R.id.main_find_user_button:
                startActivity(new Intent(getApplicationContext(), SearchUser.class));
                break;
            case R.id.main_center_list_button:
                startActivity(new Intent(getApplicationContext(), SearchCenter.class));
                break;
            case R.id.notification_button:
                break;
            case R.id.message_button:
                startActivity(new Intent(getApplicationContext(), Chat.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);

//        if (menu instanceof MenuBuilder) {
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }

        return true;
//        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
//        menu
//        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
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
        if (Account.getType() == Account.Type.Educator) {
            MenuItem resume = menu.findItem(R.id.menu_update_resume);
            resume.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account_settings:
//                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                startActivity(new Intent(getApplicationContext(), ViewUser.class));
                return true;
            case R.id.menu_update_account:
                startActivityForResult(new Intent(getApplicationContext(), UpdateAccount.class), UPDATE_ACCOUNT);
                return true;
            case R.id.menu_update_profile:
                startActivityForResult(new Intent(getApplicationContext(), UpdateProfile.class), UPDATE_PROFILE);
                return true;
            case R.id.menu_update_resume:
                Intent intent = new Intent(getApplicationContext(), AddUpdateResume.class);
                if (!Resume.getId().isEmpty())
                    intent.putExtra("resumeId", Resume.getId());
                startActivityForResult(intent, UPDATE_RESUME);
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
                    FirebaseStorage.getInstance().getReference().child("images").child(Account.getUsername()).getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                Picasso.get().load(uri.toString()).centerCrop().into(userImage);
                                    Glide.with(getApplicationContext()).load(uri.toString()).fitCenter().into(userImage);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                } else {
                }
            }
        });
    }

    private void changeLCLogo() {
        if (lc.getLogo() != null && !lc.getLogo().isEmpty()) {
            Glide.with(this).load(lc.getLogo()).error(R.drawable.logo_icon)
                    .apply(new RequestOptions().override(Utility.getScreenWidth(),
                            Utility.dpToPx(this, 256))).centerCrop().into(lcLogo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);

        Intent intent = new Intent(this, SplashScreen.class);
        intent.putExtra("update", true);
        int code = 0;
        if (requestCode == UPDATE_ACCOUNT && resultCode == RESULT_OK) {
            code = 1;
        } else if (requestCode == UPDATE_PROFILE && resultCode == RESULT_OK) {
            code = 2;
        } else if (requestCode == UPDATE_CENTER && resultCode == RESULT_OK) {
            code = 3;
        } else if (requestCode == UPDATE_RESUME && resultCode == RESULT_OK) {
            code = 0;
        }
        intent.putExtra("type", code);
        setDetails(code);
        startActivity(intent);
    }
}
