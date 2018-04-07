package com.example.pc.alarmnotificationthroughtimepickerdemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btnSetTime;
    private Button btnRepeatTime;
    private Button btnTimePicker;
    private RadioGroup radioGroup;
    private RadioButton rbNotification;
    private RadioButton rbToast;
    private TextView textAlarmPrompt;

    TimePicker myTimePicker;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textAlarmPrompt = (TextView) findViewById(R.id.alarmprompt);

        btnTimePicker = (Button) findViewById(R.id.btntimepicker);
        btnSetTime = (Button) findViewById(R.id.btnsettime);
        btnRepeatTime = (Button) findViewById(R.id.btnrepeattime);
        radioGroup = (RadioGroup) findViewById(R.id.rg);
        rbNotification = (RadioButton) findViewById(R.id.rbnotification);
        rbToast = (RadioButton) findViewById(R.id.rbtoast);

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTimePickerDialog(false);
            }
        });

        btnRepeatTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbToast.isChecked()) {
                    setAlarm(true, true);
                } else {
                    setAlarm(false, true);
                }
            }
        });


        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAlarmPrompt.setText("");
                TimePickerDialog mTimePicker;
                final Calendar calendar = Calendar.getInstance();

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);


                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        textAlarmPrompt.setText(selectedHour + ":" + selectedMinute);

                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        //Create a new PendingIntent and add it to the AlarmManager
                        Intent intent = new Intent(getApplicationContext(), AlarmNotificationReceiver.class);
                        intent.setAction("localNotification");

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);

                        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                    }
                }, hour, minute, false);//Yes 24 hour time


                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });
    }


    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(MainActivity.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            //calSet.setTimeZone(TimeZone.getTimeZone("GMT"));

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {

        textAlarmPrompt.setText("\n\n***\n" + "Alarm is set " + targetCal.getTime() + "\n" + "***\n");

        Intent intent = new Intent(getBaseContext(), AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }


    private void setAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent;
        PendingIntent pendingIntent;

        if (!isNotification) {
            intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        } else {
            intent = new Intent(getApplicationContext(), AlarmNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        }

        if (!isRepeat) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, 3000, pendingIntent);
        }
    }


}

