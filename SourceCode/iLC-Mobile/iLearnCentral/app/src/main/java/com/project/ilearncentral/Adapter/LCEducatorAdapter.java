package com.project.ilearncentral.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ilearncentral.Activity.ViewUser;
import com.project.ilearncentral.Model.Educator;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LCEducatorAdapter extends RecyclerView.Adapter<LCEducatorAdapter.LCEducatorHolder> {

    private Context context;
    private List<Educator> educators;
    private StorageReference storageRef;

    public LCEducatorAdapter(Context context, List<Educator> educators) {
        this.context = context;
        this.educators = educators;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public LCEducatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_lc_educator_item, parent, false);
        return new LCEducatorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LCEducatorHolder holder, final int position) {
        final Educator educator = educators.get(position);

        holder.name.setText(educator.getFullname());
        if (educator.getEmploymentDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            holder.dateEmployed.setText(dateFormat.getInstance()
                    .format(educator.getEmploymentDate().toDate()));
        }
        holder.status.setText(educator.getEmploymentStatus());
        holder.position.setText(educator.getPosition());

        if (holder.status.getText().toString().equalsIgnoreCase("ACTIVE")) {
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setTextColor(Color.RED);
        }

//        if (holder.userImage.getBackground() == null)
        getImage(holder.userImage, "images/", educator.getUsername());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewUser.class);
                intent.putExtra("USERNAME", educator.getUsername());
                intent.putExtra("TYPE", educator.getType());
                intent.putExtra("FULL_NAME", educator.getFullname());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return educators.size();
    }

    private void getImage(final ImageView imageView, String folderName, String filename) {
        try {
            storageRef.child(folderName).child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    imageView.setVisibility(View.VISIBLE);
                    Glide.with(context).load(uri.toString()).fitCenter().into(imageView);
//                Picasso.get().load(uri.toString()).centerCrop().fit().into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Utility.viewImage(((Activity)context), uri);
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

    public class LCEducatorHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
        private TextView name, position, dateEmployed, status;
        private LinearLayout layout;

        LCEducatorHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.lc_educator_layout);
            userImage = itemView.findViewById(R.id.lc_educator_user_image);
            name = itemView.findViewById(R.id.lc_educator_user_name);
            position = itemView.findViewById(R.id.lc_educator_position);
            dateEmployed = itemView.findViewById(R.id.lc_educator_date_employed);
            status = itemView.findViewById(R.id.lc_educator_status);
        }
    }
}
