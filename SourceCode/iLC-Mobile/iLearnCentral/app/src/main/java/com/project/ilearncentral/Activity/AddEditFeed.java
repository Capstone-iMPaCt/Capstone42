package com.project.ilearncentral.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Posts;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class AddEditFeed extends AppCompatActivity {

    private String TAG = "AddEditFeed";
    private String buttonText = "Post";
    private ImageView image;
    private CircleImageView changeImageButton;
    private Button postButton;
    private TextInputEditText titleInput, contentInput;
    private ImageHandler imageHandler;
    private ObservableString imageDone, postingDone;
    private boolean withImage, isUpdate;
    private String title, content, postId;
    private LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_feed);

        bindHolders();

        Intent i = getIntent();
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
                if(withImage) {
                    imageHandler.uploadImage("posts", postId, imageDone);
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
        ConstraintLayout imageLayout = findViewById(R.id.feed_add_image_layout);
        image = findViewById(R.id.feed_add_image);
        changeImageButton = findViewById(R.id.feed_add_change_image);
        postButton = findViewById(R.id.feed_add_button);
        titleInput = findViewById(R.id.feed_add_title);
        contentInput = findViewById(R.id.feed_add_content);
//
//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth(); // ((display.getWidth()*20)/100)
//        int height = display.getHeight();// ((display.getHeight()*30)/100)
//        System.out.println(">>>>>>>>>>>>>>>>"+display.getWidth()+">>>>>>>>"+display.getWidth()*2/3);
//        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(display.getWidth(), display.getWidth()*2/3);
//        image.setLayoutParams(layoutParams);
//        image.requestLayout();

        imageHandler = new ImageHandler(this, AddEditFeed.this);
        imageDone = new ObservableString();
        postingDone = new ObservableString();
        withImage = isUpdate = false;
        title = content = postId = "";
    }
}
