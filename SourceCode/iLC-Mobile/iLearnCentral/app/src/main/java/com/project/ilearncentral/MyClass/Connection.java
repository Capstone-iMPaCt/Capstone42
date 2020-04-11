package com.project.ilearncentral.MyClass;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.ilearncentral.Activity.Login;

public class Connection {

    public static void logOut(Activity activity) {
        if (hasInternetAccess()){
            FirebaseAuth.getInstance().signOut();
            activity.finish();
            Intent intent = new Intent(activity, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            Toast.makeText(activity, "Logged Out", Toast.LENGTH_SHORT).show();
            Account.clear();
            Resume.clearAll();
        }
        else
            Toast.makeText(activity, "No Internet Access", Toast.LENGTH_SHORT).show();
    }

    public static boolean hasInternetAccess(){
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

}
