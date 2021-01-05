package com.project.ilearncentral.Model;

import com.google.firebase.Timestamp;
import com.project.ilearncentral.MyClass.Utility;

public class Post {

    private Timestamp date;
    private String postTitle, datePosted, timePosted, postId, postSender, content, fullname;
    private boolean withImage;

    public Post(String postSender, String postTitle, Timestamp date, String postId, String content, boolean withImage) {
        this.postSender = postSender;
        this.content = content;
        this.postTitle = postTitle;
        this.date = date;
        this.datePosted = Utility.getDateStringFromTimestamp(date);
        this.timePosted = Utility.getTimeStringFromTimestamp(date);
        this.postId = postId;
        this.withImage = withImage;
        this.fullname = "";
    }

    public Post(String postSender, String postTitle, Timestamp date, String content, boolean withImage) {
        this.postSender = postSender;
        this.postTitle = postTitle;
        this.date = date;
        this.datePosted = Utility.getDateStringFromTimestamp(date);
        this.timePosted = Utility.getTimeStringFromTimestamp(date);
        this.content = content;
        this.withImage = withImage;
        this.fullname = "";
    }

    public String getPostSender() {
        return postSender;
    }

    public String getContent() {
        return content;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public String getPostId() {
        return postId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isWithImage() {
        return withImage;
    }

    public void setWithImage(boolean withImage) {
        this.withImage = withImage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
