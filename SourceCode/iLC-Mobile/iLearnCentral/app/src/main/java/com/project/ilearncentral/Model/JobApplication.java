package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class JobApplication {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String jobApplicationId;
    private String jobVacancyId;
    private String eduUsername;
    private String message;
    private String applicationStatus;
    private Timestamp applicationDate;
    private static List<JobApplication> retrieved = new ArrayList<>();

    public JobApplication() {
        this.jobApplicationId = "";
        this.jobVacancyId = "";
        this.eduUsername = "";
        this.message = "";
        this.applicationStatus = "";
        this.applicationDate = Timestamp.now();
    }

    public String getJobApplicationId() {
        return jobApplicationId;
    }

    public void setJobApplicationId(String jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
    }

    public String getJobVacancyId() {
        return jobVacancyId;
    }

    public void setJobVacancyId(String jobVacancyId) {
        this.jobVacancyId = jobVacancyId;
    }

    public String getEduUsername() {
        return eduUsername;
    }

    public void setEduUsername(String eduUsername) {
        this.eduUsername = eduUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Timestamp getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Timestamp applicationDate) {
        this.applicationDate = applicationDate;
    }

    public static List<JobApplication> getRetrieved() {
        return retrieved;
    }

    public static void setRetrieved(List<JobApplication> retrieved) {
        JobApplication.retrieved = retrieved;
    }

    public void setJobApplication(JobApplication application) {
        this.jobApplicationId = application.getJobApplicationId();
        this.jobVacancyId = application.getJobVacancyId();
        this.eduUsername = application.getEduUsername();
        this.message = application.getMessage();
        this.applicationStatus = application.getApplicationStatus();
        this.applicationDate = Timestamp.now();
    }

    public static void retrieveJobApplicationsFromDB() {
        db.collection("JobApplication")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    JobApplication application = new JobApplication();
                                    application.setJobApplicationId(document.getId());
                                    application.setJobVacancyId(document.getString("VacancyID"));
                                    application.setEduUsername(document.getString("Username"));
                                    application.setMessage(document.getString("Message"));
                                    application.setApplicationStatus(document.getString("ApplicationStatus"));
                                    application.setApplicationDate(document.getTimestamp("ApplicationDate"));

                                    int pos = getJAPositionById(document.getId());
                                    if (pos == -1) {
                                        retrieved.add(application);
                                    } else {
                                        retrieved.get(pos).setJobApplication(application);
                                    }
                                    Log.d("getJobApplication", "Job Application item retrieved: " + document
                                            .getId());
                                } catch (Exception e) {
                                    Log.d("getJobApplication", "Job Application item not retrieved: " + e
                                            .getMessage());
                                }
                            }
                        } else {
                            Log.d("getJobApplication", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void sendResume(String jobVacancyId, String username, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("VacancyID", jobVacancyId);
        data.put("Username", username);
        data.put("Message", message);
        data.put("ApplicationStatus", "open");
        data.put("ApplicationDate", Timestamp.now());

        db.collection("JobApplication")
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    retrieveJobApplicationsFromDB();
                    Log.d("JobApplication: ", "DocumentSnapshot written with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("JobApplication: ", "Error adding document", e);
                }
            });
    }

    public static int getJAPositionById(String applicationId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getJobApplicationId().equals(applicationId))
                return i;
        }
        return -1;
    }

    public static List<JobApplication> filterCourses(List<JobApplication> jobApplications, String filterBy, String filterValue) {
        filterValue = filterValue.toLowerCase().trim();
        List<JobApplication> filteredJobApplication = new ArrayList<JobApplication>();
        for (JobApplication application:jobApplications) {
            switch (filterBy.toLowerCase()) {
                case "status":
                    if ((filterValue.equalsIgnoreCase(application.getApplicationStatus()))) {
                        filteredJobApplication.add(application);
                    }
                    break;
                case "message":
                    if (application.getMessage().contains(filterValue)) {
                        filteredJobApplication.add(application);
                    }
                    break;
                case "username":
                    if (application.getEduUsername().equalsIgnoreCase(filterValue)) {
                        filteredJobApplication.add(application);
                    }
                    break;
                case "jobId":
                    if (application.getJobVacancyId().equalsIgnoreCase(filterValue)) {
                        filteredJobApplication.add(application);
                    }
                    break;
                default:
                    break;
            }
        }
        return filteredJobApplication;
    }

    public static List<String> eduAppliedTo(List<JobApplication> applications, String eduUsername) {
        List<String> jobVacancies = new ArrayList<>();
        for (JobApplication application:applications) {
            if (application.getEduUsername().equalsIgnoreCase(eduUsername)) {
                jobVacancies.add(application.jobVacancyId);
            }
        }
        return jobVacancies;
    }

    public boolean hasApplicant(List<JobApplication> applications, String vacancyId) {
        for (JobApplication application:applications) {
            if (application.getJobVacancyId().equalsIgnoreCase(vacancyId)) {
                if (application.getApplicationStatus().equalsIgnoreCase("open")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isApplicant(String jobVacancyId, String eduUsername) {
        for (JobApplication application:retrieved) {
            if (application.getJobVacancyId().equalsIgnoreCase(jobVacancyId) &&
                    application.getEduUsername().equalsIgnoreCase(eduUsername))
                return true;
        }
        return false;
    }

    public static void closeApplication(String jobVacancyId) {
        for (JobApplication application:retrieved) {
            if (application.getJobVacancyId().equalsIgnoreCase(jobVacancyId)) {
                if (!application.getApplicationStatus().equalsIgnoreCase("Approved")) {
                    application.setApplicationStatus("close");
                    db.collection("JobApplication").document(application.getJobApplicationId())
                            .update("ApplicationStatus", "close");
                }
            }
        }
    }
}
