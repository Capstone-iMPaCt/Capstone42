package com.project.ilearncentral.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Adapter.ChatListAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.Model.Message;
import com.project.ilearncentral.Model.Posts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Chat extends AppCompatActivity {

    private String TAG = "CHAT";
    private FirebaseFirestore db;
    private FirebaseUser user;
    private SearchView searchView;
    private TextView noConversations;
    private ChatListAdapter adapter;
    private ChatListAdapter filteredAdapter;
    private List<Message> chatList;
    private List<Message> filteredList;
    private List<String> addedChat;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        noConversations = findViewById(R.id.chat_no_data_text);
        chatList = new ArrayList<>();
        filteredList = new ArrayList<>();
        addedChat = new ArrayList<>();

        final RecyclerView recyclerView =  findViewById(R.id.chat_coversations_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(chatList.size() == 0) {
            noConversations.setVisibility(View.VISIBLE);
        }
        adapter = new ChatListAdapter(this, chatList);
        filteredAdapter = new ChatListAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        username = Account.getUsername();

        searchView = findViewById(R.id.chat_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredList.clear();
                for(Message message: chatList) {
                    if (message.getFullname().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(message);
                    }
                }
                recyclerView.swapAdapter(filteredAdapter, true);
                filteredAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recyclerView.swapAdapter(adapter, true);
                return false;
            }
        });
        getMessages();
    }

    private void getMessages() {
        db.collection("Messages")
                .whereEqualTo("From", username)
                .orderBy("DateSent", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message chat = new Message();
                                if (addedChat.contains(document.get("To").toString())) {
                                    for (int i=0; i<chatList.size(); i++) {
                                        if(chatList.get(i).getFrom().equals(document.get("To").toString())) {
                                            chat.setId(document.getId());
                                            chat.setTo(document.get("To").toString());
                                            chat.setFrom(document.get("From").toString());
                                            chat.setMessage(document.get("Message").toString());
                                            chat.setDateSent(document.getTimestamp("DateSent"));
                                            chat.setType("from");
                                            chatList.set(i, chat);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                else if (!addedChat.contains(document.get("To").toString())) {
                                    chat.setId(document.getId());
                                    chat.setTo(document.get("To").toString());
                                    chat.setFrom(document.get("From").toString());
                                    chat.setMessage(document.get("Message").toString());
                                    chat.setDateSent(document.getTimestamp("DateSent"));
                                    chat.setType("from");
                                    addedChat.add(document.get("To").toString());
                                    chatList.add(chat);
                                    adapter.notifyDataSetChanged();
                                }
                                if (chatList.isEmpty()) noConversations.setVisibility(View.VISIBLE);
                                else noConversations.setVisibility(View.INVISIBLE);
                                Log.d(TAG, document.getId() + " From=> " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Messages")
                .whereEqualTo("To", username)
                .orderBy("DateSent", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message chat = new Message();
                                if (addedChat.contains(document.get("From").toString())) {
                                    for (int i=0; i<chatList.size(); i++) {
                                        if(chatList.get(i).getTo().equals(document.get("From").toString())) {
                                            chat.setId(document.getId());
                                            chat.setTo(document.get("To").toString());
                                            chat.setFrom(document.get("From").toString());
                                            chat.setMessage(document.get("Message").toString());
                                            chat.setDateSent(document.getTimestamp("DateSent"));
                                            chat.setType("to");
                                            chatList.set(i, chat);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                else if (!addedChat.contains(document.get("From").toString())) {
                                    chat.setId(document.getId());
                                    chat.setTo(document.get("To").toString());
                                    chat.setFrom(document.get("From").toString());
                                    chat.setMessage(document.get("Message").toString());
                                    chat.setDateSent(document.getTimestamp("DateSent"));
                                    chat.setType("to");
                                    addedChat.add(document.get("From").toString());
                                    chatList.add(chat);
                                    adapter.notifyDataSetChanged();
                                }
                                if (chatList.isEmpty()) noConversations.setVisibility(View.VISIBLE);
                                else noConversations.setVisibility(View.INVISIBLE);
                                Log.d(TAG, document.getId() + " To=> " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_pages, menu);
        return true;
    }

}
