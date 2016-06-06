package edu.ucsb.cs.cs185.azakhor.easya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

/**
 * Created by AaronZak on 6/1/16.
 */

public class CreateTerm extends DialogFragment {

    private onTermListener listener;

    public interface onTermListener{
        void onCreateTerm(String quarterName);
    }

    public void setTermListener(onTermListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        //Create a build
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.create_term, null);
        builder.setTitle("Add a New Quarter:");


        final EditText quarterField = (EditText) contentView.findViewById(R.id.enterTermName);

        //Pass data to builder
        builder.setView(contentView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quarter = quarterField.getText().toString();
                if(quarter.length() < 1) {
                    quarter = "";
                }
                listener.onCreateTerm(quarter);
            }
        });

        builder.setView(contentView).setNegativeButton("Cancel", null);

        //Build and return dialog
        return builder.create();
    }
}

