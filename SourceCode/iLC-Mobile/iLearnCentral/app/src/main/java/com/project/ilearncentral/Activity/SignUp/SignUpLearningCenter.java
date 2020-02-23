package com.project.ilearncentral.Activity.SignUp;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.Update.UpdateLearningCenter;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpLearningCenter extends AppCompatActivity {

    private String TAG = "SIGNUP_CENTER";
    TextInputEditText nameInput, websiteInput, emailInput, contactInput,
            timeStartInput, timeEndInput, otherServiceTypeInput,
            houseNoInput, streetInput, barangayInput, cityInput,
            provinceInput, districtInput, zipCodeInput;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Spinner countryInput, serviceTypeInput;
    ImageView logo;
    List<String> operatingDays;
    CircleImageView changeImage;

    private boolean withImage;

    private ImageHandler imageHandler;
    private ObservableString imageDone;
    private int FINISH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_learning_center);

        res();
        setValues();
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
    }

    public void centerSignUpContinue(View v) {
        Utility.buttonWait((Button)v, true);
        if (checkErrors()) {
            Intent intent = new Intent(getApplicationContext(), SignUpUsers.class);
            intent.putExtra("withImage", withImage);
            startActivityForResult(intent,FINISH);
        }
        Utility.buttonWait((Button)v, false, "Continue");
    }

    private boolean checkErrors() {
        boolean valid = true;

        String name = nameInput.getText().toString();
        String website = websiteInput.getText().toString();
        String contact = contactInput.getText().toString();
        String email = emailInput.getText().toString();
        String timeStart = timeStartInput.getText().toString();
        String timeEnd = timeEndInput.getText().toString();
        String otherServiceType = otherServiceTypeInput.getText().toString();
        String houseNo = houseNoInput.getText().toString();
        String street = streetInput.getText().toString();
        String barangay = barangayInput.getText().toString();
        String city = cityInput.getText().toString();
        String province = provinceInput.getText().toString();
        String district = districtInput.getText().toString();
        String zipCode = zipCodeInput.getText().toString();
        String mon = monday.getText().toString();
        String tue = tuesday.getText().toString();
        String wed = wednesday.getText().toString();
        String thu = thursday.getText().toString();
        String fri = friday.getText().toString();
        String sat = saturday.getText().toString();
        String sun = sunday.getText().toString();
        String country = countryInput.getSelectedItem().toString();
        String serviceType = serviceTypeInput.getSelectedItem().toString();

        operatingDays = new ArrayList<>();

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


        Timestamp s, e;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            s = new Timestamp(format.parse(timeStart));
        } catch (ParseException ex) {
            s = null;
        }
        try {
            e = new Timestamp(format.parse(timeEnd));
        } catch (ParseException ex) {
            e = null;
        }
        if (s==null) {
            timeStartInput.setError("No time given");
            valid = false;
        }
        if (e==null) {
            timeEndInput.setError("No time given");
            valid = false;
        }
        if(s!=null && s.compareTo(e) >= 0) {
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

        if (valid) {
            Account.addData("bName", name);
            Account.addData("bWebsite", website);
            Account.addData("bEmail", email);
            Account.addData("bContactNumber", contact);
            Account.addData("bOpeningTime", s);
            Account.addData("bClosingTime", e);
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
            Account.addData("accessLevel", "administrator");
        }

        return valid;
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
        if (Account.hasKey("bServiceType")) {
            int position = list.indexOf(Account.getStringData("bServiceType"));
            if (position < 0) {
                serviceTypeInput.setSelection(list.indexOf("Others"));
                otherServiceTypeInput.setText(Account.getStringData("bServiceType"));
                otherServiceTypeInput.setVisibility(View.VISIBLE);
            } else {
                serviceTypeInput.setSelection(list.indexOf(Account.getStringData("bServiceType")));
            }
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
            imageHandler.setImage("bLogo", logo);
        }
        operatingDays.clear();
        if (Account.get("bOperatingDays")!=null)
            operatingDays.addAll((List<String>)Account.get("bOperatingDays"));

        setDaysChecked("Mon", monday);
        setDaysChecked("Tue", tuesday);
        setDaysChecked("Wed", wednesday);
        setDaysChecked("Thu", thursday);
        setDaysChecked("Fri", friday);
        setDaysChecked("Sat", saturday);
        setDaysChecked("Sun", sunday);
    }

    private void setDaysChecked(String text, CheckBox view) {
        if (operatingDays.contains(text))
            view.setChecked(true);
        else
            view.setChecked(false);
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
        operatingDays = new ArrayList<>();
        withImage = false;
        imageHandler = new ImageHandler(this, SignUpLearningCenter.this);
        imageDone = new ObservableString();
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
                        ((TextInputEditText) v).setError(null);
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
        setResult(RESULT_CANCELED);
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
