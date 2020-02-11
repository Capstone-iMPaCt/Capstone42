package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Adapter.MessageAdapter;
import com.project.ilearncentral.Model.Message;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesActivity extends AppCompatActivity {

    private String TAG = "Message";
    private FirebaseFirestore db;
    private FirebaseUser user;

    private String otherUser;
    private String username;
    private EditText message;
    private ImageButton send;
    private List<Message> messageList;
    private List<String> messageIds;

    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        otherUser = intent.getStringExtra("USER_NAME");
        username = user.getEmail().substring(0, user.getEmail().indexOf('@'));
        message = findViewById(R.id.message_text);
        send = findViewById(R.id.message_send);
        messageList = new ArrayList<>();
        messageIds = new ArrayList<String>();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(username, otherUser, message.getText().toString());
            }
        });
        getMessages();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(adapter);


    }

    private void getMessages() {
        db.collection("Messages")
                .whereEqualTo("From", username)
                .whereEqualTo("To", otherUser)
                .orderBy("DateSent", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!messageIds.contains(document.getId())) {
                                    Message m = new Message();
                                    messageIds.add(document.getId());
                                    m.setId(document.getId());
                                    m.setTo(document.get("To").toString());
                                    m.setFrom(document.get("From").toString());
                                    m.setMessage(document.get("Message").toString());
                                    m.setDateSent(document.getTimestamp("DateSent"));
                                    m.setType("to");
                                    messageList.add(m);
                                    adapter.notifyDataSetChanged();
                                    Log.d(TAG, document.getId() + " From=> " + document.getData());
                                    Collections.sort(messageList, new Comparator<Message>() {
                                        public int compare(Message o1, Message o2) {
                                            return o1.getDateSent().compareTo(o2.getDateSent());
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Messages")
                .whereEqualTo("From", otherUser)
                .whereEqualTo("To", username)
                .orderBy("DateSent", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!messageIds.contains(document.getId())) {
                                    Message m = new Message();
                                    messageIds.add(document.getId());
                                    m.setId(document.getId());
                                    m.setTo(document.get("To").toString());
                                    m.setFrom(document.get("From").toString());
                                    m.setMessage(document.get("Message").toString());
                                    m.setDateSent(document.getTimestamp("DateSent"));
                                    m.setType("from");
                                    messageList.add(m);
                                    adapter.notifyDataSetChanged();
                                    Log.d(TAG, document.getId() + " To=> " + document.getData());
                                    Collections.sort(messageList, new Comparator<Message>() {
                                        public int compare(Message o1, Message o2) {
                                            return o1.getDateSent().compareTo(o2.getDateSent());
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void sendMessage(String from, String to, String m) {
        Map<String, Object> data = new HashMap<>();
        data.put("DateSent", Calendar.getInstance().getTime());
        data.put("From", from);
        data.put("To", to);
        data.put("Message", m);
        db.collection("Messages")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        message.setText("");
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        getMessages();
    }
}
