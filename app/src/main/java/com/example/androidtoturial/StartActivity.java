package com.example.androidtoturial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class StartActivity extends AppCompatActivity {
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        cardView = findViewById(R.id.card_start_parent);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(StartActivity.this,MainActivity.class);
               intent.putExtra("sqlLite_Title","اموزش دیتابیس و لیست ویو");
               startActivity(intent);
            }
        });
    }
}
