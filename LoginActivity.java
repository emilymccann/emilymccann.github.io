package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameText;
    private EditText mPasswordText;
    private TextView mMessageText;
    private Button mLoginButton;
    private Button mCreateNewUserButton;

    private WeightTrackerDatabase mWeightTrackerDB;
    private UserLogin mAdminUser;

    // TextWatcher for dynamically enabling/disabling the Login and Create New User buttons
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //enable button when user enters text
            if (s.length() > 0) {
                mLoginButton.setEnabled(true);
                mCreateNewUserButton.setEnabled(true);
            }
            // when there is no text, disable button
            else {
                mLoginButton.setEnabled(false);
                mCreateNewUserButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assign widgets to fields
        mUsernameText = findViewById(R.id.usernameText);
        mPasswordText = findViewById(R.id.passwordText);
        mMessageText = findViewById(R.id.messageText);
        mLoginButton = findViewById(R.id.loginButton);
        mCreateNewUserButton = findViewById(R.id.createNewUserButton);

        mWeightTrackerDB = WeightTrackerDatabase.getInstance(getApplicationContext());

        // Set text changed listener for the password EditText field
        mPasswordText.addTextChangedListener(textWatcher);

        // Search for admin user and assign to mAdminUser
        mAdminUser = mWeightTrackerDB.getUserByUsername("admin");

        // create admin user if none exists
        if (mAdminUser == null) {
            createAdmin();
        }

    }

    public void createAdmin() {
        mAdminUser = new UserLogin();

        mAdminUser.setUsername("admin");
        mAdminUser.setPassword("admin123");
        mAdminUser.setRole("administrator");

        mWeightTrackerDB.addUser(mAdminUser);
    }



    // onClick method for the Login button - authenticates entered credentials
    public void loginClick(View view){

        //ensure both username and password have text
        if (mUsernameText.getText().toString().isEmpty() || mPasswordText.getText().toString().isEmpty()) {
            mMessageText.setText(R.string.empty_credentials);
            mMessageText.setVisibility(View.VISIBLE);
        }
        else {
            String userName = mUsernameText.getText().toString();
            String password = mPasswordText.getText().toString();

            // To check for successful authentication (user found in db)
            boolean found = mWeightTrackerDB.getUserByCredentials(userName, password);

            if (found) {
                // Launch next activity
                Intent intent = new Intent(this, DisplayDataActivity.class);
                intent.putExtra(DisplayDataActivity.EXTRA_USERNAME, userName);
                startActivity(intent);
            } else {
                // Display error message
                mMessageText.setText(R.string.invalid_credentials);
                mMessageText.setVisibility(View.VISIBLE);

                // Clear EditText fields so user can try again
                mUsernameText.setText("");
                mPasswordText.setText("");
            }

        }

    }


    // onClick method for the Create New User button - adds new user to user database
    public void createNewUserClick(View view){

        //ensure both username and password have text
        if (mUsernameText.getText().toString().isEmpty() || mPasswordText.getText().toString().isEmpty()) {
            mMessageText.setText(R.string.empty_credentials);
            mMessageText.setVisibility(View.VISIBLE);
        }
        else {
            String userName = mUsernameText.getText().toString();
            String password = mPasswordText.getText().toString();

            // create new user with "user" role as default
            UserLogin userLogin = new UserLogin(userName, password, "user");

            // To check for successful add
            boolean successfulAdd = mWeightTrackerDB.addUser(userLogin);

            // If user is added, display message that prompts login
            if (successfulAdd) {
                mMessageText.setText(R.string.successful_user_add);
                mMessageText.setVisibility(View.VISIBLE);
            }
            // If user cannot be added (ie due to duplicate username), display error message
            else {
                mMessageText.setText(R.string.unsuccessful_user_add);
                mMessageText.setVisibility(View.VISIBLE);

                // Clear EditText fields so user can try again
                mUsernameText.setText("");
                mPasswordText.setText("");
            }

        }

    }

}