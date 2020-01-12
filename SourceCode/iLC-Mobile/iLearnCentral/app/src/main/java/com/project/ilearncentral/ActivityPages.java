package com.project.ilearncentral;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityPages extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    Button featuresButton, searchButton;
    TextView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        featuresButton = (Button)findViewById(R.id.features_button);
        searchButton = (Button)findViewById(R.id.search_button);
        searchBar = (TextView) findViewById(R.id.search_bar);
        featuresButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        viewPager = (ViewPager)findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.htab_tabs);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserProfile(), "Profile");
        adapter.addFragment(new NewsFeed(), "NewsFeed");
        adapter.addFragment(new Management(), "Management");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.features_button:
                startActivity(new Intent(getApplicationContext(), Home.class));
                break;
            case R.id.search_button:
                searchBar.setVisibility(View.VISIBLE);
                break;
            default:
                return;
        }
    }
}
