package com.alcala.fahrenheittocelciusconverter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.TextView.OnEditorActionListener;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements OnEditorActionListener,
        OnClickListener{

    //define variables
    private EditText fahrenheitET;
    private TextView celciusTV;
    private Button ResetButton;

    //define instance variable
    private String tempString = "";

    DecimalFormat df = new DecimalFormat("###.##");

    private SharedPreferences savedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to the widgets
        fahrenheitET = (EditText) findViewById(R.id.fahrenheitET);
        celciusTV = (TextView) findViewById(R.id.celciusTV);
        ResetButton = (Button) findViewById(R.id.ResetButton);

        //set the listeners for the event
        fahrenheitET.setOnEditorActionListener(this);
        ResetButton.setOnClickListener(this);

        savedValue = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch ((view.getId())) {
            case R.id.ResetButton:
                fahrenheitET.setText("0");
                celciusTV.setText("0");
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    private void calculateAndDisplay() {

        //get temperature from user
        tempString = fahrenheitET.getText().toString();
        double temp;
        if(tempString.equals("")) {
            temp = 0;
        }
        else
            temp = Double.parseDouble(tempString);

        //calculate temperature
        double celcius = (temp-32)/1.8;

        //format and display
        //NumberFormat temperature = NumberFormat.getCurrencyInstance();
        celciusTV.setText(df.format(celcius));
    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = savedValue.edit();
        editor.putString("tempString", tempString);
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tempString = savedValue.getString("tempString", "");
        fahrenheitET.setText(tempString);
        calculateAndDisplay();
    }
}
