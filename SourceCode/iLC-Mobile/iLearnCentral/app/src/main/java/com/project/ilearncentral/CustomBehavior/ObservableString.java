package com.project.ilearncentral.CustomBehavior;

import com.project.ilearncentral.CustomInterface.OnStringChangeListener;

public class ObservableString {
    private OnStringChangeListener listener;

    private String value;

    public void setOnStringChangeListener(OnStringChangeListener listener)
    {
        this.listener = listener;
    }

    public String get()
    {
        return value;
    }

    public void set(String value)
    {
        this.value = value;

        if(listener != null)
        {
            listener.onStringChanged(value);
        }
    }
}
