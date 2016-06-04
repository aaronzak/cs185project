package edu.ucsb.cs.cs185.azakhor.easya;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NotePad extends Activity {

    EditText ed;

    Button btn;

    TextView result;

    String h;
    EditText title;
    String myTitle;
    Spinner secondSpinner;

    ArrayList<String> spinnerArray;
    ArrayList<String> spinnerArray2;





    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.notepad);



        ed = (EditText) findViewById(R.id.contenttxt);
        title = (EditText)findViewById(R.id.noteTitle);

        result = (TextView) findViewById(R.id.resulttxt);

        btn = (Button) findViewById(R.id.exportbtn);

        String content = getIntent().getStringExtra("getNoteContent");
        if(content!=null){
            ed.setText(content);

        }



        spinnerArray = new ArrayList<String>();


        File allMyTerms= new File(Environment.getExternalStorageDirectory() + "/EasyA");
        if(allMyTerms.isDirectory()){


            for(File file: allMyTerms.listFiles())
            {
                spinnerArray.add(file.getName());
                Log.d("allterms", file.getName());
            }
        }













        final Spinner chooseTermName = (Spinner)findViewById(R.id.chooseTermNote);
        secondSpinner = (Spinner)findViewById(R.id.chooseClassNote);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        chooseTermName.setAdapter(spinnerArrayAdapter);


        //the value you want the position for

        ArrayAdapter myAdap = (ArrayAdapter) chooseTermName.getAdapter(); //cast to an ArrayAdapter


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chooseTermName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                Spinner chooseTermName = (Spinner)findViewById(R.id.chooseTermNote);
                spinnerArray2 = new ArrayList<String>();


                File allMyClasses= new File(Environment.getExternalStorageDirectory() + "/EasyA/" + chooseTermName.getItemAtPosition(position).toString());
                if(allMyClasses.isDirectory()){



                    for(File file: allMyClasses.listFiles())
                    {
                        spinnerArray2.add(file.getName());
                        Log.d("allterms", file.getName());
                    }
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
                secondSpinner.setAdapter(spinnerArrayAdapter);

                ArrayAdapter myAdap = (ArrayAdapter) secondSpinner.getAdapter(); //cast to an ArrayAdapter

                Log.d("update spinner", allMyClasses.toString());


                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



                    h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();

                    // this will create a new name everytime and unique

                myTitle = title.getText().toString();

                    File root = new File(Environment.getExternalStorageDirectory()+"/"+ "EasyA" + "/" + chooseTermName.getSelectedItem().toString() + "/" + secondSpinner.getSelectedItem().toString() + "/" + myTitle);

                    // if external memory exists and folder with name Notes

                    if (!root.exists()) {

                        try {
                            root.createNewFile(); // this will create folder.
                            FileWriter writer = new FileWriter(root.getAbsolutePath());

                            writer.append(ed.getText().toString());

                            writer.flush();

                            writer.close();

                        }
                        catch (IOException ex){

                        }
                        String m = "File generated with name " + myTitle + ".txt";
                        result.setText(m);
                        Log.d("saved file", root.getAbsolutePath());

                        finish();



                    }
                else{
                        result.setText("Please choose another name");
                    }









            }

        });

    }

}