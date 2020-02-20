package com.project.ilearncentral.Model;

import com.google.firebase.Timestamp;
import com.project.ilearncentral.MyClass.Utility;

public class Post {

    private Timestamp date;
    private String titleTextView, dateTextView, timeTextView, contentTextView, newsUserImageView, newsContentImageView;

    public Post(String newsUserImageView, String titleTextView, Timestamp date, String newsContentImageView, String contentTextView) {
        this.newsUserImageView = newsUserImageView;
        this.newsContentImageView = newsContentImageView;
        this.titleTextView = titleTextView;
        this.date = date;
        this.dateTextView = Utility.getStringFromDate(date);
        this.timeTextView = Utility.getStringFromTime(date);
        this.contentTextView = contentTextView;
    }

    public Post(String newsUserImageView, String titleTextView, Timestamp date, String contentTextView) {
        this.newsUserImageView = newsUserImageView;
        this.titleTextView = titleTextView;
        this.date = date;
        this.dateTextView = Utility.getStringFromDate(date);
        this.timeTextView = Utility.getStringFromTime(date);
        this.contentTextView = contentTextView;
    }

    public String getNewsUserImageView() {
        return newsUserImageView;
    }

    public String getNewsContentImageView() {
        return newsContentImageView;
    }

    public String getTitleTextView() {
        return titleTextView;
    }

    public String getDateTextView() {
        return dateTextView;
    }

    public String getTimeTextView() {
        return timeTextView;
    }

    public String getContentTextView() {
        return contentTextView;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
