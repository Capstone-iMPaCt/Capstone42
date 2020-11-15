package com.project.ilearncentral.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        followers = (TextView) findViewById(R.id.learning_center_followers);
        following = (TextView) findViewById(R.id.learning_center_following);
        rating = (TextView) findViewById(R.id.learning_center_rating);

        lc = LearningCenter.getLCById(Account.getStringData("openLC"));

        if (lc == null) {
            setResult(RESULT_CANCELED);
            this.finish();
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().toString().equalsIgnoreCase("Follow")) {
                    System.out.println(Account.me.getUsername() + " follows " + lc.getCenterId());
                    lc.addFollower(Account.me.getUsername());
                    Account.me.addFollowing(lc.getCenterId());
                    User.getUserByUsername(Account.getUsername()).addFollowing(lc.getCenterId());
                    Utility.follow(lc);
                    follow.setText("Unfollow");
                    followers.setText(Utility.processCount(lc.getFollowers()));

                } else {
                    lc.getFollowers().remove(Account.me.getUsername());
                    User.getUserByUsername(Account.me.getUsername()).getFollowing().remove(lc.getCenterId());
                    Account.me.getFollowing().remove(lc.getCenterId());
                    Utility.unfollow(lc);
                    follow.setText("Follow");
                    followers.setText(Utility.processCount(lc.getFollowers()));
                }
            }
        });
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewLearningCenter.this, ViewCourses.class));
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.view_center_rating_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog ratingDialog = new Dialog(ViewLearningCenter.this, R.style.FullHeightDialog);
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.setCancelable(true);
                final RatingBar ratingBar = (RatingBar)ratingDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setRating(Float.parseFloat(lc.getRating()+""));

                TextView text = (TextView) ratingDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(lc.getBusinessName());

                Button updateButton = (Button) ratingDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lc.addRating(Account.getUsername(), (int)ratingBar.getRating());
                        rating.setText(lc.getRating()+"");
                        Utility.rate(lc);
                        ratingDialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                ratingDialog.show();
            }
        });

        if (lc.getLogo() != null && !lc.getLogo().isEmpty())
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Account.removeData("openLC");
    }
}
