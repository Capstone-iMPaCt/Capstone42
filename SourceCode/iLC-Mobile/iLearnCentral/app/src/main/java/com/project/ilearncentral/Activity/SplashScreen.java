package com.project.ilearncentral.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableInteger;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.CustomInterface.OnIntegerChangeListener;
import com.project.ilearncentral.Model.Course;
import com.project.ilearncentral.Model.Educator;
import com.project.ilearncentral.Model.JobApplication;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.Model.User;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.JobPosts;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

public class SplashScreen extends AppCompatActivity {

    private String TAG = "SplashScreen";
    private ObservableBoolean accountSet, authProfileSet,
            userSet, profileSet, centerSet;
    private boolean update;
    private int updateType;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        Intent i = getIntent();
        update = i.getBooleanExtra("update", false);
        updateType = i.getIntExtra("type", 0);

        setObservableListeners();

        if (update) {
            switch (updateType) {
                case 0:
                case 1:setAccount();
                    break;
                case 2:setProfileDetails();
                    break;
                case 3:setLearningCenterDetails();
                    break;
                default:
                    setAccount();
            }
        } else {
            Account.clear();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivity(new Intent(this, Login.class));
                finish();
            } else {
                setAccount();
            }
        }
    }

    private void setObservableListeners() {
        final ObservableInteger allLoaded = new ObservableInteger();
        allLoaded.set(0);
        allLoaded.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int value) {
                if (value >= 6) {
                    finish();
                }
            }
        });
        final ObservableBoolean userRetrieved = new ObservableBoolean();
        userRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                Educator.setUsers();
                allLoaded.set(allLoaded.get()+1);
            }
        });
        final ObservableBoolean LCRetrieved = new ObservableBoolean();
        userRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                allLoaded.set(allLoaded.get()+1);
            }
        });
        final ObservableBoolean educatorRetrieved = new ObservableBoolean();
        educatorRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                JobApplication.setEducators();
                allLoaded.set(allLoaded.get()+1);
            }
        });
        final ObservableBoolean courseRetrieved = new ObservableBoolean();
        educatorRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                allLoaded.set(allLoaded.get()+1);
            }
        });
        final ObservableBoolean vacancyRetrieved = new ObservableBoolean();
        vacancyRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                JobApplication.setJobVacancies();
                allLoaded.set(allLoaded.get()+1);
            }
        });
        final ObservableBoolean applicationsRetrieved = new ObservableBoolean();
        vacancyRetrieved.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                allLoaded.set(allLoaded.get()+1);
            }
        });

        userSet = new ObservableBoolean();
        userSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    new Thread(new Runnable() {
                        public void run() {
                            User.retrieveUsersFromDB(userRetrieved);
                            LearningCenter.retrieveLearningCentersFromDB(LCRetrieved);
                            Educator.retrieveEducatorsFromDB(educatorRetrieved);
                            Course.retrieveCoursesFromDB(courseRetrieved);
                            JobPosts.retrievePostsFromDB(vacancyRetrieved);
                            JobApplication.retrieveJobApplicationsFromDB(applicationsRetrieved);
                        }
                    }).start();
                } else {
                }
            }
        });
        profileSet = new ObservableBoolean();
        profileSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                } else {

                }
            }
        });
        centerSet = new ObservableBoolean();
        centerSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {

                } else {

                }
            }
        });
        accountSet = new ObservableBoolean();
        accountSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    Account.activateObservables(success);
                    if (user.getDisplayName() == null || user.getDisplayName().equals("")) {
                        Utility.updateProfileWithImage(TAG, authProfileSet);
                    } else {
                        authProfileSet.set(true);
                        //SETUP all methods that need Account static to be completely loaded here.
                    }
                }
            }
        });
        authProfileSet = new ObservableBoolean();
        authProfileSet.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    if (!update) {
                        Intent mainIntent = new Intent(SplashScreen.this, Main.class);
                        startActivity(mainIntent);
                    }
                } else {

                }
            }
        });
    }

    private void setAccount() {
        DocumentReference userRef = db.collection("User").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Account.setUserData(document.getData());
                        userSet.set(true);
                        Log.d(TAG, "User - DocumentSnapshot data: " + document.getData());

                        setProfileDetails();
                    } else {
                        userSet.set(false);
                        Log.d(TAG, "User - No such document");
                    }
                } else {
                    Log.d(TAG, "User - get failed with ", task.getException());
                }
            }
        });
    }

    private void setProfileDetails() {
        String collection = "";
        if (Account.getType() == Account.Type.LearningCenter) {
            collection = "LearningCenterStaff";
            Account.setType(Account.Type.LearningCenter);
        } else if (Account.getType() == Account.Type.Educator) {
            collection = "Educator";
            Account.setType(Account.Type.Educator);
        } else if (Account.getType() == Account.Type.Student) {
            collection = "Student";
            Account.setType(Account.Type.Student);
        }
        db.collection(collection)
                .whereEqualTo("Username", Account.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Account.setProfileData(document.getData());
                                profileSet.set(true);
                                if (Account.getType() == Account.Type.LearningCenter || !Account.getStringData("centerId").isEmpty()) {
                                    setLearningCenterDetails();
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                } else {
                                    accountSet.set(true);
                                }
                            }
                        } else {
                            profileSet.set(false);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setLearningCenterDetails() {
        DocumentReference docRef = db.collection("LearningCenter")
                .document(Account.getStringData("centerId"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Account.setBusinessData(document.getData());
                        accountSet.set(true);
                        centerSet.set(true);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        centerSet.set(false);
                        accountSet.set(false);
                        Log.d(TAG, "No such document in Learning Center");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
