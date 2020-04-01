package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.project.ilearncentral.Adapter.SearchCenterAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchCenter extends AppCompatActivity {

    private List<LearningCenter> retrieved;
    private List<LearningCenter> centers;

    private SearchCenterAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ObservableBoolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        centers = new ArrayList<>();
        recyclerView = findViewById(R.id.search_user_recyclerview);
        searchView = findViewById(R.id.search_user_view);
        show = new ObservableBoolean();
        show.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    centers.clear();
                    centers.addAll(retrieved);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBusinessName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                show.set(true);
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchCenter.this));
        adapter = new SearchCenterAdapter(this, centers);
        recyclerView.setAdapter(adapter);
        retrieved = LearningCenter.getRetrieved();
        if (retrieved.size()>0) show.set(true);
    }

    public void searchBusinessName(String text) {
        centers.clear();
        text = text.toLowerCase();

        for(LearningCenter center: retrieved) {
            if (center.getBusinessName().toLowerCase().contains(text) ||
                    center.getServiceType().toLowerCase().contains(text))
                centers.add(center);
        }
        Collections.sort(centers, new Comparator<LearningCenter>() {
            public int compare(LearningCenter o1, LearningCenter o2) {
                return o1.getBusinessName().compareTo(o2.getBusinessName());
            }
        });
        adapter.notifyDataSetChanged();
    }

}
