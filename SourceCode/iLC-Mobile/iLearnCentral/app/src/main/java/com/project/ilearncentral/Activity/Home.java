package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.AddonAdapter;
import com.project.ilearncentral.Model.Addon;
import com.project.ilearncentral.R;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private AddonAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Addon> addon;
    private LinearLayout userPageLink;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_activity_pages);

        userPageLink = (LinearLayout)findViewById(R.id.user_page_link);
        userPageLink.setOnClickListener(this);

        addon = new ArrayList<>();
        addon.add(new Addon("Enrollment System", "Course offer creation, enrollment, management, etc.", "000 days"));
        addon.add(new Addon("Scheduling System", "Scheduling of activities, classes, etc..", "000 days"));
        recyclerView = findViewById(R.id.addon_container_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddonAdapter(this, addon);
        recyclerView.setAdapter(adapter);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_page_link:
//                onBackPressed();
                startActivity(new Intent(getApplicationContext(), UserPages.class));
                finish();
            default:
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
        return true;
    }
}
