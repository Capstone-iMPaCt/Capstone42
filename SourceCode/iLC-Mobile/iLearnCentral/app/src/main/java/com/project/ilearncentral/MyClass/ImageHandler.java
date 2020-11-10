package com.project.ilearncentral.MyClass;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.ilearncentral.CustomBehavior.ObservableString;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ImageHandler {

    private StorageReference storageRef;
    private Context context;
    private Activity activity;

    private Uri filePath;
    private ByteArrayOutputStream bitmapBytes;
    private boolean changedBytes = false;
    private Bitmap bitmap;
    private File destination;
    private final int PICK_IMAGE_CAMERA = 11, PICK_IMAGE_GALLERY = 12;
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 150;
    private static final String TAG = "ImagePicker";
    private static final String TEMP_IMAGE_NAME = "tempImage";
    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;

    public ImageHandler(Context context, Activity activity) {
        storageRef = FirebaseStorage.getInstance().getReference();
        this.context = context;
        this.activity = activity;
        destination = null;
    }

    public void selectImage() {
        try {
            if (checkPermission()) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            activity.startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
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

    protected boolean checkPermission() {
        boolean allowed = true;
        PackageManager pm = context.getPackageManager();
        int hasCam = pm.checkPermission(Manifest.permission.CAMERA, context.getPackageName());
        int hasStore = pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, context.getPackageName());
        if (hasCam != PackageManager.PERMISSION_GRANTED || hasStore != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
            allowed = false;
        }
        return allowed;
    }


    protected void makeRequest() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                PICK_IMAGE_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PICK_IMAGE_CAMERA) {
            boolean requestGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    requestGranted = false;
            }
            if (requestGranted) selectImage();
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data, CircleImageView circleImageView, ImageView imageView, String saveKey) {
        boolean withImage = false;
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                filePath = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmapBytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bitmapBytes);
                bitmap = Bitmap.createScaledBitmap(bitmap, 560, 420, true);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/", "IMG_" + timeStamp + ".png");
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
//                filePath = Uri.parse(destination.getAbsolutePath());
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                bitmap = BitmapFactory.decodeFile(filePath.toString(), options);
//                Toast.makeText(context, filePath.toString(), Toast.LENGTH_LONG).show();

                if (circleImageView != null)
                    circleImageView.setImageBitmap(bitmap);
                else
                    imageView.setImageBitmap(bitmap);
                if (!saveKey.isEmpty()) {
                    Account.addData(saveKey, filePath.toString());
                    Account.addData(saveKey + "Bitmap", bitmap);
                    Account.addData(saveKey + "BitmapBytes", bitmapBytes);
                }
                withImage = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = context.getContentResolver().query(filePath,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(picturePath, options);
                bitmapBytes = new ByteArrayOutputStream();
//                bitmap = BitmapFactory.decodeFile(picturePath);
//                bitmapBytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bitmapBytes);
//                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 256, 256, true);
//                bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);

                bitmap = getImageResized(context, filePath);
                int rotation = getRotation(context, filePath, false);
                bitmap = rotate(bitmap, rotation);

                if (circleImageView != null)
                    circleImageView.setImageBitmap(bitmap);
                else
                    imageView.setImageBitmap(bitmap);
                if (!saveKey.isEmpty()) {
                    Account.addData(saveKey, filePath.toString());
                    Account.addData(saveKey + "Bitmap", bitmap);
                    Account.addData(saveKey + "BitmapBytes", bitmapBytes);
                }
                withImage = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return withImage;
    }

    public void uploadBitmapBytes(String saveKey, String pathName, String fileName, ImageView imageView, final ObservableString uriString) {
        changedBytes = true;
        bitmapBytes = (ByteArrayOutputStream) Account.get(saveKey);
        uploadImage(pathName, fileName, imageView, uriString);
    }

    public void uploadImage(String pathName, String fileName, ImageView imageView, final ObservableString uriString) {
        if (bitmapBytes != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap nBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            final StorageReference ref = storageRef.child(pathName + "/" + fileName);
            UploadTask uploadTask  = (UploadTask) ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    if (changedBytes) {
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bitmapBytes);
                                        changedBytes = false;
                                    }
                                    uriString.set(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            uriString.set("");
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
                            uriString.set("");
                        }
                    });
        }
    }

    public void showAlert(String Message, String label) {
        //set alert for executing the task
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("" + label);
        alert.setMessage("" + Message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        Dialog dialog = alert.create();
        dialog.show();
    }


    public void setFilePath(Uri uri, boolean refreshBitmap, String saveKey, ImageView view) {
        filePath = uri;
        if (refreshBitmap) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(filePath,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);
            bitmapBytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bitmapBytes);

            view.setImageBitmap(bitmap);
            Account.addData(saveKey, filePath.toString());
        }
    }

    public void setFilePath(Uri uri) {
        filePath = uri;
    }

    public Uri getFilePath() {
        return filePath;
    }

    public void setImage(String pathname, String filename, final ImageView view) {
        storageRef.child(pathname).child(filename).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).error(R.drawable.user).fit()
                                .into(view);
                    }
                });
    }

    public void setImage(String pathname, String filename, final CircleImageView view) {
        storageRef.child(pathname).child(filename).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).error(R.drawable.user).fit()
                                .into(view);
                    }
                });
    }

    public void setImage(String saveKey, ImageView imageView) {
        Bitmap b = (Bitmap) Account.get(saveKey + "Bitmap");
        imageView.setImageBitmap(b);
    }

    public static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    public static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Log.d(TAG, "resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }


    public static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Log.d(TAG, "Image rotation: " + rotation);
        return rotation;
    }

    public static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) return 0;

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
        return cursor.getInt(orientationColumnIndex);
    }


    public static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }


    public static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }
}
