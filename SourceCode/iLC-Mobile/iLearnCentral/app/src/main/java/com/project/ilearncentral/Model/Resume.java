package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.MyClass.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Resume {
    private static String TAG = "Resume";

    private String username;
    private String resumeId;
    private String objective;
    private List<ResumeItem> awards;
    private List<ResumeItem> educationalBackground;
    private List<ResumeItem> employmentHistory;
    private List<ResumeItem> interest;
    private List<ResumeItem> qualities;
    private List<ResumeItem> references;
    private List<ResumeItem> skills;

    public enum ResumeItemType {Award, Education, Employment, Interest, Qualities, Reference, Skill};

    public Resume() {
        username = "";
        reset();
    }
    public Resume(String username) {
        this.username = username;
        reset();
    }

    public void reset() {
        resumeId = "";
        objective = "";
        if (awards==null) awards = new ArrayList<>();
        else awards.clear();
        if (educationalBackground==null) educationalBackground = new ArrayList<>();
        else educationalBackground.clear();
        if (employmentHistory==null) employmentHistory = new ArrayList<>();
        else employmentHistory.clear();
        if (interest==null) interest = new ArrayList<>();
        else interest.clear();
        if (qualities==null) qualities = new ArrayList<>();
        else qualities.clear();
        if (references==null) references = new ArrayList<>();
        else references.clear();
        if (skills==null) skills = new ArrayList<>();
        else skills.clear();
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setId(String id) {
        this.resumeId = id;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public List<ResumeItem> getEducationalBackground() {
        return educationalBackground;
    }

    public void setEducationalBackground(List<ResumeItem> educationalBackground) {
        this.educationalBackground = educationalBackground;
    }

    public List<ResumeItem> getEmploymentHistory() {
        return employmentHistory;
    }

    public void setEmploymentHistory(List<ResumeItem> employmentHistory) {
        this.employmentHistory = employmentHistory;
    }

    public List<ResumeItem> getInterest() {
        return interest;
    }

    public void setInterest(List<ResumeItem> interest) {
        this.interest = interest;
    }

    public List<ResumeItem> getQualities() {
        return qualities;
    }

    public void setQualities(List<ResumeItem> qualities) {
        this.qualities = qualities;
    }

    public List<ResumeItem> getReferences() {
        return references;
    }

    public void setReferences(List<ResumeItem> references) {
        this.references = references;
    }

    public List<ResumeItem> getSkills() {
        return skills;
    }

    public void setSkills(List<ResumeItem> skills) {
        this.skills = skills;
    }

    public List<ResumeItem> getAwards() {
        return awards;
    }

    public void setAwards(List<ResumeItem> awards) {
        this.awards = awards;
    }

    public static Resume getResumeFromDB(String username, final ObservableBoolean done) {
        if (username == null || username.isEmpty())
            username = Account.getUsername();
        final Resume resume = new Resume(username);
        FirebaseFirestore.getInstance().collection("Resume").whereEqualTo("Username", username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        resume.setId(document.getId());
                        setData(resume, document.getData());
                        if (done!=null) done.set(true);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    if (done!=null) done.set(false);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return resume;
    }

    public void addResume(final ObservableString done) {
        FirebaseFirestore.getInstance().collection("Resume")
                .add(this.getData())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (done != null) done.set(documentReference.getId());
                        resumeId = documentReference.getId();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resumeId = "";
                        if (done != null) done.set("");
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void updateResume(String givenId, final ObservableString done) {
        if (givenId == null || givenId.isEmpty()) givenId = resumeId;
        else resumeId = givenId;
        FirebaseFirestore.getInstance().collection("Resume").document(givenId)
                .set(this.getData())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (done != null) done.set("success");
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (done != null) done.set("fail");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private static List<Object> mapDataFromResumeItem(List<ResumeItem> items, ResumeItemType type) {
        List<Object> data = new ArrayList();
        for (ResumeItem item : items) {
            data.add(item.getData(type));
        }
        return data;
    }

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();

        data.put("Awards", mapDataFromResumeItem(awards, ResumeItemType.Award));
        data.put("CareerObjective", objective);
        data.put("EducationalBackground", mapDataFromResumeItem(educationalBackground, ResumeItemType.Education));
        data.put("EmploymentHistory", mapDataFromResumeItem(employmentHistory, ResumeItemType.Employment));
        data.put("Interests", mapDataFromResumeItem(interest, ResumeItemType.Interest));
        data.put("Qualities", mapDataFromResumeItem(qualities, ResumeItemType.Qualities));
        data.put("References", mapDataFromResumeItem(references, ResumeItemType.Reference));
        data.put("Skills", mapDataFromResumeItem(skills, ResumeItemType.Skill));
        data.put("Username", username);
        return data;
    }

    public static List<ResumeItem> mapDataToResumeItem(List<Object> data, ResumeItemType type) {
        List<ResumeItem> dataList = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                Map<String, Object> d;
                switch (type) {
                    case Award:
                    case Interest:
                    case Qualities:
                    case Skill:
                        dataList.add(new ResumeItem(data.get(i).toString()));
                        break;
                    case Education:
                        d = (Map<String, Object>) data.get(i);
                        dataList.add(new ResumeItem(
                                getMapStringData(d, "SchoolName"),
                                getMapStringData(d, "SchoolAddress"),
                                getMapStringData(d, "SchoolYear")));
                        break;
                    case Employment:
                        d = (Map<String, Object>) data.get(i);
                        dataList.add(new ResumeItem(
                                getMapStringData(d, "CompanyName"),
                                getMapStringData(d, "CompanyAddress"),
                                getMapStringData(d, "DatePeriod")));
                        break;
                    case Reference:
                        d = (Map<String, Object>) data.get(i);
                        dataList.add(new ResumeItem(
                                getMapStringData(d, "ReferenceName"),
                                getMapStringData(d, "Affiliation"),
                                getMapStringData(d, "Position"),
                                getMapStringData(d, "CompanyName")));
                        break;
                }
            }
        }
        return dataList;
    }

    private static String getMapStringData(Map<String, Object> map, String key) {
        if (map.containsKey(key))
            return map.get(key).toString();
        return "";
    }

    public static void setData(Resume resume, Map<String, Object> data) {
        resume.setObjective(data.get("CareerObjective").toString());
        resume.setAwards(mapDataToResumeItem((List<Object>) data.get("Awards"), ResumeItemType.Award));
        resume.setEducationalBackground(mapDataToResumeItem((List<Object>) data.get("EducationalBackground"), ResumeItemType.Education));
        resume.setEmploymentHistory(mapDataToResumeItem((List<Object>) data.get("EmploymentHistory"), ResumeItemType.Employment));
        resume.setInterest(mapDataToResumeItem((List<Object>) data.get("Interests"), ResumeItemType.Interest));
        resume.setQualities(mapDataToResumeItem((List<Object>) data.get("Qualities"), ResumeItemType.Qualities));
        resume.setReferences(mapDataToResumeItem((List<Object>) data.get("References"), ResumeItemType.Reference));
        resume.setSkills(mapDataToResumeItem((List<Object>) data.get("Skills"), ResumeItemType.Skill));
    }
}
