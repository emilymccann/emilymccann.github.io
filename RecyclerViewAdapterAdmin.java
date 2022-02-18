package com.zybooks.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class RecyclerViewAdapterAdmin extends RecyclerView.Adapter<RecyclerViewAdapterAdmin.ViewHolder> {

    private List<UserLogin> mUsers;
    private Context mContext;
    private WeightTrackerDatabase mWeightTrackerDB;
    private UserLogin mUpdatedUser;


    // Default constructor
    public RecyclerViewAdapterAdmin(Context context, List<UserLogin> users) {
        mUsers = users;
        mContext = context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull RecyclerViewAdapterAdmin.ViewHolder holder, int position) {
        holder.bind(mUsers.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Widgets
        private TextView mUsernameText;
        private TextView mRoleText;
        private ImageButton mEditButton;
        private ImageButton mDeleteButton;

        // vars and objects
        private String mUsername;
        private UserLogin mUserLogin;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mWeightTrackerDB = WeightTrackerDatabase.getInstance(mContext.getApplicationContext());

            // Assign widgets and layouts to id's
            mUsernameText = itemView.findViewById(R.id.usernameTextView);
            mRoleText = itemView.findViewById(R.id.roleTextView);
            mEditButton = itemView.findViewById(R.id.editUserImageButton);
            mDeleteButton = itemView.findViewById(R.id.deleteUserImageButton);

        }

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

    }


}
