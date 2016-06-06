package edu.ucsb.cs.cs185.azakhor.easya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RICK on 6/4/2016.
 */
public class SetSavePicFragment extends DialogFragment {

    private OnSavePicListener listener;
    private ArrayList<String> mQuarterArray;
    private ArrayList<String> mClassArray;

    //Listener interface
    public interface OnSavePicListener{
        void onSavePic(String title, String quarter, String classname);
    }

    public void setSavePicListener(OnSavePicListener listener){
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle){
        Bundle directoryInfo = getArguments();
        final String curTerm = directoryInfo.getString(FilesNavActivity.TERM_KEY);
        final String curClass = directoryInfo.getString(FilesNavActivity.CLASS_KEY);
        //Create a build
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate layout
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.save_frag_pic, null);

        final EditText pictureTitle = (EditText)contentView.findViewById(R.id.imageTitle);

        //Get spinner of quarter
        final Spinner quarterChosen = (Spinner) contentView.findViewById(R.id.quarterSpinner);
        //Get spinner of class
        final Spinner classChosen = (Spinner) contentView.findViewById(R.id.classSpinner);

        //Fill mQuarterArray with all quarter directory names
        mQuarterArray = new ArrayList<String>();
        File allMyTerms = new File(getActivity().getExternalFilesDir(null) + "/EasyA");
        if(allMyTerms.isDirectory()){
            for(File file : allMyTerms.listFiles()){
                mQuarterArray.add(file.getName());
                Log.d("ALL_TERMS", "Terms: "+file.getName()); //TODO: Remove Log.d after testing
            }
        }

        //Populate quarter spinner with choices from mQuarterArray
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mQuarterArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterChosen.setAdapter(spinnerArrayAdapter);
        quarterChosen.setSelection(spinnerArrayAdapter.getPosition(curTerm));

        //Set listener when item is selected in the quarter spinner
        quarterChosen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get all class spinner items based on item selected in quarter spinner
                String term = parent.getItemAtPosition(position).toString();
                mClassArray = new ArrayList<String>();
                File allMyClasses = new File(getActivity().getExternalFilesDir(null) + "/EasyA/" + term);
                if(allMyClasses.isDirectory()){
                    for(File file : allMyClasses.listFiles()){
                        mClassArray.add(file.getName());
                        Log.d("ALL_CLASSES","Classes: "+file.getName()); //TODO: Remove Log.d after testing
                    }
                }

                //Populate class spinner with choices from mClassArray
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, mClassArray);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classChosen.setAdapter(spinnerArrayAdapter);
                classChosen.setSelection(spinnerArrayAdapter.getPosition(curClass));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Empty
            }
        });

        //Pass data to builder
        builder.setView(contentView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleName = (String) pictureTitle.getText().toString();
                String quarterSelect = (String) quarterChosen.getSelectedItem();
                String classSelect = (String) classChosen.getSelectedItem();
                if(titleName == null){
                    titleName = "";
                }
                if(quarterSelect == null){
                    quarterSelect = "";
                }
                if(classSelect == null){
                    classSelect = "";
                }
                Log.d("SPINNER","Spinner selections: "+quarterSelect+"|"+classSelect);
                listener.onSavePic(titleName, quarterSelect, classSelect);
            }
        });

        builder.setView(contentView).setNegativeButton("Cancel", null);

        //Build and return dialog
        return builder.create();
    }
}
