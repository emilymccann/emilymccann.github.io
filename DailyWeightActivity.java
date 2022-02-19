package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Calendar;

public class DailyWeightActivity extends AppCompatActivity {

    // Widgets
    private EditText mDateInput;
    private EditText mWeightInput;
    private Button mSaveButton;
    private DatePickerDialog datePickerDialog;

    // Vars and objects
    public static final String EXTRA_USERNAME = "com.zybooks.weighttracker.username";
    private String mUsername;
    private WeightTrackerDatabase mWeightTrackerDB;

    // TextWatcher for dynamically enabling/disabling the add button
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //enable button when user enters text
            if (s.length() > 0) {
                mSaveButton.setEnabled(true);
            }
            // when there is no text, disable button
            else {
                mSaveButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weight);

        mDateInput = findViewById(R.id.dateEditText);
        mWeightInput = findViewById(R.id.weightEditText);
        mSaveButton = findViewById(R.id.saveButton);

        mWeightTrackerDB = WeightTrackerDatabase.getInstance(getApplicationContext());

        // Get username from DisplayDataActivity
        Intent intent = getIntent();
        mUsername = intent.getStringExtra(EXTRA_USERNAME);

        // Set text changed listener for the EditText
        mWeightInput.addTextChangedListener(textWatcher);

        //make mDateInput not typable but clickable
        mDateInput.setFocusable(false);
        mDateInput.setClickable(true);

        //set on click listener for mDateInput to open date picker dialog
        mDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); //current year
                int mMonth = c.get(Calendar.MONTH); //current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); //current date

                datePickerDialog = new DatePickerDialog(DailyWeightActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                            //chosen date will appear in mDateInput
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mDateInput.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // saves the new daily weight
    public void onSaveClick(View view) {
        DailyWeight dailyWeight;
        String date = mDateInput.getText().toString();
        String dailyWeightString = mWeightInput.getText().toString();
        Short dailyWeightShort = Short.parseShort(dailyWeightString);

        // Add new daily weight
        dailyWeight = new DailyWeight(date, dailyWeightShort, mUsername);
        mWeightTrackerDB.addDailyWeight(dailyWeight);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        finish();
    }
}