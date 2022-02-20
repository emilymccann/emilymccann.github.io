## Introduction: ePortfolio Self-Assessment

In this course at SNHU, I was able to make several improvements on a project I started in the summer of 2021. When I initially created the project, I knew there were several issues I needed to fix but I developed tennis and golfer’s elbow from typing and clicking so much that I could not continue at the time. When I decided to fix the major issues in the project for this capstone course, I was intimidated but I ended up learning a lot more than I thought possible and made several more improvements than I had originally planned. I believe that this helped me to showcase my strengths because as I learned more, I kept wanting to make the project better and better. In completing this course, I became more autonomous at completing a software development project because I was able to direct where and how the improvements would be made. I was also encouraged to learn more about the java language and mobile application development as I navigated through challenges along the way. 

Additional skills I have include collaborating in a team environment and communicating to stakeholders. In my current job at a pharmaceutical company, I work in a software validation team. In this job, I write validation plans, requirements, specifications, and qualification protocols to ensure that software used by the production and lab departments meets 21 CFR Part 11 compliance. This job requires working with other members on my team to ensure the validation documents are comprehensive and accurate. It also requires communicating with the business owners of the software to ensure we have a correct understanding of their needs and timelines. One of the major themes of 21 CFR Part 11 compliance is security. The FDA requires that software used in the development and/or testing of drugs has security measures to ensure only authorized users are able to access the application and that all of their activities are logged in the system.

For this capstone project at SNHU, I made several enhancements to a single project to showcase my skills in software design/engineering, algorithms and data structures, and databases. For Enhancement One, I made two corrections to features that previously did not function correctly. This showed that I could investigate code thoroughly enough to understand what was causing the problem. It also showed that my software design and engineering skills were strong enough to develop a solution to resolve these issues. When I worked on Enhancement Two, I had to modify two data structures (Goal Weight and Daily Weight) to convert the weight parameters from String to Short, and then I had to add an additional parameter to Goal Weight to capture whether the user’s goal was to lose, gain, or maintain weight. I also modified the checkWeight() algorithm to determine if the user not only reached their goal but surpassed it. 

This built upon the improvements made in Enhancement One because there were additional options that would allow for the dialog to be shown. This showed that I was able to make existing data structures and algorithms more complex to meet different needs that customers may have. In Enhancement Three, I made further improvements that would appease customers and stakeholders, namely encrypting the username and password data to make the database more secure. I had initially planned to do this, but then I also added a role parameter for the user login data structure, which allowed for the possibility to create administrators. Any user with an administrator role would be able to delete users or change the role of other users. These improvements helped improve the overall security of the application by ensuring that credentials were secure and that an administrator figure would be able to regulate user access to the application.

Several other improvements were made during the course of this project to further enhance the security of the application. For instance, user input into the application is restricted so that there is only a 12 character limit for usernames and passwords, and daily weight and goal weights are only allowed a 3 digit maximum to prevent overflow. When entering a date for daily weights, users must choose a date from a date picker which further prevents the user from entering malicious code. Additionally, the LoginActivity.java file was improved so that the user must enter both a username and password to log in and create an account. Previously, the user was only required to enter a password for the buttons to be enabled. In this project, an If-Else block was added to check that both fields were not empty before allowing the user to proceed. Further improvements were made by converting several member variables to local variables when possible throughout the entire project to ensure that these variables would be safe from being accidentally overwritten by different methods. The Weight Tracker database always had defense against SQL injection attacks by using WHERE clause substitution arguments in its operations. 

Considering the improvements made to the project and the added emphasis on security, this project successfully shows my strengths in software design/engineering, algorithms and data structures, and databases. The skills of each category are demonstrated in the following sections, preceded by an in-depth code review that was recorded prior to making any improvements. The entire project file can be downloaded [here](./Weight Tracker.zip).

## Index:

