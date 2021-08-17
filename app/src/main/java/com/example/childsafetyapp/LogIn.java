package com.example.childsafetyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android. content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {

    EditText username, password;
    Button  btnlogin;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        username = (EditText) findViewById(R.id.edit_user);
        password = (EditText) findViewById(R.id.edit_pw);
        btnlogin= (Button) findViewById(R.id.btn_login);
        db = new DBHelper(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(LogIn.this, "please enter all the fields", Toast.LENGTH_SHORT).show();

                else {
                    Boolean checkuserpass = db.checkusernamepassword(user, pass);

                    if(checkuserpass==true && user == "admin") {

                        Intent intent = new Intent(getApplicationContext(), Admin.class);
                       startActivity(intent);

                    }


                    else if(checkuserpass==true && user != "admin") {

                        Toast.makeText(LogIn.this, "Sign in successfull", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                        username.setText("");
                        password.setText("");

                    }else{
                        Toast.makeText(LogIn.this, "Invalid credentials", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

    }

    public void onRegister(View view){
        Intent intent =new Intent(this, Register.class);
                startActivity(intent);
    }

    public void onLaunchSignIn(View view){
        Intent i =new Intent(this, Home.class);
        startActivity(i);
    }
}