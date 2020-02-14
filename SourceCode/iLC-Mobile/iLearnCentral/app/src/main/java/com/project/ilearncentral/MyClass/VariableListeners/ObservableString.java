package com.project.ilearncentral.MyClass.VariableListeners;

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
