package com.project.ilearncentral.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.project.ilearncentral.R;

public class UserProfile extends Fragment {

    TextView uname, pwd;

    public UserProfile(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_educator, container, false);
        // Codes here
        uname = (TextView)view.findViewById(R.id.email_textview);

//        uname.setText(Connection.currentUser.getEmail());
        return view;
    }
}
