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
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.R;

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
    private Educator educator;
    private JobVacancy jobVacancy;
    private static List<JobApplication> retrieved = new ArrayList<>();

    public static final String OPEN = "Open";
    public static final String HIRED = "Hired";
    public static final String REJECTED = "Rejected";

    public JobApplication() {
        this.jobApplicationId = "";
        this.jobVacancyId = "";
        this.eduUsername = "";
        this.message = "";
        this.applicationStatus = "";
        this.applicationDate = Timestamp.now();
        this.educator = null;
        this.jobVacancy = null;
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

    public Educator getEducator() {
        return educator;
    }

    public void setEducator(Educator educator) {
        this.educator = educator;
    }

    public JobVacancy getJobVacancy() {
        return jobVacancy;
    }

    public void setJobVacancy(JobVacancy jobVacancy) {
        this.jobVacancy = jobVacancy;
    }

    public void setJobApplication(JobApplication application) {
        this.jobApplicationId = application.getJobApplicationId();
        this.jobVacancyId = application.getJobVacancyId();
        this.eduUsername = application.getEduUsername();
        this.message = application.getMessage();
        this.applicationStatus = application.getApplicationStatus();
        this.applicationDate = application.getApplicationDate();
        this.educator = application.getEducator();
        this.jobVacancy = application.getJobVacancy();
    }

    public static void retrieveJobApplicationsFromDB(final ObservableBoolean done) {
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

                                    application.setEducator(Educator.getEduByUsername(application.getEduUsername()));
                                    application.setJobVacancy(JobPosts.getJobVacancyById(application.getJobVacancyId()));

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
                            if (done!=null) done.set(true);
                        } else {
                            if (done!=null) done.set(false);
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
        data.put("ApplicationStatus", JobApplication.OPEN);
        data.put("ApplicationDate", Timestamp.now());

        db.collection("JobApplication")
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    retrieveJobApplicationsFromDB(null);
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

    public static void setEducators() {
        for (JobApplication application:retrieved) {
            Educator educator = Educator.getEduByUsername(application.getEduUsername());
            if (educator!=null)
                application.setEducator(educator);
        }
    }

    public static void setJobVacancies() {
        for (JobApplication application:retrieved) {
            JobVacancy vacancy = JobPosts.getJobVacancyById(application.getJobVacancyId());
            if (vacancy!=null)
                application.setJobVacancy(vacancy);
        }
    }

    public static int getJAPositionById(String applicationId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getJobApplicationId().equals(applicationId))
                return i;
        }
        return -1;
    }

    public static JobApplication getJAById(String applicationId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getJobApplicationId().equals(applicationId))
                return retrieved.get(i);
        }
        return null;
    }

    public static List<JobApplication> getApplicantsByVacancyId(String jobVacancyId) {
        List<JobApplication> applications = new ArrayList<>();
        for (JobApplication application:retrieved) {
            if (application.getJobVacancyId().equalsIgnoreCase(jobVacancyId)) {
                applications.add(application);
            }
        }
        return applications;
    }

    public static List<JobApplication> getAllApplicantsByCenterId(String centerId) {
        List<JobApplication> applications = new ArrayList<>();
        for (JobApplication application:retrieved) {
            if (application.getJobVacancy().getCenterId().equalsIgnoreCase(centerId)) {
                applications.add(application);
            }
        }
        return applications;
    }

    public static List<JobApplication> getApplicantsByCenterId(String centerId, String status) {
        setJobVacancies();
        List<JobApplication> applications = new ArrayList<>();
        for (JobApplication application:retrieved) {
            try {
                System.out.println(application.getEduUsername() + application.getJobVacancy());
                if (application.getJobVacancy().getCenterId().equalsIgnoreCase(centerId)) {
                    if (application.getApplicationStatus().equalsIgnoreCase(status)) {
                        applications.add(application);
                    }
                }
            }catch (Exception e) {}
        }
        return applications;
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
                if (application.getApplicationStatus().equalsIgnoreCase(JobApplication.OPEN)) {
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
                if (!application.getApplicationStatus().equalsIgnoreCase(JobApplication.HIRED)) {
                    application.setApplicationStatus(JobApplication.REJECTED);
                    db.collection("JobApplication").document(application.getJobApplicationId())
                            .update("ApplicationStatus", JobApplication.REJECTED);
                }
            }
        }
    }
}
