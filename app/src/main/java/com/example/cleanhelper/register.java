package com.example.cleanhelper;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class register extends AppCompatActivity {

    EditText fname,lname,email,tp,addr,city,pcode,uname,pass,conpass,room,broom;
    Button sign,clear,cancel;
    Spinner ftype;
    ImageView image;

    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database myDB = new database(this);

        fname = findViewById(R.id.rfname);
        lname = findViewById(R.id.rlname);
        email = findViewById(R.id.remail);
        tp = findViewById(R.id.rtp);
        addr = findViewById(R.id.raddr);
        city = findViewById(R.id.rcity);
        pcode = findViewById(R.id.rpcode);
        uname = findViewById(R.id.runame);
        pass = findViewById(R.id.rpass);
        conpass = findViewById(R.id.rconpass);
        room = findViewById(R.id.rrooms);
        broom = findViewById(R.id.rbathrooms);

        sign = findViewById(R.id.rsign);
        clear = findViewById(R.id.rcls);
        cancel = findViewById(R.id.rcancel);

        ftype = findViewById(R.id.rspinner);

        image = findViewById(R.id.rimage);

        image.setOnClickListener(new View.OnClickListener() { //choose image from gallery
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add home details to database
                String floor = "not defined";
                if(ftype.getSelectedItem().toString().equals("Tile"))
                {
                    floor = "0";
                }
                else if (ftype.getSelectedItem().toString().equals("Cement"))
                {
                    floor = "1";
                }
                else if (ftype.getSelectedItem().toString().equals("Carpet"))
                {
                    floor = "2";
                }
                boolean isHomeAdded = myDB.setHome(uname.getText().toString(),room.getText().toString(),broom.getText().toString(),floor,img);
                if(isHomeAdded)
                {
                    //uname.setText(null);
                    room.setText(null);
                    broom.setText(null);
                    image.setImageResource(R.drawable.add_image);
                }

                //check required fields are empty
                if(fname.getText().toString().equals("")||lname.getText().toString().equals("")||email.getText().toString().equals("")||
                        tp.getText().toString().equals("")||addr.getText().toString().equals("")||city.getText().toString().equals("")||
                        pcode.getText().toString().equals("")||uname.getText().toString().equals("")||pass.getText().toString().equals(""))
                {
                    Toast.makeText(register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!pass.getText().toString().equals(conpass.getText().toString())) //check password is correct
                    {
                        Toast.makeText(register.this, "Check your password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Cursor respond = myDB.getLogin();
                        if(respond.getCount()!=0)
                        {
                            while (respond.moveToNext()) {
                                if (respond.getString(1).equals(uname.getText().toString())) //check username already have database
                                {
                                    Toast.makeText(register.this, "Username already has taken. choose another one", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if (!uname.getText().toString().equals("Admin") && !pass.getText().toString().equals("1234")) //check not equals to default values
                                    {
                                        boolean isLoginAdd = myDB.setLogin(uname.getText().toString(), pass.getText().toString(), "3"); // add users login details
                                        if (isLoginAdd) {
                                            //uname.setText(null);
                                            pass.setText(null);
                                            conpass.setText(null);
                                        }

                                        //add user details to database
                                        boolean isUsersAdd = myDB.setUser(fname.getText().toString() + lname.getText().toString(), email.getText().toString(),
                                                tp.getText().toString(), addr.getText().toString(), city.getText().toString(), pcode.getText().toString());
                                        if (isUsersAdd) {
                                            fname.setText(null);
                                            lname.setText(null);
                                            email.setText(null);
                                            tp.setText(null);
                                            addr.setText(null);
                                            city.setText(null);
                                            pcode.setText(null);
                                        }
                                    }
                                    else
                                    {
                                        if (!respond.getString(3).equals("0")) //check not have an admin account
                                        {
                                            boolean isAdminAdd = myDB.setLogin(uname.getText().toString(), pass.getText().toString(), "0");
                                            if (isAdminAdd) //create admin account to default values in first time
                                            {
                                                //uname.setText(null);
                                                pass.setText(null);
                                                conpass.setText(null);
                                            } else {
                                                Toast.makeText(register.this, "admin account not created", Toast.LENGTH_SHORT).show();
                                            }

                                            //add admin details to database
                                            boolean isUsersAdd = myDB.setUser(fname.getText().toString() + lname.getText().toString(), email.getText().toString(),
                                                    tp.getText().toString(), addr.getText().toString(), city.getText().toString(), pcode.getText().toString());
                                            if (isUsersAdd) {
                                                fname.setText(null);
                                                lname.setText(null);
                                                email.setText(null);
                                                tp.setText(null);
                                                addr.setText(null);
                                                city.setText(null);
                                                pcode.setText(null);
                                            }

                                        }
                                        else
                                        {
                                            Toast.makeText(register.this, "Access denied to create admin account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            if(!uname.getText().toString().equals("Admin")&&!pass.getText().toString().equals("1234")) {
                                boolean isLoginAdd = myDB.setLogin(uname.getText().toString(), pass.getText().toString(), "3"); // add users login details
                                if (isLoginAdd) {
                                    //uname.setText(null);
                                    pass.setText(null);
                                    conpass.setText(null);
                                }

                                //add user details to database
                                boolean isUsersAdd = myDB.setUser(fname.getText().toString() + lname.getText().toString(), email.getText().toString(),
                                        tp.getText().toString(), addr.getText().toString(), city.getText().toString(), pcode.getText().toString());
                                if (isUsersAdd) {
                                    fname.setText(null);
                                    lname.setText(null);
                                    email.setText(null);
                                    tp.setText(null);
                                    addr.setText(null);
                                    city.setText(null);
                                    pcode.setText(null);
                                }
                            }
                            else
                            {
                                boolean isAdminAdd = myDB.setLogin(uname.getText().toString(), pass.getText().toString(), "0"); // add users login details
                                if (isAdminAdd) {
                                    //uname.setText(null);
                                    pass.setText(null);
                                    conpass.setText(null);
                                }

                                //add user details to database
                                boolean isAdmindet = myDB.setUser(fname.getText().toString() + lname.getText().toString(), email.getText().toString(),
                                        tp.getText().toString(), addr.getText().toString(), city.getText().toString(), pcode.getText().toString());
                                if (isAdmindet) {
                                    fname.setText(null);
                                    lname.setText(null);
                                    email.setText(null);
                                    tp.setText(null);
                                    addr.setText(null);
                                    city.setText(null);
                                    pcode.setText(null);
                                }
                            }
                        }
                    }
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname.setText(null);
                lname.setText(null);
                email.setText(null);
                tp.setText(null);
                addr.setText(null);
                city.setText(null);
                pcode.setText(null);
                uname.setText(null);
                pass.setText(null);
                conpass.setText(null);
                room.setText(null);
                broom.setText(null);
                image.setImageResource(R.drawable.add_image);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(register.this,login.class);
                startActivity(i);
            }
        });
    }

    //################################################ image functions #########################################################
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri choosenImage = data.getData();

                    if (choosenImage != null) {
                        img = decodeUri(choosenImage, 400);
                        image.setImageBitmap(img);
                    }
                }
        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //############################################ end of image functions #########################################################
}