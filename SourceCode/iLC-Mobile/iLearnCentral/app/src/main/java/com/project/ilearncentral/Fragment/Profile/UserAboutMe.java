package com.project.ilearncentral.Fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.project.ilearncentral.CustomBehavior.ObservableBoolean;
import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;
import com.project.ilearncentral.MyClass.Account;
import com.project.ilearncentral.MyClass.Utility;
import com.project.ilearncentral.R;

public class UserAboutMe extends Fragment {

    TextView addressOutput, birthdateOutput, religionOutput, citizenshipOutput, maritalStatusOutput;

    public UserAboutMe() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_information, container, false);

        setRetainInstance(true);

        addressOutput = view.findViewById(R.id.aboutme_address);
        birthdateOutput = view.findViewById(R.id.aboutme_birthdate);
        religionOutput = view.findViewById(R.id.aboutme_religion);
        citizenshipOutput = view.findViewById(R.id.aboutme_citizenship);
        maritalStatusOutput = view.findViewById(R.id.aboutme_marital_status);

        ObservableBoolean update = new ObservableBoolean();
        update.setOnBooleanChangeListener(new OnBooleanChangeListener() {
            @Override
            public void onBooleanChanged(boolean success) {
                if (success) {
                    setOutputs();
                } else {
                }
            }
        });

        setOutputs();

        Account.updateObservables.add(update);
        if (Account.profileSet) update.set(true);

        return view;
    }

    private void setOutputs() {
        addressOutput.setText(Account.getAddress());
        if (Account.getTimeStampData("birthday")!=null)
            birthdateOutput.setText(Utility.getDateStringFromTimestamp(Account.getTimeStampData("birthday")));
        else
            birthdateOutput.setText("");
        religionOutput.setText(Account.getStringData("religion"));
        citizenshipOutput.setText(Account.getStringData("citizenship"));
        maritalStatusOutput.setText(Account.getStringData("maritalStatus"));
    }
}
