package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.LCEducator;
import com.project.ilearncentral.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LCEducatorAdapter extends RecyclerView.Adapter<LCEducatorAdapter.LCEducatorHolder> {

    private Context context;
    private ArrayList<LCEducator> educators;

    public LCEducatorAdapter(Context context, ArrayList<LCEducator> educators) {
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
        final LCEducator educator = educators.get(position);

        holder.name.setText(educator.getEducatorName());
        holder.dateEmployed.setText(educator.getEducatorDateEmployed());
        holder.status.setText(educator.getEducatorStatus());

        if (holder.status.getText().equals("ACTIVE")) {
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setTextColor(Color.RED);
        }
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
        private TextView name, dateEmployed, status;

        LCEducatorHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.lc_educator_user_image);
            name = itemView.findViewById(R.id.lc_educator_user_name);
            dateEmployed = itemView.findViewById(R.id.lc_educator_date_employed);
            status = itemView.findViewById(R.id.lc_educator_status);
        }
    }
}
