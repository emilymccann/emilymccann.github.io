package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class GoalWeightActivity extends AppCompatActivity {

    // Widgets
    private EditText mGoalInput;
    private Button mSaveButton;
    private RadioButton mLoseRadio;
    private RadioButton mGainRadio;
    private RadioButton mMaintainRadio;

    // Vars and objects
    public static final String EXTRA_USERNAME = "com.zybooks.weighttracker.username";
    private String mUsername;
    private WeightTrackerDatabase mWeightTrackerDB;
    private GoalWeight mGoalWeight;
    private String mGoal;


    // TextWatcher for dynamically enabling/disabling the save button
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
        setContentView(R.layout.activity_goal_weight);


        mGoalInput = findViewById(R.id.weightEditText);
        mSaveButton = findViewById(R.id.goalSaveButton);
        mLoseRadio = findViewById(R.id.radioButtonLose);
        mGainRadio = findViewById(R.id.radioButtonGain);
        mMaintainRadio = findViewById(R.id.radioButtonMaintain);

        mWeightTrackerDB = WeightTrackerDatabase.getInstance(getApplicationContext());

        // Get username from DisplayDataActivity
        Intent intent = getIntent();
        mUsername = intent.getStringExtra(EXTRA_USERNAME);

        mGoalWeight = mWeightTrackerDB.getGoalWt(mUsername);

        //if user is editing an existing goal weight, show their previous entry/selection
        if (mGoalWeight != null) {
            Short goalWeightShort = mGoalWeight.getGoalWeight();
            String goalWeightString = goalWeightShort.toString();
            String goal = mGoalWeight.getGoal();

            mGoalInput.setText(goalWeightString);

            if (goal != null) {
                switch(goal) {
                    case "lose":
                        mLoseRadio.setChecked(true);
                        break;
                    case "gain":
                        mGainRadio.setChecked(true);
                        break;
                    case "maintain":
                        mMaintainRadio.setChecked(true);
                        break;
                    default:
                        mLoseRadio.setChecked(false);
                        mGainRadio.setChecked(false);
                        mMaintainRadio.setChecked(false);
                        break;
                }
            }

            //button enabled since fields will contain previous data when editing
            mSaveButton.setEnabled(true);
        }

        // Set text changed listener for the EditText
        mGoalInput.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonLose:
                if (checked){
                    mGoal = "lose";
                }
                break;
            case R.id.radioButtonGain:
                if (checked){
                    mGoal = "gain";
                }
                break;
            case R.id.radioButtonMaintain:
                if (checked){
                    mGoal = "maintain";
                }
                break;
            default:
                mGoal = "";
                break;
        }
    }

    // Save user's entered goal weight
    public void onGoalSaveClick(View view) {
        String goalWeightString = mGoalInput.getText().toString();
        Short goalWeightShort = Short.parseShort(goalWeightString);

        if (mGoalWeight == null) {
            // Add new Goal Weight if none exists for user - with notifications turned on
            mGoalWeight = new GoalWeight(goalWeightShort, mUsername, mGoal, "yes");
            mWeightTrackerDB.addGoalWeight(mGoalWeight);
        } else {
            // Update Goal Weight if user already has one
            mGoalWeight.setGoalWeight(goalWeightShort);
            mGoalWeight.setGoal(mGoal);
            mGoalWeight.setCongrats("yes"); //turn on notifications after goal weight is updated
            mWeightTrackerDB.updateGoalWeight(mGoalWeight);
        }

        finish();
    }

}