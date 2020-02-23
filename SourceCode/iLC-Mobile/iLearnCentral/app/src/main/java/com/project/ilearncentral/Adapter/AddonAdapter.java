package com.project.ilearncentral.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ilearncentral.Model.Addon;
import com.project.ilearncentral.R;

import java.util.List;

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.AddonViewHolder> {

    private Context context;
    private List<Addon> addon;
    private Intent intent;

    public AddonAdapter(Context context, List<Addon> addon) {
        this.context = context;
        this.addon = addon;
    }

    @NonNull
    @Override
    public AddonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_subscription_row, parent, false);
        return new AddonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddonViewHolder holder, int position) {
        holder.addonContainerLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        holder.titleTextView.setText(addon.get(position).getTitle());
        holder.descriptionTextView.setText(addon.get(position).getDescription());
        holder.countdownTextView.setText(addon.get(position).getCountdown());
    }

    @Override
    public int getItemCount() {
        return addon.size();
    }

    public class AddonViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout addonContainerLayout;
        private TextView titleTextView, descriptionTextView,
            subscriptionLabelTextView, endLabelTextView, countdownTextView;
        private Button subscriptionButton;

        AddonViewHolder(@NonNull View itemView) {
            super(itemView);

            addonContainerLayout = (RelativeLayout)itemView.findViewById(R.id.item_post_container);
            titleTextView = (TextView)itemView.findViewById(R.id.addon_title_textview);
            descriptionTextView = (TextView)itemView.findViewById(R.id.addon_description_textview);
            subscriptionLabelTextView = (TextView)itemView.findViewById(R.id.addon_subscribe_button);
            endLabelTextView = (TextView)itemView.findViewById(R.id.subscription_ends_textview);
            countdownTextView = (TextView)itemView.findViewById(R.id.subscription_expiry_countdown_textview);
            subscriptionButton = (Button)itemView.findViewById(R.id.addon_subscribe_button);
        }
    }
}
