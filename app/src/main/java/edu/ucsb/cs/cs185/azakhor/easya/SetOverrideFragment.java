package edu.ucsb.cs.cs185.azakhor.easya;

/**
 * Created by AaronZak on 6/5/16.
 */

import android.app.Dialog;


        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;

/**
 * Created by RICK on 6/5/2016.
 */
public class SetOverrideFragment extends DialogFragment {

    private onOverrideListener listener;

    public interface onOverrideListener{
        void onOverride();
    }

    public void setOverrideListener(onOverrideListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        //Create a build
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.override_frag_layout, null);

        TextView overrideText = (TextView) contentView.findViewById(R.id.override_message);
        final EditText title = (EditText) getActivity().findViewById(R.id.title_editor);
        final String filename = title.getText().toString();
        overrideText.setText("File "+filename+" already exists. Do you want to override it?");

        //Pass data to builder
        builder.setView(contentView).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOverride();
            }
        });

        builder.setView(contentView).setNegativeButton("No", null);

        //Build and return dialog
        return builder.create();
    }

}
