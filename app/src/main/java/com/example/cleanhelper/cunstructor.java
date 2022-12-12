package com.example.cleanhelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class cunstructor extends AppCompatActivity {

    EditText user;
    Button set, kick, viewU, viewC, viewF;
    Spinner choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cunstructor);

        database myDB = new database(this);

        user = findViewById(R.id.cusername);
        set = findViewById(R.id.cUpromote);
        kick = findViewById(R.id.cUdemote);
        viewU = findViewById(R.id.cViewU);
        viewC = findViewById(R.id.cViewC);
        viewF = findViewById(R.id.cViewF);
        choose = findViewById(R.id.cUSpinner);

        //set login ids in to spinner
        ArrayList<String> allIDs = myDB.getLoginID();
        ArrayAdapter<String> IDAdapter = new ArrayAdapter<String>(cunstructor.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allIDs);
        choose.setAdapter(IDAdapter);
        //end of putting values to spinner

        choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor respond = myDB.getLoginInfoToSpinner(choose.getSelectedItem().toString());
                if(respond.getCount() != 0)
                {
                    while (respond.moveToNext())
                    {
                        user.setText(respond.getString(1));
                    }
                }
                else
                {
                    user.setText(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = myDB.updateLevel(choose.getSelectedItem().toString(),"2");
                if(isUpdate)
                {
                    Toast.makeText(cunstructor.this, "Set cleaner", Toast.LENGTH_SHORT).show();
                }
            }
        });

        kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = myDB.updateLevel(choose.getSelectedItem().toString(),"3");
                if(isUpdate)
                {
                    Toast.makeText(cunstructor.this, "Set user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor respond = myDB.getLogin();
                if(respond.getCount() != 0)
                {
                    StringBuffer sb = new StringBuffer();
                    while (respond.moveToNext())
                    {
                        if(respond.getString(3).equals("3"))
                        {
                            sb.append(respond.getString(1)+"\n");
                        }
                        AlertDialogBox("Customers", sb.toString());
                    }
                }
                else
                {
                    user.setText(null); //purpose - prevents button crash
                }
            }
        });

        viewC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor respond = myDB.getLogin();
                if(respond.getCount() != 0)
                {
                    StringBuffer sb = new StringBuffer();
                    while (respond.moveToNext())
                    {
                        if(respond.getString(3).equals("2"))
                        {
                            sb.append(respond.getString(1)+"\n");
                        }
                        AlertDialogBox("Cleaners", sb.toString());
                    }
                }
                else
                {
                    user.setText(null); //purpose - prevents button crash
                }
            }
        });

        viewF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor respond = myDB.getFeedbacks();
                if(respond.getCount() != 0)
                {
                    StringBuffer sb = new StringBuffer();
                    while (respond.moveToNext())
                    {
                        sb.append("Name: "+respond.getString(1)+"\n");
                        sb.append("Feedback: "+respond.getString(2)+"\n");
                        sb.append("----------------------------\n");
                    }
                    AlertDialogBox("Feedbacks", sb.toString());
                }
                else
                {
                    user.setText(null); // purpose - prevents the button crash
                }
            }
        });
    }

    //alert dialog box
    public void AlertDialogBox(String title, String msg){
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setCancelable(true);
        ad.setTitle(title);
        ad.setMessage(msg);
        ad.show();
    }
}