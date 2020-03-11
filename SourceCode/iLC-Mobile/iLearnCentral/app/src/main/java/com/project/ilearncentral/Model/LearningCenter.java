package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class LearningCenter {
    private String centerId, businessName, businessAddress, companyWebsite, contactEmail, contactNumber, description, serviceType, logo;
    private List<Map<String, String>> accounts;
    private List<String> operatingDays;
    private Timestamp open, close;
    private boolean followedUser;
    private boolean followingMe;
    private static List<LearningCenter> retrieved = new ArrayList<>();

    public LearningCenter() {
        businessName = businessAddress = companyWebsite = contactEmail = contactNumber = description = serviceType = logo = "";
        followingMe = followedUser = false;
        accounts = new ArrayList<>();
        operatingDays = new ArrayList<>();
        open = close = Timestamp.now();
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Map<String, String>> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Map<String, String>> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Map<String, String> data) {
        accounts.add(data);
    }

    public List<String> getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(List<String> operatingDays) {
        this.operatingDays = operatingDays;
    }

    public void addOperatingDay(String value) {
        operatingDays.add(value);
    }

    public Timestamp getOpen() {
        return open;
    }

    public void setOpen(Timestamp open) {
        this.open = open;
    }

    public Timestamp getClose() {
        return close;
    }

    public void setClose(Timestamp close) {
        this.close = close;
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

    public void setLearningCenter(LearningCenter lc) {
        businessName = lc.getBusinessName();
        businessAddress = lc.getBusinessAddress();
        companyWebsite = lc.getCompanyWebsite();
        contactEmail = lc.getContactEmail();
        contactNumber = lc.getContactNumber();
        description = lc.getDescription();
        serviceType = lc.getServiceType();
        logo = lc.getLogo();
        followingMe = lc.isFollowingMe();
        followedUser = lc.isFollowedUser();
        accounts = lc.getAccounts();
        operatingDays = lc.getOperatingDays();
        open = lc.getOpen();
        close = lc.getClose();
    }

    public static List<LearningCenter> getRetrieved() {
        return retrieved;
    }

    public static void setRetrieved(List<LearningCenter> retrieved) {
        LearningCenter.retrieved = retrieved;
    }

    public static void retrieveLearningCentersFromDB() {
        FirebaseFirestore.getInstance().collection("LearningCenter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LearningCenter lc = new LearningCenter();
                                lc.setCenterId(document.getId());
                                lc.setBusinessName(document.getString("BusinessName"));
                                lc.setCompanyWebsite(document.getString("CompanyWebsite"));
                                lc.setContactEmail(document.getString("ContactEmail"));
                                lc.setContactNumber(document.getString("ContactNumber"));
                                lc.setDescription(document.getString("Description"));
                                lc.setServiceType(document.getString("ServiceType"));
                                lc.setLogo(document.getString("Logo"));
                                lc.setFollowedUser(false);
                                lc.setFollowingMe(false);
                                List <Object> accounts = (List<Object>) document.get("Accounts");
                                for (int i=0;i<accounts.size();i++) {
                                    Map <String, String> data = new HashMap<>();
                                    for (Map.Entry entry : ((Map<String, Object>) accounts.get(i)).entrySet()) {
                                        data.put(entry.getKey().toString(), entry.getValue()
                                                .toString());
                                    }
                                    lc.addAccount(data);
                                }
                                List<String> op = (List<String>) document.get("OperatingDays");
                                for(int i=0;i<op.size();i++) {
                                    lc.addOperatingDay(op.get(i));
                                }
                                lc.setClose(document.getTimestamp("ClosingTime"));
                                lc.setOpen(document.getTimestamp("OpeningTime"));

                                Map<String, String> addressData = ((Map<String, String>) document.get("BusinessAddress"));
                                String address = "";
                                if (!addressData.get("HouseNo").isEmpty())
                                    address += ", " + addressData.get("HouseNo");
                                if (!addressData.get("Street").isEmpty())
                                    address += ", " + addressData.get("Street");
                                if (!addressData.get("Barangay").isEmpty())
                                    address += ", " + addressData.get("Barangay");
                                if (!addressData.get("City").isEmpty())
                                    address += ", " + addressData.get("City");
                                if (!addressData.get("District").isEmpty())
                                    address += ", " + addressData.get("District");
                                if (!addressData.get("Province").isEmpty())
                                    address += ", " + addressData.get("Province");
                                if (!addressData.get("Country").isEmpty())
                                    address += ", " + addressData.get("Country");
                                if (!addressData.get("ZipCode").isEmpty())
                                    address += ", " + addressData.get("ZipCode");
                                if (address.length() > 1 && address.charAt(0) == ',')
                                    address = address.substring(1);
                                address.replaceAll("\\s", " ");
                                lc.setBusinessAddress(address);
                                int pos = getLCPositionById(document.getId());
                                if (pos == -1) {
                                    retrieved.add(lc);
                                } else {
                                    retrieved.get(pos).setLearningCenter(lc);
                                }
                            }
                        } else {
                            Log.d("getUsers", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static int getLCPositionById(String centerId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getCenterId().equals(centerId))
                return i;
        }
        return -1;
    }
    public static LearningCenter getLCById(String centerId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getCenterId().equals(centerId))
                return retrieved.get(i);
        }
        return null;
    }

}
