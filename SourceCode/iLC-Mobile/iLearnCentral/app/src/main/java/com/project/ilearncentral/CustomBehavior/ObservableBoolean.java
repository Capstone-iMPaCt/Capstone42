package com.project.ilearncentral.CustomBehavior;


import com.project.ilearncentral.CustomInterface.OnBooleanChangeListener;

public class ObservableBoolean {
    private OnBooleanChangeListener listener;

    private boolean value;

    public void setOnBooleanChangeListener(OnBooleanChangeListener listener)
    {
        this.listener = listener;
    }

    public boolean get()
    {
        return value;
    }

    public void set(boolean value)
    {
        this.value = value;

        if(listener != null)
        {
            listener.onBooleanChanged(value);
        }
    }
}
