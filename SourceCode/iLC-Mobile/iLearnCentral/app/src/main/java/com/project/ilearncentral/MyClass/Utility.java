package com.project.ilearncentral.MyClass;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.Payment;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomBehavior.ObservableObject;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Utility {

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser user = mAuth.getCurrentUser();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public static int screenWidth, screenHeight;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    public static void buttonWait(Button button, boolean wait) {
        buttonWait(button, wait, "");
    }

    public static void buttonWait(Button button, boolean wait, String continueText) {
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
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String capsEachWord(String text) {
        String[] words = text.split("\\s");
        String capText = "";
        for (String word : words) {
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

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String getDayString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) + "";
    }

    public static String getDateAsString(Date date) {
        return dateFormat.format(date);
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
                        if (address.length() > 1 && address.charAt(0) == ',')
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
                                            }
                                        }
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

    public static String getStringFromDateFull(Timestamp value) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        return format.format(value.toDate());
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

    public static String getStringFromDay(Timestamp value) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        return dayFormat.format(value.toDate());
    }

    // convert integer to pixel
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    // convert pixel to integer
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static List<Map<String, Object>> cloneMapList(List<Map<String, Object>> data) {
        List<Map<String, Object>> newData = new ArrayList<>();
        for (Map<String, Object> map : data) {
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
        for (String s : data) {
            newData.add(s);
        }
        return newData;
    }

    public static String processCount(List<String> list) {
        if (list != null) {
            double in = (double) list.size();
            String s;
            if (in >= 1000000000) {
                s = in / 1000000000 + "";
                return (s.substring(0, 4) + "B");
            } else if (in >= 100000000) {
                s = in / 1000000 + "";
                return (s.substring(0, 3) + "M");
            } else if (in >= 10000000) {
                s = in / 1000000 + "";
                return (s.substring(0, 5) + "M");
            } else if (in >= 1000000) {
                s = in / 1000000 + "";
                return (s.substring(0, 4) + "M");
            } else if (in >= 100000) {
                s = in / 1000 + "";
                return (s.substring(0, 3) + "K");
            } else if (in >= 10000) {
                s = in / 1000 + "";
                return (s.substring(0, 5) + "K");
            } else if (in >= 1000) {
                s = in / 1000 + "";
                return (s.substring(0, 4) + "K");
            } else
                return ((int) in + "");
        }
        return "0";
    }

    public static void follow(User u) {
        if (Account.me.getFollowing().size() > 0)
            db.collection("User").document(user.getUid()).update("Following", FieldValue.arrayUnion(u.getUsername()));
        else {
            db.collection("User").document(user.getUid()).update("Following", Account.me.getFollowing());
        }
        if (u.getFollowers().size() > 0)
            db.collection("User").document(u.getUserId()).update("Followers", FieldValue.arrayUnion(Account.me.getUsername()));
        else {
            db.collection("User").document(u.getUserId()).update("Followers", u.getFollowers());
        }
    }

    public static void follow(LearningCenter lc) {
        if (Account.me.getFollowing().size() > 0)
            db.collection("User").document(user.getUid()).update("Following", FieldValue.arrayUnion(lc.getCenterId()));
        else {
            db.collection("User").document(user.getUid()).update("Following", Account.me.getFollowing());
        }
        if (lc.getFollowers().size() > 0)
            db.collection("LearningCenter").document(lc.getCenterId()).update("Followers", FieldValue.arrayUnion(Account.me.getUsername()));
        else {
            db.collection("LearningCenter").document(lc.getCenterId()).update("Followers", lc.getFollowers());
        }
    }

    public static void unfollow(User u) {
        db.collection("User").document(user.getUid()).update("Following", FieldValue.arrayRemove(u.getUsername()));
        db.collection("User").document(u.getUserId()).update("Followers", FieldValue.arrayRemove(Account.me.getUsername()));
    }

    public static void unfollow(LearningCenter lc) {
        db.collection("User").document(user.getUid()).update("Following", FieldValue.arrayRemove(lc.getCenterId()));
        db.collection("LearningCenter").document(lc.getCenterId()).update("Followers", FieldValue.arrayRemove(Account.me.getUsername()));
    }

    public static void rate(User u) {
        db.collection("User").document(u.getUserId()).update("Ratings", u.getRatings());
    }

    public static void rate(LearningCenter lc) {
        db.collection("LearningCenter").document(lc.getCenterId()).update("Ratings", lc.getRatings());
    }


    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        Utility.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        Utility.screenHeight = screenHeight;
    }

    public static void displayCurrencyInfoForLocale(Locale locale) {
        System.out.println("Locale: " + locale.getDisplayName());
        Currency currency = Currency.getInstance(locale);
        System.out.println("Currency Code: " + currency.getCurrencyCode());
        System.out.println("Symbol: " + currency.getSymbol());
        System.out.println("Default Fraction Digits: " + currency.getDefaultFractionDigits());
        System.out.println();
    }

    public static String showPriceInPHP(double price) {
//        NumberFormat php = NumberFormat.getCurrencyInstance();
//        php.setMinimumFractionDigits(0);
//        php.setMaximumFractionDigits(2);
//        php.setCurrency(Currency.getInstance("PHP"));
        Locale philippines = new Locale("en", "PH");
        NumberFormat phpFormat = NumberFormat.getCurrencyInstance(philippines);
        return phpFormat.format(price);
    }

    /**
     * Credit/Debit Card number checker.
     * @param cardNumber String
     * @return boolean
     */
    // Return true if the card number is valid
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber.isEmpty())
            return false;
        else {
            long cnumber = Long.parseLong(cardNumber.toString().replaceAll("[\\D.]", ""));
            return (getSize(cnumber) >= 13 && getSize(cnumber) <= 16) && (prefixMatch(cnumber, 4)
                    || prefixMatch(cnumber, 5) || prefixMatch(cnumber, 37) || prefixMatch(cnumber, 6))
                    && ((sumDoubleEven(cnumber) + sumOdd(cnumber)) % 10 == 0);
        }
    }

    // Get the result from Step 2
    private static int sumDoubleEven(long cardNumber) {
        int sum = 0;
        String num = cardNumber + "";
        for (int i = getSize(cardNumber) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);
        return sum;
    }

    // Return this cardNumber if it is a single digit, otherwise,
    // return the sum of the two digits
    private static int getDigit(int cardNumber) {
        if (cardNumber < 9)
            return cardNumber;
        return cardNumber / 10 + cardNumber % 10;
    }

    // Return sum of odd-place digits in cardNumber
    private static int sumOdd(long cardNumber) {
        int sum = 0;
        String num = cardNumber + "";
        for (int i = getSize(cardNumber) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for cardNumber
    private static boolean prefixMatch(long cardNumber, int digit) {
        return getPrefix(cardNumber, getSize(digit)) == digit;
    }

    // Return the number of digits in digit
    private static int getSize(long digit) {
        String num = digit + "";
        return num.length();
    }

    // Return the first i number of digits from
    // number. If the number of digits in number
    // is less than i, return number.
    private static long getPrefix(long cardNumber, int i) {
        if (getSize(cardNumber) > i) {
            String num = cardNumber + "";
            return Long.parseLong(num.substring(0, i));
        }
        return cardNumber;
    }
}
