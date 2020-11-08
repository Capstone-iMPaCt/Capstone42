package com.project.ilearncentral.MyClass;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Model.JobVacancy;
import com.project.ilearncentral.Model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class JobPosts {
    private static String TAG = "Job Posts";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static List<JobVacancy> jobPosts = new ArrayList<>();
    private static JobVacancy curPost = new JobVacancy();

    public static void addPost(String jobId, JobVacancy post) {
        post.setJobId(jobId);
        jobPosts.add(post);
    }

    public static void addPost(JobVacancy post) {
        jobPosts.add(post);
    }

    public static void addAllPosts(List<JobVacancy> allPosts) {
        jobPosts.addAll(allPosts);
    }

    public static void clearPosts() {
        jobPosts.clear();
    }

    public static int getSize() {
        return jobPosts.size();
    }

    public static JobVacancy getJobPostById(String jobId) {
        for (JobVacancy job:jobPosts) {
            if (job.getJobId().equals(jobId))
                return job;
        }
        return null;
    }


    public static int getJobPostPositionById(String jobId) {
        for (int i=0; i<jobPosts.size();i++) {
            if (jobPosts.get(i).getJobId().equals(jobId))
                return i;
        }
        return -1;
    }

    public static List<JobVacancy> getJobPostsByUsername(String username) {
        List<JobVacancy> list = new ArrayList<>();
        for (JobVacancy job:jobPosts) {
            if (job.getUsername().equals(username))
                list.add(job);
        }
        return list;
    }

    public static List<JobVacancy> getJobPostsByCenterId(String centerId) {
        List<JobVacancy> list = new ArrayList<>();
        for (JobVacancy job:jobPosts) {
            if (job.getCenterId().equals(centerId))
                list.add(job);
        }
        return list;
    }

    public static List<JobVacancy> getJobPosts() {
        return jobPosts;
    }

    public static void retrievePostsFromDB(final ObservableBoolean done) {
        db.collection("JobVacancies")
            .whereEqualTo("Status", "open")
            .orderBy("Date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int pos = getJobPostPositionById(document.getId());
                            if (pos==-1) {
                                jobPosts.add(new JobVacancy(document.getId(), document.getData()));
                            } else {
                                jobPosts.get(pos).setJobVacancy(document.getId(), document.getData());
                            }
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        done.set(true);
                    } else {
                        done.set(false);
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public static void retrievePostFromDB(final String postId, final ObservableBoolean done) {
        DocumentReference docRef = db.collection("JobVacancies").document(postId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        curPost = getJobPostById(postId);
                        if(curPost != null) {
                            curPost.setJobVacancy(postId, document.getData());
                        } else {
                            jobPosts.add(new JobVacancy(document.getId(), document.getData()));
                        }
                        if (done!=null) done.set(true);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        if (done!=null) done.set(false);
                        Log.d(TAG, "No such document");
                    }
                } else {
                    if (done!=null) done.set(false);
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static void addPostToDB(final Map<String, Object> data, final ObservableString done) {
        data.put("Status", "open");
        data.put("CenterID", Account.getStringData("centerId"));
        data.put("Username", Account.getUsername());
        data.put("Date", Timestamp.now());
        db.collection("JobVacancies")
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    jobPosts.add(new JobVacancy(documentReference.getId(), data));
                    if (done!=null) done.set(documentReference.getId());
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (done!=null) done.set("");
                    Log.w(TAG, "Error adding document", e);
                }
            });
    }

    public static void addPostToDB(JobVacancy data, ObservableString done) {
        addPostToDB(data.getJobVacancyData(), done);
    }

    public static void updatePostToDB(final String jobId, final Map<String, Object> data, final ObservableString done) {
        data.put("Date", Timestamp.now());
        db.collection("JobVacancies").document(jobId)
            .set(data)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (done!=null) done.set(jobId);
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (done!=null) done.set("");
                    Log.w(TAG, "Error writing document", e);
                }
            });
    }

    public static void updatePostToDB(String jobId, JobVacancy data, ObservableString done) {
        updatePostToDB(jobId, data.getJobVacancyData(), done);
    }

    public static JobVacancy getPostFromDB(String jobId, final ObservableBoolean done) {
        final JobVacancy[] job = {new JobVacancy()};
        DocumentReference docRef = db.collection("JobVacancies").document(jobId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        job[0] = new JobVacancy(document.getId(),document.getData());

                        if (done!=null) done.set(true);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        if (done!=null) done.set(false);
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return job[0];
    }

    public static JobVacancy getJobVacancyAt(int position) {
        if (position<jobPosts.size())
            return jobPosts.get(position);
        return null;
    }

    public static List<JobVacancy> searchText(String text, boolean isAll) {
        text = text.toLowerCase();
        List<JobVacancy> jobs = new ArrayList<>();


        for(JobVacancy job:jobPosts) {
            if ((job.getBusinessData().get("BusinessName").toLowerCase().contains(text) ||
                    job.getJobDescription().toLowerCase().contains(text) ||
                    job.getPosition().toLowerCase().contains(text)) &&
                (isAll || (!isAll && job.getCenterId().contains(Account.getCenterId())))
            ) {
                jobs.add(job);
            }
        }
        Collections.sort(jobPosts, new Comparator<JobVacancy>() {
            public int compare(JobVacancy o1, JobVacancy o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return jobs;
    }

    public static void setCurPost(JobVacancy job) {
        curPost = job;
    }

    public static boolean hasCurrent() {
        return curPost!=null;
    }

}
