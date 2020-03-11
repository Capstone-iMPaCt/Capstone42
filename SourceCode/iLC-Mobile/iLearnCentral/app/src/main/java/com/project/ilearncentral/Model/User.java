package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.MyClass.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class User {
    private String username;
    private String fullname;
    private String type;
    private String image;
    private boolean followedUser;
    private boolean followingMe;
    private static List<User> retrievedUsers = new ArrayList<>();

    public User() {
        username = fullname = type = "";
        followingMe = followedUser = false;
    }

    public User(String username, String fullname, String type, String imageString) {
        this.username = username;
        this.fullname = fullname;
        this.type = type;
        followingMe = followedUser = false;
        image = imageString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(boolean followedUser) {
        this.followedUser = followedUser;
    }

    public boolean isFollowingMe() {
        return followingMe;
    }

    public void setFollowingMe(boolean followingMe) {
        this.followingMe = followingMe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStringType() {
        if (isLearningCenter())
            return "Learning Center";
        else
            return type;
    }

    public boolean isLearningCenter() {
        return type.equals("LearningCenter");
    }

    public void setUser(User user) {
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.type = user.getType();
        this.image = user.getImage();
        followingMe = user.isFollowingMe();
        followedUser = user.isFollowedUser();
    }

    public static void retrieveUsersFromDB() {
        FirebaseFirestore.getInstance().collection("User")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String collection = "";
                            if (document.get("AccountType").equals("learningcenter")) {
                                collection = "LearningCenterStaff";
                            } else if (document.get("AccountType").equals("educator")) {
                                collection = "Educator";
                            } else if (document.get("AccountType").equals("student")) {
                                collection = "Student";
                            }
                            final Map<String, Object> userData = ((Map<String, Object>) document.getData());
                            if (document.get("Image") == null) userData.put("Image", "");
                            FirebaseFirestore.getInstance().collection(collection).whereEqualTo("Username", document.getString("Username"))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, String> nameDB = (Map<String, String>) document.get("Name");

                                                    String fullname = Utility.formatFullName(nameDB.get("FirstName"), nameDB.get("MiddleName"), nameDB.get("LastName"));
                                                    int pos = getUserPositionByUsername(document.getString("Username"));
                                                    User curUser = new User(document.getString("Username"), fullname, userData.get("AccountType")+"", userData.get("Image").toString());
                                                    if (pos==-1) {
                                                        retrievedUsers.add(curUser);
                                                    } else {
                                                        retrievedUsers.get(pos).setUser(curUser);
                                                    }
                                                }
                                            } else {
                                            }
                                        }
                                    });
                        }
                    } else {
                        Log.d("getUsers", "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public static int getUserPositionByUsername(String username) {
        for (int i=0; i<retrievedUsers.size();i++) {
            if (retrievedUsers.get(i).getUsername().equals(username))
                return i;
        }
        return -1;
    }
    public static User getUserByUsername(String username) {
        for (int i=0; i<retrievedUsers.size();i++) {
            if (retrievedUsers.get(i).getUsername().equals(username))
                return retrievedUsers.get(i);
        }
        return null;
    }

    public static List<User> getRetrievedUsers() {
        return retrievedUsers;
    }
}
