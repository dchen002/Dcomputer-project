package com.dajustock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.dajustock.stockchart.ChartSearch;
import com.dajustock.stockchart.R;


public class mainpage extends AppCompatActivity {
    //Declare the required buttons.
    private Button search;
    private Button news;
    private Button pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        //Find the corresponding button in the xml file by findViewById.
        search = findViewById(R.id.bt_main_2);
        news = findViewById(R.id.bt_main_3);
        pre = findViewById(R.id.bt_image);
                setListener();
    }

    private void setListener(){
        OnClick onClick = new OnClick();
        //set click listeners on the button object in the corresponding activity
        search.setOnClickListener(onClick);
        news.setOnClickListener(onClick);
        pre.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        //Switch to different activities by clicking on different buttons on the home page by using class intent.
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.bt_main_2:
                    intent = new Intent(mainpage.this, ChartSearch.class);
                    break;
                case R.id.bt_main_3:
                    intent = new Intent(mainpage.this,news.class);
                    break;

                case R.id.bt_image:
                    intent = new Intent(mainpage.this, MainActivity3.class);
                    break;
            }
            startActivity(intent);
        }
    }
}