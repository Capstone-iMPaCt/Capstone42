package com.project.ilearncentral.MyClass;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.CustomBehavior.ObservableObject;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.Model.Post;
import com.project.ilearncentral.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Utility {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    public static void buttonWait(Button button, boolean wait) {
        buttonWait(button, wait, "");
    } public static void buttonWait(Button button, boolean wait, String continueText) {
        if (wait) {
            button.setEnabled(false);
            if (continueText.isEmpty())
                button.setText("Please wait...");
            else
                button.setText(continueText);
        } else {
            button.setEnabled(true);
            button.setText(continueText);
        }
    }
    public static String caps(String text) {
        if (text.isEmpty()) {
            return "";
        } else if (text.length() == 1) {
            return text.toUpperCase();
        }
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    public static String capsEachWord(String text) {
        String[] words = text.split("\\s");
        String capText = "";
        for (String word:words) {
            capText += caps(word) + " ";
        }
        return capText.trim();
    }

    public static String formatFullName(String firstName, String middleName, String lastName) {
        String name = firstName + " ";
        if (!middleName.isEmpty())
            name += middleName.toUpperCase().charAt(0) + ". ";
        name += lastName;
        return name;
    }

    public static void getFullName(final String username, final ObservableString fullname) {
        db.collection("User").whereEqualTo("Username", username).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String collection = "";
                        if (document.get("AccountType").equals("learningcenter")) {
                            collection = "LearningCenterStaff";
                        } else if (document.get("AccountType").equals("educator")) {
                            collection = "Educator";
                        } else if (document.get("AccountType").equals("student")) {
                            collection = "Student";
                        }
                        db.collection(collection).whereEqualTo("Username", username)
                            .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, String> nameDB = (Map<String, String>) document.get("Name");
                                                fullname.set(formatFullName(nameDB.get("FirstName"), nameDB.get("MiddleName"), nameDB.get("LastName")));
                                            }
                                        } else {
                                        }
                                    }
                                });
                    }
                } else {
                }
            }
        });
    }

    public static void getBusinessName(final String centerId, final ObservableObject businessName) {
        db.collection("LearningCenter").document(centerId)
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, String> addressData = ((Map<String, String>) document.get("BusinessAddress"));
                    if (document.exists()) {
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
                        if (address.length()>1 && address.charAt(0)==',')
                            address = address.substring(1);
                        address.replaceAll("\\s", " ");
                        Map<String, String> details = new HashMap<>();
                        details.put("BusinessName", document.getString("BusinessName"));
                        details.put("CompanyWebsite", document.getString("CompanyWebsite"));
                        details.put("ContactEmail", document.getString("ContactEmail"));
                        details.put("ContactNumber", document.getString("ContactNumber"));
                        details.put("BusinessAddress", address);
                        businessName.set(details);
                    } else {
                    }
                } else {
                }
            }
        });
    }
    
    public static void updateProfileWithImage(final String TAG, final ObservableBoolean done) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
        storageRef.child("images/").child(Account.getUsername()).getDownloadUrl()
            .addOnSuccessListener(new OnSuccessListener<Uri>() {
            public void onSuccess(final Uri uri) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Account.getName())
                .setPhotoUri(uri)
                .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DocumentReference lcRef = db.collection("User").document(user.getUid());
                            lcRef
                                .update("Image", uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        done.set(true);
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        done.set(false);
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                            Log.d(TAG, "User profile updated.");
                        }}
                });
            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Account.getName())
                    .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    done.set(true);
                                }
                            }
                        });
            }
        });
        } catch (Exception e) {
            done.set(false);
        }
    }

    public static Timestamp getDateFromString(String value) {
        Timestamp t;
        try {
            t = new Timestamp(dateFormat.parse(value));
        } catch (ParseException e) {
            t = null;
        }
        return t;
    }

    public static String getStringFromDate(Timestamp value) {
        return dateFormat.format(value.toDate());
    }

    public static Timestamp getTimeFromString(String value) {
        Timestamp t;
        try {
            t = new Timestamp(timeFormat.parse(value));
        } catch (ParseException e) {
            t = null;
        }
        return t;
    }

    public static String getStringFromTime(Timestamp value) {
        return timeFormat.format(value.toDate());
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static List<Map<String, Object>> cloneMapList(List<Map<String, Object>> data) {
        List<Map<String, Object>> newData = new ArrayList<>();
        for (Map<String, Object> map: data) {
            Map<String, Object> newMap = new HashMap<>();
            for (Map.Entry entry : map.entrySet()) {
                newMap.put(entry.getKey().toString(), entry.getValue());
            }
            newData.add(newMap);
        }
        return newData;
    }
    public static List<String> cloneStringList(List<String> data) {
        List<String> newData = new ArrayList<>();
        for (String s: data) {
            newData.add(s);
        }
        return newData;
    }
}
