package edu.ucsb.cs.cs185.azakhor.easya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by AaronZak on 6/1/16.
 * <p/>
 * public class CreateTerm extends FragmentActivity {
 *
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * setContentView(R.layout.create_term);
 * <p/>
 * <p/>
 * <p/>
 * Button addNewTermButton = (Button)findViewById(R.id.confirmNewTerm);
 * addNewTermButton.setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View v) {
 * EditText enterTermName = (EditText) findViewById(R.id.enterTermName);
 * <p/>
 * String newTermString = enterTermName.getText().toString();
 * <p/>
 * Log.d("Enter new term", newTermString);
 * <p/>
 * File dir = new File(CreateTerm.this.getExternalFilesDir(null) + "/EasyA/" + newTermString);
 * if (!dir.exists()) {
 * dir.mkdirs();
 * Intent intent = new Intent(v.getContext(),MainActivity.class);
 * <p/>
 * finish();
 * } else {
 * Snackbar.make(v, "This name is already in use, please enter another", Snackbar.LENGTH_LONG)
 * .setAction("Action", null).show();
 * }
 * <p/>
 * <p/>
 * }
 * });
 * Button cancelNewTerm = (Button)findViewById(R.id.cancelNewTerm);
 * cancelNewTerm.setOnClickListener(new View.OnClickListener() {
 * @Override public void onClick(View v) {
 * finish();
 * }
 * });
 * <p/>
 * }
 * }
 */

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
        final View contentView = getActivity().getLayoutInflater().inflate(R.layout.create_term, null);
        builder.setTitle("Add a New Quarter:");


        final EditText quarterField = (EditText) contentView.findViewById(R.id.enterTermName);

        //Pass data to builder
        builder.setView(contentView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quarter = quarterField.getText().toString();
                if(quarter.length() < 1){
                    quarter = "";
                }
                listener.onCreateTerm(quarter);
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            }

        });

        builder.setView(contentView).setNegativeButton("Cancel", null);

        //Build and return dialog
        return builder.create();
    }
}