1. Code Review Videos
2. Enhancement One
    * Narrative
    * [Highlights](https://github.com/emilymccann/emilymccann.github.io/blob/main/index.md#enhancement-one-highlights)
4. Enhancement Two
    * Narrative
    * [Highlights](https://github.com/emilymccann/emilymccann.github.io/blob/main/index.md#enhancement-two-highlights)
6. Enhancement Three
    * Narrative
    * [Highlights](https://github.com/emilymccann/emilymccann.github.io/blob/main/index.md#enhancement-three-highlights)
	


## CS499 Code Review Video links:

1. Part 1: [https://www.youtube.com/watch?v=CGCOhTVLu7g](https://www.youtube.com/watch?v=CGCOhTVLu7g)
2. Part 2: [https://www.youtube.com/watch?v=VlytDm2GrSY](https://www.youtube.com/watch?v=VlytDm2GrSY)
3. Part 3: [https://www.youtube.com/watch?v=xwCvtkHlErM](https://www.youtube.com/watch?v=xwCvtkHlErM)



## Enhancement One - Software Design and Engineering
### Narrative

The artifact I enhanced for this category was the mobile application I created in the SNHU course CS360, in June 2021. This application is a weight-tracking app which allows users to create an account, log in, enter a goal weight, and enter daily weights. The user is notified once they reach their goal weight. I selected this item to include in my portfolio because it incorporated all 3 categories that are part of the capstone final project. The application shows my software design/engineering skills because it is well modularized and there is efficient communication across different activities. The application initially did not function correctly in some ways, and I resolved those issues for this milestone. One issue was that the recycler view did not update correctly when an attempt was made to either edit or delete a daily weight entry. The second issue was that the SMS notification feature did not work correctly, and I wanted to replace this with a dialog notification. 

I did meet the course objectives I planned to meet as I was successful in resolving both issues. To fix the recycler view issue, I first had to ensure I was replacing or removing the items not only from the database but from the list used to generate the recycler view. I learned that this list is what was providing the database data to the recycler view, so changes had to be made there as well. In addition, to edit daily weight items, I was passing the username as an extra to the editing activity, but I should have been passing the daily weight ID and getting that entry from the database rather than creating a new daily weight entry. This required creating a new method in WeightTrackerDatabase.java called getDailyWeight(). 

To resolve the SMS notification issue, I had to remove all SMS notification pieces of code and replace them with a dialog fragment. The app still failed to generate a dialog when the goal weight was reached, and it took me some time to figure out why. I was using the startActivityForResult method which was deprecated. Once I updated the app to use the ActivityResultLauncher, I was get data back from the launched activity and then call the checkWeight() method to see if the goal weight had been reached. Another change I made to the application was to add if/else checks to the LoginActivity to make sure the user enters both a username and password before attempting to login or create a new account. In resolving these issues, I was able to show that I can pinpoint issues in code and utilize my engineering and design skills to solve them.


### Enhancement One Highlights

Correcting the recycler view error consisted of updating/deleting not only the user's daily weights in the database, but also in the list used to generate the recycler view. This is shown in the [Recycler View file](./RecyclerViewAdapter.java):

```java
            // OnClick for editing individual daily weight entries
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // intent to launch edit activity
                    Intent intent = new Intent(mContext, EditDailyWeightActivity.class);
                    intent.putExtra(EditDailyWeightActivity.EXTRA_DAILY_WT_ID, mId);
                    mContext.startActivity(intent);

                    //After EditDailyWeightActivity finishes, get updated entry from db
                    mUpdatedDailyWeight = mWeightTrackerDB.getDailyWeight(mId);

                    int index = mDailyWeights.indexOf(mDailyWeight);
                    if (index >= 0) {
                        // Replace the daily weight in the list
                        mDailyWeights.set(index, mUpdatedDailyWeight);

                        // Notify adapter of daily weight update
                        notifyItemChanged(index);
                    }

                }
            });

            // OnClick for deleting individual daily weight entries
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWeightTrackerDB.deleteDailyWeight(mId);
                    int index = mDailyWeights.indexOf(mDailyWeight);
                    if (index >= 0) {
                        // Remove the daily Weight
                        mDailyWeights.remove(index);

                        // Notify adapter of daily Weight removal
                        notifyItemRemoved(index);
                    }
                }
                });
```

To replace the SMS notification feature, all references to SMS were removed and a dialog fragment feature was added. After a user enters a daily weight, the [Daily Weight Activity](./DailyWeightActivity.java) sends a result code back to [Display Data Activity](./DisplayDataActivity.java). If the result code is OK, Display Data Activity calls the checkWeight() method. 

```java
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
```

The checkWeight() method checks the last entered daily weight and compares it to the user's goal weight. onGoalReached() is called when the goal weight is reached (or surpassed, as shown in [Enhancement Two Highlights](https://github.com/emilymccann/emilymccann.github.io/blob/main/index.md#enhancement-two-highlights)), which shows the congratulations dialog and sets receiveCongrats to "no" so that future daily weights do not continue to set off the dialog notification (this variable is set to "yes" again when a user changes their goal weight). 

The dialog fragment is generated by the [following](./GoalReachedDialogFrag.java):

```java
public class GoalReachedDialogFrag extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.congrats);
        builder.setMessage(R.string.goal_reached);
        builder.setPositiveButton(R.string.ok, null);
        return builder.create();
    }
}
```



## Enhancement Two - Algorithms and Data Structures
### Narrative

The artifact I enhanced for this category was the mobile application I created in the SNHU course CS360, in June 2021. This application is a weight-tracking app which allows users to create an account, log in, enter a goal weight, enter daily weights, and be notified once they reach their goal weight. I selected this item to include in my portfolio because it incorporated all 3 categories that are part of the capstone final project. I was also interested in making improvements in 3 different areas to enhance the overall project. The application shows my skills in algorithms and data structure because it uses an algorithm to check if the goal weight has been reached, and it utilizes several different objects (user login, goal weight, and daily weight) which are data structures. For for this milestone, I improved the artifact by adjusting the algorithm that checks if the goal weight has been reached by also checking to see if the goal weight had been surpassed. This depended on whether the user was trying to lose or gain weight, so I added an option to GoalWeightActivity for the user to select whether they are trying to lose, gain, or maintain weight. 

This required adding a new field to the goal weight class to hold the user’s goal. I also had to convert the data types for goal weight and daily weight from String to Short to allow for numerical comparisons. Then, I updated the algorithm to check what the user’s goal was (lose, gain, or maintain) and then to compare their last entered daily weight with the goal weight. For example, if the user was trying to lose weight, the algorithm would check if the last daily weight was less than or equal to the goal weight. I thought this might get annoying for the user if they keep entering daily weights that are lower than the goal weight, so I added another field to the goal weight class, which was “receiveCongrats.” This field holds a string value “yes” or “no” to indicate to the app whether to show the dialog. I tried to use a boolean for this field, but it was not possible to perform some operations in the SQLite database using the boolean, so I used a string instead. When the goal weight is first created, this field is set to “yes” by default. Once the user reaches/surpasses their goal weight, the receiveCongrats field is set to “no.” Then whenever the user updates their goal weight, it is set to “yes” again. This way, the user will only see the dialog once after they reach/surpass a goal weight. 

I did meet the course outcomes with this enhancement because I figured out a way to expand the algorithm and add more complexity to some of the objects to make the algorithm work and not be an annoyance to the end user. I did add a small update to the UI, which was a line underneath the goal weight display that shows the user whether they are trying to lose, gain, or maintain weight. I thought this might be too much on the screen, so I considered updating the recycler view to use cardviews with a long click listener that displays delete and edit options in a contextual action bar. I decided not to use this display yet because it looked a little messier. If I do use that display later on, I will need to remove the individual edit and delete buttons next to each daily weight. 

One challenge I faced while making this enhancement was that, for users who wish to maintain their weight, I could not use the “==“ comparator to check for equality between the goal weight and the daily weight. I could not figure out why this didn’t work for a long time, because the values I was comparing were Short type and not strings. I then tried “.equals” and that worked. I learned that the “==“ checks to see if the items being compared are actually the same object. This was not the case in my application, which is why it was not working. 



### Enhancement Two Highlights

In the [Goal Weight Activity file](./GoalWeightActivity.java), radio buttons were added to allow the user to select whether they are attempting to lose, gain, or maintain weight. When the goal weight is saved, the "receiveCongrats" variable is set to "yes" so that when a user first reaches their goal weight, the app will show them a dialog congratulating them.

```java
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
```

The checkWeight() method checks the last entered daily weight and compares it to the user's goal weight. The type of comparison is based on whether the user's goal is to lose, gain, or maintain weight. If the goal weight has been reached or surpassed, onGoalReached() is called, which shows the congratulations dialog and sets receiveCongrats to "no" as described in Enhancement One.

```java
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
```


## Enhancement Three - Databases
### Narrative

The artifact I enhanced for this milestone was the mobile application I created in the SNHU course CS360, in June 2021. This application is a weight-tracking app which allows users to create an account, log in, enter a goal weight, enter daily weights, and be notified once they reach their goal weight. I selected this item to include in my portfolio because it incorporated all 3 categories that are part of the capstone final project. I was also interested in making improvements in 3 different areas to enhance the overall project, and I also knew when I first created it that there were several issues I had to fix. The application shows my skills in databases because it uses a SQLite database to store user login info, users’ goal weights, and users’ daily weights. 

This artifact was improved by adding an XOR encryption method to encrypt the users’ usernames and passwords. I did not realize before working on this enhancement that I could actually see my database tables in the Database Inspector. This really helped me to troubleshoot issues and to see that the encryption worked. In the Database Inspector, the letter portions of usernames and passwords did not show at all (I am guessing that letters are encrypted to strange symbols that Android Studio cannot show) but any numbers showed up as letters.

To many of the database methods, I had to call the encryption/decryption method when the username was used in a query. This was to ensure the encrypted username could be found, and that it could also be decrypted when being returned (for instance, inside the goal weight, daily weight, or user login objects). 
 
I also added an administration feature to the app. In the LoginActivity, an admin is created if it does not already exist. To designate admins from basic users, I added a “role” field to the UserLogin class and then updated my “users” database table to have a role column. By default, all users who are not “admin” are created with the “user” role. A basic user does not have the admin icon in their app bar in the DisplayDataActivity. This is only visible for users with the admin role. Once an admin clicks on the admin icon, a new activity launches (AdminActivity), which uses a recycler view to show a list of all active users. The admin can delete any user, removing them from the database, or they can edit the user’s role if desired. I previously had a deleteUser method in my database but it was not being used. I had to add an updateUser method to make it possible for the admin to edit the user’s role. 

I did meet the course objectives that I had planned to meet in module 1. I was able to add an encryption algorithm to protect sensitive information in the database, though I am sure professional apps use a much more advanced and sophisticated method. I was also able to add more functionality and control over the database by creating an admin role who can edit or delete other users. I also made several other improvements, such as updates to the UI for a more exciting appearance, and reading through the warnings and making edits accordingly. Many of the warnings were about member variables that could be converted to local variables, or unused import statements or unused variables. I also fixed an issue I was having with up bar. Previously, whenever I tried to navigate back to the calling activity, the app would crash (unless I was going from DisplayDataActivity back to LoginActivity). It turned out the fix was very simple and I just needed to specify a singleTop launch mode in the Android manifest for the other activities. 

One  challenge I encountered was that, when I deleted a user, their daily weights would be removed from the database, but their goal weight would remain. I looked into the issue by inspecting the onCreate method in WeightTrackerDatabase.java. I noticed that the DailyWeightTable specified a  foreign key (username) and “on delete cascade.” I was missing this from my GoalWeightTable. Once I added it, the goal weights were removed upon user deletion in addition to the user’s daily weights. 

### Enhancement 3 Highlights

The [Weight Tracker Database file](./WeightTrackerDatabase.java) was modified to use XOR encryption on the usernames and passwords stored in the Users table. The following method is used to encrypt and decrypt usernames and passwords when performing various operations (adding users, finding and authenticating users, obtaining users' data, etc.).

```java
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
 ```
 
This file was also modified to allow for editing a user's role and deleting a user: 

```java
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
```

In the [Login Activity file](./LoginActivity.java), a basic main administrator is created if one does not already exist: 

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        . . . 
        
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
```

Any user with a role of "administrator" has access to the admin icon in the app bar. Upon clicking this icon, the [Admin Activity](./AdminActivity.java) launches and allows the admin to edit or delete users. The list of active users are displayed in a [recycler view](./RecyclerViewAdapterAdmin.java) with edit and delete buttons. Only the main admin does not have these buttons so that other admins cannot edit or delete this user.

```java
        public void bind(UserLogin userLogin, int position) {

            String role;

            mUserLogin = userLogin;
            mUsername = userLogin.getUsername();
            role = userLogin.getRole();

            mUsernameText.setText(mUsername);
            mRoleText.setText(role);
            mEditButton.setTag(mUsername);
            mDeleteButton.setTag(mUsername);

            // if the user is the main admin, set edit and delete buttons to invisible
            //  so that other admins cannot delete or edit the main admin
            if (mUsername.equals("admin")) {
                mEditButton.setVisibility(View.INVISIBLE);
                mDeleteButton.setVisibility(View.INVISIBLE);
            }
            // for all other users, set on click listeners for edit & delete buttons
            else {
                // OnClick for editing individual daily weight entries
                mEditButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // intent to launch edit activity
                        Intent intent = new Intent(mContext, EditUserActivity.class);
                        intent.putExtra(EditUserActivity.EXTRA_USERNAME, mUsername);
                        mContext.startActivity(intent);

                        //After EditUserActivity finishes, get updated entry from db
                        mUpdatedUser = mWeightTrackerDB.getUserByUsername(mUsername);


                        int index = mUsers.indexOf(mUpdatedUser);
                        if (index >= 0) {
                            // Replace the user in the list
                            mUsers.set(index, mUpdatedUser);

                            // Notify adapter of daily weight update
                            notifyItemChanged(index);
                        }

                    }
                });

                // OnClick for deleting individual users
                mDeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWeightTrackerDB.deleteUser(mUsername);

                        int index = mUsers.indexOf(mUserLogin);
                        if (index >= 0) {
                            // Remove the daily Weight
                            mUsers.remove(index);

                            // Notify adapter of daily Weight removal
                            notifyItemRemoved(index);
                        }
                    }
                });
            }


        }
```



### Support or Contact

Having trouble with Pages? Check out our [documentation](https://docs.github.com/categories/github-pages-basics/) or [contact support](https://support.github.com/contact) and we’ll help you sort it out.
