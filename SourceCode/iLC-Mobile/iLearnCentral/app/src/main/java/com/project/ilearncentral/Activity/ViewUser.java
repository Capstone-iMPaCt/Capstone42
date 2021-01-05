package com.project.ilearncentral.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUser extends AppCompatActivity {

    private final String TAG = "ViewUser";
    private CircleImageView imageView;
    private CardView aboutMe;
    private TextView usernameView, expertiseView, addressOutput,
            birthdateOutput, religionOutput, citizenshipOutput, maritalStatusOutput,
            following, followers, rating;
    private Button chat, follow;

    private Map<String, Object> userData;
    private String username;
    private User user;

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
        if (i.hasExtra("USERNAME")) {
            username = i.getStringExtra("USERNAME");
            user = User.getUserByUsername(username);
        }
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
        followers = findViewById(R.id.view_user_followers);
        following = findViewById(R.id.view_user_following);
        rating = findViewById(R.id.view_user_rating);

        follow = findViewById(R.id.view_user_follow);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().toString().equalsIgnoreCase("Follow")) {
                    user.addFollower(Account.me.getUsername());
                    Account.me.addFollowing(username);
                    User.getUserByUsername(Account.getUsername()).addFollowing(username);
                    Utility.follow(user);
                    follow.setText("Unfollow");
                    followers.setText(Utility.processCount(user.getFollowers()));

                } else {
                    user.getFollowers().remove(Account.me.getUsername());
                    User.getUserByUsername(Account.me.getUsername()).getFollowing().remove(username);
                    Account.me.getFollowing().remove(username);
                    Utility.unfollow(user);
                    follow.setText("Follow");
                    followers.setText(Utility.processCount(user.getFollowers()));
                }
            }
        });
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
                final Dialog ratingDialog = new Dialog(ViewUser.this, R.style.FullHeightDialog);
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.setCancelable(true);
                final RatingBar ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setRating(Float.parseFloat(user.getRating() + ""));

                TextView text = (TextView) ratingDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(user.getFullname());

                Button updateButton = (Button) ratingDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.addRating(Account.getUsername(), (int) ratingBar.getRating());
                        rating.setText(user.getRating() + "");
                        Utility.rate(user);
                        ratingDialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                ratingDialog.show();
            }
        });
        if (Account.getUsername().equals(username)) {
            follow.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
    }

    private void setValues() {
        usernameView.setText(userData.get("FullName").toString());
        if (!getString("Image").isEmpty()) {
            Picasso.get().load(Uri.parse(getString("Image"))).error(R.drawable.avatar_boy).fit().centerCrop().into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.viewImage(ViewUser.this, Uri.parse(getString("Image")));
                }
            });
        }
        expertiseView.setText(getString("AccountType"));
        if (Account.me.isFollowing(user.getUsername())) {
            follow.setText("Unfollow");
        }
        followers.setText(Utility.processCount(user.getFollowers()));
        following.setText(Utility.processCount(user.getFollowing()));
        rating.setText(user.getRating() + "");
        try {
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
            if (address.length() > 1 && address.charAt(0) == ',')
                address = address.substring(1);
            address.replaceAll("\\s", " ");
            userData.put("Address", address.trim());
        } catch (Exception e) {
            userData.put("Address", "");
        }

        addressOutput.setText(getString("Address"));
        birthdateOutput.setText(Utility.getDateStringFromTimestamp((Timestamp) userData.get("Birthday")));
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
                                                        String fullname = Utility.formatFullName(name.get("FirstName") + "", name.get("MiddleName") + "", name.get("LastName") + "");
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
