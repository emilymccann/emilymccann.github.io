package com.zybooks.weighttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class WeightTrackerDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 9;
    private static final String DATABASE_NAME = "userLogin.db";

    private static WeightTrackerDatabase mWeightTrackerDB;

    public static WeightTrackerDatabase getInstance(Context context) {
        if (mWeightTrackerDB == null) {
            mWeightTrackerDB = new WeightTrackerDatabase(context);
        }
        return mWeightTrackerDB;
    }

    private WeightTrackerDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_ROLE = "role";
    }

    private static final class GoalWeightTable {
        private static final String TABLE = "goal_weight";
        private static final String COL_USERNAME = "username";
        private static final String COL_GOAL_WT = "goal_weight";
        private static final String COL_LOSE_GAIN = "lose_gain";
        private static final String COL_RECEIVE_CONGRATS = "receive_congrats";

    }

    private static final class DailyWeightTable {
        private static final String TABLE = "daily_weight";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_WEIGHT = "weight";
        private static final String COL_USERNAME = "username";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the User table; username is primary key to ensure unique logins
        db.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.COL_USERNAME + " primary key, " +
                UserTable.COL_PASSWORD + ", " +
                UserTable.COL_ROLE + " )");

        // Create goal weight table with username as primary key to ensure each user only
        //  has one goal weight at a time
        // specify username as foreign key that cascade deletes
        db.execSQL("create table " + GoalWeightTable.TABLE + " (" +
                GoalWeightTable.COL_USERNAME + " primary key, " +
                GoalWeightTable.COL_GOAL_WT + ", " +
                GoalWeightTable.COL_LOSE_GAIN + ", " +
                GoalWeightTable.COL_RECEIVE_CONGRATS + ", " +
                "foreign key(" + GoalWeightTable.COL_USERNAME + ") references " +
                UserTable.TABLE + "(" + UserTable.COL_USERNAME + ") on delete cascade)");

        // Create daily weight table with foreign key that cascade deletes
        db.execSQL("create table " + DailyWeightTable.TABLE + " (" +
                DailyWeightTable.COL_ID + " integer primary key autoincrement, " +
                DailyWeightTable.COL_DATE + ", " +
                DailyWeightTable.COL_WEIGHT + ", " +
                DailyWeightTable.COL_USERNAME + ", " +
                "foreign key(" + DailyWeightTable.COL_USERNAME + ") references " +
                UserTable.TABLE + "(" + UserTable.COL_USERNAME + ") on delete cascade)");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserTable.TABLE);
        db.execSQL("drop table if exists " + GoalWeightTable.TABLE);
        db.execSQL("drop table if exists " + DailyWeightTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }

    //to encrypt a user's username and pw in the database
    //and also to decrypt username when getting data from the database
    public String encryptDecrypt(String inputString) {

        char key = 'x';
        int inputLength = inputString.length();

        assert(inputLength > 0);  //make sure there is data in the input string

        String outputString = "";

        for (int i = 0; i < inputLength; i++){
            outputString = outputString + ((char) (inputString.charAt(i) ^ key));
         }

        return outputString;
    }

    // For adding a new user to the database
    public boolean addUser(UserLogin userLogin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();
        String role = userLogin.getRole();

        //encrypt username and password before storing in db
        String encryptedUsername = encryptDecrypt(username);
        String encryptedPassword = encryptDecrypt(password);

        values.put(UserTable.COL_USERNAME, encryptedUsername);
        values.put(UserTable.COL_PASSWORD, encryptedPassword);
        values.put(UserTable.COL_ROLE, role);
        long id = db.insert(UserTable.TABLE, null, values);
        return id != -1;
    }


    // For getting username and password for authentication checks
    public boolean getUserByCredentials(String username, String password) {
        boolean credentialsFound;
        SQLiteDatabase db = this.getReadableDatabase();

        // encrypt username and password to search for successful match in db
        String encryptedUsername = encryptDecrypt(username);
        String encryptedPassword = encryptDecrypt(password);

        String sql = "select * from " + UserTable.TABLE + " where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { encryptedUsername, encryptedPassword });
        if (cursor.moveToFirst()) {
            credentialsFound = true;
        }
        else {
            credentialsFound = false;
        }
        cursor.close();

        return credentialsFound;
    }

    // For getting the user data by username only
    public UserLogin getUserByUsername(String username) {
        UserLogin userLogin = null;

        // encrypt username to search for successful match in db
        String encryptedUsername = encryptDecrypt(username);

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + UserTable.TABLE + " where "
                + UserTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { encryptedUsername });

        if (cursor.moveToFirst()) {
            userLogin = new UserLogin();
            userLogin.setUsername(encryptDecrypt(cursor.getString(0)));  //decrypt username so returned object has the user's raw username
            userLogin.setPassword(cursor.getString(1));  //leave password encrypted for security
            userLogin.setRole(cursor.getString(2));
        }
        cursor.close();
        return userLogin;
    }


    // For getting a list of usernames from db table
    public List<UserLogin> getUsers() {
        UserLogin userLogin;
        List<UserLogin> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from " + UserTable.TABLE;
        Cursor cursor = db.rawQuery(sql, new String[] {});
        if (cursor.moveToFirst()) {
            do {
                userLogin = new UserLogin();
                userLogin.setUsername(encryptDecrypt(cursor.getString(0)));  //decrypt username so returned object has the user's raw username
                userLogin.setPassword(cursor.getString(1));  //leave password encrypted for security
                userLogin.setRole(cursor.getString(2));
                users.add(userLogin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    // For updating a user's role
    public void updateUser(UserLogin userLogin) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String username = userLogin.getUsername();
        // encrypt username to update correct user in db
        String encryptedUsername = encryptDecrypt(username);

        values.put(UserTable.COL_USERNAME, encryptedUsername);
        values.put(UserTable.COL_ROLE, userLogin.getRole());
        db.update(UserTable.TABLE, values,
                UserTable.COL_USERNAME + " = ?", new String[] { encryptedUsername });
    }

    // For deleting a user
    public void deleteUser(String username) {
        // encrypt username to delete correct user in db
        String encryptedUsername = encryptDecrypt(username);

        SQLiteDatabase db = getWritableDatabase();
        db.delete(UserTable.TABLE,
                UserTable.COL_USERNAME + " = ?", new String[] { encryptedUsername });
    }


    // For adding a goal weight to the goal weight table for a given user
    public boolean addGoalWeight(GoalWeight goalWeight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String username = goalWeight.getUsername();
        // encrypt username to update goal weight for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        values.put(GoalWeightTable.COL_USERNAME, encryptedUsername);
        values.put(GoalWeightTable.COL_GOAL_WT, goalWeight.getGoalWeight());
        values.put(GoalWeightTable.COL_LOSE_GAIN, goalWeight.getGoal());
        values.put(GoalWeightTable.COL_RECEIVE_CONGRATS, goalWeight.getCongrats());
        long id = db.insert(GoalWeightTable.TABLE, null, values);
        return id != -1;
    }

    // For getting the Goal Weight data from db table
    public GoalWeight getGoalWt(String username) {
        GoalWeight goalWeight = null;

        // encrypt username to get goal weight for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + GoalWeightTable.TABLE + " where "
                + GoalWeightTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { encryptedUsername });

        if (cursor.moveToFirst()) {
            goalWeight = new GoalWeight();
            goalWeight.setUsername(encryptDecrypt(cursor.getString(0))); //decrypt username so returned object has the user's raw username
            goalWeight.setGoalWeight(cursor.getShort(1));
            goalWeight.setGoal(cursor.getString(2));
            goalWeight.setCongrats(cursor.getString(3));
        }
        cursor.close();

        return goalWeight;
    }


    // For updating a user's existing goal weight
    public void updateGoalWeight(GoalWeight goalWeight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String username = goalWeight.getUsername();
        // encrypt username to update goal weight for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        values.put(GoalWeightTable.COL_USERNAME, encryptedUsername);
        values.put(GoalWeightTable.COL_GOAL_WT, goalWeight.getGoalWeight());
        values.put(GoalWeightTable.COL_LOSE_GAIN, goalWeight.getGoal());
        values.put(GoalWeightTable.COL_RECEIVE_CONGRATS, goalWeight.getCongrats());
        db.update(GoalWeightTable.TABLE, values,
                GoalWeightTable.COL_USERNAME + " = ?", new String[] { encryptedUsername });
    }


    // For deleting a user's goal weight
    public void deleteGoalWeight(String username) {
        // encrypt username to delete goal weight for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        SQLiteDatabase db = getWritableDatabase();
        db.delete(GoalWeightTable.TABLE,
                GoalWeightTable.COL_USERNAME + " = ?", new String[] { encryptedUsername });
    }


    // For adding a new daily weight to the daily weight table for a given user
    public boolean addDailyWeight(DailyWeight dailyWeight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String username = dailyWeight.getUsername();
        // encrypt username to add daily weight for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        values.put(DailyWeightTable.COL_DATE, dailyWeight.getDate());
        values.put(DailyWeightTable.COL_WEIGHT, dailyWeight.getDailyWeight());
        values.put(DailyWeightTable.COL_USERNAME, encryptedUsername);
        long id = db.insert(DailyWeightTable.TABLE, null, values);
        dailyWeight.setId(id);

        return id != -1;
    }

    // For getting the Daily Weight data from db table
    public List<DailyWeight> getDailyWts(String username) {
        List<DailyWeight> dailyWeights = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        // encrypt username to get daily weights for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        String sql = "select * from " + DailyWeightTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { encryptedUsername });
        if (cursor.moveToFirst()) {
            do {
                DailyWeight dailyWeight = new DailyWeight();
                dailyWeight.setId(cursor.getLong(0));
                dailyWeight.setDate(cursor.getString(1));
                dailyWeight.setDailyWeight(cursor.getShort(2));
                dailyWeight.setUsername(encryptDecrypt(cursor.getString(3))); //decrypt username so returned object has the user's raw username
                dailyWeights.add(dailyWeight);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dailyWeights;
    }

    // For getting a Daily Weight from db table - need for editing daily weight entries
    public DailyWeight getDailyWeight(long dailyWeightId) {
        DailyWeight dailyWeight = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select * from " + DailyWeightTable.TABLE + " where " + DailyWeightTable.COL_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { Float.toString(dailyWeightId) });
        if (cursor.moveToFirst()) {
                dailyWeight = new DailyWeight();
                dailyWeight.setId(cursor.getLong(0));
                dailyWeight.setDate(cursor.getString(1));
                dailyWeight.setDailyWeight(cursor.getShort(2));
                dailyWeight.setUsername(encryptDecrypt(cursor.getString(3))); //decrypt username so returned object has the user's raw username
        }
        cursor.close();

        return dailyWeight;
    }



    // For updating a daily weight entry
    public void updateDailyWeight(DailyWeight dailyWeight) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String username = dailyWeight.getUsername();
        // encrypt username to update daily weight for correct user in db
        String encryptedUsername = encryptDecrypt(username);

        values.put(DailyWeightTable.COL_ID, dailyWeight.getId());
        values.put(DailyWeightTable.COL_DATE, dailyWeight.getDate());
        values.put(DailyWeightTable.COL_WEIGHT, dailyWeight.getDailyWeight());
        values.put(DailyWeightTable.COL_USERNAME, encryptedUsername);
        db.update(DailyWeightTable.TABLE, values,
                DailyWeightTable.COL_ID + " = " + dailyWeight.getId(), null);
    }


    // For deleting a daily weight entry
    public void deleteDailyWeight(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DailyWeightTable.TABLE,
                DailyWeightTable.COL_ID + " = " + id, null);
    }


}
