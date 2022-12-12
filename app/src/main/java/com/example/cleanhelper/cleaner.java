package com.example.cleanhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class cleaner extends AppCompatActivity {

    Spinner chooseid;
    EditText user, rooms, bRooms, fType, date, time, fBack;
    Button accept, feedback;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner);

        database myDB = new database(this);

        chooseid = findViewById(R.id.spinner);
        user = findViewById(R.id.user);
        rooms = findViewById(R.id.rooms);
        bRooms = findViewById(R.id.brooms);
        fType = findViewById(R.id.ftype);
        date = findViewById(R.id.cDate);
        time = findViewById(R.id.cTime);
        fBack = findViewById(R.id.cFeedback);
        accept = findViewById(R.id.cView);
        feedback = findViewById(R.id.cFeedbackBtn);
        image = findViewById(R.id.cImage);


        Cursor projects = myDB.getProject();
        if(projects.getCount() != 0) {
            while (projects.moveToNext()) {
                if(projects.getString(4).equals("0")) {
                    //set home ids in to spinner
                    ArrayList<String> allIDs = myDB.getHomeID();
                    ArrayAdapter<String> IDAdapter = new ArrayAdapter<String>(cleaner.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allIDs);
                    chooseid.setAdapter(IDAdapter);
                    //end of putting values to spinner
                }
                else
                {

                }
            }
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = myDB.acceptProject(chooseid.getSelectedItem().toString(),"1");
                if(isUpdate)
                {
                    Toast.makeText(cleaner.this, "accepted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSend = myDB.setFeedback("@Cleaner",fBack.getText().toString());
                if(isSend)
                {
                    fBack.setText(null);
                    Toast.makeText(cleaner.this, "Send success", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //getting home info according to spinner value
        chooseid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor respond = myDB.getHomeInfoToSpinner(chooseid.getSelectedItem().toString());
                Cursor dates = myDB.getProject();
                if (respond.getCount() == 0 || dates.getCount() == 0) {
                    Toast.makeText(cleaner.this, "Project Information are not available, Check Selected ID..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StringBuffer sb = new StringBuffer();
                    while(respond.moveToNext() && dates.moveToNext())
                    {
                        if (dates.getString(1).equals(respond.getString(1))) //check project and home users same
                        {
                            if(dates.getString(4).equals("0")) { // show only not accepted requests

                                user.setText(sb.append("Customer: ").append(respond.getString(1)));
                                rooms.setText(sb.append("Rooms: ").append(respond.getString(2)));
                                bRooms.setText(sb.append("Bathrooms: ").append(respond.getString(3)));
                                fType.setText(sb.append("Flore Type: ").append(respond.getString(4)));
                                date.setText(sb.append("Date :").append(dates.getString(2)));
                                time.setText(sb.append("Time: ").append(dates.getString(3)));

                                }
                        }
                        else
                        {
                            user.setText(null);
                        }
                    }
                    //image.setImageBitmap(myDB.getImage(user.getText().toString()));
                    Toast.makeText(cleaner.this, "Here your information..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}