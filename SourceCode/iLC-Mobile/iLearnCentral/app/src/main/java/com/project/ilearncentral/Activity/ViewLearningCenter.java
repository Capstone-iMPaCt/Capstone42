package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.List;
import java.util.Map;

public class ViewLearningCenter extends AppCompatActivity {

    ImageView logo;
    Button message, follow, courses;
    LearningCenter lc;
    TextView name, followers, following, rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_learning_center);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_center_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logo = (ImageView) findViewById(R.id.view_center_image);
        follow = (Button) findViewById(R.id.view_center_follow_button);
        message = (Button) findViewById(R.id.view_center_message_button);
        courses = (Button) findViewById(R.id.view_center_courses_button);
        name = (TextView) findViewById(R.id.view_center_full_name);

        lc = LearningCenter.getLCById(Account.getStringData("openLC"));

        if (lc == null) {
            setResult(RESULT_CANCELED);
            this.finish();
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().toString().equalsIgnoreCase("Follow")) {
//                    user.addFollower(Account.getUsername());
//                    Account.me.addFollowing(username);
//                    User.getUserByUsername(Account.getUsername()).addFollowing(username);
//                    Utility.follow(user);
//                    followers.setText(Utility.processCount(user.getFollowers()));
//                    follow.setText("Unfollow");

//                } else {
//                    user.getFollowers().remove(Account.getUsername());
//                    User.getUserByUsername(Account.getUsername()).getFollowing().remove(username);
//                    Account.me.getFollowing().remove(username);
//                    Utility.unfollow(user);
//                    follow.setText("Follow");
//                    followers.setText(Utility.processCount(user.getFollowers()));
                }
            }
        });
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewLearningCenter.this, Courses.class));
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> acc = lc.getAccounts();
                for (int i = 0; i < acc.size(); i++) {
                    Map<String, String> data = acc.get(i);
                    if (data.get("AccessLevel").equalsIgnoreCase("administrator") &&
                            data.get("Status").equalsIgnoreCase("active")) {
                        Intent intent = new Intent(ViewLearningCenter.this, Messages.class);
                        intent.putExtra("USER_NAME", data.get("Username"));
                        intent.putExtra("FULL_NAME", User.getFullnameByUsername(data.get("Username")));
                        startActivity(intent);
                    }
                }
            }
        });

        if (!lc.getLogo().isEmpty())
            Glide.with(this).load(lc.getLogo()).error(R.drawable.logo_icon)
                    .apply(new RequestOptions().override(Utility.getScreenWidth(),
                            Utility.dpToPx(this, 256))).centerCrop().into(logo);
        name.setText(lc.getBusinessName());
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
