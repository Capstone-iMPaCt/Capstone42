package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.Educator;
import com.project.ilearncentral.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LCEducatorAdapter extends RecyclerView.Adapter<LCEducatorAdapter.LCEducatorHolder> {

    private Context context;
    private List<Educator> educators;

    public LCEducatorAdapter(Context context, List<Educator> educators) {
        this.context = context;
        this.educators = educators;
    }

    @NonNull
    @Override
    public LCEducatorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_lc_educator_item, parent, false);
        return new LCEducatorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LCEducatorHolder holder, final int position) {
        final Educator educator = educators.get(position);

        holder.name.setText(educator.getFullname());
        if (educator.getEmploymentDate()!= null) {
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

        if (holder.userImage.getBackground() == null)
            Picasso.get().load(Uri.parse(educator.getImage())).error(R.drawable.user).fit().into(holder.userImage);
        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Position: " + (position+1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return educators.size();
    }

    public class LCEducatorHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
        private TextView name, position, dateEmployed, status;

        LCEducatorHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.lc_educator_user_image);
            name = itemView.findViewById(R.id.lc_educator_user_name);
            position = itemView.findViewById(R.id.lc_educator_position);
            dateEmployed = itemView.findViewById(R.id.lc_educator_date_employed);
            status = itemView.findViewById(R.id.lc_educator_status);
        }
    }
}
