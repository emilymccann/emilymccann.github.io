package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private WeightTrackerDatabase mWeightTrackerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mWeightTrackerDB = WeightTrackerDatabase.getInstance(getApplicationContext());


        displayUsers();
    }



    @Override
    protected void onResume(){
        super.onResume();

        displayUsers();
    }

    @Override
    protected void onStart(){
        super.onStart();

        displayUsers();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    private List<UserLogin> loadUsers() {
        return mWeightTrackerDB.getUsers();
    }

    // To display the recycler view of user data
    private void displayUsers() {
        RecyclerView mRecyclerViewAdmin;
        com.zybooks.weighttracker.RecyclerViewAdapterAdmin mRecyclerViewAdapterAdmin;

        mRecyclerViewAdmin = findViewById(R.id.recyclerViewAdmin);

        // Shows the available daily weights
        mRecyclerViewAdapterAdmin = new RecyclerViewAdapterAdmin(this, loadUsers());
        mRecyclerViewAdmin.setAdapter(mRecyclerViewAdapterAdmin);
        mRecyclerViewAdmin.setLayoutManager(new LinearLayoutManager(this));
    }
}