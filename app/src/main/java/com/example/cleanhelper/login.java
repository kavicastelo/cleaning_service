package com.example.cleanhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class login extends AppCompatActivity {

    EditText user, pass;
    Button log, sign, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database myDB = new database(this);

        user = findViewById(R.id.luname);
        pass = findViewById(R.id.lpass);

        log = findViewById(R.id.llogin);
        sign = findViewById(R.id.lsign);
        cancel = findViewById(R.id.lcancel);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().equals("")||pass.getText().toString().equals("")) // check fields are empty
                {
                    Toast.makeText(login.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Cursor respond = myDB.getLogin(); //get login details
                    if(respond.getCount()!=0)
                    {
                        while (respond.moveToNext())
                        {
                            if(user.getText().toString().equals(respond.getString(1))) // check username
                            {
                                if(pass.getText().toString().equals(respond.getString(2))) // check password
                                {
                                        if (respond.getString(3).equals("0")) {
                                            //admin
                                            Intent i = new Intent(login.this, admin.class);
                                            startActivity(i);
                                        } else if (respond.getString(3).equals("1")) {
                                            //constructor
                                            Intent i = new Intent(login.this, cunstructor.class);
                                            startActivity(i);
                                        } else if (respond.getString(3).equals("2")) {
                                            //cleaner
                                            Intent i = new Intent(login.this, cleaner.class);
                                            startActivity(i);
                                        } else if (respond.getString(3).equals("3")) {
                                            //user
                                            Intent i = new Intent(login.this, user.class);
                                            startActivity(i);
                                        }
                                }
                                else
                                {
                                    //Toast.makeText(login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(login.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(login.this, "No data available in database", Toast.LENGTH_SHORT).show();
                    }
                    user.setText(null);
                    pass.setText(null);
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this,register.class);
                startActivity(i);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}