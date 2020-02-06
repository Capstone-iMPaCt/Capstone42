package com.project.ilearncentral.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.project.ilearncentral.Adapter.UserPagesAdapter;
import com.project.ilearncentral.CustomBehavior.CustomAppBarLayoutBehavior;
import com.project.ilearncentral.Fragment.Feed;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Fragment.Management;
import com.project.ilearncentral.Fragment.UserProfile;
import com.project.ilearncentral.MyClass.Connection;
import com.project.ilearncentral.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPages extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button featuresButton, notificationButton, messageButton;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private InputMethodManager keyPad;
    private CoordinatorLayout.LayoutParams clLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        if (Connection.currentUser == null){
            startActivity(new Intent(this, Login.class));
            finish();
        }

        toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        keyPad = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        circleImageView = (CircleImageView)findViewById(R.id.profile_imageview);
        featuresButton = (Button)findViewById(R.id.features_button);
        notificationButton = (Button)findViewById(R.id.notification_button);
        messageButton = (Button)findViewById(R.id.message_button);
        appBarLayout = (AppBarLayout)findViewById(R.id.home_app_bar);
        toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        clLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior)clLayoutParams.getBehavior()).setScrollBehavior(true);

        setSupportActionBar(toolbar);

        featuresButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        circleImageView.setOnClickListener(this);

        viewPager = (ViewPager)findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        UserPagesAdapter adapter = new UserPagesAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserProfile(), "Profile");
        adapter.addFragment(new Feed(), "Feeds");
        adapter.addFragment(new JobPost(), "Job Posts");
        adapter.addFragment(new Management(), "Fo");
        adapter.addFragment(new Management(), "Management");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL|TabLayout.GRAVITY_CENTER);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        appBarLayout.setExpanded(true);
                        ((CustomAppBarLayoutBehavior)clLayoutParams.getBehavior()).setScrollBehavior(true);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        appBarLayout.setExpanded(false);
                        ((CustomAppBarLayoutBehavior)clLayoutParams.getBehavior()).setScrollBehavior(false);
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
    }

    public void disableSearchFunction(){

    }

    public void logOut(){
        Connection.logOut();
        finish();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.features_button:
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                break;
            case R.id.notification_button:
                break;
            case R.id.message_button:
                startActivity(new Intent(getApplicationContext(), Chat.class));
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
        return super.onCreateOptionsMenu(menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.account_settings:
                Toast.makeText(this, "Account Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                logOut();
//                Connection.logOut();
//                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
