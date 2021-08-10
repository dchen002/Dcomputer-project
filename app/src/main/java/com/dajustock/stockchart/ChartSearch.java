package com.dajustock.stockchart;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

public class ChartSearch extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.dajustock.stockchart.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Make this page accessible to the network without any problems.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Click on the button to switch to the DisplayChart page.
        Intent intent = new Intent(this, DisplayChart.class);
        //Reads and saves the information entered by the user..
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}