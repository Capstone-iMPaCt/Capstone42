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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.haozhang.lib.SlantedTextView;
import com.project.ilearncentral.Activity.Payment;
import com.project.ilearncentral.Model.Addon;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Subscription;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.Date;
import java.util.List;

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.AddonViewHolder> {

    private static final String TAG = "AddonAdapter";
    private Context context;
    private List<Addon> addons;
    private Subscription subscription;

    private FirebaseFirestore db;

    public AddonAdapter(Context context, List<Addon> addon) {
        this.context = context;
        this.addons = addon;
    }

    @NonNull
    @Override
    public AddonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_subscription_row, parent, false);
        return new AddonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddonViewHolder holder, int position) {
        final Addon addon = addons.get(position);
        subscription = new Subscription();
        db = FirebaseFirestore.getInstance();

        holder.addonContainerLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.titleTextView.setText(addon.getTitle());
        holder.descriptionTextView.setText(addon.getDescription());
        holder.countdownTextView.setText("Subscription ends on " + addon.getCountdown());
        holder.fee.setText(Utility.showPriceInPHP(addon.getFee()));
        if (addon.getFee() == 0) {
            holder.subscribeButton.setText("Free");
            holder.subscribeButton.setEnabled(false);
            holder.subscribeButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dark_gray));
            holder.horizontalLine.setVisibility(View.VISIBLE);
            holder.countdownTextView.setText("Lifetime free");
            holder.countdownTextView.setVisibility(View.VISIBLE);
        }

        holder.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Payment.class);
                intent.putExtra("fee", addon.getFee());
                if (addon.getTitle().equals("Subscription Level 1"))
                    intent.putExtra("level", 1);
                else if (addon.getTitle().equals("Subscription Level 2"))
                    intent.putExtra("level", 2);
                context.startActivity(intent);
            }
        });

        db.collection("Subscription")
                .document(Account.getCenterId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    int subscriptionLevel = document.getDouble("SubscriptionLevel").intValue();
                    if (document.exists()) {
                        if (subscriptionLevel == 1) {
                            if (addon.getTitle().equals("Subscription Level 1")) {
                                Timestamp timestamp = (com.google.firebase.Timestamp) document.get("SubscriptionExpiry");
                                Date dateNow = new Date();
                                if (dateNow.compareTo(timestamp.toDate()) < 0) {
                                    // If dateNow occurs before SubscriptionExpiry
                                    holder.countdownTextView.setText("Expires on: " + timestamp.toDate().toString());
                                    setSubscribeButton(holder);
                                }
                            }
                        } else if (subscriptionLevel == 2) {
                            if (addon.getTitle().equals("Subscription Level 1") || addon.getTitle().equals("Subscription Level 2")) {
                                Timestamp timestamp = (com.google.firebase.Timestamp) document.get("SubscriptionExpiry");
                                Date dateNow = new Date();
                                if (dateNow.compareTo(timestamp.toDate()) < 0) {
                                    // If dateNow occurs before SubscriptionExpiry
                                    holder.countdownTextView.setText("Expires on: " + timestamp.toDate().toString());
                                    setSubscribeButton(holder);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void setSubscribeButton(AddonViewHolder holder) {
        holder.subscribeButton.setText("Subscribed");
        holder.subscribeButton.setEnabled(false);
        holder.subscribeButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dark_gray));
        holder.horizontalLine.setVisibility(View.VISIBLE);
        holder.countdownTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return addons.size();
    }

    public class AddonViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout addonContainerLayout;
        private View horizontalLine;
        private TextView titleTextView, descriptionTextView,
                subscriptionLabelTextView, endLabelTextView, countdownTextView;
        private Button subscribeButton;
        private SlantedTextView fee;

        AddonViewHolder(@NonNull View itemView) {
            super(itemView);

            addonContainerLayout = (RelativeLayout) itemView.findViewById(R.id.subscription_item_post_container);
            titleTextView = (TextView) itemView.findViewById(R.id.subscription_title_textview);
            descriptionTextView = (TextView) itemView.findViewById(R.id.subscription_description_textview);
            subscriptionLabelTextView = (TextView) itemView.findViewById(R.id.subscription_subscribe_button);
            countdownTextView = (TextView) itemView.findViewById(R.id.subscription_expiry_countdown_textview);
            subscribeButton = (Button) itemView.findViewById(R.id.subscription_subscribe_button);
            horizontalLine = itemView.findViewById(R.id.subscription_horizontal_line_divider);
            fee = (SlantedTextView) itemView.findViewById(R.id.subscription_fee);
        }
    }
}
