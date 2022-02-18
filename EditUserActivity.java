package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class EditUserActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.zybooks.weighttracker.username";

    private WeightTrackerDatabase mWeightTrackerDB;
    private UserLogin mUserLogin;
    private String mRole;

    //widgets
    private RadioButton mUserRole;
    private RadioButton mAdminRole;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        String username;
        TextView usernameText;

        usernameText = findViewById(R.id.usernameTextView);
        mUserRole = findViewById(R.id.radioButtonUser);
        mAdminRole = findViewById(R.id.radioButtonAdmin);
        mSaveButton = findViewById(R.id.userSaveButton);


        mWeightTrackerDB = WeightTrackerDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        username = intent.getStringExtra(EXTRA_USERNAME);

        usernameText.setText(username);

        mUserLogin = mWeightTrackerDB.getUserByUsername(username);

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonUser:
                if (checked){
                    mRole = "user";
                }
                break;
            case R.id.radioButtonAdmin:
                if (checked){
                    mRole = "administrator";
                }
                break;
            default:
                mRole = "user";
                break;
        }
        mSaveButton.setEnabled(true);
    }

    // Save user's updated role
    public void onSaveClick(View view) {
        mUserLogin.setRole(mRole);
        mWeightTrackerDB.updateUser(mUserLogin);

        finish();
    }

}