package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Educator extends User {
    private String eduId, centerId, username, employmentStatus, position;
    private String fullname, firstName, lastName, middleName, extension;
    private String address, citizenship, gender, martitalStatus, religion;
    private List<String> employmentType;
    private Timestamp birthday, employmentDate;
    private static List<Educator> retrieved = new ArrayList<>();

    public Educator() {
        super();
        eduId = centerId = username = employmentStatus = position = "";
        fullname = firstName = lastName = middleName = extension = "";
        address = citizenship = gender = martitalStatus = religion = "";
        employmentType = new ArrayList<>();
        birthday = Timestamp.now();
        employmentDate = null;
    }

    public String getEduId() {
        return eduId;
    }

    public void setEduId(String eduId) {
        this.eduId = eduId;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    @Override
    public String getFullname() {
        return fullname;
    }

    @Override
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMartitalStatus() {
        return martitalStatus;
    }

    public void setMartitalStatus(String martitalStatus) {
        this.martitalStatus = martitalStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Timestamp getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Timestamp employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setEducator(Educator educator) {
        eduId = educator.getEduId();
        centerId = educator.getCenterId();
        fullname = educator.getFullname();
        firstName = educator.getFirstName();
        lastName = educator.getLastName();
        middleName = educator.getMiddleName();
        extension = educator.getExtension();
        username = educator.getUsername();
        employmentStatus = educator.getEmploymentStatus();
        employmentType = educator.getEmploymentType();
        position = educator.getPosition();
        address = educator.getAddress();
        citizenship = educator.getCitizenship();
        gender = educator.getGender();
        martitalStatus = educator.getMartitalStatus();
        religion = educator.getReligion();
        birthday = educator.getBirthday();
        employmentDate = educator.getEmploymentDate();
    }

    public void setUser(User user) {
        super.setUser(user);
    }

    public static void retrieveEducatorsFromDB(final ObservableBoolean done) {
        FirebaseFirestore.getInstance().collection("Educator")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Educator edu = new Educator();
                        edu.setEduId(document.getId());
                        edu.setCenterId(document.getString("CenterID"));
                        edu.setUsername(document.getString("Username"));
                        edu.setPosition(document.getString("Position"));
                        edu.setEmploymentStatus(document.getString("EmploymentStatus"));
                        edu.setCitizenship(document.getString("Citizenship"));
                        edu.setGender(document.getString("Gender"));
                        edu.setMartitalStatus(document.getString("MaritalStatus"));
                        edu.setReligion(document.getString("Religion"));
                        edu.setBirthday(document.getTimestamp("Birthday"));
                        if (!(edu.getEmploymentStatus().equals("none") || edu.getEmploymentStatus().isEmpty()))
                            edu.setEmploymentDate(document.getTimestamp("EmploymentDate"));
                        edu.setBirthday(document.getTimestamp("Birthday"));

                        Map<String, String> nameData = ((Map<String, String >) document.get("Name"));
                        String name = "";
                        if (!nameData.get("FirstName").isEmpty()) {
                            name += " " + nameData.get("FirstName");
                            edu.setFirstName(nameData.get("FirstName"));
                        }
                        if (!nameData.get("MiddleName").isEmpty()) {
                            name += " " + nameData.get("MiddleName");
                            edu.setMiddleName(nameData.get("MiddleName"));
                        }
                        if (!nameData.get("LastName").isEmpty()) {
                            name += " " + nameData.get("LastName");
                            edu.setLastName(nameData.get("LastName"));
                        }
                        if (!nameData.get("Extension").isEmpty()) {
                            name += " " + nameData.get("Extension");
                            edu.setExtension(nameData.get("Extension"));
                        }
                        name = name.replaceAll("\\s", " ").trim();
                        edu.setFullname(name);

                        List<String> types = (List<String>) document.get("EmploymentType");
                        if (types !=null) {
                            for (int i = 0; i < types.size(); i++) {
                                edu.addEmploymentType(types.get(i));
                            }
                        }
                        Map<String, String> addressData = ((Map<String, String>) document.get("Address"));
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
                        edu.setAddress(address);

                        User user = User.getUserByUsername(edu.getUsername());
                        if (user!=null)
                            edu.setUser(User.getUserByUsername(edu.getUsername()));

                        int pos = getEduPositionById(document.getId());
                        if (pos == -1) {
                            retrieved.add(edu);
                        } else {
                            retrieved.get(pos).setEducator(edu);
                        }
                    }
                    if (done!=null) done.set(true);
                } else {
                    if (done!=null) done.set(false);
                    Log.d("getEducators", "Error getting documents: ", task.getException());
                }
                }
            });
    }

    private void addEmploymentType(String type) {
        if (employmentType!=null)
            employmentType.add(type);
    }

    public static void setUsers() {
        for (Educator educator:retrieved) {
            User user = User.getUserByUsername(educator.getUsername());
            if (user!=null)
                educator.setUser(user);
        }
    }

    public static int getEduPositionById(String eduId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getEduId().equals(eduId))
                return i;
        }
        return -1;
    }

    public static Educator getEduByUsername(String username) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getUsername().equals(username))
                return retrieved.get(i);
        }
        return null;
    }

    public static List<Educator> getEducatorsByEmployment(boolean employed, String centerId) {
        List<Educator> educators = new ArrayList<>();
        for (Educator educator:retrieved) {
            if (employed==false && (educator.getEmploymentStatus().equals("none") || educator.getEmploymentStatus().isEmpty())) {
                educators.add(educator);
            } else if (employed==true && centerId.isEmpty() && !(educator.getEmploymentStatus().equals("none") || educator.getEmploymentStatus().isEmpty())) {
                educators.add(educator);
            } else if (employed==true && educator.getCenterId().equals(centerId) && !(educator.getEmploymentStatus().equals("none") || educator.getEmploymentStatus().isEmpty())) {
                educators.add(educator);
            }
        }
        return educators;
    }

    public static List<Educator> getFilteredEducators(String centerId, int statusChecked, int typeChecked) {
        List<Educator> educators = new ArrayList<>();
        for (Educator educator:retrieved) {
            if (educator.getCenterId().equalsIgnoreCase(centerId)) {
                if ((statusChecked == R.id.lc_educators_search_option_active && educator.getEmploymentStatus().equalsIgnoreCase("Active")) ||
                (statusChecked == R.id.lc_educators_search_option_inactive && educator.getEmploymentStatus().equalsIgnoreCase("Inactive")) ||
                (typeChecked == R.id.lc_educators_search_option_fullTime && educator.getEmploymentType().contains("Full Time")) ||
                (typeChecked == R.id.lc_educators_search_option_partTime && educator.getEmploymentType().contains("Part Time")) ||
                (typeChecked == R.id.lc_educators_search_option_contractual && educator.getEmploymentType().contains("Contractual")) ||
                        (statusChecked == -1 && typeChecked == -1))
                    educators.add(educator);
            }
        }
        return educators;
    }

    public static List<Educator> getEducatorsByCenterId(String centerId) {
        List<Educator> educators = new ArrayList<>();
        for (Educator educator:retrieved) {
            if (educator.getCenterId().equalsIgnoreCase(centerId)) {
                educators.add(educator);
            }
        }
        return educators;
    }

    public static void hireEducator(Educator educator, String centerId, List<String> types, String position) {
        educator.setCenterId(centerId);
        educator.setEmploymentStatus("Active");
        educator.setEmploymentDate(Timestamp.now());
        educator.setEmploymentType(types);
        educator.setPosition(position);
        Map<String,Object> updates = new HashMap<>();
        updates.put("CenterID", educator.getCenterId());
        updates.put("EmploymentDate", educator.getEmploymentDate());
        updates.put("EmploymentStatus", educator.getEmploymentStatus());
        updates.put("EmploymentType", educator.getEmploymentType());
        updates.put("Position", educator.getPosition());
        FirebaseFirestore.getInstance().collection("Educator").document(educator.getEduId())
                .update(updates);
    }

    public List<String> getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(List<String> employmentType) {
        this.employmentType = employmentType;
    }
}
