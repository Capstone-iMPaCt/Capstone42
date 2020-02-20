package com.project.ilearncentral.Model;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account {

    private static String status = "";
    private static Type type;
    private static Map<String, Object> data = new HashMap<>();
    private static List<Map<String, Object>> accounts = new ArrayList<>();

    public enum Type {LearningCenter, Educator, Student}

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


    private static void setValidatedData(String oldKey, Map<String, Object> userData, String newKey) {
        if (userData.containsKey(oldKey)) {
            data.put(newKey, userData.get(oldKey));
        } else {
            data.put(newKey, null);
        }
    }

    public static void setUserData(Map<String, Object> userData) {
        if (userData != null) {
            if (userData.containsKey("AccountType"))
                setType(userData.get("AccountType").toString());
            String[] oldKeys = {"AccountStatus", "ContactNo", "Email", "Username", "Image", "Question", "Answer"};
            String[] newKeys = {"accountStatus", "contactNo", "email", "username", "image", "question", "answer"};
            for (int i = 0; i < oldKeys.length; i++) {
                setValidatedData(oldKeys[i], userData, newKeys[i]);
            }
        }
    }

    public static Map<String, Object> getUserData() {
        Map<String, Object> userData = new HashMap<>();
        try {
            if (hasKey("accountStatus"))
                userData.put("AccountStatus", "accountStatus");
            else
                userData.put("AccountStatus", "active");
            userData.put("AccountType", type.toString().toLowerCase());
            userData.put("Email", getStringData("email"));
            userData.put("ContactNo", getStringData("contactNo"));
            userData.put("Username", getStringData("username"));
            userData.put("Image", data.get("image"));
            userData.put("Question", getStringData("question"));
            userData.put("Answer", getStringData("answer"));
        } catch (Exception e) {
            return null;
        }
        return userData;
    }

    public static void temp(final String collection) {
        //temporary method to create bulk changes in database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference ref = db.collection(collection).document(document.getId());
                                List<Map<String, Object>> addressList = (List<Map<String, Object>>) document.get("Address");
                                ref.update("Address", addressList.get(0))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        } else {
                        }
                    }
                });

    }

    public static void setProfileData(Map<String, Object> profileData) {
        if (profileData != null) {
            if (type == Type.Educator) {
                setValidatedData("Position", profileData, "position");
                setValidatedData("EmploymentStatus", profileData, "employmentStatus");
            } else if (type == Type.Student) {
                setValidatedData("EnrolmentStatus", profileData, "enrolmentStatus");
            } else if (type == Type.LearningCenter) {
                setValidatedData("AccessLevel", profileData, "accessLevel");
            }
            String[] oldKeys = {"Username", "Religion", "Citizenship", "Gender", "MaritalStatus", "Birthday", "CenterID"};
            String[] newKeys = {"username", "religion", "citizenship", "gender", "maritalStatus", "birthday", "centerId"};
            for (int i = 0; i < oldKeys.length; i++) {
                setValidatedData(oldKeys[i], profileData, newKeys[i]);
            }
            if (profileData.containsKey("Name")) {
                Map<String, Object> name = ((Map<String, Object>) profileData.get("Name"));
                setValidatedData("FirstName", name, "firstName");
                setValidatedData("MiddleName", name, "middleName");
                setValidatedData("LastName", name, "lastName");
                setValidatedData("Extension", name, "extension");
            }
            if (profileData.containsKey("Address")) {
                Map<String, Object> address = ((Map<String, Object>) profileData.get("Address"));
                setValidatedData("HouseNo", address, "houseNo");
                setValidatedData("Street", address, "street");
                setValidatedData("Barangay", address, "barangay");
                setValidatedData("City", address, "city");
                setValidatedData("Province", address, "province");
                setValidatedData("District", address, "district");
                setValidatedData("ZipCode", address, "zipCode");
                setValidatedData("Country", address, "country");
            }
        }
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
        profileData.put("Gender", getStringData("gender"));
        profileData.put("MaritalStatus", getStringData("maritalStatus"));
        profileData.put("Birthday", getTimeStampData("birthday"));
        if (hasKey("centerId"))
            profileData.put("CenterID", getStringData("centerId"));
        else
            profileData.put("CenterID", "");
        Map<String, Object> address = new HashMap<>();
        address.put("HouseNo", getStringData("houseNo"));
        address.put("Street", getStringData("street"));
        address.put("Barangay", getStringData("barangay"));
        address.put("City", getStringData("city"));
        address.put("Province", getStringData("province"));
        address.put("District", getStringData("district"));
        address.put("ZipCode", getStringData("zipCode"));
        address.put("Country", getStringData("country"));
        profileData.put("Address", address);
        if (type == Type.Educator) {
            if (hasKey("position"))
                profileData.put("Position", "position");
            else
                profileData.put("Position", "none");
            if (hasKey("employmentStatus"))
                profileData.put("EmploymentStatus", "employmentStatus");
            else
                profileData.put("EmploymentStatus", "none");
        } else if (type == Type.Student) {
            if (hasKey("enrolmentStatus"))
                profileData.put("EnrolmentStatus", "enrolmentStatus");
            else
                profileData.put("EnrolmentStatus", "none");
        } else if (type == Type.LearningCenter) {
            profileData.put("AccessLevel", getStringData("accessLevel"));
        }

        return profileData;
    }

    public static void setBusinessData(Map<String, Object> businessData) {
        if (businessData != null) {
            String[] oldKeys = {"BusinessName", "CompanyWebsite", "ContactEmail", "ContactNumber", "ServiceType", "ClosingTime", "OpeningTime", "Logo"};
            String[] newKeys = {"bName", "bWebsite", "bEmail", "bContactNumber", "bServiceType", "bClosingTime", "bOpeningTime", "bLogo"};
            for (int i = 0; i < oldKeys.length; i++) {
                setValidatedData(oldKeys[i], businessData, newKeys[i]);
            }
            if (businessData.containsKey("BusinessAddress")) {
                Map<String, Object> address = ((Map<String, Object>) businessData.get("BusinessAddress"));
                setValidatedData("HouseNo", address, "bHouseNo");
                setValidatedData("Street", address, "bStreet");
                setValidatedData("Barangay", address, "bBarangay");
                setValidatedData("City", address, "bCity");
                setValidatedData("Province", address, "bProvince");
                setValidatedData("District", address, "bDistrict");
                setValidatedData("ZipCode", address, "bZipCode");
                setValidatedData("Country", address, "bCountry");
            }

            accounts.clear();
            accounts.addAll((List<Map<String, Object>>) businessData.get("Accounts"));
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).get("Username").toString().equals(Account.getStringData("Username"))) {
                    setValidatedData("AccessLevel", accounts.get(i), "accessLevel");
                    setValidatedData("Status", accounts.get(i), "centerStatus");
                }
            }
        }
    }

    public static Map<String, Object> getBusinessData() {
        Map<String, Object> businessData = new HashMap<>();
        businessData.put("BusinessName", getStringData("bName"));
        businessData.put("CompanyWebsite", getStringData("bWebsite"));
        businessData.put("ContactEmail", getStringData("bEmail"));
        businessData.put("ContactNumber", getStringData("bContactNumber"));
        businessData.put("ServiceType", getStringData("bServiceType"));
        Map<String, Object> address = new HashMap<>();
        address.put("HouseNo", getStringData("bHouseNo"));
        address.put("Street", getStringData("bStreet"));
        address.put("Barangay", getStringData("bBarangay"));
        address.put("City", getStringData("bCity"));
        address.put("Province", getStringData("bProvince"));
        address.put("District", getStringData("bDistrict"));
        address.put("ZipCode", getStringData("bZipCode"));
        address.put("Country", getStringData("bCountry"));
        businessData.put("BusinessAddress", address);
        businessData.put("ClosingTime", getTimeStampData("bClosingTime"));
        businessData.put("OpeningTime", getTimeStampData("bOpeningTime"));
        businessData.put("Logo", data.get("bLogo"));

        if (accounts.isEmpty()) {
            Map<String, Object> account = new HashMap<>();
            account.put("AccessLevel", "administrator");
            account.put("Status", "active");
            account.put("Username", getStringData("username"));
            accounts.add(account);
        }
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

    public static String getAddress() {
        String address = "";
        if (data.containsKey("houseNo"))
            address += data.get("houseNo") + " ";
        if (data.containsKey("street"))
            address += data.get("street") + ", ";
        if (data.containsKey("barangay"))
            address += data.get("barangay") + ", ";
        if (data.containsKey("city"))
            address += data.get("city") + ", ";
        if (data.containsKey("district"))
            address += data.get("district") + " ";
        if (data.containsKey("province"))
            address += data.get("province") + ", ";
        if (data.containsKey("country"))
            address += data.get("country") + ", ";
        if (data.containsKey("zipCode"))
            address += data.get("zipCode") + ", ";
        return address;
    }
    public static String getBusinessAddress() {
        String address = "";
        if (data.containsKey("bHouseNo"))
            address += data.get("bHouseNo") + " ";
        if (data.containsKey("bStreet"))
            address += data.get("bHtreet") + ", ";
        if (data.containsKey("bBarangay"))
            address += data.get("bBarangay") + ", ";
        if (data.containsKey("bCity"))
            address += data.get("bCity") + ", ";
        if (data.containsKey("bDistrict"))
            address += data.get("bDistrict") + " ";
        if (data.containsKey("bProvince"))
            address += data.get("bProvince") + ", ";
        if (data.containsKey("bCountry"))
            address += data.get("bCountry") + ", ";
        if (data.containsKey("bZipCode"))
            address += data.get("bZipCode") + ", ";
        return address;
    }

    public static void clearData() {
        data.clear();
    }

    public static void clear() {
        type = Type.Student;
        status = "";
        data.clear();
        accounts.clear();
    }

    public static Map<String, Object> getData() {
        return data;
    }

    public static String getStringData(String key) {
        if (data.containsKey(key)) {
            if (data.get(key) == null)
                return "";
            return data.get(key).toString();
        }
        return "";
    }

    public static Timestamp getTimeStampData(String key) {
        if (data.containsKey(key) && data.get(key) != null) {
            return (Timestamp) data.get(key);
        }
        return null;
    }

    public static Date getDateData(String key) {
        if (data.containsKey(key)) {
            return ((Timestamp) data.get(key)).toDate();
        }
        return null;
    }

    public static Uri getUriData(String key) {
        if (data.containsKey(key) && data.get(key) != null) {
            return Uri.parse(data.get(key).toString());
        }
        return null;
    }

    public static Type getType() {
        return type;
    }

    public static boolean isType(String newType) {
        return (type + "").equals(newType);
    }

    public static void setType(Type type) {
        Account.type = type;
    }

    public static void setType(String stringType) {
        switch (stringType) {
            case "learningcenter":
                type = Type.LearningCenter;
                break;
            case "educator":
                type = Type.Educator;
                break;
            case "student":
                type = Type.Student;
                break;
            default:
                type = Type.Student;
                break;
        }
    }

    public static Object get(String key) {
        if (data.containsKey(key))
            return  data.get(key);
        return null;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Account.status = status;
    }

    public static List<Map<String, Object>> getAccounts() {
        return accounts;
    }

    public static void setAccounts(List<Map<String, Object>> accounts) {
        Account.accounts = accounts;
    }
}
