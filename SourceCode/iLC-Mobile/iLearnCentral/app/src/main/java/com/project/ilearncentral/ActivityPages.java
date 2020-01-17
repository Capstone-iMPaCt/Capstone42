package com.project.ilearncentral;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityPages extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button featuresButton, searchButton;
    private TextView searchBar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private int appBar_minH, appBar_maxH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        featuresButton = (Button)findViewById(R.id.features_button);
        searchButton = (Button)findViewById(R.id.search_button);
        searchBar = (TextView) findViewById(R.id.search_bar);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        searchBar.setVisibility(View.INVISIBLE);
                        searchButton.setVisibility(View.INVISIBLE);
                        appBarLayout.setExpanded(true);
                        appBar_maxH = appBarLayout.getHeight();
                        break;
                    case 1:
                    case 2:
                        searchBar.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(View.VISIBLE);
                        appBarLayout.setExpanded(false);
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
                break;
            case R.id.search_button:
                if(searchBar.getVisibility() == View.VISIBLE)
                    searchBar.setVisibility(View.INVISIBLE);
                else
                    searchBar.setVisibility(View.VISIBLE);
                break;
            default:
                return;
        }
    }
}
