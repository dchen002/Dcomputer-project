package com.dajustock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dajustock.KNN.KNNException;
import com.dajustock.KNN.PredictMachine;
import com.dajustock.stockchart.R;

public class DisplayPredictionValue extends AppCompatActivity {
    PredictMachine predictMachine = new PredictMachine();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_prediction);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(StockNeedToPredict.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView5);
        //The historical data obtained is used to make predictions using the knn algorithm.
        try {
            //Display forecast results
            textView.setText(predictMachine.predict(message));
        } catch (KNNException e) {
            e.printStackTrace();
        }
    }
}