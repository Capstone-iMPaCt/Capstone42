package com.project.ilearncentral.Model;

import com.google.firebase.Timestamp;

import java.util.Comparator;

public class Message implements Comparator<Message> {

    private String id;
    private String to;
    private String from;
    private String message;
    private Timestamp dateSent;
    private String type;

    public Message() {
        this.id = "";
        this.to = "";
        this.from = "";
        this.message = "";
        this.dateSent = Timestamp.now();
        this.type = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDateSent() {
        return dateSent;
    }

    public void setDateSent(Timestamp dateSent) {
        this.dateSent = dateSent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int compare(Message message1, Message message2) {

        Timestamp dateSent1 = message1.getDateSent();
        Timestamp dateSent2 = message2.getDateSent();

        //ascending order
        //return dateSent1.compareTo(dateSent2);

        //descending order
        return dateSent2.compareTo(dateSent1);
    }
//
//    public int compareTo(Message message1) {
//
//        Timestamp compareDate = (message1).getDateSent();
//
//        //ascending order
//        //return this.dateSent.compareTo(compareDate);
//
//        //descending order
//        return compareDate.compareTo(this.dateSent);
//    }
}
