package com.project.ilearncentral.Activity.SignUp;

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
import android.util.Patterns;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.ilearncentral.Model.Account;
import com.project.ilearncentral.MyClass.ImagePicker;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUser extends AppCompatActivity {

    private String TAG = "CREATE_USER";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private ObservableBoolean logInAgain;

    private TextView title;
    private TextInputEditText usernameInput, passwordInput, confirmInput,
            emailInput, answerInput, fNameInput, mNameInput, lNameInput,
            extensionInput, citizenshipInput, religionInput, birthDateInput,
            houseNoInput, streetInput, barangayInput, cityInput,
            provinceInput, districtInput, zipCodeInput;
    private DatePickerDialog picker;
    private Spinner questionInput, accessLevelInput, maritalStatusInput, countryInput;
    private Button confirmBtn;
    private CircleImageView image;
    private CircleImageView changeimage;
    private RadioButton maleInput, femaleInput;

    private String username, password, confirm, email, answer, question, accessLevel,
            fName, mName, lName, extension, birthday, citizenship, religion, maritalStatus,
            houseNo, street, barangay, city, province, district, zipCode, country;
    private String btnText, oldEmail, oldPassword;
    private Timestamp t;
    private SimpleDateFormat format;

    private boolean valid, creationSuccess, withImage;
    private Uri filePath;
    private String imgPath = null;
    private Bitmap bitmap;
    private File destination = null;
    private final int PICK_IMAGE_CAMERA = 11, PICK_IMAGE_GALLERY = 12;
    private String newId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        res();

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
                picker = new DatePickerDialog(CreateUser.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthDateInput.setText((monthOfYear + 1) +  "/" + dayOfMonth + "/" + year);
                        birthDateInput.setError(null);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        logInAgain.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                }
            }
        });

        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        confirmBtn.setOnClickListener(createUser);
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
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating...");
            progressDialog.show();

            storageRef = storageRef.child("images/"+ txtid);
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        progressDialog.dismiss();
                        if (withImage) {
                            Account.addData("image", uri.toString());
                            DocumentReference userRef = db.collection("User").document(newId);
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
                        }
                        }
                    });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Utility.buttonWait(confirmBtn, false, "Update");
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
                        Utility.buttonWait(confirmBtn, false, "Update");
                    }
                });
        }
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

    private View.OnClickListener createUser = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkErrors()) {
                Utility.buttonWait(confirmBtn, true);
                db.collection("User").add(getUserData())
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                        newId = documentReference.getId();
                        db.collection("LearningCenterStaff").add(getProfileData())
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    if(withImage) {
                                        uploadImage(username);
                                    } else {
                                        createdUser();
                                    }
                                    Log.d(TAG, "LearningCenterStaff written with ID: " + documentReference.getId());
                                }
                            });
                        Log.d(TAG, "User written with ID: " + documentReference.getId());
                        }
                    });
            } else {
                Toast.makeText(getApplicationContext(), "Please correct errors", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    private Map<String, Object> getUserData() {
        Map<String, Object> userData = new HashMap<>();
        try {
            userData.put("AccountStatus", "active");
            userData.put("AccountType", Account.getType().toString().toLowerCase());
            userData.put("Email", email);
            userData.put("Username", username);
            userData.put("Image", filePath);
            userData.put("Question", question);
            userData.put("Answer", answer);
        } catch(Exception e) {
            return null;
        }
        return userData;
    }

    private Map<String, Object> getProfileData() {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("Username", username);
            Map<String, String> name = new HashMap<>();
            name.put("FirstName", fName);
            name.put("MiddleName", mName);
            name.put("LastName", lName);
            name.put("Extension", extension);
        profileData.put("Name", name);
        profileData.put("Religion", religion);
        profileData.put("Citizenship", citizenship);
        if (maleInput.isChecked())
            profileData.put("Gender", "Male");
        else
            profileData.put("Gender", "Female");
        profileData.put("MaritalStatus", maritalStatus);
        profileData.put("Birthday", t);
        profileData.put("CenterID", Account.getStringData("centerId"));
        Map<String, Object> address = new HashMap<>();
            address.put("HouseNo", houseNo);
            address.put("Street", street);
            address.put("Barangay", barangay);
            address.put("City", city);
            address.put("Province", province);
            address.put("District", district);
            address.put("ZipCode", zipCode);
            address.put("Country", country);
        profileData.put("Address", address);
            profileData.put("AccessLevel", accessLevel);

        return profileData;
    }

    private void createdUser() {
        logInAgain.set(true);
        creationSuccess = true;
        Toast.makeText(getApplicationContext(), "User Created!", Toast.LENGTH_SHORT)
                .show();
        Utility.buttonWait(confirmBtn, false, btnText);
    }

    private boolean checkErrors() {
        valid = true;

        username = usernameInput.getText().toString().toLowerCase();
        password = passwordInput.getText().toString();
        confirm = confirmInput.getText().toString();
        email = emailInput.getText().toString();
        answer = answerInput.getText().toString().toLowerCase();
        question = questionInput.getSelectedItem().toString();
        accessLevel = accessLevelInput.getSelectedItem().toString();
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

        if (username.isEmpty()) {
            usernameInput.setError("Username is empty.");
            valid = false;
        } else if(!username.matches("[a-z0-9_]{3,}")) {
            usernameInput.setError("Username invalid format.");
            valid = false;
        } else {
            db.collection("User").whereEqualTo("Username", username).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    usernameInput.setError("Username already exists");
                                    valid = false;
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is empty");
            valid = false;
        } else if (password.length() < 6) {
            passwordInput.setError("Password is too short");
            valid = false;
        }
        if (!confirm.equals(password)) {
            confirmInput.setError("Password does not match");
            valid = false;
        }
        if (email.isEmpty()) {
            emailInput.setError("Email is empty");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email invalid format");
            valid = false;
        }
        if (answer.isEmpty()) {
            answerInput.setError("Please provide an answer.");
            valid = false;
        }

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

        return valid;
    }

    private void res() {
        title = findViewById(R.id.create_user_title);
        image = findViewById(R.id.create_user_image);
        changeimage = findViewById(R.id.create_user_image_change);
        usernameInput = findViewById(R.id.create_user_username);
        passwordInput = findViewById(R.id.create_user_password);
        confirmInput = findViewById(R.id.create_user_confirm_password);
        emailInput = findViewById(R.id.create_user_email);
        answerInput = findViewById(R.id.create_user_security_answer);
        questionInput = findViewById(R.id.create_user_security_question);
        accessLevelInput = findViewById(R.id.create_user_access_level);
        fNameInput = findViewById(R.id.create_user_first_name);
        mNameInput = findViewById(R.id.create_user_middle_name);
        lNameInput = findViewById(R.id.create_user_last_name);
        extensionInput = findViewById(R.id.create_user_extension);
        citizenshipInput = findViewById(R.id.create_user_citizenship);
        religionInput = findViewById(R.id.create_user_religion);
        houseNoInput = findViewById(R.id.create_user_house_no);
        streetInput = findViewById(R.id.create_user_street);
        barangayInput = findViewById(R.id.create_user_barangay);
        cityInput = findViewById(R.id.create_user_city);
        provinceInput = findViewById(R.id.create_user_province);
        districtInput = findViewById(R.id.create_user_district);
        zipCodeInput = findViewById(R.id.create_user_zip_code);
        birthDateInput = findViewById(R.id.create_user_birthday);
        birthDateInput.setInputType(InputType.TYPE_NULL);
        maritalStatusInput = findViewById(R.id.create_user_marital_status);
        countryInput = findViewById(R.id.create_user_country);
        maleInput = findViewById(R.id.create_user_gender_male);
        femaleInput = findViewById(R.id.create_user_gender_female);
        confirmBtn = findViewById(R.id.create_user_confirm_button);
        btnText = confirmBtn.getText().toString();
        email = Account.getStringData("email");
        password = Account.getStringData("password");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        withImage = creationSuccess = false;
        format = new SimpleDateFormat("MM/dd/yyyy");
        username = password = confirm = email = answer = question =
            fName = mName = lName = extension = birthday = citizenship = religion = houseNo =
            street = barangay = city = province = district = zipCode = country = maritalStatus = "";
        logInAgain = new ObservableBoolean();
        newId = "";
    }

    @Override
    public void onBackPressed() {
        if (creationSuccess)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        //retrieveData();
        Log.d(TAG, "onBackPressed Called");
        super.onBackPressed();
    }
}
