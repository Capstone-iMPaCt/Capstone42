package com.project.ilearncentral.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.Adapter.MessageAdapter;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.Model.Message;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Messages extends AppCompatActivity {

    private String TAG = "Message";
    private FirebaseFirestore db;
    private FirebaseUser user;

    private String otherUser, username, fullName;
    private EditText message;
    private ImageButton send;
    private TextView other;
    private RecyclerView recyclerView;
    private CircleImageView image;
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
        username = Account.getUsername();
        fullName = intent.getStringExtra("FULL_NAME");

        message = findViewById(R.id.message_text);
        send = findViewById(R.id.message_send);
        other = findViewById(R.id.message_user_name);
        image = findViewById(R.id.message_user_profile_picture);

        messageList = new ArrayList<>();
        messageIds = new ArrayList<>();

        other.setText(fullName);

        FirebaseStorage.getInstance().getReference().child("images/" + otherUser).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(image);
                        image.setVisibility(View.VISIBLE);
                    }
                });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(username, otherUser, message.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        getMessages();
        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(adapter);
        checkMessages();
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
                                    m.setFullname(fullName);
                                    messageList.add(m);
                                    Collections.sort(messageList, new Comparator<Message>() {
                                        public int compare(Message o1, Message o2) {
                                            return o1.getDateSent().compareTo(o2.getDateSent());
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(messageList.size() - 1);
                                    Log.d(TAG, document.getId() + " From=> " + document.getData());
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
                                    m.setFullname(fullName);
                                    messageList.add(m);
                                    Collections.sort(messageList, new Comparator<Message>() {
                                        public int compare(Message o1, Message o2) {
                                            return o1.getDateSent().compareTo(o2.getDateSent());
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(messageList.size() - 1);
                                    Log.d(TAG, document.getId() + " To=> " + document.getData());
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void sendMessage(String from, String to, String m) {
        message.setText("");
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

    private void checkMessages() {
        db.collection("Messages")
            .whereEqualTo("To", username)
            .whereEqualTo("From", otherUser)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    for (QueryDocumentSnapshot doc : value) {
                        if (!messageIds.contains(doc.getId())) {
                            Message m = new Message();
                            messageIds.add(doc.getId());
                            m.setId(doc.getId());
                            m.setTo(doc.get("To").toString());
                            m.setFrom(doc.get("From").toString());
                            m.setMessage(doc.get("Message").toString());
                            m.setDateSent(doc.getTimestamp("DateSent"));
                            m.setType("from");
                            m.setFullname(fullName);
                            messageList.add(m);
                            Collections.sort(messageList, new Comparator<Message>() {
                                public int compare(Message o1, Message o2) {
                                    return o1.getDateSent().compareTo(o2.getDateSent());
                                }
                            });
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                }
            });
    }
}
