package com.project.ilearncentral.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.Model.LearningCenter;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

import java.util.ArrayList;
import java.util.List;

public class LearningCenterProfile extends Fragment {

    private TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    private TextView businessName, serviceType, businessAddress, openingTime, closingTime, email, contact, website, aboutUs;
    private LinearLayout emailLayout, contactLayout, websiteLayout;
    private CardView aboutUsLayout;
    private List<String> operatingDays;
    private ObservableBoolean update;

    public LearningCenterProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_learning_center, container, false);

        sunday = view.findViewById(R.id.course_sched_day_sun);
        monday = view.findViewById(R.id.course_sched_day_mon);
        tuesday = view.findViewById(R.id.course_sched_day_tue);
        wednesday = view.findViewById(R.id.course_sched_day_wed);
        thursday = view.findViewById(R.id.course_sched_day_thu);
        friday = view.findViewById(R.id.course_sched_day_fri);
        saturday = view.findViewById(R.id.course_sched_day_sat);
        businessName = view.findViewById(R.id.learning_center_name);
        serviceType = view.findViewById(R.id.learningcenter_type);
        businessAddress = view.findViewById(R.id.learning_center_address);
        openingTime = view.findViewById(R.id.learning_center_opening_time);
        closingTime = view.findViewById(R.id.learning_center_closing_time);
        email = view.findViewById(R.id.learningcenter_profile_email);
        contact = view.findViewById(R.id.learningcenter_profile_contact);
        website = view.findViewById(R.id.learningcenter_profile_website);
        aboutUs = view.findViewById(R.id.learning_center_about_us);
        emailLayout = view.findViewById(R.id.learningcenter_profile_email_layout);
        contactLayout = view.findViewById(R.id.learningcenter_profile_contact_layout);
        websiteLayout = view.findViewById(R.id.learningcenter_profile_website_layout);
        aboutUsLayout = view.findViewById(R.id.learning_center_about_us_layout);
        operatingDays = new ArrayList<>();

        update = new ObservableBoolean();
        update.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean newValue) {
                if (newValue) {
                    businessName.setText(Account.getBusinessName());
                    businessAddress.setText(Account.getBusinessAddress());
                    serviceType.setText(Account.getStringData("bServiceType"));
                    openingTime.setText(Utility
                            .getStringFromTime(Account.getTimeStampData("bOpeningTime")));
                    closingTime.setText(Utility
                            .getStringFromTime(Account.getTimeStampData("bClosingTime")));
                    operatingDays.clear();
                    operatingDays.addAll((List<String>) Account.get("bOperatingDays"));
                    checkDay("Mon", monday);
                    checkDay("Tue", tuesday);
                    checkDay("Wed", wednesday);
                    checkDay("Thu", thursday);
                    checkDay("Fri", friday);
                    checkDay("Sat", saturday);
                    checkDay("Sun", sunday);

                    if (Account.getStringData("bEmail").isEmpty()) {
                        emailLayout.setVisibility(View.GONE);
                    } else {
                        emailLayout.setVisibility(View.VISIBLE);
                        email.setText(Account.getStringData("bEmail"));
                    }
                    if (Account.getStringData("bContactNumber").isEmpty()) {
                        contactLayout.setVisibility(View.GONE);
                    } else {
                        contactLayout.setVisibility(View.VISIBLE);
                        contact.setText(Account.getStringData("bContactNumber"));
                    }
                    if (Account.getStringData("bWebsite").isEmpty()) {
                        websiteLayout.setVisibility(View.GONE);
                    } else {
                        websiteLayout.setVisibility(View.VISIBLE);
                        website.setText(Account.getStringData("bWebsite"));
                    }
                    if (Account.getStringData("bDescription").isEmpty()) {
                        aboutUsLayout.setVisibility(View.GONE);
                    } else {
                        aboutUsLayout.setVisibility(View.VISIBLE);
                        aboutUs.setText(Account.getStringData("bDescription"));
                    }
                    Account.businessSet = false;
                }
            }
        });
        Account.updateObservables.add(update);
        if (Account.businessSet) update.set(true);
        if (Account.openCenter) {
            LearningCenter lc = LearningCenter.getLCById(Account.getStringData("openLC"));
            businessName.setText(lc.getBusinessName());
            businessAddress.setText(lc.getBusinessAddress());
            serviceType.setText(lc.getServiceType());
            openingTime.setText(Utility
                    .getStringFromTime(lc.getOpen()));
            closingTime.setText(Utility
                    .getStringFromTime(lc.getClose()));
            operatingDays.clear();
            operatingDays.addAll((List<String>) lc.getOperatingDays());
            checkDay("Mon", monday);
            checkDay("Tue", tuesday);
            checkDay("Wed", wednesday);
            checkDay("Thu", thursday);
            checkDay("Fri", friday);
            checkDay("Sat", saturday);
            checkDay("Sun", sunday);

            if (lc.getContactEmail().isEmpty()) {
                emailLayout.setVisibility(View.GONE);
            } else {
                emailLayout.setVisibility(View.VISIBLE);
                email.setText(lc.getContactEmail());
            }
            if (lc.getContactNumber().isEmpty()) {
                contactLayout.setVisibility(View.GONE);
            } else {
                contactLayout.setVisibility(View.VISIBLE);
                contact.setText(lc.getContactNumber());
            }
            if (lc.getCompanyWebsite().isEmpty()) {
                websiteLayout.setVisibility(View.GONE);
            } else {
                websiteLayout.setVisibility(View.VISIBLE);
                website.setText(lc.getCompanyWebsite());
            }
            if (lc.getDescription().isEmpty()) {
                aboutUsLayout.setVisibility(View.GONE);
            } else {
                aboutUsLayout.setVisibility(View.VISIBLE);
                aboutUs.setText(lc.getDescription());
            }
            Account.openCenter = false;
        }
        return view;
    }

    private void checkDay(String text, View v) {
        if (operatingDays.contains(text))
            v.setBackgroundResource(R.drawable.bg_selected_day_rounded);
    }

    @Override
    public void onResume() {
        super.onResume();
        update.set(true);
    }
}
