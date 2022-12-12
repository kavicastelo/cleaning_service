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

public class admin extends AppCompatActivity {

    EditText room, broom, tile, cement, carpet, user, nUser, nPass;
    Button viewU, updateP, viewF, viewI, viewP, promote, demote, cngAdmin;
    Spinner choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        database myDB = new database(this);

        room = findViewById(R.id.aRoom);
        broom = findViewById(R.id.aBRoom);
        tile = findViewById(R.id.aTile);
        cement = findViewById(R.id.aCement);
        carpet = findViewById(R.id.aCarpet);
        user = findViewById(R.id.ausername);
        nUser = findViewById(R.id.aChnUser);
        nPass = findViewById(R.id.aChnPass);
        viewU = findViewById(R.id.aViewUsers);
        updateP = findViewById(R.id.aUpdatePrices);
        viewF = findViewById(R.id.aViewFeedbacks);
        viewI = findViewById(R.id.aIncome);
        viewP = findViewById(R.id.aProfit);
        promote = findViewById(R.id.aUpromote);
        demote = findViewById(R.id.aUdemote);
        cngAdmin = findViewById(R.id.aCngAdmin);
        choose = findViewById(R.id.aUSpinner);

        //set login ids in to spinner
        ArrayList<String> allIDs = myDB.getLoginID();
        ArrayAdapter<String> IDAdapter = new ArrayAdapter<String>(admin.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allIDs);
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

        viewU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor respond = myDB.getUsers();
                if(respond.getCount() != 0)
                {
                    StringBuffer sb = new StringBuffer();
                    while (respond.moveToNext())
                    {
                        sb.append("Name: "+respond.getString(1)+"\n");
                        sb.append("Email: "+respond.getString(2)+"\n");
                        sb.append("TP: "+respond.getString(3)+"\n");
                        sb.append("Postal Code: "+respond.getString(6)+"\n");
                        sb.append("-----------------------------\n");
                    }
                    AlertDialogBox("Users",sb.toString());
                }
                else
                {
                    Toast.makeText(admin.this, "No users available", Toast.LENGTH_SHORT).show();
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
                    room.setText(null); // purpose - prevents the button crash
                }
            }
        });

        updateP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(room.getText().toString().equals("")||broom.getText().toString().equals("")||tile.getText().toString().equals("")||
                        cement.getText().toString().equals("")||carpet.getText().toString().equals(""))
                {
                    Toast.makeText(admin.this, "update all prices", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Cursor respond = myDB.getPrices();
                    if(respond.getCount() != 0)
                    {
                        boolean isUpdate = myDB.updatePrices("1",room.getText().toString(),broom.getText().toString(),
                                tile.getText().toString(), cement.getText().toString(),carpet.getText().toString());
                        if(isUpdate)
                        {
                            room.setText(null);
                            broom.setText(null);
                            tile.setText(null);
                            cement.setText(null);
                            carpet.setText(null);
                        }
                    }
                    else
                    {
                        boolean isAddPrices = myDB.setPrices(room.getText().toString(),broom.getText().toString(),tile.getText().toString(),
                                cement.getText().toString(),carpet.getText().toString());
                        if(isAddPrices)
                        {
                            room.setText(null);
                            broom.setText(null);
                            tile.setText(null);
                            cement.setText(null);
                            carpet.setText(null);
                        }
                    }
                }
            }
        });

        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = myDB.updateLevel(choose.getSelectedItem().toString(),"1");
                if(isUpdate)
                {
                    Toast.makeText(admin.this, "Promote constructor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        demote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = myDB.updateLevel(choose.getSelectedItem().toString(),"3");
                if(isUpdate)
                {
                    Toast.makeText(admin.this, "demote user ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cngAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nUser.getText().toString().equals("")||nPass.getText().toString().equals(""))
                {
                    Toast.makeText(admin.this, "Required field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    boolean isUpdateAdmin = myDB.changeAdmin("0",nUser.getText().toString(),nPass.getText().toString());
                    if(isUpdateAdmin)
                    {
                        nUser.setText(null);
                        nPass.setText(null);
                        Toast.makeText(admin.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor income = myDB.getBudget();
                if(income.getCount()!=0)
                {
                    StringBuffer sb = new StringBuffer();
                    while (income.moveToNext())
                    {
                        sb.append(income.getString(2)+"\n");
                    }
                    AlertDialogBox("Total income", sb.toString());
                }
                else
                {
                    Toast.makeText(admin.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor profit = myDB.getBudget();
                if(profit.getCount()!=0)
                {
                    StringBuffer sb = new StringBuffer();
                    while (profit.moveToNext())
                    {
                        sb.append(profit.getString(3)+"\n");
                    }
                    AlertDialogBox("Total profit", sb.toString());
                }
                else
                {
                    Toast.makeText(admin.this, "No data available", Toast.LENGTH_SHORT).show();
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