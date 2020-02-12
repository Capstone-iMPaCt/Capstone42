package com.project.ilearncentral.MyClass;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {

    private static String status = "";
    private static Type type;
    private static Map<String, Object> data = new HashMap<>();

    public static String getName() {
        String name = getStringData("firstName") + " ";
        if (!getStringData("middleName").isEmpty())
            name += getStringData("middleName").toUpperCase().charAt(0) + " ";
        name += getStringData("lastName");
        return name;
    }

    public static Map<String, Object> getUserData() {
        Map<String, Object> userData = new HashMap<>();
        try {
            userData.put("AccountStatus", "active");
            userData.put("AccountType", type.toString().toLowerCase());
            userData.put("Email", getStringData("email"));
            userData.put("Username", getStringData("username"));
            userData.put("Image", data.get("image"));
        } catch(Exception e) {
            return null;
        }
        return userData;
    }

    public static Map<String, Object> getProfileData() {
        Map<String, Object> profileData = new HashMap<>();
            Map<String, String> name = new HashMap<>();
                name.put("FirstName", getStringData("firstName"));
                name.put("MiddleName", getStringData("middleName"));
                name.put("LastName", getStringData("lastName"));
                name.put("Extension", getStringData("extension"));
        profileData.put("Name", name);
        profileData.put("Position", "none");
        profileData.put("Religion", getStringData("religion"));
        profileData.put("Citizenship", getStringData("citizenship"));
        profileData.put("Username", getStringData("username"));
        profileData.put("EmailAddress", getStringData("email"));
        profileData.put("EmploymentStatus", "none");
        profileData.put("Gender", getStringData("gender"));
        profileData.put("MaritalStatus", getStringData("maritalStatus"));
        profileData.put("CenterID", "");
        profileData.put("Birthday", getTimeStampData("birthday"));
        List<Map<String, Object>> add = new ArrayList<>();
            Map<String, Object> address = new HashMap<>();
                address.put("HouseNo", getStringData("houseNo"));
                address.put("Street", getStringData("street"));
                address.put("Barangay", getStringData("barangay"));
                address.put("City", getStringData("city"));
                address.put("Province", getStringData("province"));
                address.put("District", getStringData("district"));
                address.put("ZipCode", getStringData("zipCode"));
                address.put("Country", getStringData("country"));
                address.put("CurrentAddress", true);
        add.add(address);
        profileData.put("Address", add);

        return profileData;
    }

    public enum Type { LearningCenter, Educator, Student }

    public static void addData(String key, Object value) {
        data.put(key, value);
    }

    public static boolean hasKey(String key) {
        return data.containsKey(key);
    }

    public static boolean hasValue(String value) {
        return data.containsValue(value);
    }

    public static void clearData() {
        data.clear();
    }

    public static Map<String, Object> getData() {
        return data;
    }

    public static String getStringData(String key) {
        if(data.containsKey(key)) {
            return data.get(key).toString();
        }
        return "";
    }

    public static Timestamp getTimeStampData(String key) {
        if(data.containsKey(key)) {
            return (Timestamp) data.get(key);
        }
        return null;
    }

    public static Uri getUriData(String key) {
        if(data.containsKey(key)) {
            return (Uri) data.get(key);
        }
        return null;
    }

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
