package com.project.ilearncentral.Activity.SignUp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ilearncentral.R;

import java.util.Calendar;

public class SignUpEducator extends AppCompatActivity {

    private DatePickerDialog picker;
    private TextInputEditText birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_educator);

        birthDate = (TextInputEditText) findViewById(R.id.birthdateTextInput);
        birthDate.setInputType(InputType.TYPE_NULL);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendarldr = Calendar.getInstance();
                int day = calendarldr.get(Calendar.DAY_OF_MONTH);
                int month = calendarldr.get(Calendar.MONTH);
                int year = calendarldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignUpEducator.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }
}
