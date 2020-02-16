package com.project.ilearncentral.Activity.Update;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.ImagePicker;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateLearningCenter extends AppCompatActivity {

    private String TAG = "SIGNUP_CENTER";
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
    private String buttonText;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private StorageReference ref;

    private Uri filePath;
    ByteArrayOutputStream bitmapBytes;
    private Bitmap bitmap;
    private File destination = null;
    private String imgPath = null;
    private boolean withImage, updated;
    private final int PICK_IMAGE_CAMERA = 11, PICK_IMAGE_GALLERY = 12, FINISH = 1;

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
                selectImage();
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

    public void centerSignUpContinue(final View v) {
        Utility.buttonWait((Button)v, true);
        if (checkErrors()) {
            db.collection("LearningCenter").document(Account.getStringData("centerId")).set(Account.getBusinessData())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(withImage) {
                                uploadImage(Account.getStringData("centerId"));
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
            filePath = Account.getUriData("bLogo");
            changeProfileImage();
        }
    }

    public void changeProfileImage() {
        new Thread(new Runnable() {
            public void run() {
                storageRef.child("images").child(Account.getStringData("centerId")).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).error(R.drawable.user)
                                .into(logo);
                        }
                        });
            }
        }).start();
    }

    public void showAlert(String Message,String label)
    {
        //set alert for executing the task
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(""+label);
        alert.setMessage(""+Message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int id){
                dialog.cancel();
            }
        });

        Dialog dialog = alert.create();
        dialog.show();
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
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        buttonText = "Update";
        withImage = updated = false;
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

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                filePath = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmapBytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bitmapBytes);

                bitmap = ImagePicker.getImageResized(this, filePath);
                int rotation = ImagePicker.getRotation(this, filePath, true);
                bitmap = ImagePicker.rotate(bitmap, rotation);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bitmapBytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgPath = destination.getAbsolutePath();
                logo.setImageBitmap(bitmap);
                withImage = true;
                Account.addData("image", filePath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(filePath,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                bitmap = BitmapFactory.decodeFile(picturePath);
                bitmapBytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bitmapBytes);

                bitmap = ImagePicker.getImageResized(this, filePath);
                int rotation = ImagePicker.getRotation(this, filePath, false);
                bitmap = ImagePicker.rotate(bitmap, rotation);

                logo.setImageBitmap(bitmap);
                withImage = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == FINISH && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void uploadImage(String txtid){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating...");
            progressDialog.show();

            byte[] data = bitmapBytes.toByteArray();
            ref = storageRef.child("images/"+ txtid);
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    if (withImage) {
                                        Account.addData("bLogo", uri.toString());
                                        DocumentReference lcRef = db.collection("LearningCenter").document(Account.getStringData("centerId"));
                                        lcRef
                                                .update("Logo", uri.toString())
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
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Utility.buttonWait(updateButton, false, "Update");
                            showAlert("An Error Occured", "ERROR");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 0.0;
                            progress = (100.0 * taskSnapshot
                                    .getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading data " + (int) progress + "%");
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            Utility.buttonWait(updateButton, false, "Update");
                        }
                    });
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        return cursor.getString(columnIndex);
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
