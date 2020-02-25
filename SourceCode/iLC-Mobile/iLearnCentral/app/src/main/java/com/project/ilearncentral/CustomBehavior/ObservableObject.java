package com.project.ilearncentral.CustomBehavior;

import com.project.ilearncentral.CustomInterface.OnObjectChangeListener;

public class ObservableObject {
    private OnObjectChangeListener listener;

    private Object value;

    public void setOnObjectChangeListener(OnObjectChangeListener listener) {
        this.listener = listener;
    }

    public Object get() {
        return value;
    }

    public void set(Object value) {
        this.value = value;

        if (listener != null) {
            listener.onChanged(value);
        }
    }
}
