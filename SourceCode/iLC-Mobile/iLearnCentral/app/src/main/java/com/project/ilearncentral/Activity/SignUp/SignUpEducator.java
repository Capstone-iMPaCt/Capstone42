package com.project.ilearncentral.Activity.SignUp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ilearncentral.Activity.UserPages;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpEducator extends AppCompatActivity {

    private String TAG = "SIGNUP_EDUCATOR";
    private CircleImageView image;
    private CircleImageView changeimage;
    private TextInputEditText fNameInput, mNameInput, lNameInput,
            extensionInput, citizenshipInput, religionInput, birthDateInput,
            houseNoInput, streetInput, barangayInput, cityInput,
            provinceInput, districtInput, zipCodeInput;
    private DatePickerDialog picker;
    private Spinner maritalStatusInput, countryInput;
    private RadioButton maleInput, femaleInput;
    private Button signUpButton;

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
        setContentView(R.layout.activity_sign_up_educator);

        res();

        changeimage.setOnClickListener(selectphoto);
        Log.d(TAG, Account.getStringData("username"));
        birthDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendarldr = Calendar.getInstance();
                int day = calendarldr.get(Calendar.DAY_OF_MONTH);
                int month = calendarldr.get(Calendar.MONTH);
                int year = calendarldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignUpEducator.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthDateInput.setText((monthOfYear + 1) +  "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        signUpButton.setOnClickListener(signUp);
    }

    private View.OnClickListener signUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (checkErrors()) {
                        signUpButton.setEnabled(false);
                        mAuth.createUserWithEmailAndPassword(Account.getStringData("username") + getString(R.string.emailSuffix),
                                Account.getStringData("password"))
                            .addOnCompleteListener(SignUpEducator.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        db.collection("User").document(user.getUid()).set(Account.getUserData());
                                        db.collection("Educator").document(user.getUid()).set(Account.getProfileData());

                                        uploadImage(Account.getStringData("username"));
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(Account.getName())
                                                .setPhotoUri(Account.getUriData("image"))
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            if (!withImage) updateUI();
                                                            Log.d(TAG, "User profile updated.");
                                                        }
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpEducator.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        signUpButton.setEnabled(true);
                                    }
                                }
                            });
                    }
                }
            });
        }
    };

    public void updateUI() {
        startActivity(new Intent(getApplicationContext(), UserPages.class));
        Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private boolean checkErrors() {
        boolean valid = true;
        String fName = fNameInput.getText().toString();
        String mName = mNameInput.getText().toString();
        String lName = lNameInput.getText().toString();
        String extension = extensionInput.getText().toString();
        String birthday = birthDateInput.getText().toString();
        String citizenship = citizenshipInput.getText().toString();
        String religion = religionInput.getText().toString();
        String houseNo = houseNoInput.getText().toString();
        String street = streetInput.getText().toString();
        String barangay = barangayInput.getText().toString();
        String city = cityInput.getText().toString();
        String province = provinceInput.getText().toString();
        String district = districtInput.getText().toString();
        String zipCode = zipCodeInput.getText().toString();
        String country = countryInput.getSelectedItem().toString();
        String maritalStatus = maritalStatusInput.getSelectedItem().toString();

        if (fName.isEmpty()) {
            fNameInput.setError("First Name is empty");
            valid = false;
        }
        if (lName.isEmpty()) {
            lNameInput.setError("Last Name is empty");
            valid = false;
        }
        if (citizenship.isEmpty()) {
            citizenshipInput.setError("Citizenship is empty");
            valid = false;
        }
        if (religion.isEmpty()) {
            religionInput.setError("Religion is empty");
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
        Timestamp t;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            t = new Timestamp(format.parse(birthday));
        } catch (ParseException e) {
            t = null;
        }
        if (t==null) {
            birthDateInput.setError("Birthday has incorrect format");
            valid = false;
        }
        if (province.isEmpty()) {
            provinceInput.setError("Province is empty");
            valid = false;
        }
        if (maritalStatusInput.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), "Select Marital Status", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid) {
            Account.addData("firstName", fName);
            Account.addData("middleName", mName);
            Account.addData("lastName", lName);
            Account.addData("extension", extension);
            Account.addData("citizenship", citizenship);
            Account.addData("birthday", t);
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
            if (maleInput.isChecked())
                Account.addData("gender", "Male");
            else
                Account.addData("gender", "Female");
        }
        return valid;
    }

    private View.OnClickListener selectphoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(new Runnable() {
                public void run() {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_IMAGE_REQUEST);
                }
            }).start();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();;
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
                Account.addData("image", filePath);
                withImage = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(String txtid){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Signing Up...");
            progressDialog.show();

            ref = storageRef.child("images/"+ txtid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    //showAlert("Successfully Added","Success");
                                    if (withImage) {
                                        updateUI();
                                    }
                                }
                            });
                        }
                    })
                    .   addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showAlert("An Error Occured","ERROR");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading data "+(int)progress+"%");
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

    private void res() {
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
        signUpButton = findViewById(R.id.sign_up_button_educator);
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        withImage = false;
    }
}
