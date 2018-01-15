package com.jaykallen.sanctuary;
// Created by Jay Kallen on 1/14/2017.

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddApptActivity extends AppCompatActivity {
    static final int REQUEST_CODE = 1;
    private Toolbar mToolbar;
    Button mAddButton;
    TimePicker mTime;
    EditText mTitle;
    Spinner mImage;
    Appointment mAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appt);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_add);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTime = (TimePicker) findViewById(R.id.add_time);
        mTitle = (EditText) findViewById(R.id.add_title);
        mImage = (Spinner) findViewById(R.id.add_image_spinner);
        mAddButton = (Button) findViewById(R.id.add_appt_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String time = mTime.getText().toString();
                int hour = mTime.getCurrentHour();
                int minute = mTime.getCurrentMinute();
                String time = GetTime(hour, minute);
                String title  = mTitle.getText().toString();
                String image = mImage.getSelectedItem().toString();
                mAppointment = new Appointment("",time,title,"N/A",image, false);

                Intent data = new Intent();
                data.putExtra("appt", mAppointment);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private String GetTime (int hrs, int mins) {
        String hourstring;
        String minstring;
        if (hrs < 10) {
            hourstring = "0" + hrs;
        } else {
            hourstring = "" + hrs;
        }
        if (mins < 10) {
            minstring = "0" + mins;
        } else {
            minstring = "" + mins;
        }
        return hourstring + ":" + minstring;
    }

}
