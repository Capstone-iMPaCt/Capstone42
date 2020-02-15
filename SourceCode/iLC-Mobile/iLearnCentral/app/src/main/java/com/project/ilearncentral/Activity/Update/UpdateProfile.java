package com.project.ilearncentral.Activity.Update;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

public class UpdateProfile extends AppCompatActivity {

    private String TAG = "Update_Profile";
    private TextView formTitle;
    private CircleImageView image;
    private CircleImageView changeimage;
    private TextInputEditText fNameInput, mNameInput, lNameInput,
            extensionInput, citizenshipInput, religionInput, birthDateInput,
            houseNoInput, streetInput, barangayInput, cityInput,
            provinceInput, districtInput, zipCodeInput;
    private DatePickerDialog picker;
    private Spinner maritalStatusInput, countryInput;
    private RadioButton maleInput, femaleInput;
    private Button updateButton;
    private Timestamp t;
    private SimpleDateFormat format;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private StorageReference ref;

    private Uri filePath;
    private String imgPath = null;
    private boolean withImage;
    private boolean updated;
    private Bitmap bitmap;
    private File destination = null;
    private final int PICK_IMAGE_CAMERA = 11, PICK_IMAGE_GALLERY = 12;

    private String fName, mName, lName, extension, birthday, citizenship, religion,
        houseNo, street, barangay, city, province, district, zipCode, country, maritalStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_others);

        res();
        setValues();

        Log.d(TAG, Account.getStringData("username"));

        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        birthDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendarldr = Calendar.getInstance();
                if (t!=null) {
                    calendarldr.setTime(t.toDate());
                }
                int day = calendarldr.get(Calendar.DAY_OF_MONTH);
                int month = calendarldr.get(Calendar.MONTH);
                int year = calendarldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(UpdateProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthDateInput.setText((monthOfYear + 1) +  "/" + dayOfMonth + "/" + year);
                        birthDateInput.setError(null);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        formTitle.setText("Update Profile");
        updateButton.setText("Update");
        updateButton.setOnClickListener(updateProfile);
    }

    private View.OnClickListener updateProfile = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (checkErrors()) {
                        Utility.buttonWait(updateButton, true);
                        user = mAuth.getCurrentUser();
                        if (Account.getType() == Account.Type.Student)
                            db.collection("Student").document(user.getUid()).set(Account.getProfileData());
                        else if(Account.getType() == Account.Type.Educator)
                            db.collection("Educator").document(user.getUid()).set(Account.getProfileData());
                        else
                            db.collection("LearningCenterStaff").document(user.getUid()).set(Account.getProfileData());

                        if (!withImage) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Account.getName())
                                .build();
                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            updateUI();
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });
                        } else {
                            uploadImage(Account.getStringData("username"));
                        }
                    }
                }
            });
        }
    };

    public void updateUI() {
        updated = true;
        Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
        Utility.buttonWait(updateButton, false, "Update");
    }

    private boolean checkErrors() {
        boolean valid = true;
        fName = fNameInput.getText().toString();
        mName = mNameInput.getText().toString();
        lName = lNameInput.getText().toString();
        extension = extensionInput.getText().toString();
        birthday = birthDateInput.getText().toString();
        citizenship = citizenshipInput.getText().toString();
        religion = religionInput.getText().toString();
        houseNo = houseNoInput.getText().toString();
        street = streetInput.getText().toString();
        barangay = barangayInput.getText().toString();
        city = cityInput.getText().toString();
        province = provinceInput.getText().toString();
        district = districtInput.getText().toString();
        zipCode = zipCodeInput.getText().toString();
        country = countryInput.getSelectedItem().toString();
        maritalStatus = maritalStatusInput.getSelectedItem().toString();

        if (fName.isEmpty()) {
            fNameInput.setError("First Name is empty");
            valid = false;
        }
        if (lName.isEmpty()) {
            lNameInput.setError("Last Name is empty");
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
        try {
            t = new Timestamp(format.parse(birthday));
        } catch (ParseException e) {
            t = null;
        }
        if (t==null) {
            birthDateInput.setError("Birthday has incorrect format");
            valid = false;
        }
        if (maritalStatusInput.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), "Select Marital Status", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid) {
            retrieveData();
        }
        return valid;
    }

    private void retrieveData() {
        Account.addData("firstName", fName);
        Account.addData("middleName", mName);
        Account.addData("lastName", lName);
        Account.addData("extension", extension);
        Account.addData("citizenship", citizenship);
        if (t!=null) Account.addData("birthday", t);
        Account.addData("religion", religion);
        Account.addData("houseNo", houseNo);
        Account.addData("street", street);
        Account.addData("barangay", barangay);
        Account.addData("city", city);
        Account.addData("province", province);
        Account.addData("district", district);
        Account.addData("zipCode", zipCode);
        Account.addData("country", country);
        Account.addData("maritalStatus", maritalStatus);
        if (maleInput.isChecked()) {
            Account.addData("gender", "Male");
        }
        else {
            Account.addData("gender", "Female");
        }
        if (withImage) {
            Account.addData("image", filePath.toString());
        }
    }

    private void setValues() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        fNameInput.setText(Account.getStringData("firstName"));
        mNameInput.setText(Account.getStringData("middleName"));
        lNameInput.setText(Account.getStringData("lastName"));
        extensionInput.setText(Account.getStringData("extension"));
        citizenshipInput.setText(Account.getStringData("citizenship"));
        if (Account.hasKey("birthday"))
            birthDateInput.setText(format.format(Account.getDateData("birthday")));
        religionInput.setText(Account.getStringData("religion"));
        houseNoInput.setText(Account.getStringData("houseNo"));
        streetInput.setText(Account.getStringData("street"));
        barangayInput.setText(Account.getStringData("barangay"));
        cityInput.setText(Account.getStringData("city"));
        provinceInput.setText(Account.getStringData("province"));
        districtInput.setText(Account.getStringData("district"));
        zipCodeInput.setText(Account.getStringData("zipCode"));
        List<String> list = new ArrayList<>();
        for (String s : getResources().getStringArray(R.array.countries)) {
            list.add(s);
        }
        countryInput.setSelection(list.indexOf(Account.getStringData("country")));

        list.clear();
        for (String s : getResources().getStringArray(R.array.marital_status)) {
            list.add(s);
        }
        maritalStatusInput.setSelection(list.indexOf(Account.getStringData("maritalStatus")));
        if (Account.hasKey("image")) {
            filePath = Account.getUriData("image");
            changeProfileImage();
        }
    }

    public void changeProfileImage() {
        new Thread(new Runnable() {
            public void run() {
                if (user.getPhotoUrl() != null) {
                    storageRef.child("images").child(Account.getStringData("username")).getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri.toString()).error(R.drawable.user)
                                            .into(image);
                                }
                            });
                }
            }
        }).start();
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
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                bitmap = ImagePicker.getImageResized(this, selectedImage);
                int rotation = ImagePicker.getRotation(this, selectedImage, true);
                bitmap = ImagePicker.rotate(bitmap, rotation);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                image.setImageBitmap(bitmap);
                filePath = selectedImage;
                withImage = true;
                Account.addData("image", selectedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            System.out.println(filePath);
            setImage();
        }
    }

    private void setImage() {
        try {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(filePath,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            image.setImageBitmap(bitmap);
            withImage = true;
            Account.addData("image", filePath.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void uploadImage(String txtid){
        System.out.println("final" + filePath);
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating...");
            progressDialog.show();

            ref = storageRef.child("images/"+ txtid);
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    if (withImage) {
                                        Account.addData("image", uri.toString());
                                        DocumentReference userRef = db.collection("User")
                                                .document(user.getUid());
                                        userRef
                                                .update("Image", uri.toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                        updateProfileWithImage(uri, true);
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

    public void updateProfileWithImage(final Uri uri, final boolean continueSignIn) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Account.getName())
                .setPhotoUri(uri)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (continueSignIn) {
                                DocumentReference lcRef = db.collection("User").document(user.getUid());
                                lcRef
                                    .update("Image", uri.toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            updateUI();
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                            }
                            //update image urk in db
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
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
        formTitle = findViewById(R.id.sign_up_form_title);
        image = findViewById(R.id.sign_up_image_educator);
        changeimage = findViewById(R.id.sign_up_image_change_educator);
        fNameInput = findViewById(R.id.sign_up_first_name_educator);
        mNameInput = findViewById(R.id.sign_up_middle_name_educator);
        lNameInput = findViewById(R.id.sign_up_last_name_educator);
        extensionInput = findViewById(R.id.sign_up_extension_educator);
        citizenshipInput = findViewById(R.id.sign_up_citizenship_educator);
        religionInput = findViewById(R.id.sign_up_religion_educator);
        houseNoInput = findViewById(R.id.sign_up_house_no_educator);
        streetInput = findViewById(R.id.sign_up_street_educator);
        barangayInput = findViewById(R.id.sign_up_barangay_educator);
        cityInput = findViewById(R.id.sign_up_city_educator);
        provinceInput = findViewById(R.id.sign_up_province_educator);
        districtInput = findViewById(R.id.sign_up_district_educator);
        zipCodeInput = findViewById(R.id.sign_up_zip_code_educator);
        birthDateInput = findViewById(R.id.sign_up_birthday_educator);
        birthDateInput.setInputType(InputType.TYPE_NULL);
        maritalStatusInput = findViewById(R.id.sign_up_marital_status_educator);
        countryInput = findViewById(R.id.sign_up_country_educator);
        maleInput = findViewById(R.id.sign_up_gender_male_educator);
        femaleInput = findViewById(R.id.sign_up_gender_female_educator);
        updateButton = findViewById(R.id.sign_up_button_educator);
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        format = new SimpleDateFormat("MM/dd/yyyy");
        withImage = false;
        updated = false;
        fName = mName = lName = extension = birthday = citizenship = religion = houseNo
             = street = barangay = city = province = district = zipCode = country = maritalStatus = "";
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
