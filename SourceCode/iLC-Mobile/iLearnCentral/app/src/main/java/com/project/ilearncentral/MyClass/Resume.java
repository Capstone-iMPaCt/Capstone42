package com.project.ilearncentral.MyClass;

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
import com.project.ilearncentral.Model.ResumeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Resume {

    private static final String TAG = "Resume";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static String id = "";
    private static String objective = "";
    private static List<ResumeItem> awards = new ArrayList<>();
    private static List<ResumeItem> educationalBackground = new ArrayList<>();
    private static List<ResumeItem> employmentHistory = new ArrayList<>();
    private static List<ResumeItem> interest = new ArrayList<>();
    private static List<ResumeItem> qualities = new ArrayList<>();
    private static List<ResumeItem> references = new ArrayList<>();
    private static List<ResumeItem> skills = new ArrayList<>();
    public static boolean resumeChange = true;

    public enum ResumeItemType {Award, Education, Employment, Interest, Qualities, Reference, Skill}

    ;

    public static void clearAll() {
        id = "";
        objective = "";
        awards.clear();
        educationalBackground.clear();
        employmentHistory.clear();
        interest.clear();
        qualities.clear();
        references.clear();
        skills.clear();
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Resume.id = id;
    }

    public static String getObjective() {
        return objective;
    }

    public static void setObjective(String objective) {
        Resume.objective = objective;
    }

    public static List<ResumeItem> getEducationalBackground() {
        return educationalBackground;
    }

    public static void setEducationalBackground(List<ResumeItem> educationalBackground) {
        Resume.educationalBackground = educationalBackground;
    }

    public static List<ResumeItem> getEmploymentHistory() {
        return employmentHistory;
    }

    public static void setEmploymentHistory(List<ResumeItem> employmentHistory) {
        Resume.employmentHistory = employmentHistory;
    }

    public static List<ResumeItem> getInterest() {
        return interest;
    }

    public static void setInterest(List<ResumeItem> interest) {
        Resume.interest = interest;
    }

    public static List<ResumeItem> getQualities() {
        return qualities;
    }

    public static void setQualities(List<ResumeItem> qualities) {
        Resume.qualities = qualities;
    }

    public static List<ResumeItem> getReferences() {
        return references;
    }

    public static void setReferences(List<ResumeItem> references) {
        Resume.references = references;
    }

    public static List<ResumeItem> getSkills() {
        return skills;
    }

    public static void setSkills(List<ResumeItem> skills) {
        Resume.skills = skills;
    }

    public static List<ResumeItem> getAwards() {
        return awards;
    }

    public static void setAwards(List<ResumeItem> awards) {
        Resume.awards = awards;
    }

    public static void getResumeFromDB(String username, final ObservableBoolean done) {
        if (username == null || username.isEmpty())
            username = Account.getUsername();
        db.collection("Resume").whereEqualTo("Username", username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        setData(document.getData());
                        id = document.getId();
                        done.set(true);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    done.set(false);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public static void addResume(final ObservableString done) {
        db.collection("Resume")
                .add(getData())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (done != null) done.set(documentReference.getId());
                        id = documentReference.getId();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        id = "";
                        if (done != null) done.set("");
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static void updateResume(String givenId, final ObservableString done) {
        if (givenId == null || givenId.isEmpty()) givenId = id;
        else id = givenId;
        db.collection("Resume").document(givenId)
                .set(getData())
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

    public static Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();

        data.put("Awards", mapDataFromResumeItem(awards, ResumeItemType.Award));
        data.put("CareerObjective", objective);
        data.put("EducationalBackground", mapDataFromResumeItem(educationalBackground, ResumeItemType.Education));
        data.put("EmploymentHistory", mapDataFromResumeItem(employmentHistory, ResumeItemType.Employment));
        data.put("Interests", mapDataFromResumeItem(interest, ResumeItemType.Interest));
        data.put("Qualities", mapDataFromResumeItem(qualities, ResumeItemType.Qualities));
        data.put("References", mapDataFromResumeItem(references, ResumeItemType.Reference));
        data.put("Skills", mapDataFromResumeItem(skills, ResumeItemType.Skill));
        data.put("Username", Account.getUsername());
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
                                d.get("SchoolName").toString(),
                                d.get("SchoolAddress").toString(),
                                d.get("SchoolYear").toString()));
                        break;
                    case Employment:
                        d = (Map<String, Object>) data.get(i);
                        dataList.add(new ResumeItem(
                                d.get("CompanyName").toString(),
                                d.get("CompanyAddress").toString(),
                                d.get("DatePeriod").toString()));
                        break;
                    case Reference:
                        d = (Map<String, Object>) data.get(i);
                        dataList.add(new ResumeItem(
                                d.get("ReferenceName").toString(),
                                d.get("Affiliation").toString(),
                                d.get("Position").toString(),
                                d.get("ContactNo").toString()));
                        break;
                }
            }
        }
        return dataList;
    }

    public static void setData(Map<String, Object> data) {
        objective = data.get("CareerObjective").toString();
        awards = mapDataToResumeItem((List<Object>) data.get("Awards"), ResumeItemType.Award);
        educationalBackground = mapDataToResumeItem((List<Object>) data.get("EducationalBackground"), ResumeItemType.Education);
        employmentHistory = mapDataToResumeItem((List<Object>) data.get("EmploymentHistory"), ResumeItemType.Employment);
        interest = mapDataToResumeItem((List<Object>) data.get("Interests"), ResumeItemType.Interest);
        qualities = mapDataToResumeItem((List<Object>) data.get("Qualities"), ResumeItemType.Qualities);
        references = mapDataToResumeItem((List<Object>) data.get("References"), ResumeItemType.Reference);
        skills = mapDataToResumeItem((List<Object>) data.get("Skills"), ResumeItemType.Skill);
    }
}
