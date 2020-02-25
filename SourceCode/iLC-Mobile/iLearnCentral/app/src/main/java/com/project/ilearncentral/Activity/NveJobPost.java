package com.project.ilearncentral.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ilearncentral.Adapter.AddUpdateResumeSingleListAdapter;
import com.project.ilearncentral.Adapter.JobPostEducListAdapter;
import com.project.ilearncentral.Adapter.JobPostSingleListAdapter;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.Model.ResumeItem;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Resume;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NveJobPost extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddEditFeed";
    private String buttonText = "Post";
    private String jobId;
    private JobVacancy job;
    public boolean isView;
    private ObservableString doneUpload;
    private int nve;

    private ImageView logo;
    private TextView name, address, website, email, contact,
        educLabel, qualLabel, respLabel, skillLabel, appmLabel;
    private TextInputEditText position, description;
    private CheckBox full, part, contract;
    private RecyclerView educRecycler, qualRecycler, respRecycler, skillRecycler, appmRecycler;
    private JobPostEducListAdapter educAdapter;
    private JobPostSingleListAdapter qualAdapter, respAdapter, skillAdapter, appmAdapter;
    private ImageButton educAdd, educDel, qualAdd, qualDel,
            respAdd, respDel, skillAdd, skillDel, appmAdd, appmDel;
    private LinearLayout educEdit, qualEdit, respEdit, skillEdit, appmEdit;
    private List<Map<String, Object>> educList;
    private List<String> qualList, respList, skillList, appmList;
    private Button confirm;
    private Switch status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nve_job_post);

        bindLayout();

        Intent i = getIntent();
        if (i.hasExtra("jobId")) {
            jobId = i.getStringExtra("jobId");
            isView = i.getBooleanExtra("View", false);
            job = JobPosts.getJobPostById(jobId);
            if (isView) nve = 0;
            else nve = -1;
        } else
            nve = 1;
        setValues();

        doneUpload.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String jobId) {
                Utility.buttonWait(confirm, false, "Confirm");
                if (jobId.isEmpty()) {
                }
                else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("jobId", jobId);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    private void bindLayout() {
        logo = findViewById(R.id.job_post_nve_logo);
        name = findViewById(R.id.job_post_nve_name);
        address = findViewById(R.id.job_post_nve_address);
        website = findViewById(R.id.job_post_nve_website);
        email = findViewById(R.id.job_post_nve_email);
        contact = findViewById(R.id.job_post_nve_number);
        confirm = findViewById(R.id.job_post_nve_confirm);
        position = findViewById(R.id.job_post_nve_position);
        description = findViewById(R.id.job_post_nve_description);
        full = findViewById(R.id.job_post_nve_fulltime);
        part = findViewById(R.id.job_post_nve_parttime);
        contract = findViewById(R.id.job_post_nve_contractual);
        educLabel = findViewById(R.id.job_post_nve_educ_label);
        qualLabel = findViewById(R.id.job_post_nve_qual_label);
        respLabel = findViewById(R.id.job_post_nve_resp_label);
        skillLabel = findViewById(R.id.job_post_nve_skill_label);
        appmLabel = findViewById(R.id.job_post_nve_appm_label);
        educEdit = findViewById(R.id.job_post_nve_educ_edit);
        qualEdit = findViewById(R.id.job_post_nve_qual_edit);
        respEdit = findViewById(R.id.job_post_nve_resp_edit);
        skillEdit = findViewById(R.id.job_post_nve_skill_edit);
        appmEdit = findViewById(R.id.job_post_nve_appm_edit);
        educAdd = findViewById(R.id.job_post_nve_educ_add);
        educDel = findViewById(R.id.job_post_nve_educ_remove);
        qualAdd = findViewById(R.id.job_post_nve_qual_add);
        qualDel = findViewById(R.id.job_post_nve_qual_remove);
        respAdd = findViewById(R.id.job_post_nve_resp_add);
        respDel = findViewById(R.id.job_post_nve_resp_remove);
        skillAdd = findViewById(R.id.job_post_nve_skill_add);
        skillDel = findViewById(R.id.job_post_nve_skill_remove);
        appmAdd = findViewById(R.id.job_post_nve_appm_add);
        appmDel = findViewById(R.id.job_post_nve_appm_remove);
        educRecycler = findViewById(R.id.job_post_nve_educ_recycler);
        qualRecycler = findViewById(R.id.job_post_nve_qual_recycler);
        respRecycler = findViewById(R.id.job_post_nve_resp_recycler);
        skillRecycler = findViewById(R.id.job_post_nve_skill_recycler);
        appmRecycler = findViewById(R.id.job_post_nve_appm_recycler);
        status = findViewById(R.id.job_post_nve_status);

        educList = new ArrayList<>();
        qualList = new ArrayList<>();
        respList = new ArrayList<>();
        skillList = new ArrayList<>();
        appmList = new ArrayList<>();
        doneUpload = new ObservableString();

    }

    private void setValues() {
        if (nve<=0) {
            FirebaseStorage.getInstance().getReference()
                    .child("images/").child(job.getCenterId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri.toString()).error(getDrawable(R.drawable.logo_icon)).into(logo);
                }
            });
            status.setVisibility(View.VISIBLE);
            status.setChecked(job.getStatus().equalsIgnoreCase("open"));
            name.setText(job.getBusinessData().get("BusinessName"));
            website.setText(job.getBusinessData().get("CompanyWebsite"));
            address.setText(job.getBusinessData().get("BusinessAddress"));
            email.setText(job.getBusinessData().get("ContactEmail"));
            contact.setText(job.getBusinessData().get("ContactNumber"));
            if (website.getText().toString().isEmpty())
                website.setVisibility(View.GONE);
            if (address.getText().toString().isEmpty())
                address.setVisibility(View.GONE);
            if (email.getText().toString().isEmpty())
                email.setVisibility(View.GONE);
            if (contact.getText().toString().isEmpty())
                contact.setVisibility(View.GONE);

            educList = Utility.cloneMapList(job.getEducationalRequirements());
            qualList = Utility.cloneStringList(job.getQualification());
            respList = Utility.cloneStringList(job.getResponsibilities());
            skillList = Utility.cloneStringList(job.getSkills());
            appmList = Utility.cloneStringList(job.getApplicationMethods());
            for (int i=0; i<job.getJobTypes().size();i++) {
                if (job.getJobTypes().get(i).equalsIgnoreCase("full time"))
                    full.setChecked(true);
                else if (job.getJobTypes().get(i).equalsIgnoreCase("part time"))
                    part.setChecked(true);
                else if (job.getJobTypes().get(i).equalsIgnoreCase("contractual"))
                    contract.setChecked(true);
            }
            position.setText(job.getPosition());
            description.setText(job.getJobDescription());

            if (isView) {
                status.setVisibility(View.GONE);
                position.setFocusable(false);
                description.setFocusable(false);
                if (!full.isChecked()) full.setVisibility(View.GONE);
                else full.setClickable(false);
                if (!part.isChecked()) part.setVisibility(View.GONE);
                else part.setClickable(false);
                if (!contract.isChecked()) contract.setVisibility(View.GONE);
                else contract.setClickable(false);
                if (educList.isEmpty()) {
                    educLabel.setVisibility(View.GONE);
                    educRecycler.setVisibility(View.GONE);
                }
                if (qualList.isEmpty()) {
                    qualLabel.setVisibility(View.GONE);
                    qualRecycler.setVisibility(View.GONE);
                }
                if (respList.isEmpty()) {
                    respLabel.setVisibility(View.GONE);
                    respRecycler.setVisibility(View.GONE);
                }
                if (skillList.isEmpty()) {
                    skillLabel.setVisibility(View.GONE);
                    skillRecycler.setVisibility(View.GONE);
                }
                if (appmList.isEmpty()) {
                    appmLabel.setVisibility(View.GONE);
                    appmRecycler.setVisibility(View.GONE);
                }
                educEdit.setVisibility(View.GONE);
                qualEdit.setVisibility(View.GONE);
                skillEdit.setVisibility(View.GONE);
                respEdit.setVisibility(View.GONE);
                appmEdit.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
            } else {
                if(job.getStatus().isEmpty())
                    status.setChecked(true);
                else {
                    if (job.getStatus().equalsIgnoreCase("close"))
                        status.setChecked(true);
                    else status.setChecked(false);
                }
            }
        } else {
            status.setVisibility(View.GONE);
            job = new JobVacancy();
        }
        if (!isView) setButtonClickListeners();

        educRecycler.setLayoutManager(new LinearLayoutManager(this));
        educAdapter = new JobPostEducListAdapter(this, educList, isView);
        educRecycler.setAdapter(educAdapter);
        qualRecycler.setLayoutManager(new LinearLayoutManager(this));
        qualAdapter = new JobPostSingleListAdapter(this, qualList, "Qualification", isView);
        qualRecycler.setAdapter(qualAdapter);
        respRecycler.setLayoutManager(new LinearLayoutManager(this));
        respAdapter = new JobPostSingleListAdapter(this, respList, "Responsibility", isView);
        respRecycler.setAdapter(respAdapter);
        skillRecycler.setLayoutManager(new LinearLayoutManager(this));
        skillAdapter = new JobPostSingleListAdapter(this, skillList, "Skill", isView);
        skillRecycler.setAdapter(skillAdapter);
        appmRecycler.setLayoutManager(new LinearLayoutManager(this));
        appmAdapter = new JobPostSingleListAdapter(this, appmList, "Application Method", isView);
        appmRecycler.setAdapter(appmAdapter);
    }

    private void setButtonClickListeners() {
        status.setChecked(true);
        educAdd.setOnClickListener(this);
        educDel.setOnClickListener(this);
        qualAdd.setOnClickListener(this);
        qualDel.setOnClickListener(this);
        respAdd.setOnClickListener(this);
        respDel.setOnClickListener(this);
        skillAdd.setOnClickListener(this);
        skillDel.setOnClickListener(this);
        appmAdd.setOnClickListener(this);
        appmDel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.job_post_nve_educ_add:
                Map <String, Object> educ = new HashMap<>();
                educ.put("Graduated", false);
                educ.put("EducationalLevel", "");
                educ.put("Degree", "");
                educ.put("Major", "");
                educ.put("MinimumUnits", "");
                educList.add(educ);
                educAdapter.notifyItemInserted(educList.size());
                break;
            case R.id.job_post_nve_educ_remove:
                if (!educList.isEmpty())
                    educList.remove(educList.size() - 1);
                educAdapter.notifyItemRemoved(educList.size());
                break;
            case R.id.job_post_nve_qual_add:
                qualList.add("");
                qualAdapter.notifyItemInserted(qualList.size());
                break;
            case R.id.job_post_nve_qual_remove:
                if (!qualList.isEmpty())
                    qualList.remove(qualList.size() - 1);
                qualAdapter.notifyItemRemoved(qualList.size());
                break;
            case R.id.job_post_nve_resp_add:
                respList.add("");
                respAdapter.notifyItemInserted(respList.size());
                break;
            case R.id.job_post_nve_resp_remove:
                if (!respList.isEmpty())
                    respList.remove(respList.size() - 1);
                respAdapter.notifyItemRemoved(respList.size());
                break;
            case R.id.job_post_nve_skill_add:
                skillList.add("");
                skillAdapter.notifyItemInserted(skillList.size());
                break;
            case R.id.job_post_nve_skill_remove:
                if (!skillList.isEmpty())
                    skillList.remove(skillList.size() - 1);
                skillAdapter.notifyItemRemoved(skillList.size());
                break;
            case R.id.job_post_nve_appm_add:
                appmList.add("");
                appmAdapter.notifyItemInserted(appmList.size());
                break;
            case R.id.job_post_nve_appm_remove:
                if (!appmList.isEmpty())
                    appmList.remove(appmList.size() - 1);
                appmAdapter.notifyItemRemoved(appmList.size());
                break;
            case R.id.job_post_nve_confirm:
                Utility.buttonWait(confirm, true);
                job.setEducationalRequirements(educList);
                job.setQualification(qualList);
                job.setResponsibilities(respList);
                job.setSkills(skillList);
                job.setApplicationMethods(appmList);
                job.setPosition(position.getText().toString());
                job.setJobDescription(description.getText().toString());
                job.getJobTypes().clear();
                if (full.isChecked())
                    job.getJobTypes().add("Full Time");
                if (part.isChecked())
                    job.getJobTypes().add("Part Time");
                if (contract.isChecked())
                    job.getJobTypes().add("Contractual");
                if (nve == 1)
                    JobPosts.addPostToDB(job, doneUpload);
                else {
                    if (status.isChecked())
                        job.setStatus("open");
                    else
                        job.setStatus("close");
                    JobPosts.updatePostToDB(jobId, job, doneUpload);
                }
                break;
        }
    }
}
