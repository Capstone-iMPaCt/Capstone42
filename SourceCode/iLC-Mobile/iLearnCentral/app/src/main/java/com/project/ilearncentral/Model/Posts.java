package com.project.ilearncentral.Model;

import android.util.Log;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
                                if (!posts.containsKey(document.getId())) {
                                    posts.put(document.getId(), (Map<String, Object>) document
                                            .getData());
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

    public static Post setPostToView(String postId) {
        if (posts.containsKey(postId)) {
            curPost = posts.get(postId);
            return new Post(stringCurPost("Username"), stringCurPost("Title"), (Timestamp) curPost.get("Date"), postId, stringCurPost("Content"));
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
}
