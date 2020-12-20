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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class User {
    private String userId, username, fullname, type, image, status, answer, question, email, contactNo;
    private List<String> followers;
    private List<String> following;
    private double rating;
    private Map<String, Integer> ratings;
    private static List<User> retrievedUsers = new ArrayList<>();

    public User() {
        userId = username = fullname = type = image = status = answer = question = email = contactNo = "";
        following = new ArrayList<>();
        followers = new ArrayList<>();
        rating = 0;
        ratings = new HashMap<>();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public boolean isFollower(String id) {
        if (getFollowerPosition(id) == -1)
            return false;
        return true;
    }

    public void addFollower(String id) {
        followers.add(id);
    }

    public void addFollower(List<String> list) {
        followers.addAll(list);
    }

    public int getFollowerPosition(String id) {
        if (followers!=null) {
            for (int i = 0; i < followers.size(); i++) {
                if (followers.get(i).equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setFollowers(List<String> followers) {
        if (followers!=null)
            this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public boolean isFollowing(String id) {
        if (getFollowingPosition(id) == -1)
            return false;
        return true;
    }

    public void addFollowing(String id) {
        following.add(id);
    }

    public void addFollowing(List<String> list) {
        following.addAll(list);
    }

    public int getFollowingPosition(String id) {
        if (following!=null) {
            for (int i = 0; i < following.size(); i++) {
                if (following.get(i).equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setFollowing(List<String> following) {
        if (following!= null)
            this.following = following;
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
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.type = user.getType();
        this.image = user.getImage();
        this.status = user.getStatus();
        this.answer = user.getAnswer();
        this.question = user.getQuestion();
        this.email = user.getEmail();
        this.contactNo = user.getContactNo();
        following = user.getFollowing();
        followers = user.getFollowers();
        rating = user.getRating();
        ratings = user.getRatings();
    }

    public static void retrieveUsersFromDB() {
        retrieveUsersFromDB(null);
    }

    public static void retrieveUsersFromDB(final ObservableBoolean userRetrieved) {
        FirebaseFirestore.getInstance().collection("User")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot doc : task.getResult()) {
                        final User user = new User();
                        String collection = "";
                        if (doc.get("AccountType").equals("learningcenter")) {
                            collection = "LearningCenterStaff";
                        } else if (doc.get("AccountType").equals("educator")) {
                            collection = "Educator";
                        } else if (doc.get("AccountType").equals("student")) {
                            collection = "Student";
                        }

                        user.setUserId(doc.getId());
                        if (doc.contains("Username")) user.setUsername(doc.get("Username").toString());
                        if (doc.contains("AccountType")) user.setType(doc.get("AccountType").toString());
                        if (doc.contains("Image") && doc.get("Image")!=null) user.setImage(doc.get("Image").toString());
                        else user.setImage("");
                        if (doc.contains("AccountStatus")) user.setStatus(doc.get("AccountStatus").toString());
                        if (doc.contains("Answer")) user.setAnswer(doc.get("Answer").toString());
                        if (doc.contains("Question")) user.setQuestion(doc.get("Question").toString());
                        if (doc.contains("Email")) user.setEmail(doc.get("Email").toString());
                        if (doc.contains("ContactNo")) user.setContactNo(doc.get("ContactNo").toString());
                        if(doc.contains("Following")) {
                            user.addFollowing((List<String> ) doc.get("Following"));
                        }
                        if(doc.contains("Followers")) {
                            user.addFollower((List<String>) doc.get("Followers"));
                        }
                        if(doc.contains("Ratings"))
                            user.addRating((Map<String, Integer>) doc.get("Ratings"));
                        final Map<String, Object> userData = ((Map<String, Object>) doc.getData());
                        if (doc.get("Image") == null) userData.put("Image", "");
                        FirebaseFirestore.getInstance().collection(collection).whereEqualTo("Username", doc.getString("Username"))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, String> nameDB = (Map<String, String>) document.get("Name");

                                                String fullname = Utility.formatFullName(nameDB.get("FirstName"), nameDB.get("MiddleName"), nameDB.get("LastName"));
                                                user.setFullname(fullname);

                                                int pos = getUserPositionByUsername(document.getString("Username"));
                                                if (pos==-1) {
                                                    retrievedUsers.add(user);
                                                } else {
                                                    retrievedUsers.get(pos).setUser(user);
                                                }
                                            }
                                        } else {
                                        }
                                    }
                                });
                    }
                    if (userRetrieved!=null) userRetrieved.set(true);
                } else {
                    if (userRetrieved!=null) userRetrieved.set(false);
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
        for (User user:retrievedUsers) {
            if (user.getUsername().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }
    public static User getUserById(String id) {
        for (int i=0; i<retrievedUsers.size();i++) {
            if (retrievedUsers.get(i).getUserId().equals(id))
                return retrievedUsers.get(i);
        }
        return null;
    }
    public static String getFullnameByUsername(String username) {
        for (int i=0; i<retrievedUsers.size();i++) {
            if (retrievedUsers.get(i).getUsername().equals(username))
                return retrievedUsers.get(i).getFullname();
        }
        return null;
    }

    public static List<User> getRetrievedUsers() {
        return retrievedUsers;
    }

    public double getRating() {
        setRating();
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Map<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Integer> ratings) {
        this.ratings = ratings;
        setRating();
    }

    public void addRating(String username, int rating) {
        ratings.put(username, rating);
        setRating();
    }

    public void addRating(Map<String, Integer> r) {
        for (Map.Entry entry : r.entrySet()) {
            ratings.put(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString()));
        }
        setRating();
    }

    private void setRating() {
        if (ratings!=null && ratings.size()>0) {
            rating = 0;
            for (Map.Entry entry : ratings.entrySet()) {
                rating += Double.valueOf(entry.getValue()+"");
            }
            rating /= ratings.size();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
