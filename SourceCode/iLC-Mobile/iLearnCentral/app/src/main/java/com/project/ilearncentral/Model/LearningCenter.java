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
    private List<String> followers;
    private List<String> following;
    private double rating;
    private Map<String, Integer> ratings;
    private static List<LearningCenter> retrieved = new ArrayList<>();

    public LearningCenter() {
        businessName = businessAddress = companyWebsite = contactEmail = contactNumber = description = serviceType = logo = "";
        following = new ArrayList<>();
        followers = new ArrayList<>();
        rating = 0;
        ratings = new HashMap<>();
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

    public List<String> getFollowers() {
        return followers;
    }

    public boolean isFollower(String id) {
        if (getFollowerPosition(id) == -1)
            return false;
        return true;
    }

    public void addFollower(String id) {
        followers.add(id);
    }

    public void addFollower(List<String> list) {
        followers.addAll(list);
    }

    public int getFollowerPosition(String id) {
        if (followers!=null) {
            for (int i = 0; i < followers.size(); i++) {
                if (followers.get(i).equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setFollowers(List<String> followers) {
        if (followers!=null)
            this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public boolean isFollowing(String id) {
        if (getFollowingPosition(id) == -1)
            return false;
        return true;
    }

    public void addFollowing(String id) {
        following.add(id);
    }

    public void addFollowing(List<String> list) {
        following.addAll(list);
    }

    public int getFollowingPosition(String id) {
        if (following!=null) {
            for (int i = 0; i < following.size(); i++) {
                if (following.get(i).equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setFollowing(List<String> following) {
        if (following!= null)
            this.following = following;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Map<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Integer> ratings) {
        this.ratings = ratings;
        setRating();
    }

    public void addRating(String username, int rating) {
        ratings.put(username, rating);
        setRating();
    }

    public void addRating(Map<String, Integer> r) {
        for (Map.Entry entry : r.entrySet()) {
            ratings.put(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString()));
        }
        setRating();
    }

    private void setRating() {
        if (ratings!=null && ratings.size()>0) {
            rating = 0;
            for (Map.Entry entry : ratings.entrySet()) {
                rating += Double.valueOf(entry.getValue()+"");
            }
            rating /= ratings.size();
            System.out.println("Rating set " + rating + " " + ratings.size());
        }
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
        following = lc.getFollowing();
        followers = lc.getFollowers();
        rating = lc.getRating();
        ratings = lc.getRatings();
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
