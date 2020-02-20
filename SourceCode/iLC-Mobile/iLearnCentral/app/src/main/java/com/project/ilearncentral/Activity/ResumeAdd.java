package com.project.ilearncentral.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.R;

public class ResumeAdd extends AppCompatActivity {

    private View educationalBackgroundView;
    private ViewGroup parent;
    private LinearLayout educationalBackgroundLayout;

    private ImageButton educationalBackgroundAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_add);

        educationalBackgroundAddButton = findViewById(R.id.resume_add_educational_background_addbutton);
        educationalBackgroundView = findViewById(R.id.resume_add_employment_history_view);
        parent = (ViewGroup) educationalBackgroundView.getParent();
        parent.removeView(educationalBackgroundView);

        educationalBackgroundLayout = findViewById(R.id.resume_add_employment_history_layout);
        educationalBackgroundLayout.removeAllViews();
//        educationalBackgroundLayout.setOrientation(LinearLayout.VERTICAL);
//        educationalBackgroundLayout.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));


        educationalBackgroundAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditText(educationalBackgroundLayout, getApplicationContext(), "School Name");
                setEditText(educationalBackgroundLayout, getApplicationContext(), "School Address");
                setEditText(educationalBackgroundLayout, getApplicationContext(), "School Year");
            }
        });
    }

    private EditText setEditText(LinearLayout layout, Context context, String hint) {
        EditText editText = new EditText(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParams);
        editText.setHint(hint);
        editText.setTextSize(14);
        layout.addView(editText);
        return editText;
    }

    private int pxToDp(int px) {
        return (int) (px * Resources.getSystem().getDisplayMetrics().density);
    }

    private int dpToPx(int dp) {
        return (int) (dp / Resources.getSystem().getDisplayMetrics().density);
    }
}
