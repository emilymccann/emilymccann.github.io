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


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<DailyWeight> mDailyWeights;
    private Context mContext;
    private WeightTrackerDatabase mWeightTrackerDB;



    // Default constructor
    public RecyclerViewAdapter(Context context, List<DailyWeight> dailyWeights) {
        mDailyWeights = dailyWeights;
        mContext = context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_daily_weight, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(mDailyWeights.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mDailyWeights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Widgets
        private DailyWeight mDailyWeight;
        private TextView mDateText;
        private TextView mWeightText;
        private ImageButton mEditButton;
        private ImageButton mDeleteButton;

        // vars
        private long mId;
        private DailyWeight mUpdatedDailyWeight;


        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mWeightTrackerDB = WeightTrackerDatabase.getInstance(mContext.getApplicationContext());

            // Assign widgets and layouts to id's
            mDateText = itemView.findViewById(R.id.dateTextView);
            mWeightText = itemView.findViewById(R.id.weightTextView);
            mEditButton = itemView.findViewById(R.id.editDataImageButton);
            mDeleteButton = itemView.findViewById(R.id.deleteDataImageButton);

        }

        public void bind(DailyWeight dailyWeight, int position) {
            String weightString;
            String date;
            Short weightShort;

            mDailyWeight = dailyWeight;
            date = mDailyWeight.getDate();
            weightShort = mDailyWeight.getDailyWeight();
            mId = mDailyWeight.getId();

            mDateText.setText(date);
            weightString = weightShort.toString();
            mWeightText.setText(weightString);

            mEditButton.setTag(mId);
            mDeleteButton.setTag(mId);


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

        }

    }


}
