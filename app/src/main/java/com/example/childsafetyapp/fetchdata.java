package com.example.childsafetyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class fetchdata extends AppCompatActivity {
    ImageButton imageadd;
    RecyclerView recyclerView;
    ArrayList<contactmodel> dataholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetchdata);
        imageadd =  findViewById(R.id.btn_add_helpline);
        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), AddHelpLine.class));
            }
        });
        recyclerView =findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = new DBHelper(this).getData();
        dataholder = new ArrayList<>();
        while (cursor.moveToNext()){
            contactmodel obj = new contactmodel(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            dataholder.add(obj);

        }
        helplineadapter adapter = new helplineadapter(dataholder);
        recyclerView.setAdapter(adapter);
    }


}