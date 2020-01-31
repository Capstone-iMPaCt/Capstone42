package com.project.ilearncentral.Model;

public class News {

    private int newsUserImageView, newsContentImageView;
    private String titleTextView, dateTextView, timeTextView, contentTextView;

    public News(int newsUserImageView, String titleTextView, String dateTextView, String timeTextView, int newsContentImageView, String contentTextView) {
        this.newsUserImageView = newsUserImageView;
        this.newsContentImageView = newsContentImageView;
        this.titleTextView = titleTextView;
        this.dateTextView = dateTextView;
        this.timeTextView = timeTextView;
        this.contentTextView = contentTextView;
    }

    public News(int newsUserImageView, String titleTextView, String dateTextView, String timeTextView, String contentTextView) {
        this.newsUserImageView = newsUserImageView;
        this.titleTextView = titleTextView;
        this.dateTextView = dateTextView;
        this.timeTextView = timeTextView;
        this.contentTextView = contentTextView;
    }

    public int getNewsUserImageView() {
        return newsUserImageView;
    }

    public void setNewsUserImageView(int newsUserImageView) {
        this.newsUserImageView = newsUserImageView;
    }

    public int getNewsContentImageView() {
        return newsContentImageView;
    }

    public void setNewsContentImageView(int newsContentImageView) {
        this.newsContentImageView = newsContentImageView;
    }

    public String getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(String titleTextView) {
        this.titleTextView = titleTextView;
    }

    public String getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(String dateTextView) {
        this.dateTextView = dateTextView;
    }

    public String getTimeTextView() {
        return timeTextView;
    }

    public void setTimeTextView(String timeTextView) {
        this.timeTextView = timeTextView;
    }

    public String getContentTextView() {
        return contentTextView;
    }

    public void setContentTextView(String contentTextView) {
        this.contentTextView = contentTextView;
    }
}
