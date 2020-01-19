package com.project.ilearncentral;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
    private InputMethodManager keyPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        keyPad = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        featuresButton = (Button)findViewById(R.id.features_button);
        searchButton = (Button)findViewById(R.id.search_button);
        searchBar = (TextView) findViewById(R.id.search_bar);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        featuresButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        viewPager = (ViewPager)findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserProfile(), "Profile");
        adapter.addFragment(new NewsFeed(), "News Feed");
        adapter.addFragment(new Management(), "Job Post");
        adapter.addFragment(new Management(), "Recommendations");
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
//                        searchBar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout));
//                        searchButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout));
                    case 4:
                        searchButton.setVisibility(View.INVISIBLE);
                        hideSearchBar();
                        break;
                    case 1:
                    case 2:
                    case 3:
//                        searchBar.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(View.VISIBLE);
//                        searchBar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein));
//                        searchButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein));
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

    public void hideSearchBar(){
        searchBar.setVisibility(View.INVISIBLE);
        searchBar.setText(null);
        searchBar.clearFocus();
        keyPad.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.features_button:
                startActivity(new Intent(getApplicationContext(), Home.class));
                break;
            case R.id.search_button:
                if(searchBar.getVisibility() == View.VISIBLE)
                    hideSearchBar();
                else {
                    searchBar.setVisibility(View.VISIBLE);
                    keyPad.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                break;
            default:
                return;
        }
    }
}
