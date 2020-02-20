package com.project.ilearncentral.Activity.Update;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnStringChangeListener;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateLearningCenter extends AppCompatActivity {

    private String TAG = "SIGNUP_CENTER";
    private String buttonText = "Update";
    TextInputEditText nameInput, websiteInput, emailInput, contactInput,
            timeStartInput, timeEndInput, otherServiceTypeInput,
            houseNoInput, streetInput, barangayInput, cityInput,
            provinceInput, districtInput, zipCodeInput;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Spinner countryInput, serviceTypeInput;
    ImageView logo;
    CircleImageView changeImage;
    private Button updateButton;

    String name, website, contact, email, timeStart, timeEnd, otherServiceType,
        houseNo, street, barangay, city, province, district, zipCode,
        mon, tue, wed, thu, fri, sat, sun, country, serviceType;
    List<String> operatingDays;
    Timestamp start, end;

    private FirebaseFirestore db;

    private ImageHandler imageHandler;
    private ObservableString imageDone;
    private int FINISH = 1;
    private boolean withImage, updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_learning_center);

        res();
        setValues();

        updateButton.setText(buttonText);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHandler.selectImage();
            }
        });
        serviceTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(serviceTypeInput.getItemAtPosition(position).equals("Others")) {
                    otherServiceTypeInput.setVisibility(View.VISIBLE);
                } else {
                    otherServiceTypeInput.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        imageDone.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String uri) {
                if (!uri.isEmpty()) {
                    Account.addData("bLogo", uri);
                    DocumentReference lcRef = db.collection("LearningCenter").document(Account.getStringData("centerId"));
                    lcRef
                        .update("Logo", uri)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updated = true;
                                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT)
                                        .show();
                                Utility.buttonWait(updateButton, false, buttonText);
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error Updating Logo!", Toast.LENGTH_SHORT)
                                        .show();
                                Utility.buttonWait(updateButton, false, buttonText);
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
                } else {
                    Utility.buttonWait(updateButton, false, buttonText);
                }
            }
        });
    }

    public void centerSignUpContinue(final View v) {
        Utility.buttonWait((Button)v, true);
        if (checkErrors()) {
            db.collection("LearningCenter").document(Account.getStringData("centerId")).set(Account.getBusinessData())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(withImage) {
                                imageHandler.uploadImage("images", Account.getStringData("centerId"), imageDone);
                            } else {
                                updated = true;
                                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT)
                                        .show();
                                Utility.buttonWait((Button) v, false, buttonText);
                                finish();
                            }
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                            Utility.buttonWait((Button)v, false, buttonText);
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    private boolean checkErrors() {
        boolean valid = true;

        name = nameInput.getText().toString();
        website = websiteInput.getText().toString();
        contact = contactInput.getText().toString();
        email = emailInput.getText().toString();
        timeStart = timeStartInput.getText().toString();
        timeEnd = timeEndInput.getText().toString();
        otherServiceType = otherServiceTypeInput.getText().toString();
        houseNo = houseNoInput.getText().toString();
        street = streetInput.getText().toString();
        barangay = barangayInput.getText().toString();
        city = cityInput.getText().toString();
        province = provinceInput.getText().toString();
        district = districtInput.getText().toString();
        zipCode = zipCodeInput.getText().toString();
        mon = monday.getText().toString();
        tue = tuesday.getText().toString();
        wed = wednesday.getText().toString();
        thu = thursday.getText().toString();
        fri = friday.getText().toString();
        sat = saturday.getText().toString();
        sun = sunday.getText().toString();
        country = countryInput.getSelectedItem().toString();
        serviceType = serviceTypeInput.getSelectedItem().toString();
        operatingDays.clear();

        if(monday.isChecked())
            operatingDays.add(mon);
        if(tuesday.isChecked())
            operatingDays.add(tue);
        if(wednesday.isChecked())
            operatingDays.add(wed);
        if(thursday.isChecked())
            operatingDays.add(thu);
        if(friday.isChecked())
            operatingDays.add(fri);
        if(saturday.isChecked())
            operatingDays.add(sat);
        if(sunday.isChecked())
            operatingDays.add(sun);

        if (operatingDays.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No operating days selected", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (name.isEmpty()) {
            nameInput.setError("Business Name is empty");
            valid = false;
        }
        if (contact.isEmpty()) {
            contactInput.setError("Contact is empty");
            valid = false;
        }
        if (barangay.isEmpty()) {
            barangayInput.setError("Barangay is empty");
            valid = false;
        }
        if (city.isEmpty()) {
            cityInput.setError("City is empty");
            valid = false;
        }
        if (province.isEmpty()) {
            provinceInput.setError("Province is empty");
            valid = false;
        }

        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            start = new Timestamp(format.parse(timeStart));
        } catch (ParseException ex) {
            start = null;
        }
        try {
            end = new Timestamp(format.parse(timeEnd));
        } catch (ParseException ex) {
            end = null;
        }
        if(start!=null && start.compareTo(end) >= 0) {
            Toast.makeText(getApplicationContext(), "Time End should be after start", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (serviceType.equals("Others") && otherServiceType.isEmpty()) {
            otherServiceTypeInput.setError("Service Type is Empty");
            valid = false;
        }

        if (serviceTypeInput.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please select service Type", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if(valid) {
            retrieveValues();
        }
        return valid;
    }

    private void retrieveValues() {
        Account.addData("bName", name);
        Account.addData("bWebsite", website);
        Account.addData("bEmail", email);
        Account.addData("bContactNumber", contact);
        Account.addData("bOpeningTime", start);
        Account.addData("bClosingTime", end);
        Account.addData("bOperatingDays", operatingDays);
        if (serviceType.equals("Others"))
            Account.addData("bServiceType", otherServiceType);
        else
            Account.addData("bServiceType", serviceType);
        Account.addData("bHouseNo", houseNo);
        Account.addData("bStreet", street);
        Account.addData("bBarangay", barangay);
        Account.addData("bCity", city);
        Account.addData("bProvince", province);
        Account.addData("bDistrict", district);
        Account.addData("bZipCode", zipCode);
        Account.addData("bCountry", country);
        Account.addData("accessLevel", Account.getStringData("accessLevel"));
    }

    private void setValues() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        nameInput.setText(Account.getStringData("bName"));
        websiteInput.setText(Account.getStringData("bWebsite"));
        emailInput.setText(Account.getStringData("bEmail"));
        contactInput.setText(Account.getStringData("bContactNumber"));
        if (Account.hasKey("bOpeningTime"))
            timeStartInput.setText(format.format(Account.getTimeStampData("bOpeningTime").toDate()));
        if (Account.hasKey("bClosingTime"))
            timeEndInput.setText(format.format(Account.getTimeStampData("bClosingTime").toDate()));
        List <String> list = new ArrayList<>();
        for(String s:getResources().getStringArray(R.array.service_types)) {
            list.add(s);
        }
        int position = list.indexOf(Account.getStringData("bServiceType"));
        if (position<0) {
            serviceTypeInput.setSelection(list.indexOf("Others"));
            otherServiceTypeInput.setText(Account.getStringData("bServiceType"));
            otherServiceTypeInput.setVisibility(View.VISIBLE);
        } else {
            serviceTypeInput.setSelection(list.indexOf(Account.getStringData("bServiceType")));
        }
        houseNoInput.setText(Account.getStringData("bHouseNo"));
        streetInput.setText(Account.getStringData("bStreet"));
        barangayInput.setText(Account.getStringData("bBarangay"));
        cityInput.setText(Account.getStringData("bCity"));
        provinceInput.setText(Account.getStringData("bProvince"));
        districtInput.setText(Account.getStringData("bDistrict"));
        zipCodeInput.setText(Account.getStringData("bZipCode"));
        list.clear();
        for(String s:getResources().getStringArray(R.array.countries2)) {
            list.add(s);
        }
        countryInput.setSelection(list.indexOf(Account.getStringData("bCountry")));
        if (Account.hasKey("bLogo")) {
            imageHandler.setFilePath(Account.getUriData("bLogo"));
            imageHandler.setImage("images", Account.getStringData("centerId"), logo);
        }
    }

    private void res() {
        nameInput = findViewById(R.id.sign_up_business_name_lc);
        websiteInput = findViewById(R.id.sign_up_website_lc);
        emailInput = findViewById(R.id.sign_up_email_lc);
        contactInput = findViewById(R.id.sign_up_contact_lc);
        timeStartInput = findViewById(R.id.sign_up_time_open_lc);
        timeEndInput = findViewById(R.id.sign_up_time_close_lc);
        otherServiceTypeInput = findViewById(R.id.sign_up_other_service_lc);
        houseNoInput = findViewById(R.id.sign_up_house_no_lc);
        streetInput = findViewById(R.id.sign_up_street_lc);
        barangayInput = findViewById(R.id.sign_up_barangay_lc);
        cityInput = findViewById(R.id.sign_up_city_lc);
        provinceInput = findViewById(R.id.sign_up_province_lc);
        districtInput = findViewById(R.id.sign_up_district_lc);
        zipCodeInput = findViewById(R.id.sign_up_zip_code_lc);
        monday = findViewById(R.id.sign_up_monday_lc);
        tuesday = findViewById(R.id.sign_up_tuesday_lc);
        wednesday = findViewById(R.id.sign_up_wednesday_lc);
        thursday = findViewById(R.id.sign_up_thursday_lc);
        friday = findViewById(R.id.sign_up_friday_lc);
        saturday = findViewById(R.id.sign_up_saturday_lc);
        sunday = findViewById(R.id.sign_up_sunday_lc);
        countryInput = findViewById(R.id.sign_up_country_lc);
        serviceTypeInput = findViewById(R.id.sign_up_service_type_lc);
        logo = findViewById(R.id.sign_up_image_lc);
        changeImage = findViewById(R.id.sign_up_image_change_lc);
        updateButton = findViewById(R.id.sign_up_button_lc);
        db = FirebaseFirestore.getInstance();
        withImage = updated = false;
        imageHandler = new ImageHandler(this, UpdateLearningCenter.this);
        imageDone = new ObservableString();
        name = website = contact = email = timeStart = timeEnd = otherServiceType =
            houseNo = street = barangay = city = province = district = zipCode =
            mon = tue = wed = thu = fri = sat = sun = country = serviceType = "";
        operatingDays = new ArrayList<>();
    }

    public void inputTime(final View v) {

        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog dialog =
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = "";
                        if (hourOfDay == 0) {
                            time += 12;
                        } else if (hourOfDay <= 12){
                            time += hourOfDay;
                        }else {
                            time += (hourOfDay - 12);
                        }
                        if (minute<10)
                            time += ":0" + minute;
                        else
                            time += ":" + minute;
                        if (hourOfDay<12)
                            time += " am";
                        else
                            time += " pm";
                        ((TextInputEditText) v).setText(time);
                    }
                }, mHour, mMinute, false);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FINISH && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else
            withImage = imageHandler
                    .onActivityResult(requestCode, resultCode, data, null, logo, "bLogo");
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        imageHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (updated)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        //retrieveData();
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
