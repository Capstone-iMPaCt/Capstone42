package com.project.ilearncentral.MyClass;

import android.widget.Button;

public class Utility {

    public static void buttonWait(Button button, boolean wait, String continueText) {
        if (wait) {
            button.setEnabled(false);
            button.setText("Please wait...");
        } else {
            button.setEnabled(true);
            button.setText(continueText);
        }
    }
}
