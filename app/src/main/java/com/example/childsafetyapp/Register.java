package com.example.childsafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText username, password,repassword;
    Button signup;
    TextView signin;
    TextView email;
    DBHelper MYDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.edt_username);
        password = (EditText) findViewById(R.id.edt_password);
        repassword = (EditText) findViewById(R.id.confirm_pw);
        email   = (EditText) findViewById(R.id.edt_email);
        signup     = (Button) findViewById(R.id.reg_button);
        signin   = (TextView) findViewById(R.id.go_signin);
        MYDB = new DBHelper(this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString();
                String pass = password.getText().toString();
                String emai = email.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("") || pass.equals("") || repass.equals(""))
                    Toast.makeText(Register.this, "please enter all the fields", Toast.LENGTH_SHORT).show();

                else{

                   if(pass.equals(repass)) {
                       Boolean checkuser = MYDB.checkusername(user);
                       if(checkuser==false){
                           Boolean insert = MYDB.insertdata(user, pass, emai);
                           if(insert==true){

                               Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), LogIn.class);
                               startActivity(intent);
                               username.setText("");
                               password.setText("");
                               repassword.setText("");
                               email.setText("");


                           }else{
                               Toast.makeText(Register.this, "Registered failed", Toast.LENGTH_SHORT).show();

                           }
                       }/*else{

                           Toast.makeText(Register.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();

                       }*/

                   }else{
                       Toast.makeText(Register.this, "Password not marching", Toast.LENGTH_SHORT).show();

                   }
                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
            }
        });




    }
}