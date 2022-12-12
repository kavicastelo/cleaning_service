package com.example.cleanhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class user extends AppCompatActivity {

    EditText user, date, time, cost, acceptance, feedback;
    Button makeReq, confirmOrder, subFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        database myDB = new database(this);

        user = findViewById(R.id.uUname);
        date = findViewById(R.id.udate);
        time = findViewById(R.id.utime);
        cost = findViewById(R.id.uprice);
        acceptance = findViewById(R.id.ureqAccept);
        feedback = findViewById(R.id.ufeedback);

        makeReq = findViewById(R.id.umakereq);
        confirmOrder = findViewById(R.id.upconfirm);
        subFeedback = findViewById(R.id.usubfeedback);

        makeReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().equals("")||date.getText().toString().equals("")||time.getText().toString().equals(""))
                {
                    Toast.makeText(user.this, "Required data missing", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(user.this, "confirm to request", Toast.LENGTH_SHORT).show();
                }

                Cursor prices = myDB.getPrices();
                Cursor homes = myDB.getHome();
                if(prices.getCount() != 0 || homes.getCount() != 0)
                {
                    while (prices.moveToNext() && homes.moveToNext())
                    {
                            Float room = Float.parseFloat(prices.getString(1));
                            Float broom = Float.parseFloat(prices.getString(2));
                            Float NoOfRoom = Float.parseFloat(homes.getString(2));
                            Float NoOfBroom = Float.parseFloat(homes.getString(3));
                            Float fType = Float.parseFloat(homes.getString(4));

                            if (homes.getString(4).equals("0"))
                            {
                                fType = Float.parseFloat(prices.getString(3));
                            }
                            else if (homes.getString(4).equals("1"))
                            {
                                fType = Float.parseFloat(prices.getString(4));
                            }
                            else if (homes.getString(4).equals("2"))
                            {
                                fType = Float.parseFloat(prices.getString(5));
                            }

                            Float tot = room*NoOfRoom + broom*NoOfBroom + fType;
                            cost.setText(String.valueOf(tot));
                    }
                }
                else
                {
                    Toast.makeText(user.this, "Values not set yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String income = cost.getText().toString();
                Float cal = Float.parseFloat(cost.getText().toString());
                Float profit = cal/100*10;

                boolean isAddBudget = myDB.addBudget(user.getText().toString(),income,String.valueOf(profit));
                if(isAddBudget)
                {
                    user.setText(null);
                    date.setText(null);
                    time.setText(null);
                }

                boolean isadded = myDB.setProject(user.getText().toString(),date.getText().toString(),time.getText().toString(),"0");
                if(isadded)
                {
                    Toast.makeText(user.this, "request send", Toast.LENGTH_SHORT).show();
                }

                Cursor getProject = myDB.getProject();
                if(getProject.getCount()!=0)
                {
                    while (getProject.moveToNext())
                    {
                        if(getProject.getString(4).equals("0"))
                        {
                            acceptance.setText("Pending");
                        }
                        else if(getProject.getString(4).equals("1"))
                        {
                            acceptance.setText("Accepted");
                        }
                    }
                }
            }
        });

        subFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feedback.getText().toString().equals("")||!user.getText().toString().equals(""))
                {
                    boolean isSend = myDB.setFeedback(user.getText().toString(),feedback.getText().toString());
                    {
                        if(isSend)
                        {
                            feedback.setText(null);
                            Toast.makeText(user.this, "Send Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(user.this, "Cannot send empty feedback or check your user name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}