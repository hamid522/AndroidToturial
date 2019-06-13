package com.example.androidtoturial;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {

    TextView txtTitle,txtClock;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        txtTitle = findViewById(R.id.txt_alaram_title);
        txtClock=findViewById(R.id.txt_alarm_clock);
        String tittle = getIntent().getExtras().getString("message");
        String time = getIntent().getExtras().getString("clock");
        txtTitle.setText(tittle);
        txtClock.setText(time);
        mediaPlayer = MediaPlayer.create(this,R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}
