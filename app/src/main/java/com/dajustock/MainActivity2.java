package com.dajustock;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dajustock.stockchart.R;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    //declare buttons need to use in this page
    private Button mybutlogin;
    private EditText mETUser;
    private EditText mETpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //find buttons in xml files by using findViewById.
        mybutlogin  = findViewById(R.id.btn_login);
        mETUser = findViewById(R.id.et_1);
        mETpassword = findViewById(R.id.et_2);


        mybutlogin.setOnClickListener(this);
    }
    public void onClick(View v){
        //get user input content.
        String username = mETUser.getText().toString();
        String password = mETpassword.getText().toString();
        //Shows successful login if username and password are correct
        String ok = "Login successful";
        //Show login failure if username and password are incorrect
        String fail="Login failed";
        Intent intent = null;
        //Determine if the username and password are correct
        if(username.equals("123")&&password.equals("123")){

            Toast.makeText(getApplicationContext(),ok,Toast.LENGTH_SHORT).show();
            //if username and password are incorrect go to main page
            intent = new Intent(MainActivity2.this, mainpage.class);
            startActivity(intent);

        }else{
            //Show login failure if username and password are incorrect
            Toast toastCenter = Toast.makeText(getApplicationContext(),fail,Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();


        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}