package com.project.ilearncentral.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.Model.Verification;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingVerificationsAdapter extends RecyclerView.Adapter<PendingVerificationsAdapter.PendingVerificationViewHolder> {

    private Context context;
    private List<Verification> verificationList;
    private int lastPosition = -1;
    private StorageReference storageRef;
    private ObservableBoolean listener;

    public PendingVerificationsAdapter(Context context, List<Verification> verificationList, ObservableBoolean listener) {
        this.context = context;
        this.verificationList = verificationList;
        this.listener = listener;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public PendingVerificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_pending_verification_row, parent, false);
        return new PendingVerificationViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return verificationList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingVerificationViewHolder holder, final int position) {
        final Verification verification = verificationList.get(position);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.move_up : R.anim.move_down));

        holder.name.setText(verification.getName());
        holder.date.setText(Utility.getDateAsString(verification.getDate()));
        holder.date.setVisibility(View.GONE);
        getImage(holder.permitPhoto, verification.getFolder(), verification.getSubfolder(), verification.getPermitFile());
        getImage(holder.birPhoto, verification.getFolder(), verification.getSubfolder(), verification.getBirFile());

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true).setTitle("Confirm Approval").setMessage("Approve verification?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> verificationStatus = new HashMap<>();
                                verificationStatus.put("VerificationStatus", "verified");
                                FirebaseFirestore.getInstance().collection("LearningCenter")
                                        .document(verification.getSubfolder())
                                        .update(verificationStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Account " +
                                                        verification.getSubfolder() +
                                                        " verified.", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true).setTitle("Confirm Rejection").setMessage("Reject verification?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> verificationStatus = new HashMap<>();
                                verificationStatus.put("VerificationStatus", "rejected");
                                FirebaseFirestore.getInstance().collection("LearningCenter")
                                        .document(verification.getSubfolder())
                                        .update(verificationStatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Account " +
                                                        verification.getSubfolder() +
                                                        " rejected.", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void getImage(final ImageView imageView, String folder, String subfolder, String file) {
        try {
            storageRef.child(folder)
                    .child(subfolder)
                    .child(file)
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    Glide.with(context).load(uri.toString()).fitCenter().into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewImage(uri);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {}
    }

    private void viewImage(Uri uri){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View mView = ((Activity)context).getLayoutInflater().inflate(R.layout.fragment_dialog_photoview, null);
        final PhotoView photoView = mView.findViewById(R.id.photoview);
        photoView.setMinimumHeight(Utility.getScreenHeight());
        Glide.with(context).load(uri.toString()).centerCrop().into(photoView);
        builder.setView(mView);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected class PendingVerificationViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView name, date;
        private ImageView permitPhoto, birPhoto;
        private Button approve, reject;

        public PendingVerificationViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.rpvr_root);
            name = itemView.findViewById(R.id.rpvr_user_name);
            date = itemView.findViewById(R.id.rpvr_date);
            permitPhoto = itemView.findViewById(R.id.rpvr_permit_imageview);
            birPhoto = itemView.findViewById(R.id.rpvr_bir_imageview);
            approve = itemView.findViewById(R.id.rpvr_approve_button);
            reject = itemView.findViewById(R.id.rpvr_reject_button);
        }
    }
}
