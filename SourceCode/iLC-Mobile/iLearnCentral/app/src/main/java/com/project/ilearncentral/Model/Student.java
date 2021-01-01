package com.project.ilearncentral.Model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
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

public class Student extends User {
    private String stuId, centerId, username, enrolmentStatus;
    private String fullname, firstName, lastName, middleName, extension;
    private String address, citizenship, gender, martitalStatus, religion;
    private Timestamp birthday, enrolmentDate;
    private static List<Student> retrieved = new ArrayList<>();

    public Student() {
        super();
        stuId = centerId = username = enrolmentStatus = "";
        fullname = firstName = lastName = middleName = extension = "";
        address = citizenship = gender = martitalStatus = religion = "";
        birthday = Timestamp.now();
        enrolmentDate = null;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
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

    public String getEnrolmentStatus() {
        return enrolmentStatus;
    }

    public void setEnrolmentStatus(String enrolmentStatus) {
        this.enrolmentStatus = enrolmentStatus;
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

    public Timestamp getEnrolmentDate() {
        return enrolmentDate;
    }

    public void setEnrolmentDate(Timestamp enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public void setStudent(Student student) {
        stuId = student.getStuId();
        centerId = student.getCenterId();
        fullname = student.getFullname();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        middleName = student.getMiddleName();
        extension = student.getExtension();
        username = student.getUsername();
        enrolmentStatus = student.getEnrolmentStatus();
        address = student.getAddress();
        citizenship = student.getCitizenship();
        gender = student.getGender();
        martitalStatus = student.getMartitalStatus();
        religion = student.getReligion();
        birthday = student.getBirthday();
        enrolmentDate = student.getEnrolmentDate();
    }

    public void setUser(User user) {
        super.setUser(user);
    }

    public static void retrieveStudentsFromDB(final ObservableBoolean done, String query, String value) {
        CollectionReference db = FirebaseFirestore.getInstance().collection("Student");
        if (query.isEmpty()) {
            db.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        onCompleteRetrieve(done, task);
                    }
                });
        } else if (query.equalsIgnoreCase("centerId")) {
            db.whereEqualTo("CenterId", value).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        onCompleteRetrieve(done, task);
                    }
                });
        }
    }

    public static void onCompleteRetrieve (final ObservableBoolean done, final Task<QuerySnapshot> task) {

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Student stu = new Student();
                stu.setStuId(document.getId());
                stu.setCenterId(document.getString("CenterID"));
                stu.setUsername(document.getString("Username"));
                stu.setEnrolmentStatus(document.getString("EnrolmentStatus"));
                stu.setCitizenship(document.getString("Citizenship"));
                stu.setGender(document.getString("Gender"));
                stu.setMartitalStatus(document.getString("MaritalStatus"));
                stu.setReligion(document.getString("Religion"));
                stu.setBirthday(document.getTimestamp("Birthday"));
                if (!(stu.getEnrolmentStatus().equals("none") || stu.getEnrolmentStatus().isEmpty()))
                    stu.setEnrolmentDate(document.getTimestamp("EmploymentDate"));
                stu.setBirthday(document.getTimestamp("Birthday"));

                Map<String, String> nameData = ((Map<String, String >) document.get("Name"));
                String name = "";
                if (!nameData.get("FirstName").isEmpty()) {
                    name += " " + nameData.get("FirstName");
                    stu.setFirstName(nameData.get("FirstName"));
                }
                if (!nameData.get("MiddleName").isEmpty()) {
                    name += " " + nameData.get("MiddleName");
                    stu.setMiddleName(nameData.get("MiddleName"));
                }
                if (!nameData.get("LastName").isEmpty()) {
                    name += " " + nameData.get("LastName");
                    stu.setLastName(nameData.get("LastName"));
                }
                if (!nameData.get("Extension").isEmpty()) {
                    name += " " + nameData.get("Extension");
                    stu.setExtension(nameData.get("Extension"));
                }
                name = name.replaceAll("\\s", " ").trim();
                stu.setFullname(name);

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
                stu.setAddress(address);

                User user = User.getUserByUsername(stu.getUsername());
                if (user!=null)
                    stu.setUser(User.getUserByUsername(stu.getUsername()));

                int pos = getStuPositionById(document.getId());
                if (pos == -1) {
                    retrieved.add(stu);
                } else {
                    retrieved.get(pos).setStudent(stu);
                }
            }
            if (done!=null) done.set(true);
        } else {
            if (done!=null) done.set(false);
            Log.d("getStudents", "Error getting documents: ", task.getException());
        }
    }

    public static void setUsers() {
        for (Student student:retrieved) {
            User user = User.getUserByUsername(student.getUsername());
            if (user!=null)
                student.setUser(user);
        }
    }

    public static int getStuPositionById(String stuId) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getStuId().equals(stuId))
                return i;
        }
        return -1;
    }

    public static Student getStuByUsername(String username) {
        for (int i=0; i<retrieved.size();i++) {
            if (retrieved.get(i).getUsername().equals(username))
                return retrieved.get(i);
        }
        return null;
    }

    public static List<Student> getStudentsByEnrolment(boolean enrolled, String centerId) {
        List<Student> students = new ArrayList<>();
        for (Student student:retrieved) {
            if (enrolled==false && (student.getEnrolmentStatus().equals("none") || student.getEnrolmentStatus().isEmpty())) {
                students.add(student);
            } else if (enrolled==true && centerId.isEmpty() && !(student.getEnrolmentStatus().equals("none") || student.getEnrolmentStatus().isEmpty())) {
                students.add(student);
            } else if (enrolled==true && student.getCenterId().equals(centerId) && !(student.getEnrolmentStatus().equals("none") || student.getEnrolmentStatus().isEmpty())) {
                students.add(student);
            }
        }
        return students;
    }

    public static List<Student> getStudentsByCenterId(String centerId) {
        List<Student> students = new ArrayList<>();
        for (Student student:retrieved) {
            if (student.getCenterId().equalsIgnoreCase(centerId)) {
                students.add(student);
            }
        }
        return students;
    }
}
