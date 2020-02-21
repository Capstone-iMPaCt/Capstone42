package com.project.ilearncentral.Activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class ResumeAdd extends AppCompatActivity implements View.OnClickListener  {

    private View educationalBackgroundView, employmentHistoryView, skillsView;
    private ViewGroup educationalBackgroundParent, employmentHistoryParent, skillsParent;
    private LinearLayout educationalBackgroundLayout, employmentHistoryLayout, skillsLayout;
    private List<Button> educationalBackgroundDeleteButton, employmentHistoryDeleteButton,
            skillsDeleteButton, awardsDeleteButton, qualitiesDeleteButton, interestsDeleteButton,
            referencesDeleteButton;
    private List<EditText> schoolName, schoolAddress, schoolYear, employerName,
            employerAddress, employmentPeriod, skills;
    private ImageButton educationalBackgroundAddButton, employmentHistoryAddButton, skillsAddbutton,
            awardsAddButton, qualitiesAddbutton, interestsAddButton, referencesAddButton;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_resume);

//        title = findViewById(R.id.resume_title);

        educationalBackgroundAddButton = findViewById(R.id.resume_add_educational_background_addbutton);
        educationalBackgroundLayout = findViewById(R.id.resume_add_employment_history_layout);
        educationalBackgroundView = findViewById(R.id.resume_add_employment_history_view);
        educationalBackgroundParent = (ViewGroup) educationalBackgroundView.getParent();
        educationalBackgroundDeleteButton = new ArrayList<>();
        schoolName = new ArrayList<>();
        schoolAddress = new ArrayList<>();
        schoolYear = new ArrayList<>();

//        employmentHistoryAddButton = findViewById(R.id.add_update_resume_employment_history_addbutton);
//        employmentHistoryLayout = findViewById(R.id.add_update_resume_employment_history_layout);
//        employmentHistoryView = findViewById(R.id.add_update_resume_employment_history_view);
//        employmentHistoryParent = (ViewGroup) employmentHistoryView.getParent();
//        employmentHistoryDeleteButton = new ArrayList<>();
//        employerName = new ArrayList<>();
//        employerAddress = new ArrayList<>();
//        employmentPeriod = new ArrayList<>();

//        skillsAddbutton = findViewById(R.id.add_update_resume_skills_addbutton);
//        skillsLayout = findViewById(R.id.add_update_resume_skills_layout);
//        skillsView = findViewById(R.id.add_update_resume_skills_view);
//        skillsParent = (ViewGroup) skillsView.getParent();
//        skillsDeleteButton = new ArrayList<>();
//        skills = new ArrayList<>();
//
//        awardsAddButton = findViewById(R.id.add_update_resume_awards_addbutton);
//        qualitiesAddbutton = findViewById(R.id.add_update_resume_qualities_addbutton);
//        interestsAddButton = findViewById(R.id.add_update_resume_interests_addbutton);
//        referencesAddButton = findViewById(R.id.add_update_resume_references_addbutton);

        educationalBackgroundAddButton.setOnClickListener(this);
//        employmentHistoryAddButton.setOnClickListener(this);
//        skillsAddbutton.setOnClickListener(this);
//        awardsAddButton.setOnClickListener(this);
//        qualitiesAddbutton.setOnClickListener(this);
//        interestsAddButton.setOnClickListener(this);
//        referencesAddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == educationalBackgroundAddButton.getId()) {
            setDeleteButton(educationalBackgroundLayout, educationalBackgroundDeleteButton);
            setEditText(educationalBackgroundLayout, schoolName, "School Name");
            setEditText(educationalBackgroundLayout, schoolAddress, "School Address");
            setEditText(educationalBackgroundLayout, schoolYear, "School Year");
            for (Button btn : educationalBackgroundDeleteButton)
                btn.setOnClickListener(this);
        } else if (v.getId() == employmentHistoryAddButton.getId()) {
            setDeleteButton(employmentHistoryLayout, employmentHistoryDeleteButton);
            setEditText(employmentHistoryLayout, employerName, "Employer Name");
            setEditText(employmentHistoryLayout, employerAddress, "Employer Address");
            setEditText(employmentHistoryLayout, employmentPeriod, "Employment Period");
            for (Button btn : employmentHistoryDeleteButton)
                btn.setOnClickListener(this);
        } else if (v.getId() == skillsAddbutton.getId()) {
            setDeleteButton(skillsLayout, skillsDeleteButton);
            setEditText(skillsLayout, skills, "Skill");
            for (Button btn : skillsDeleteButton)
                btn.setOnClickListener(this);
        } else if (educationalBackgroundDeleteButton.contains(v)) {
            int index = educationalBackgroundDeleteButton.indexOf(v);
            educationalBackgroundParent.removeView(educationalBackgroundDeleteButton.get(index));
            educationalBackgroundDeleteButton.remove(index);
            educationalBackgroundParent.removeView(schoolName.get(index));
            schoolName.remove(index);
            educationalBackgroundParent.removeView(schoolAddress.get(index));
            schoolAddress.remove(index);
            educationalBackgroundParent.removeView(schoolYear.get(index));
            schoolYear.remove(index);
        } else if (employmentHistoryDeleteButton.contains(v)) {
            int index = employmentHistoryDeleteButton.indexOf(v);
            employmentHistoryParent.removeView(employmentHistoryDeleteButton.get(index));
            employmentHistoryDeleteButton.remove(index);
            employmentHistoryParent.removeView(employerName.get(index));
            employerName.remove(index);
            employmentHistoryParent.removeView(employerAddress.get(index));
            employerAddress.remove(index);
            employmentHistoryParent.removeView(employmentPeriod.get(index));
            employmentPeriod.remove(index);
        } else if (skillsDeleteButton.contains(v)) {
            int index = skillsDeleteButton.indexOf(v);
            skillsParent.removeView(skillsDeleteButton.get(index));
            skillsDeleteButton.remove(index);
            skillsParent.removeView(skills.get(index));
            skills.remove(index);
        }
    }

    private Button setDeleteButton(LinearLayout layout, List<Button> buttons) {
        int index = buttons.size();
        buttons.add(new Button(getApplicationContext()));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utility.dpToPx(getApplicationContext(),70), Utility.dpToPx(getApplicationContext(),70));
        layoutParams.gravity = Gravity.END;
        buttons.get(index).setLayoutParams(layoutParams);
        buttons.get(index).setBackgroundResource(R.drawable.ic_remove_circle_mint_50dp);
        layoutParams.setMargins(0, Utility.dpToPx(getApplicationContext(),16), 0, 0);
        layout.addView(buttons.get(index));
        return buttons.get(index);
    }

    private EditText setEditText(LinearLayout layout, List<EditText> editTexts, String hint) {
        int index = editTexts.size();
        editTexts.add(new EditText(getApplicationContext()));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTexts.get(index).setLayoutParams(layoutParams);
        editTexts.get(index).setHint(hint);
        editTexts.get(index).setTextSize(16);
        editTexts.get(index).setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        editTexts.get(index).setHintTextColor(this.getResources().getColor(R.color.light_gray));
        editTexts.get(index).setHighlightColor(this.getResources().getColor(R.color.mint_blue));
        editTexts.get(index).getBackground().setColorFilter(getApplication().getResources().getColor(R.color.dark_gray), PorterDuff.Mode.SRC_ATOP);
        layout.addView(editTexts.get(index));
        return editTexts.get(index);
    }
}
