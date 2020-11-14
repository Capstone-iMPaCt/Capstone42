package com.project.ilearncentral.MyClass;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.ilearncentral.Activity.Login;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {

    private static final String TAG = "Connection status: ";

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
//        return true;
//        try {
////            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//            HttpURLConnection urlc = (HttpURLConnection)(new URL("http://clients3.google.com/generate_204").openConnection());
//            urlc.setRequestProperty("User-Agent", "Test");
//            urlc.setRequestProperty("Connection", "close");
//            urlc.setConnectTimeout(1500);
//            urlc.connect();
//            return (urlc.getResponseCode() == 200);
//        } catch (IOException e) {
//            Log.e(TAG, "Internet connection error!", e);
//            return false;
//        }
    }
}
