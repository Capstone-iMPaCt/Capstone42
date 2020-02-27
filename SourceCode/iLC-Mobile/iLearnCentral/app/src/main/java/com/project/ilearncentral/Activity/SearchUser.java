package com.project.ilearncentral.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Adapter.SearchUserAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SearchUser extends AppCompatActivity {

    private final String TAG = "SearchUser";
    private List<User> retreivedUsers;
    private List<User> users;
    private SearchUserAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        users = new ArrayList<>();
        retreivedUsers = new ArrayList<>();
        recyclerView = findViewById(R.id.search_user_recyclerview);
        searchView = findViewById(R.id.search_user_view);
        retrievePostsFromDB();
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
                showAll();
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchUser.this));
        adapter = new SearchUserAdapter(this, users);
        recyclerView.setAdapter(adapter);
    }

    private void showAll() {
        users.clear();
        users.addAll(retreivedUsers);
        adapter.notifyDataSetChanged();
    }


    public void retrievePostsFromDB() {
        FirebaseFirestore.getInstance().collection("User")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String collection = "";
                            if (document.get("AccountType").equals("learningcenter")) {
                                collection = "LearningCenterStaff";
                            } else if (document.get("AccountType").equals("educator")) {
                                collection = "Educator";
                            } else if (document.get("AccountType").equals("student")) {
                                collection = "Student";
                            }
                            final Map<String, Object> userData = ((Map<String, Object>) document.getData());
                            if (document.get("Image") == null) userData.put("Image", "");
                            FirebaseFirestore.getInstance().collection(collection).whereEqualTo("Username", document.getString("Username"))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, String> nameDB = (Map<String, String>) document.get("Name");

                                                    String fullname = Utility.formatFullName(nameDB.get("FirstName"), nameDB.get("MiddleName"), nameDB.get("LastName"));
                                                    int pos = getUserPositionByUsername(document.getString("Username"));
                                                    User curUser = new User(document.getString("Username"), fullname, userData.get("AccountType")+"", userData.get("Image").toString());
                                                    if (pos==-1) {
                                                        retreivedUsers.add(curUser);
                                                    } else {
                                                        retreivedUsers.get(pos).setUser(curUser);
                                                    }
                                                }
                                                showAll();
                                            } else {
                                            }
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public int getUserPositionByUsername(String username) {
        for (int i=0; i<users.size();i++) {
            if (users.get(i).getUsername().equals(username))
                return i;
        }
        return -1;
    }

    public void searchFullnames(String text) {
        users.clear();
        text = text.toLowerCase();

        for(User user:retreivedUsers) {
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
