package com.example.androidtoturial;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Printer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    MyDatabase myDatabase;
    RecyclerView recyclerView;
    private List<NoteModel> dataList;
    private static final String TAG  = "LOG" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setupView();
        getDataFromDB();
        recyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new NoteAdapter(SecondActivity.this,dataList));

    }

    private void getDataFromDB()
    {
        myDatabase = new MyDatabase(getApplicationContext());
        Cursor cursor = myDatabase.getInfo();
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            NoteModel noteModel = new NoteModel();
            noteModel.setId(cursor.getInt(0));
            noteModel.setTitle(cursor.getString(1));
            noteModel.setDesc(cursor.getString(2));
            noteModel.setRemember(cursor.getInt(3));
            noteModel.setDate(cursor.getString(4));
            noteModel.setTime(cursor.getString(5));
            dataList.add(noteModel);
        }
    }

    private void setupView() {
        recyclerView = findViewById(R.id.rv_second_noteList);
        dataList=new ArrayList<>();
    }
}
