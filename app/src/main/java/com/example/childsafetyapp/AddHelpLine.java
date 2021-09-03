package com.example.childsafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddHelpLine extends AppCompatActivity {
    EditText orgname, orgnumber,orgemail;
    Button addhelpline;
    DBHelper MYDB;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_helplines);

        orgname = (EditText) findViewById(R.id.txt_helpline_name);
        orgnumber = (EditText) findViewById(R.id.txt_helplinenumber);
        orgemail = (EditText) findViewById(R.id.txt_helpline_email);
        addhelpline     = (Button) findViewById(R.id.btn_add_line2);
        MYDB = new DBHelper(this);

        addhelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orname = orgname.getText().toString();
                String ornumber = orgnumber.getText().toString();
                String oremai = orgemail.getText().toString();

                if(orname.equals("") || ornumber.equals("") || oremai.equals(""))
                    Toast.makeText(AddHelpLine.this, "please enter all the fields", Toast.LENGTH_SHORT).show();

                else{
                            Boolean insert = MYDB.inserthelplinedata(orname, ornumber, oremai);
                            if(insert==true){

                                Toast.makeText(AddHelpLine.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), fetchdata.class);
                                startActivity(intent);
                                orgname.setText("");
                                orgnumber.setText("");
                                orgemail.setText("");


                            }else{
                                Toast.makeText(AddHelpLine.this, "Registered failed", Toast.LENGTH_SHORT).show();

                            }
                        }


                }



        });


    }
}