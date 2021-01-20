package com.project.ilearncentral.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.ImageHandler;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.project.ilearncentral.databinding.ActivityVerifyBusinessBinding;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VerifyBusiness extends AppCompatActivity {

    private ActivityVerifyBusinessBinding binding;
    private final int PERMIT = 11, BIR = 12;
    private final int PICK_PERMIT_IMAGE_CAMERA = 13, PICK_PERMIT_IMAGE_GALLERY = 14;
    private final int PICK_BIR_IMAGE_CAMERA = 15, PICK_BIR_IMAGE_GALLERY = 16;
    private final String TAG = "VerifyBusiness";

    private String centerID, businessName;
    private Date dateSubmitted;

    private ObservableString imageDone;
    private ImageHandler imageHandler;
    private String permitPhotoPath, birPhotoPath;
    private Uri permitPhotoUri, birPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        binding.verifyBusinessCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.verifyBusinessPermitChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(PERMIT);
            }
        });
        binding.verifyBusinessPermitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImage(PERMIT);
            }
        });
        binding.verifyBusinessBirChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(BIR);
            }
        });
        binding.verifyBusinessBirImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImage(BIR);
            }
        });
        binding.verifyBusinessSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permitPhotoUri != null && birPhotoUri != null) {
                    Map<String, Object> verificationStatus = new HashMap<>();
                    verificationStatus.put("VerificationStatus", "pending");
                    FirebaseFirestore.getInstance().collection("LearningCenter")
                            .document(Account.getCenterId())
                            .update(verificationStatus)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    submitImage(PERMIT);
                                    submitImage(BIR);
                                    Account.addData("VerificationStatus", "pending");
                                    LearningCenter.getLCById(Account.getCenterId()).setVerificationStatus("pending");
                                    Utility.setVerificationListenerValue(true);
                                    finish();
                                    toastMessage("Submission successful.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toastMessage("Submission failed. Please try again.");
                                }
                            });
                } else {
                    toastMessage("Please attach all the required files.");
                }
            }
        });
    }

    private void submitImage(int requestCode) {
        String fileName = "";
        Uri photoUri = null;
        if (requestCode == PERMIT) {
            fileName = "PERMIT_" + Account.getCenterId();
            photoUri = permitPhotoUri;
        } else if (requestCode == BIR) {
            fileName = "BIR_" + Account.getCenterId();
            photoUri = birPhotoUri;
        }
        Utility.buttonWait(binding.verifyBusinessSubmitButton, true, "Submitting...");
        FirebaseStorage.getInstance().getReference()
                .child("business_proof")
                .child(Account.getCenterId())
                .child(fileName)
                .putFile(photoUri);
    }

    private void initialize() {
        binding = ActivityVerifyBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageHandler = new ImageHandler(this, VerifyBusiness.this);
        imageDone = new ObservableString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PERMIT_IMAGE_CAMERA && resultCode == RESULT_OK) {
            permitPhotoUri = Uri.fromFile(new File(permitPhotoPath));
            binding.verifyBusinessPermitImage.setImageURI(permitPhotoUri);
        } else if (requestCode == PICK_PERMIT_IMAGE_GALLERY && resultCode == RESULT_OK) {
            permitPhotoUri = data.getData();
            binding.verifyBusinessPermitImage.setImageURI(permitPhotoUri);
        } else if (requestCode == PICK_BIR_IMAGE_CAMERA && resultCode == RESULT_OK) {
            birPhotoUri = Uri.fromFile(new File(birPhotoPath));
            binding.verifyBusinessBirImage.setImageURI(birPhotoUri);
        } else if (requestCode == PICK_BIR_IMAGE_GALLERY && resultCode == RESULT_OK) {
            birPhotoUri = data.getData();
            binding.verifyBusinessBirImage.setImageURI(birPhotoUri);
        }
    }

    private void dispatchTakePictureIntent(final int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(requestCode);
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                if (requestCode == PICK_PERMIT_IMAGE_CAMERA) {
                    permitPhotoUri = FileProvider.getUriForFile(this,
                            "com.project.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, permitPhotoUri);
                } else if (requestCode == PICK_BIR_IMAGE_CAMERA) {
                    birPhotoUri = FileProvider.getUriForFile(this,
                            "com.project.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, birPhotoUri);
                }
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createImageFile(final int requestCode) throws IOException {
        // Create an image file name
        String imageName = "";
        if (requestCode == PICK_PERMIT_IMAGE_CAMERA) {
            imageName = "PERMIT_";
        } else if (requestCode == PICK_BIR_IMAGE_CAMERA) {
            imageName = "BIR_";
        }
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageName + Account.getCenterId(),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if (requestCode == PICK_PERMIT_IMAGE_CAMERA) {
            permitPhotoPath = image.getAbsolutePath();
        } else if (requestCode == PICK_BIR_IMAGE_CAMERA) {
            birPhotoPath = image.getAbsolutePath();
        }
        return image;
    }

    private void changeImage(final int requestCode) {
        try {
            if (imageHandler.checkPermission()) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(VerifyBusiness.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            if (requestCode == PERMIT)
                                dispatchTakePictureIntent(PICK_PERMIT_IMAGE_CAMERA);
                            else if (requestCode == BIR)
                                dispatchTakePictureIntent(PICK_BIR_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            if (requestCode == PERMIT)
                                startActivityForResult(pickPhoto, PICK_PERMIT_IMAGE_GALLERY);
                            else if (requestCode == BIR)
                                startActivityForResult(pickPhoto, PICK_BIR_IMAGE_GALLERY);
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

    private void viewImage(final int requestCode) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VerifyBusiness.this);
        View mView = getLayoutInflater().inflate(R.layout.fragment_dialog_photoview, null);
        final PhotoView photoView = mView.findViewById(R.id.photoview);
        photoView.setMinimumHeight(Utility.getScreenHeight());
        if (requestCode == PERMIT)
            photoView.setImageURI(permitPhotoUri);
        else if (requestCode == BIR)
            photoView.setImageURI(birPhotoUri);
//        Glide.with(getApplicationContext()).load(photoUri.toString()).fitCenter().into(photoView);
        builder.setView(mView);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void toastMessage(String message) {
        Toast.makeText(VerifyBusiness.this, message, Toast.LENGTH_SHORT).show();
    }
}