package com.zybooks.weighttracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import java.util.List;

public class DisplayDataActivity extends AppCompatActivity {

    // Widgets
    private TextView mGoalWeightDisplay;
    private TextView mLoseGainText;

    // Vars and objects
    private Menu mMenu;
    private WeightTrackerDatabase mWeightTrackerDB;
    private GoalWeight mGoalWeight = new GoalWeight();
    private String mUsername;
    public static final String EXTRA_USERNAME = "com.zybooks.weighttracker.username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // Assign widgets to fields
        mGoalWeightDisplay = findViewById(R.id.goalWeightTextView);
        mLoseGainText = findViewById(R.id.loseGainMaintain);

        //Hosting activity provides the username of the logged in user
        Intent intent = getIntent();
        mUsername = intent.getStringExtra(EXTRA_USERNAME);

        mWeightTrackerDB = WeightTrackerDatabase.getInstance(getApplicationContext());

        displayGoalWeight();
        displayDailyWeight();
    }

    ActivityResultLauncher<Intent> dailyWtActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        mGoalWeight = mWeightTrackerDB.getGoalWt(mUsername);
                        String congrats = mGoalWeight.getCongrats();

                        if (congrats.equals("yes")) {
                            //when DailyWeightActivity ends, check to see if newest daily weight equals goal weight
                            checkWeight();
                        }
                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        UserLogin userLogin;
        String role;
        MenuItem adminIcon;

        adminIcon = mMenu.findItem(R.id.admin_icon);

        userLogin = mWeightTrackerDB.getUserByUsername(mUsername);
        role = userLogin.getRole();

        // only show admin icon if the user is admin
        if (role.equals("administrator")){
            adminIcon.setVisible(true);
        }
        else {
            adminIcon.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.admin_icon) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        displayGoalWeight();
        displayDailyWeight();
    }

    @Override
    protected void onStart(){
        super.onStart();
        displayGoalWeight();
        displayDailyWeight();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }


    private List<DailyWeight> loadDailyWeights() {
        return mWeightTrackerDB.getDailyWts(mUsername);
    }


    public void onGoalReached() {
        //show dialog
        FragmentManager manager = getSupportFragmentManager();
        GoalReachedDialogFrag dialog = new GoalReachedDialogFrag();
        dialog.show(manager, "goalReached");

        //turn notification off
        mGoalWeight.setCongrats("no");
        mWeightTrackerDB.updateGoalWeight(mGoalWeight);
    }

    // check for goal weight reached; will show dialog if goal weight is reached
    public void checkWeight() {
        Short dailyWeight;
        Short goalWeight;
        String goal;
        List<DailyWeight> dailyWeights;

        mGoalWeight = mWeightTrackerDB.getGoalWt(mUsername);
        goal = mGoalWeight.getGoal();
        dailyWeights = loadDailyWeights(); //added - array was empty before

        if (dailyWeights.size() != 0) {
            DailyWeight dailyWeightObj;

            //get last daily weight
            dailyWeightObj = dailyWeights.get(dailyWeights.size()-1);

            dailyWeight = dailyWeightObj.getDailyWeight();

            goalWeight = mGoalWeight.getGoalWeight();

            if (goal.equals("lose") && dailyWeight <= goalWeight) {    //fixed - before had mGoalWeight in (), so it was comparing daily weight string to an object
                onGoalReached();
            }

            if (goal.equals("gain") && dailyWeight >= goalWeight) {    //fixed - before had mGoalWeight in (), so it was comparing daily weight string to an object
                onGoalReached();
            }

            if (goal.equals("maintain") && dailyWeight.equals(goalWeight)) {    //fixed - before had mGoalWeight in (), so it was comparing daily weight string to an object
                onGoalReached();
            }

        }

    }



    // onClick method to add OR edit goal weight
    public void editGoalWeightClick(View view) {
        Intent intent = new Intent(this, GoalWeightActivity.class);
        intent.putExtra(GoalWeightActivity.EXTRA_USERNAME, mUsername);
        startActivity(intent);
    }

    // onClick method to delete goal weight
    public void deleteGoalWeightClick(View view) {
        mWeightTrackerDB.deleteGoalWeight(mUsername);
        displayGoalWeight();
    }



    // method to display the user's goal weight
    public void displayGoalWeight() {
        Short goalWeightShort;
        String goalWeightText;
        String goalLoseGain;

        mGoalWeight = mWeightTrackerDB.getGoalWt(mUsername);

        if (mGoalWeight != null) {
            goalWeightShort = mGoalWeight.getGoalWeight();
            goalWeightText = goalWeightShort.toString();

            goalLoseGain = mGoalWeight.getGoal();

        } else {
            goalWeightText = "";
            goalLoseGain = "";
        }

        mGoalWeightDisplay.setText(goalWeightText);
        mLoseGainText.setText(goalLoseGain);

    }


    // onClick method to add new daily weight data
    public void addNewDailyWeightClick(View view) {
        Intent intent = new Intent(this, DailyWeightActivity.class);
        intent.putExtra(DailyWeightActivity.EXTRA_USERNAME, mUsername);
        dailyWtActivityResultLauncher.launch(intent);
    }



    // To display the recycler view of daily weight data
    private void displayDailyWeight() {
        RecyclerView mRecyclerView;
        RecyclerViewAdapter mRecyclerViewAdapter;

        mRecyclerView = findViewById(R.id.recyclerView);

        // Shows the available daily weights
        mRecyclerViewAdapter = new RecyclerViewAdapter(this, loadDailyWeights());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



}