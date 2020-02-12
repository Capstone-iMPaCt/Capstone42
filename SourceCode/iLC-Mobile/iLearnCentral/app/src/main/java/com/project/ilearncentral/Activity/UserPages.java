package com.project.ilearncentral.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.project.ilearncentral.Adapter.UserPagesAdapter;
import com.project.ilearncentral.CustomBehavior.CustomAppBarLayoutBehavior;
import com.project.ilearncentral.Fragment.Feed;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Fragment.Management;
import com.project.ilearncentral.Fragment.Profile.EducatorProfile;
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
    private CoordinatorLayout.LayoutParams clLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pages);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        circleImageView = (CircleImageView) findViewById(R.id.profile_imageview);
        featuresButton = (Button) findViewById(R.id.features_button);
        notificationButton = (Button) findViewById(R.id.notification_button);
        messageButton = (Button) findViewById(R.id.message_button);
        appBarLayout = (AppBarLayout) findViewById(R.id.home_app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        clLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehavior) clLayoutParams.getBehavior()).setScrollBehavior(true);

        setSupportActionBar(toolbar);

        featuresButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        circleImageView.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        UserPagesAdapter adapter = new UserPagesAdapter(getSupportFragmentManager());
        adapter.addFragment(new EducatorProfile(), "Profile");
        adapter.addFragment(new Feed(), "Feeds");
        adapter.addFragment(new JobPost(), "Job Posts");
        adapter.addFragment(new Management(), "My Activies");
        adapter.addFragment(new Management(), "Management");

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
        switch (item.getItemId()) {
            case R.id.account_settings:
                startActivity(new Intent(getApplicationContext(), AccountSettings.class));
                return true;
            case R.id.logout:
                Connection.logOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
