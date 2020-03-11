package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewUser extends AppCompatActivity {

    private final String TAG = "ViewUser";
    private CircleImageView imageView;
    private CardView aboutMe;
    private TextView usernameView, expertiseView, addressOutput,
            birthdateOutput, religionOutput, citizenshipOutput, maritalStatusOutput;
    private Button chat, follow;

    private Map<String, Object> userData;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_user_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userData = new HashMap<>();

        Intent i = getIntent();
        if (i.hasExtra("USERNAME"))
            username = i.getStringExtra("USERNAME");
        retrieveUserFromDB();

        imageView = findViewById(R.id.view_user_image);
        usernameView = findViewById(R.id.view_user_full_name);
        expertiseView = findViewById(R.id.view_user_expertise);
        addressOutput = findViewById(R.id.aboutme_address);
        birthdateOutput = findViewById(R.id.aboutme_birthdate);
        religionOutput = findViewById(R.id.aboutme_religion);
        citizenshipOutput = findViewById(R.id.aboutme_citizenship);
        maritalStatusOutput = findViewById(R.id.aboutme_marital_status);
        aboutMe = findViewById(R.id.view_user_aboutme);

        chat = findViewById(R.id.view_user_chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewUser.this, Messages.class);
                intent.putExtra("USER_NAME", username);
                intent.putExtra("FULL_NAME", getString("FullName"));
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.view_user_rating_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setValues() {
        usernameView.setText(userData.get("FullName").toString());
        if (!getString("Image").isEmpty())
            Picasso.get().load(Uri.parse(getString("Image"))).error(R.drawable.avatar_boy).fit().into(imageView);
        expertiseView.setText(getString("AccountType"));

        Map<String, Object> addressMap = (Map<String, Object>) userData.get("Address");
        String address = "";
        if (addressMap.containsKey("HouseNo"))
            address += addressMap.get("HouseNo") + " ";
        if (addressMap.containsKey("Street"))
            address += " " + addressMap.get("Street");
        if (addressMap.containsKey("Barangay"))
            address += ", " + addressMap.get("Barangay");
        if (addressMap.containsKey("City"))
            address += ", " + addressMap.get("City");
        if (addressMap.containsKey("District"))
            address += ", " + addressMap.get("District");
        if (addressMap.containsKey("Province"))
            address += ", " + addressMap.get("Province");
        if (addressMap.containsKey("Country"))
            address += ", " + addressMap.get("Country");
        if (addressMap.containsKey("ZipCode"))
            address += ", " + addressMap.get("ZipCode");
        if (address.length()>1 && address.charAt(0)==',')
            address = address.substring(1);
        address.replaceAll("\\s", " ");
        userData.put("Address", address.trim());

        addressOutput.setText(getString("Address"));
        birthdateOutput.setText(Utility.getStringFromDate((Timestamp)userData.get("Birthday")));
        religionOutput.setText(getString("Religion"));
        citizenshipOutput.setText(getString("Citizenship"));
        maritalStatusOutput.setText(getString("MaritalStatus"));
        aboutMe.setVisibility(View.VISIBLE);
    }

    private String getString(String key) {
        if (userData.containsKey(key))
            return userData.get(key).toString();
        return "";
    }

    public void retrieveUserFromDB() {
        FirebaseFirestore.getInstance().collection("User")
            .whereEqualTo("Username", username)
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
                            userData.putAll(document.getData());
                            if (document.get("Image") == null) userData.put("Image", "");
                            FirebaseFirestore.getInstance().collection(collection)
                                .whereEqualTo("Username", username)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                userData.putAll(document.getData());
                                                Map<String, Object> name = (Map<String, Object>) userData.get("Name");
                                                String fullname = Utility.formatFullName(name.get("FirstName")+"", name.get("MiddleName")+"", name.get("LastName")+"");
                                                userData.put("FullName", fullname);
                                            }
                                            setValues();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
        }
        return true;
    }
}
