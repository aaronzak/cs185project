package edu.ucsb.cs.cs185.azakhor.easya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AaronZak on 6/2/16.
 */
public class CreateClass extends DialogFragment {


    /*List<String> allTermsforSpinner;
    ArrayList<String> spinnerArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class);

        String myString = getIntent().getStringExtra("getCurrentClass");

        spinnerArray = new ArrayList<String>();
        File allMyClasses= new File(this.getExternalFilesDir(null) + "/EasyA");
        if(allMyClasses.isDirectory()){


            for(File file: allMyClasses.listFiles())
            {
                spinnerArray.add(file.getName());
                Log.d("allterms", file.getName());
            }
        }













        Spinner chooseTermName = (Spinner)findViewById(R.id.chooseTerm);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        chooseTermName.setAdapter(spinnerArrayAdapter);


        //the value you want the position for

        ArrayAdapter myAdap = (ArrayAdapter) chooseTermName.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(myString);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allTermsforSpinner);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner



//set the default according to value
        chooseTermName.setSelection(spinnerPosition);



        Button addNewTermButton = (Button)findViewById(R.id.confirmNewTerm);
        addNewTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner chooseTermName = (Spinner)findViewById(R.id.chooseTerm);

                EditText newClassName = (EditText)findViewById(R.id.enterClassName);

                String myTermString = chooseTermName.getSelectedItem().toString();

                String myClassName = newClassName.getText().toString();





                File dir = new File(CreateClass.this.getExternalFilesDir(null) + "/EasyA/" + myTermString + "/" + myClassName);
                if (!dir.exists()) {
                    Log.d("Enter new term", dir.getAbsolutePath());
                    dir.mkdirs();
                    finish();
                }
                else {
                    Snackbar.make(v, "This name is already in use, please enter another", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

        Button cancelNewClass = (Button)findViewById(R.id.cancelNewClass);
        cancelNewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }*/

    private onCourseListener listener;

    public interface onCourseListener{
        void onCreateCourse(String courseName);
    }

    public void setTermListener(onCourseListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        //Create a build
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.create_class, null);
        builder.setTitle("Add a New Course:");


        final EditText courseField = (EditText) contentView.findViewById(R.id.enterCourseName);

        //Pass data to builder
        builder.setView(contentView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String course = courseField.getText().toString();
                if(course.length() < 1){
                    course = "";
                }
                listener.onCreateCourse(course);
            }
        });

        builder.setView(contentView).setNegativeButton("Cancel", null);

        //Build and return dialog
        return builder.create();
    }



}
