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
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Posts {

    private static String TAG = "Posts";
    private static String idTag = "PostId";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Map<String, Map<String, Object>> posts = new HashMap<>();
    private static Map<String, Object> curPost = new HashMap<>();

    public static void addPost(String postId, Map<String, Object> post) {
        posts.put(postId, post);
    }

    public static void addAllPosts(Map<String, Map<String, Object>> allPosts) {
        posts.putAll(allPosts);
    }

    public static void clearPosts() {
        posts.clear();
    }

    public static int getSize() {
        return posts.size();
    }

    public static Map<String, Object> getPostById(String postId) {
        if (posts.containsKey(postId))
            return posts.get(postId);
        return null;
    }

    public static Map<String, Map<String, Object>> getPostByNewsUser(String username) {
        Map<String, Map<String, Object>> userPost = new HashMap<>();
        for (Map.Entry postEntry : posts.entrySet()) {
            curPost = (Map<String, Object>) postEntry.getValue();
            if (curPost.get("Username").equals(username)) {
                userPost.put(postEntry.getKey().toString(), curPost);
            }
            curPost = null;
        }
        return userPost;
    }

    public static void retrievePostsFromDB(final ObservableBoolean done) {
        db.collection("Post")
                .orderBy("Date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                posts.put(document.getId(), (Map<String, Object>) document
                                        .getData());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            if (done!=null) done.set(true);
                        } else {
                            if (done!=null) done.set(false);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void retrievePostFromDB(String postId, final ObservableBoolean done) {
        DocumentReference docRef = db.collection("Post").document(postId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        posts.put(document.getId(), document.getData());
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
        db.collection("Post")
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    posts.put(documentReference.getId(), data);
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

    public static void addPost(String title, String content, boolean withImage, final ObservableString done) {
        Map<String, Object> data = new HashMap<>();
        data.put("Title", title);
        data.put("Content", content);
        data.put("Username", Account.getStringData("username"));
        data.put("Date", Timestamp.now());
        data.put("Image", withImage);
        addPostToDB(data, done);
    }

    public static void updatePostToDB(final String postId, final Map<String, Object> data, final ObservableString done) {
        db.collection("Post").document(postId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (done!=null) done.set(postId);
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

    public static void updatePost(String postId, String title, String content, boolean withImage, final ObservableString done) {
        Map<String, Object> data = getPostById(postId);
        data.put("Title", title);
        data.put("Content", content);
        data.put("Username", Account.getStringData("username"));
        data.put("Date", Timestamp.now());
        data.put("Image", withImage);
        updatePostToDB(postId, data, done);
    }

    public static Map<String, Object> getPostFromDB(String postId, final ObservableBoolean done) {
        final Map<String, Object> post = new HashMap<>();
        DocumentReference docRef = db.collection("Post").document(postId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        post.putAll(document.getData());

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
        return post;
    }

    public static Post setPostToView(String postId) {
        if (posts.containsKey(postId)) {
            curPost = posts.get(postId);
            return new Post(stringCurPost("Username"), stringCurPost("Title"),
                    (Timestamp) curPost.get("Date"), postId, stringCurPost("Content"),
                    (boolean)curPost.get("Image"));
        }
        return null;
    }

    public static ArrayList<Post> setPostsToView() {
        ArrayList<Post> postViews = new ArrayList<>();
        for (Map.Entry postEntry : posts.entrySet()) {
            postViews.add(setPostToView(postEntry.getKey().toString()));
        }
        Collections.sort(postViews, new Comparator<Post>() {
            public int compare(Post o1, Post o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return postViews;
    }

    public static ArrayList<Post> searchText(String text) {
        text = text.toLowerCase();
        ArrayList<Post> postViews = new ArrayList<>();
        for (Map.Entry postEntry : posts.entrySet()) {
            Map<String, Object> post = (Map<String, Object>)postEntry.getValue();
            if (post.get("Title").toString().toLowerCase().contains(text) ||
                post.get("Content").toString().toLowerCase().contains(text) ||
                post.get("FullName").toString().toLowerCase().contains(text))
                    postViews.add(setPostToView(postEntry.getKey().toString()));
        }
        Collections.sort(postViews, new Comparator<Post>() {
            public int compare(Post o1, Post o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return postViews;
    }

    public static ArrayList<Post> myPosts() {
        ArrayList<Post> postViews = new ArrayList<>();
        for (Map.Entry postEntry : posts.entrySet()) {
            Map<String, Object> post = (Map<String, Object>)postEntry.getValue();
            if (post.get("Username").toString().toLowerCase().contains(Account.getStringData("username")))
                    postViews.add(setPostToView(postEntry.getKey().toString()));
        }
        Collections.sort(postViews, new Comparator<Post>() {
            public int compare(Post o1, Post o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return postViews;
    }

    private static String stringCurPost(String key) {
        if (curPost.containsKey(key))
            return curPost.get(key).toString();
        return "";
    }

    private static String dateCurPost(String key) {
        if (curPost.containsKey(key))
            return Utility.getStringFromDate((Timestamp) curPost.get(key));
        return null;
    }

    private static String timeCurPost(String key) {
        if (curPost.containsKey(key))
            return Utility.getStringFromTime((Timestamp) curPost.get(key));
        return "";
    }

    public static String getIdOfPost(String username, String content) {
        for (Map.Entry postEntry : posts.entrySet()) {
            curPost = (Map<String, Object>) postEntry.getValue();
            if (curPost.get("Username").equals(username) && curPost.get("Content").equals(content)) {
                return postEntry.getKey().toString();
            }
            curPost = null;
        }
        return "";
    }

    public static void setCurPost(String postId) {
        if (posts.containsKey(postId))
            curPost = posts.get(postId);
    }

    public static boolean hasCurrent() {
        return curPost!=null;
    }
}
