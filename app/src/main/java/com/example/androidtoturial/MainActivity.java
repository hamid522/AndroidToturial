package com.example.androidtoturial;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "LOG";
    EditText edt_Name, edt_PassWord;
    Button btn_save, btn_Show;
    MyDatabase myDatabase;
    ImageView timePicker, datePicker;
    TextView txtTime, txtDate,txtToolbarTitle;
    AlarmManager alarmManager;
    AppCompatCheckBox checkBox;
    private boolean isPm=false;
    ArrayList<PendingIntent> pendingIntents;
    private int requestCode=1001;
    private CheckNetworkReciever checkNetworkReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNetworkReciever = new CheckNetworkReciever();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        registerReceiver(checkNetworkReciever,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        getCurrentTime();
        getCurrentDateAndTime();
        callBroadcast();
        setupViews();
        txtToolbarTitle.setText(getMyIntent());

        myDatabase = new MyDatabase(getApplicationContext());

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(MainActivity.this, MainActivity.this, 12, 25, true);
                dialog.show();
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDate.setText(year + "/" + month + "/" + dayOfMonth);
                    }
                }, 2019, 6, 4);
                dialog.show();
            }
        });
        btn_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (edt_Name.getText().toString().isEmpty() || edt_PassWord.getText().toString().isEmpty() || txtDate.getText().toString().isEmpty() || txtTime.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "وارد کردن اطلاعات ضرورری است", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = myDatabase.addInfo(edt_Name.getText().toString(),edt_PassWord.getText().toString(), checkBox.isChecked() ? 1 : 0, txtDate.getText().toString(), txtTime.getText().toString());
                        setAlarm();
                        Toast.makeText(MainActivity.this, id + "", Toast.LENGTH_SHORT).show();
                        edt_Name.getText().clear();
                        edt_PassWord.getText().clear();
                        edt_Name.requestFocus();
                    }
                } else {
                    if (edt_Name.getText().toString().isEmpty() || edt_PassWord.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "وارد کردن اطلاعات ضرورری است", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = myDatabase.addInfo(edt_Name.getText().toString(),
                                edt_PassWord.getText().toString(), checkBox.isChecked() ? 1 : 0, txtDate.getText().toString(), txtTime.getText().toString());
                        Toast.makeText(MainActivity.this, id + "", Toast.LENGTH_SHORT).show();
                        edt_Name.getText().clear();
                        edt_PassWord.getText().clear();
                        edt_Name.requestFocus();
                    }
                }


            }
        });
    }


    private void setupViews() {
        txtToolbarTitle=findViewById(R.id.txt_toolbar_toolbarTitle);
        alarmManager =(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        edt_Name = findViewById(R.id.edt_main_userName);
        edt_PassWord = findViewById(R.id.edt_main_password);
        btn_save = findViewById(R.id.btn_main_saveMyNote);
        btn_Show = findViewById(R.id.btn_main_showMyNote);
        datePicker = findViewById(R.id.img_main_datePicker);
        timePicker = findViewById(R.id.img_main_timrPicker);
        txtDate = findViewById(R.id.txt_main_date);
        txtTime = findViewById(R.id.txt_main_time);
        checkBox = findViewById(R.id.cb_main_checkbox);

    }

    public String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        txtTime.setText(hourOfDay + ":" + minute);
    }

    public void getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        int am= calendar.get(Calendar.AM_PM);
        Log.i(TAG, "getCurrentDateAndTime: " + year + "/" + month + "/" + day + " time is: " + hour + ":" + min + ":" + sec);
    }

    public void setAlarm() {
        pendingIntents=new ArrayList<>();
        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
        intent.putExtra("message",edt_Name.getText().toString());
        intent.putExtra("clock",txtTime.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calendar = Calendar.getInstance();
        String date= txtDate.getText().toString();
        String time= txtTime.getText().toString();

        String[] times= time.split(":");
        String[] dates= date.split("/");
        if (Integer.parseInt(times[0])>12)
        {
            isPm=true;
        }

        calendar.set(Calendar.YEAR,Integer.parseInt(dates[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
        calendar.set(Calendar.HOUR, isPm?Integer.parseInt(times[0])-12:Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.AM_PM,isPm?1:0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        pendingIntents.add(pendingIntent);
        isPm=false;
        requestCode++;
    }
    private void callBroadcast() {
        Intent intent = new Intent("customBroadcast");
        sendBroadcast(intent);
    }
    private class CheckNetworkReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Network State is Changed", Toast.LENGTH_SHORT).show();
        }
    }
    private String getMyIntent()
    {
        return getIntent().getExtras().getString("sqlLite_Title");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(checkNetworkReciever);
    }
}
