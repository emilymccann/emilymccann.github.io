## Introduction: ePortfolio Self-Assessment

Click this link to download the entire project: [Weight Tracker Project File](./Weight Tracker.zip)

You can use the [editor on GitHub](https://github.com/emilymccann/emilymccann.github.io/edit/main/index.md) to maintain and preview the content for your website in Markdown files.

Whenever you commit to this repository, GitHub Pages will run [Jekyll](https://jekyllrb.com/) to rebuild the pages in your site, from the content in your Markdown files.



## CS499 Code Review Video links:

1. Part 1: [https://www.youtube.com/watch?v=CGCOhTVLu7g](https://www.youtube.com/watch?v=CGCOhTVLu7g)
2. Part 2: [https://www.youtube.com/watch?v=VlytDm2GrSY](https://www.youtube.com/watch?v=VlytDm2GrSY)
3. Part 3: [https://www.youtube.com/watch?v=xwCvtkHlErM](https://www.youtube.com/watch?v=xwCvtkHlErM)



## Enhancement One - Software Design and Engineering
###### Narrative

The artifact I enhanced for this category was the mobile application I created in the SNHU course CS360, in June 2021. This application is a weight-tracking app which allows users to create an account, log in, enter a goal weight, and enter daily weights. The user is notified once they reach their goal weight. I selected this item to include in my portfolio because it incorporated all 3 categories that are part of the capstone final project. The application shows my software design/engineering skills because it is well modularized and there is efficient communication across different activities. The application initially did not function correctly in some ways, and I resolved those issues for this milestone. One issue was that the recycler view did not update correctly when an attempt was made to either edit or delete a daily weight entry. The second issue was that the SMS notification feature did not work correctly, and I wanted to replace this with a dialog notification. 

I did meet the course objectives I planned to meet as I was successful in resolving both issues. To fix the recycler view issue, I first had to ensure I was replacing or removing the items not only from the database but from the list used to generate the recycler view. I learned that this list is what was providing the database data to the recycler view, so changes had to be made there as well. In addition, to edit daily weight items, I was passing the username as an extra to the editing activity, but I should have been passing the daily weight ID and getting that entry from the database rather than creating a new daily weight entry. This required creating a new method in WeightTrackerDatabase.java called getDailyWeight(). 

To resolve the SMS notification issue, I had to remove all SMS notification pieces of code and replace them with a dialog fragment. The app still failed to generate a dialog when the goal weight was reached, and it took me some time to figure out why. I was using the startActivityForResult method which was deprecated. Once I updated the app to use the ActivityResultLauncher, I was get data back from the launched activity and then call the checkWeight() method to see if the goal weight had been reached. Another change I made to the application was to add if/else checks to the LoginActivity to make sure the user enters both a username and password before attempting to login or create a new account. In resolving these issues, I was able to show that I can pinpoint issues in code and utilize my engineering and design skills to solve them.



## Enhancement Two - Algorithms and Data Structures
###### Narrative

The artifact I enhanced for this category was the mobile application I created in the SNHU course CS360, in June 2021. This application is a weight-tracking app which allows users to create an account, log in, enter a goal weight, enter daily weights, and be notified once they reach their goal weight. I selected this item to include in my portfolio because it incorporated all 3 categories that are part of the capstone final project. I was also interested in making improvements in 3 different areas to enhance the overall project. The application shows my skills in algorithms and data structure because it uses an algorithm to check if the goal weight has been reached, and it utilizes several different objects (user login, goal weight, and daily weight) which are data structures. For for this milestone, I improved the artifact by adjusting the algorithm that checks if the goal weight has been reached by also checking to see if the goal weight had been surpassed. This depended on whether the user was trying to lose or gain weight, so I added an option to GoalWeightActivity for the user to select whether they are trying to lose, gain, or maintain weight. 

This required adding a new field to the goal weight class to hold the user’s goal. I also had to convert the data types for goal weight and daily weight from String to Short to allow for numerical comparisons. Then, I updated the algorithm to check what the user’s goal was (lose, gain, or maintain) and then to compare their last entered daily weight with the goal weight. For example, if the user was trying to lose weight, the algorithm would check if the last daily weight was less than or equal to the goal weight. I thought this might get annoying for the user if they keep entering daily weights that are lower than the goal weight, so I added another field to the goal weight class, which was “receiveCongrats.” This field holds a string value “yes” or “no” to indicate to the app whether to show the dialog. I tried to use a boolean for this field, but it was not possible to perform some operations in the SQLite database using the boolean, so I used a string instead. When the goal weight is first created, this field is set to “yes” by default. Once the user reaches/surpasses their goal weight, the receiveCongrats field is set to “no.” Then whenever the user updates their goal weight, it is set to “yes” again. This way, the user will only see the dialog once after they reach/surpass a goal weight. 

I did meet the course outcomes with this enhancement because I figured out a way to expand the algorithm and add more complexity to some of the objects to make the algorithm work and not be an annoyance to the end user. I did add a small update to the UI, which was a line underneath the goal weight display that shows the user whether they are trying to lose, gain, or maintain weight. I thought this might be too much on the screen, so I considered updating the recycler view to use cardviews with a long click listener that displays delete and edit options in a contextual action bar. I decided not to use this display yet because it looked a little messier. If I do use that display later on, I will need to remove the individual edit and delete buttons next to each daily weight. 

One challenge I faced while making this enhancement was that, for users who wish to maintain their weight, I could not use the “==“ comparator to check for equality between the goal weight and the daily weight. I could not figure out why this didn’t work for a long time, because the values I was comparing were Short type and not strings. I then tried “.equals” and that worked. I learned that the “==“ checks to see if the items being compared are actually the same object. This was not the case in my application, which is why it was not working. 



## Enhancement Three - Databases
###### Narrative

The artifact I enhanced for this milestone was the mobile application I created in the SNHU course CS360, in June 2021. This application is a weight-tracking app which allows users to create an account, log in, enter a goal weight, enter daily weights, and be notified once they reach their goal weight. I selected this item to include in my portfolio because it incorporated all 3 categories that are part of the capstone final project. I was also interested in making improvements in 3 different areas to enhance the overall project, and I also knew when I first created it that there were several issues I had to fix. The application shows my skills in databases because it uses a SQLite database to store user login info, users’ goal weights, and users’ daily weights. 

This artifact was improved by adding an XOR encryption method to encrypt the users’ usernames and passwords. I did not realize before working on this enhancement that I could actually see my database tables in the Database Inspector. This really helped me to troubleshoot issues and to see that the encryption worked. In the Database Inspector, the letter portions of usernames and passwords did not show at all (I am guessing that letters are encrypted to strange symbols that Android Studio cannot show) but any numbers showed up as letters.

To many of the database methods, I had to call the encryption/decryption method when the username was used in a query. This was to ensure the encrypted username could be found, and that it could also be decrypted when being returned (for instance, inside the goal weight, daily weight, or user login objects). 
 
I also added an administration feature to the app. In the LoginActivity, an admin is created if it does not already exist. To designate admins from basic users, I added a “role” field to the UserLogin class and then updated my “users” database table to have a role column. By default, all users who are not “admin” are created with the “user” role. A basic user does not have the admin icon in their app bar in the DisplayDataActivity. This is only visible for users with the admin role. Once an admin clicks on the admin icon, a new activity launches (AdminActivity), which uses a recycler view to show a list of all active users. The admin can delete any user, removing them from the database, or they can edit the user’s role if desired. I previously had a deleteUser method in my database but it was not being used. I had to add an updateUser method to make it possible for the admin to edit the user’s role. 

I did meet the course objectives that I had planned to meet in module 1. I was able to add an encryption algorithm to protect sensitive information in the database, though I am sure professional apps use a much more advanced and sophisticated method. I was also able to add more functionality and control over the database by creating an admin role who can edit or delete other users. I also made several other improvements, such as updates to the UI for a more exciting appearance, and reading through the warnings and making edits accordingly. Many of the warnings were about member variables that could be converted to local variables, or unused import statements or unused variables. I also fixed an issue I was having with up bar. Previously, whenever I tried to navigate back to the calling activity, the app would crash (unless I was going from DisplayDataActivity back to LoginActivity). It turned out the fix was very simple and I just needed to specify a singleTop launch mode in the Android manifest for the other activities. 

One  challenge I encountered was that, when I deleted a user, their daily weights would be removed from the database, but their goal weight would remain. I looked into the issue by inspecting the onCreate method in WeightTrackerDatabase.java. I noticed that the DailyWeightTable specified a  foreign key (username) and “on delete cascade.” I was missing this from my GoalWeightTable. Once I added it, the goal weights were removed upon user deletion in addition to the user’s daily weights. 

###### Enhancement 3 Highlights

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


### Jekyll Themes

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/emilymccann/emilymccann.github.io/settings/pages). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://docs.github.com/categories/github-pages-basics/) or [contact support](https://support.github.com/contact) and we’ll help you sort it out.
