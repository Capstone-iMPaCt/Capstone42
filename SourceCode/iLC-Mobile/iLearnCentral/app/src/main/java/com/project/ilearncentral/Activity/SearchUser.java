package com.project.ilearncentral.Activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Adapter.SearchUserAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.FrequencyComparator;
import com.project.ilearncentral.MyClass.SearchHistory;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SearchUser extends AppCompatActivity implements Observer {

    private static final String TAG = "SearchUser";
    private List<User> retrievedUsers;
    private List<User> users;

    private SearchUserAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ObservableBoolean show;
    private TextView message;

    private ImageButton recommendToggle;
    private boolean recommend;

    private List<String> queryList;

    @Override
    public void update(Observable o, Object arg) {
        queryList = ((SearchHistory) o).getQueryList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initialize();

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
                setRecommend(false);
                message.setVisibility(View.GONE);
                searchFullnames(query);
                queryList.add(query.toLowerCase().trim());
                SearchHistory.getInstance()
                        .document(Account.getUsername())
                        .queryList(queryList)
                        .save();
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
                setRecommend(false);
                show.set(true);
                message.setVisibility(View.GONE);
                return false;
            }
        });
        if (!Account.isType("learningcenter")) {
            findViewById(R.id.search_center_app_bar_vertical_line_divider).setVisibility(View.GONE);
            recommendToggle.setVisibility(View.GONE);
        } else {
            findViewById(R.id.search_center_app_bar_vertical_line_divider).setVisibility(View.VISIBLE);
            recommendToggle.setVisibility(View.VISIBLE);
            recommendToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recommend) {
                        setRecommend(false);
                        show.set(true);
                        message.setVisibility(View.GONE);
                    } else {
                        setRecommend(true);
                        users.clear();
                        for (User user : retrievedUsers) {
                            for (String item : getTopFiveSearchKeys()) {
                                if (user.getFullname().toLowerCase().contains(item)
                                        && user.getType().toLowerCase().equals("educator")) {
                                    if (!users.contains(user))
                                        users.add(user);
                                }
                            }
                        }
                        if (users.size() == 0) {
                            message.setVisibility(View.VISIBLE);
                            message.setText("Sorry, there are no recommendations for you at the moment.");
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchUser.this));
        adapter = new SearchUserAdapter(this, users);
        recyclerView.setAdapter(adapter);
        retrievedUsers = User.getRetrievedUsers();
        if (retrievedUsers.size() > 0) show.set(true);
    }

    private void initialize() {
        users = new ArrayList<>();
        show = new ObservableBoolean();
        recyclerView = findViewById(R.id.search_user_recyclerview);
        searchView = findViewById(R.id.search_user_view);
        message = findViewById(R.id.search_user_message);

        queryList = new ArrayList<>();
        recommendToggle = findViewById(R.id.search_center_app_bar_toggle_recommend);

        getTopFiveSearchKeys();
    }

    public void searchFullnames(String text) {
        users.clear();
        text = text.toLowerCase();

        for (User user : retrievedUsers) {
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

    private void setRecommend(boolean value) {
        if (value) {
            recommendToggle.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 235, 59)));
            recommend = value;
        } else {
            recommendToggle.setBackgroundTintList(null);
            recommend = value;
        }
        adapter.notifyDataSetChanged();
    }

    private List<String> getTopFiveSearchKeys() {
        SearchHistory.getInstance().document(Account.getUsername())
                .queryList(queryList)
                .get();
        Map<String, Integer> searchKeys = new HashMap<>();
        for (String item : queryList) {
            int count = searchKeys.containsKey(item) ? searchKeys.get(item) : 0;
            searchKeys.put(item, count + 1);
        }
        List<String> top5Keys = new ArrayList<>();

        List<Map.Entry<String, Integer>> top5Set = FrequencyComparator.findGreatest(searchKeys, 5);
        for (Map.Entry<String, Integer> entry : top5Set) {
            top5Keys.add(entry.getKey());
            Log.d(TAG, entry.getKey() + " | " + entry.getValue());
        }
        return top5Keys;
    }
}
