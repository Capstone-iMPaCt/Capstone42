package com.project.ilearncentral.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Posts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEditFeed extends AppCompatActivity {

    private String TAG = "AddEditFeed";
    private String buttonText = "Post";
    private ConstraintLayout imageLayout;
    private ImageView image;
    private ImageButton orientation;
    private CircleImageView changeImageButton;
    private Button postButton;
    private TextInputEditText titleInput, contentInput;
    private ImageHandler imageHandler;
    private ObservableString imageDone, postingDone;
    private boolean withImage, isUpdate, isPortrait = true;;
    private String title, content, postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_feed);

        bindHolders();

        final Intent i = getIntent();
        if (i.hasExtra("postId")) {
            postId = i.getStringExtra("postId");
            isUpdate = true;
        }

        if (!postId.isEmpty())
            setValues();

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHandler.selectImage();
            }
        });
        orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintSet set = new ConstraintSet();
                set.clone(imageLayout);
                if (isPortrait) {
                    set.setDimensionRatio(image.getId(), "2:1");
                    isPortrait = false;
                } else {
                    set.setDimensionRatio(image.getId(), "1");
                    isPortrait = true;
                }
                set.applyTo(imageLayout);
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkErrors()) {
                    Utility.buttonWait(postButton, true, "Posting...");
                    if (isUpdate) {
                        Posts.updatePost(postId, title, content, withImage, postingDone);
                    } else {
                        Posts.addPost(title, content, withImage, postingDone);
                    }
                }
            }
        });
        postingDone.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String id) {
                postId = id;
                if (withImage) {
                    imageHandler.uploadImage("posts", postId, image, imageDone);
                } else {
                    finishPost();
                }
            }
        });
        imageDone.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String uri) {
                finishPost();
            }
        });
    }

    private void setValues() {
        Map<String, Object> post = Posts.getPostById(postId);
        title = post.get("Title").toString();
        titleInput.setText(title);
        content = post.get("Content").toString();
        contentInput.setText(content);
        withImage = (boolean) post.get("Image");
        if (withImage) {
            FirebaseStorage.getInstance().getReference()
                    .child("posts").child(postId)
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri.toString()).into(image);
                }
            });
        }
    }

    private void finishPost() {
        Utility.buttonWait(postButton, false, buttonText);
        Intent i = new Intent();  // or // Intent i = getIntent()
        i.putExtra("postId", postId);
        setResult(RESULT_OK, i);
        finish();
    }

    private boolean checkErrors() {
        boolean valid = true;
        title = titleInput.getText().toString();
        content = contentInput.getText().toString();

        if (title.isEmpty()) {
            titleInput.setError("Title is empty.");
            valid = false;
        }
        if (content.isEmpty()) {
            contentInput.setError("Content is empty.");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        withImage = imageHandler
                .onActivityResult(requestCode, resultCode, data, null, image, "");
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        imageHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void bindHolders() {
        imageLayout = findViewById(R.id.feed_add_image_layout);
        image = findViewById(R.id.feed_add_image);
        orientation = findViewById(R.id.feed_add_orient_image);
        changeImageButton = findViewById(R.id.feed_add_change_image);
        postButton = findViewById(R.id.feed_add_button);
        titleInput = findViewById(R.id.feed_add_title);
        contentInput = findViewById(R.id.feed_add_content);

        imageHandler = new ImageHandler(this, AddEditFeed.this);
        imageDone = new ObservableString();
        postingDone = new ObservableString();
        withImage = isUpdate = false;
        title = content = postId = "";
    }
}
