package com.zybooks.weighttracker;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

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
