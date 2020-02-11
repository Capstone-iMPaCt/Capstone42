package com.project.ilearncentral.MyClass;

public class Account {

    private static String status;
    private static Type type;

    public static enum Type { LearningCenter, Educator, Student }

    public static Type getType() {
        return type;
    }

    public static void setType(Type type) {
        Account.type = type;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Account.status = status;
    }
}
