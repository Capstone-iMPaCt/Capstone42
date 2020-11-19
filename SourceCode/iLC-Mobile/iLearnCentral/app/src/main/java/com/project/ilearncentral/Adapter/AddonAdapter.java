package com.project.ilearncentral.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.Model.Addon;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.Calendar;
import java.util.List;

public class AddonAdapter extends RecyclerView.Adapter<AddonAdapter.AddonViewHolder> {

    private Context context;
    private List<Addon> addon;
    private Intent intent;

    private DatePickerDialog datePickerDialog;
    private Calendar currentDate;

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

        holder.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                final AlertDialog alertDialog;

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.fragment_payment_scheme, (ViewGroup) view.findViewById(R.id.payment_tabhost));
                final TabHost tabHost = layout.findViewById(R.id.payment_tabhost);
                tabHost.setup();
                tabHost.addTab(tabHost.newTabSpec("Card").setContent(R.id.payment_card).setIndicator("Card"));
                tabHost.addTab(tabHost.newTabSpec("Cash").setContent(R.id.payment_cash).setIndicator("Cash"));
                tabHost.addTab(tabHost.newTabSpec("Others").setContent(R.id.payment_others).setIndicator("Others"));
                ((TextView) tabHost.getTabWidget().findViewById(android.R.id.title)).setTextColor(Color.WHITE);
                tabHost.getCurrentTabView().setBackground(context.getDrawable(R.color.colorPrimary));
                tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String s) {
                        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
                            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                            tabHost.getTabWidget().getChildAt(i).setBackground(context.getDrawable(R.drawable.bg_transparent_80));
                            textView.setTextColor(context.getResources().getColor(R.color.dark_gray));
                            if (textView.getText() == s) {
                                tabHost.getCurrentTabView().setBackground(context.getDrawable(R.color.colorPrimary));
                                textView.setTextColor(Color.WHITE);
                            }
                        }
                    }
                });
                builder = new AlertDialog.Builder(context);
                builder.setCancelable(true).setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
                final TextInputEditText cardNumber = alertDialog.findViewById(R.id.payment_card_number);
                final LinearLayout validity = alertDialog.findViewById(R.id.payment_card_validity_layout);
                cardNumber.addTextChangedListener(new TextWatcher() {
                    private static final char space = ' ';
                    boolean checkerIsOn = false;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        if (s.length() == 19)
                            checkerIsOn = true;
                        if (checkerIsOn && !s.toString().isEmpty()) {
                            if (Utility.isValidCardNumber(s.toString()) && s.toString().length() == 19) {
                                validity.setVisibility(View.VISIBLE);
                                cardNumber.setError(null);
                            } else {
                                validity.setVisibility(View.GONE);
                                cardNumber.setError("CARD NUMBER IS INVALID");
//                                Toast.makeText(context, "CARD IS INVALID", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            validity.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().isEmpty()) {
                            // Remove all spacing char
                            int pos = 0;
                            while (true) {
                                if (pos >= s.length()) break;
                                if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                                    s.delete(pos, pos + 1);
                                } else {
                                    pos++;
                                }
                            }
                            // Insert char where needed.
                            pos = 4;
                            while (true) {
                                if (pos >= s.length()) break;
                                final char c = s.charAt(pos);
                                // Only if its a digit where there should be a space we insert a space
                                if ("0123456789".indexOf(c) >= 0) {
                                    s.insert(pos, "" + space);
                                }
                                pos += 5;
                            }
                        }
                    }
                });
                final TextInputEditText cardExpiry = alertDialog.findViewById(R.id.payment_card_expiry_date);
                cardExpiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDate = Calendar.getInstance();
                        int year = currentDate.get(Calendar.YEAR);
                        int month = currentDate.get(Calendar.MONTH);
                        int day = currentDate.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(alertDialog.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                cardExpiry.setText((month+1) + "/" + year);
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                });
                final TextInputEditText cardSecurity = alertDialog.findViewById(R.id.payment_card_security);

                // CASH TAB
                CardForm cardForm = alertDialog.findViewById(R.id.card_form);
                Button buy = alertDialog.findViewById(R.id.btnBuy);
                cardForm.cardRequired(true)
                        .expirationRequired(true)
                        .cvvRequired(true)
//                        .saveCardCheckBoxVisible(true)
//                        .cardholderName(CardForm.FIELD_REQUIRED)
                        .setup((AppCompatActivity) context);
                cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addon.size();
    }

    public class AddonViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout addonContainerLayout;
        private TextView titleTextView, descriptionTextView,
                subscriptionLabelTextView, endLabelTextView, countdownTextView;
        private Button subscribeButton;

        AddonViewHolder(@NonNull View itemView) {
            super(itemView);

            addonContainerLayout = (RelativeLayout) itemView.findViewById(R.id.item_post_container);
            titleTextView = (TextView) itemView.findViewById(R.id.addon_title_textview);
            descriptionTextView = (TextView) itemView.findViewById(R.id.addon_description_textview);
            subscriptionLabelTextView = (TextView) itemView.findViewById(R.id.addon_subscribe_button);
            endLabelTextView = (TextView) itemView.findViewById(R.id.subscription_ends_textview);
            countdownTextView = (TextView) itemView.findViewById(R.id.subscription_expiry_countdown_textview);
            subscribeButton = (Button) itemView.findViewById(R.id.addon_subscribe_button);
        }
    }
}
