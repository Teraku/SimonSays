package com.hugo.simonsays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Danny on 12-1-2017.
 */

public class AboutUsFragment extends DialogFragment {

    //Create the dialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder aboutusDialog = new AlertDialog.Builder(getActivity());

        aboutusDialog.setTitle("About Us");

        aboutusDialog.setMessage("©Copyright 2017: Danny Tam & Hugo Zink");

        return aboutusDialog.create();
    }
}
