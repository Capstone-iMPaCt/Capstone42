package com.project.ilearncentral.Model;

public class User {
    private String username;
    private String fullname;
    private String type;
    private String image;
    private boolean followedUser;
    private boolean followingMe;

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
}
