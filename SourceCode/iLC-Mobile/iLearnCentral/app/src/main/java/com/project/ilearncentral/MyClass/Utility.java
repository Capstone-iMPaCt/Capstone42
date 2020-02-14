package com.project.ilearncentral.MyClass;

import android.widget.Button;

public class Utility {

    public static void buttonWait(Button button, boolean wait) {
        buttonWait(button, wait, "");
    } public static void buttonWait(Button button, boolean wait, String continueText) {
        if (wait) {
            button.setEnabled(false);
            if (continueText.isEmpty())
                button.setText("Please wait...");
            else
                button.setText(continueText);
        } else {
            button.setEnabled(true);
            button.setText(continueText);
        }
    }
    public static String caps(String text) {
        if (text.isEmpty()) {
            return "";
        } else if (text.length() == 1) {
            return text.toUpperCase();
        }
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    public static String capsEachWord(String text) {
        String[] words = text.split("\\s");
        String capText = "";
        for (String word:words) {
            capText += caps(word) + " ";
        }
        return capText.trim();
    }
}
