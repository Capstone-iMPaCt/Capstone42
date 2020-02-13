package com.project.ilearncentral.Activity.SignUp;

import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpLearningCenter extends AppCompatActivity {

    private String TAG = "SIGNUP_CENTER";
    TextInputEditText nameInput, websiteInput, contactInput,
            timeStartInput, timeEndInput, otherServiceTypeInput,
            houseNoInput, streetInput, barangayInput, cityInput,
            provinceInput, districtInput, zipCodeInput;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    Spinner countryInput, serviceTypeInput;
    Button signUpButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private StorageReference ref;

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private boolean withImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_learning_center);

        res();

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

    private boolean checkErrors() {
        boolean valid = true;

        String name = nameInput.getText().toString();
        String website = websiteInput.getText().toString();
        String contact = contactInput.getText().toString();
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

        List<String> operatingDays = new ArrayList<>();

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

        Timestamp s, e;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm am/pm");
        try {
            s = new Timestamp(format.parse(timeStart));
        } catch (ParseException ex) {
            s = null;
        }
        if (s==null) {
            timeStartInput.setError("Time Start has incorrect format");
            valid = false;
        }
        try {
            e = new Timestamp(format.parse(timeStart));
        } catch (ParseException ex) {
            e = null;
        }
        if (e==null) {
            timeEndInput.setError("Time End has incorrect format");
            valid = false;
        }
        if(s.compareTo(e) >= 0) {
            timeEndInput.setError("Time End should be after start");
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

        return valid;
    }

    private void res() {
        nameInput = findViewById(R.id.sign_up_business_name_lc);
        websiteInput = findViewById(R.id.sign_up_website_lc);
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
        countryInput = findViewById(R.id.sign_up_country_educator);
        serviceTypeInput = findViewById(R.id.sign_up_service_type_lc);
        signUpButton = findViewById(R.id.sign_up_button_educator);
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        withImage = false;
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
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
