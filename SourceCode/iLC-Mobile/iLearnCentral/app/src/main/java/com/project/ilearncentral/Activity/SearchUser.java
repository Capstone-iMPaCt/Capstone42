package com.project.ilearncentral.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.SearchUserAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchUser extends AppCompatActivity {

    private List<User> retrievedUsers;
    private List<User> users;

    private SearchUserAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ObservableBoolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        users = new ArrayList<>();
        recyclerView = findViewById(R.id.search_user_recyclerview);
        searchView = findViewById(R.id.search_user_view);
        show = new ObservableBoolean();
        show.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    users.clear();
                    users.addAll(retrievedUsers);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFullnames(query);
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

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchUser.this));
        adapter = new SearchUserAdapter(this, users);
        recyclerView.setAdapter(adapter);
        retrievedUsers = User.getRetrievedUsers();
        if (retrievedUsers.size()>0) show.set(true);
    }

    public void searchFullnames(String text) {
        users.clear();
        text = text.toLowerCase();

        for(User user: retrievedUsers) {
            if (user.getFullname().toLowerCase().contains(text) ||
                    user.getType().toLowerCase().contains(text))
                users.add(user);
        }
        Collections.sort(users, new Comparator<User>() {
            public int compare(User o1, User o2) {
                return o2.getFullname().compareTo(o1.getFullname());
            }
        });
        adapter.notifyDataSetChanged();
    }

}
