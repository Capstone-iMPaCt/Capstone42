package com.project.ilearncentral.Model;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {

    private static String status = "";
    private static Type type;
    private static Map<String, Object> data = new HashMap<>();

    public enum Type { LearningCenter, Educator, Student }

    public static String getName() {
        String name = getStringData("firstName") + " ";
        if (!getStringData("middleName").isEmpty())
            name += getStringData("middleName").toUpperCase().charAt(0) + " ";
        name += getStringData("lastName");
        return name;
    }

    public static String getBusinessName() {
        return data.get("bName").toString();
    }

    public static Map<String, Object> getUserData() {
        Map<String, Object> userData = new HashMap<>();
        try {
            userData.put("AccountStatus", "active");
            userData.put("AccountType", type.toString().toLowerCase());
            userData.put("Email", getStringData("email"));
            userData.put("Username", getStringData("username"));
            userData.put("Image", data.get("image"));
            userData.put("Question", getStringData("question"));
            userData.put("Answer", getStringData("answer"));
        } catch(Exception e) {
            return null;
        }
        return userData;
    }

    public static Map<String, Object> getProfileData() {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("Username", getStringData("username"));
            Map<String, String> name = new HashMap<>();
                name.put("FirstName", getStringData("firstName"));
                name.put("MiddleName", getStringData("middleName"));
                name.put("LastName", getStringData("lastName"));
                name.put("Extension", getStringData("extension"));
            profileData.put("Name", name);
        profileData.put("Religion", getStringData("religion"));
        profileData.put("Citizenship", getStringData("citizenship"));
        profileData.put("EmailAddress", getStringData("email"));
        profileData.put("Gender", getStringData("gender"));
        profileData.put("MaritalStatus", getStringData("maritalStatus"));
        profileData.put("Birthday", getTimeStampData("birthday"));
        profileData.put("CenterID", "");
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
        if (type == Type.Educator) {
            profileData.put("Position", "none");
            profileData.put("EmploymentStatus", "none");
        } else if (type == Type.Student) {
            profileData.put("EnrolmentStatus", "none");
        } else if (type == Type.LearningCenter) {
            profileData.put("AccessLevel", getStringData("accessLevel"));
            profileData.put("CenterID", getStringData("centerId"));
        }

        return profileData;
    }

    public static Map<String, Object> getBusinessData() {
        Map<String, Object> businessData = new HashMap<>();
        businessData.put("BusinessName", getStringData("bName"));
        businessData.put("CompanyWebsite", getStringData("bWebsite"));
        businessData.put("ContactEmail", getStringData("bEmail"));
        businessData.put("ContactNumber", getStringData("bContactNumber"));
        businessData.put("ServiceType", getStringData("bServiceType"));
        List<Map<String, Object>> add = new ArrayList<>();
            Map<String, Object> address = new HashMap<>();
                address.put("HouseNo", getStringData("bHouseNo"));
                address.put("Street", getStringData("bStreet"));
                address.put("Barangay", getStringData("bBarangay"));
                address.put("City", getStringData("bCity"));
                address.put("Province", getStringData("bProvince"));
                address.put("District", getStringData("bDistrict"));
                address.put("ZipCode", getStringData("bZipCode"));
                address.put("Country", getStringData("bCountry"));
            add.add(address);
        businessData.put("BusinessAddress", add);
        businessData.put("ClosingTime", getTimeStampData("bClosingTime"));
        businessData.put("OpeningTime", getTimeStampData("bOpeningTime"));

        List<Map<String, Object>> accounts = new ArrayList<>();
            Map<String, Object> account = new HashMap<>();
                account.put("AccessLevel", "administrator");
                account.put("Status", "active");
                account.put("Username", getStringData("username"));
            accounts.add(account);
        businessData.put("Accounts", accounts);
        return businessData;
    }

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

    public static Date getDateData(String key) {
        if(data.containsKey(key)) {
            return ((Timestamp) data.get(key)).toDate();
        }
        return null;
    }

    public static Uri getUriData(String key) {
        if(data.containsKey(key)) {
            return Uri.parse(data.get(key).toString());
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
