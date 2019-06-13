package com.example.androidtoturial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    MyDatabase myDatabase;
    private List<NoteModel> dataList;
    List<PendingIntent> pendingIntents;
    AlarmManager alarmManager;
    private boolean isPM=false;
    private int requestCode=1001;
    @Override
    public void onReceive(Context context, Intent intent) {

        myDatabase = new MyDatabase(context);
        dataList = new ArrayList<>();
        pendingIntents = new ArrayList<>();
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent alaramIntent = new Intent(context,AlarmActivity.class);
        alaramIntent.addFlags(alaramIntent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Cursor cursor = myDatabase.getSomeInfo(1);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            NoteModel noteModel = new NoteModel();

            noteModel.setId(cursor.getInt(0));
            noteModel.setTitle(cursor.getString(1));
            noteModel.setDesc(cursor.getString(2));
            noteModel.setRemember(cursor.getInt(3));
            noteModel.setDate(cursor.getString(3));
            noteModel.setTime(cursor.getString(5));
            dataList.add(noteModel);

            Calendar calendar = Calendar.getInstance();

            String[] dates = noteModel.getDate().split("/");
            String[] times = noteModel.getTime().split(":");
            if (Integer.parseInt(times[0])>12)
            {
                isPM=true;
            }

            calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
            calendar.set(Calendar.HOUR, isPM?Integer.parseInt(times[0])-12:Integer.parseInt(times[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.AM_PM,isPM?1:0);
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            pendingIntents.add(pendingIntent);
            isPM=false;
            requestCode++;

        }
    }
}
