package com.project.ilearncentral.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.BankAccountDetail;
import com.project.ilearncentral.Model.Enrolment;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.databinding.ActivityEnrolmentPaymentBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrolmentPayment extends AppCompatActivity {

    private ActivityEnrolmentPaymentBinding binding;
    private final int PICK_IMAGE_CAMERA = 11, PICK_IMAGE_GALLERY = 12;
    private final String TAG = "EnrolmentPayment";

    private double fee;
    private String centerID, businessName, courseID, title;
    private Date courseStarts, courseEnds;

    private Intent intent;
    private List<BankAccountDetail> bankAccountList;
    private OnBooleanChangeListener bankDataListener;
    private ObservableString imageDone, confirmDone;
    private ImageHandler imageHandler;
    private boolean withImage;
    private String photoPath;
    private Uri photoUri;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        binding.enrolmentPaymentCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.enrolmentPaymentChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (imageHandler.checkPermission()) {
                        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(EnrolmentPayment.this);
                        builder.setTitle("Select Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals("Take Photo")) {
                                    dialog.dismiss();
                                    dispatchTakePictureIntent();
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
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.enrolmentPaymentEnrolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoUri != null) {
                    Utility.buttonWait(binding.enrolmentPaymentEnrolButton, true, "Saving...");
                    FirebaseStorage.getInstance().getReference()
                            .child("enrolment_payment_proof")
                            .child(centerID)
                            .child(courseID)
                            .child(Account.getUsername())
                            .putFile(photoUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Log.d(TAG, photoUri.toString());
                                    Enrolment enrolment = new Enrolment();
                                    enrolment.setCenterID(centerID);
                                    enrolment.setCourseID(courseID);
                                    enrolment.setCourseEnrolled(title);
                                    enrolment.setStudentID(Account.getUsername());
                                    enrolment.setStudentName(Account.getName());
                                    enrolment.setDateCourseStarts(courseStarts);
                                    enrolment.setDateCourseEnds(courseEnds);
                                    enrolment.setProcessedDate(new Date());
                                    enrolment.setEnrolmentFee(fee);
                                    enrolment.setEnrolmentStatus("pending");
                                    FirebaseFirestore.getInstance()
                                            .collection("Enrolment")
                                            .add(enrolment);
                                    toastMessage("Saving enrolment successful.");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toastMessage("Saving enrolment failed. Please try again.");
                                    return;
                                }
                            });
                } else {
                    toastMessage("Please attach photo of the payment you made.");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        withImage = imageHandler
//                .onActivityResult(requestCode, resultCode, data, null, binding.enrolmentPaymentImage, "");
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            photoUri = Uri.fromFile(new File(photoPath));
            binding.enrolmentPaymentImage.setImageURI(photoUri);
//            Glide.with(this).load(Uri.fromFile(file).toString()).centerCrop().into(binding.enrolmentPaymentImage);
//            Picasso.get().load(Uri.fromFile(file).toString()).fit().into(binding.enrolmentPaymentImage);
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get(MediaStore.EXTRA_OUTPUT);
//            binding.enrolmentPaymentImage.setImageBitmap(imageBitmap);
        } else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK) {
            photoUri = data.getData();
            binding.enrolmentPaymentImage.setImageURI(photoUri);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.project.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                courseID,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    private void initialize() {
        binding = ActivityEnrolmentPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageHandler = new ImageHandler(this, EnrolmentPayment.this);
        imageDone = new ObservableString();
        confirmDone = new ObservableString();

        intent = getIntent();
        centerID = intent.getStringExtra("CenterID");
        businessName = intent.getStringExtra("BusinessName");
        title = intent.getStringExtra("Title");
        fee = Double.parseDouble(String.valueOf(intent.getDoubleExtra("Fee", 0)));
        courseID = intent.getStringExtra("CourseID");
        courseStarts = new Date();
        courseEnds = new Date();
        courseStarts.setTime(intent.getLongExtra("DateCourseStarts", -1));
        courseEnds.setTime(intent.getLongExtra("DateCourseEnds", -1));

        bankAccountList = new ArrayList<>();
        bankDataListener = new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean value) {
                if (value) {
                    setBankDetails();
                }
            }
        };
        BankAccountDetail.getDataByCenterID(centerID, bankAccountList, bankDataListener);

        binding.enrolmentPaymentCenterName.setText(businessName);
        binding.enrolmentPaymentCourseTitle.setText(title);
        binding.enrolmentPaymentCourseFee.setText(Utility.showPriceInPHP(fee));
    }

    private void finishPayment() {
        Utility.buttonWait(binding.enrolmentPaymentEnrolButton, false, "CONFIRM");
        Intent i = new Intent();
//        i.putExtra("proofOfPayment", proofOfPayment);
        setResult(RESULT_OK, i);
        finish();
    }

    private void setBankDetails() {
        for (BankAccountDetail details : bankAccountList) {
            binding.enrolmentPaymentBankDetails.append("* " + details.getBankName()
                    + ": " + details.getBankAccountNumber() + "\n");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(EnrolmentPayment.this, message, Toast.LENGTH_SHORT).show();
    }
}