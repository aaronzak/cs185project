package edu.ucsb.cs.cs185.azakhor.easya;

//TODO: Change the package name

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextFileActivity extends AppCompatActivity {

    private EditText mTitleField;
    private EditText mTextField;
    private String mQuarter;
    private String mClass;
    private boolean isNewFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mTitleField = (EditText) findViewById(R.id.title_editor);
        mTextField = (EditText) findViewById(R.id.text_editor);

        //TODO: Change MainActivity.<key> to wherever the key is stored
        String initTitle = (String) getIntent().getExtras().get(FilesNavActivity.FILENAME_KEY);
        mQuarter = (String) getIntent().getExtras().get(FilesNavActivity.TERM_KEY);
        mClass = (String) getIntent().getExtras().get(FilesNavActivity.CLASS_KEY);

        if(initTitle.length() > 0 &&
                mQuarter.length() > 0 &&
                mClass.length() > 0){
            isNewFile = false;
            mTitleField.setText(initTitle);
        }
        else{
            isNewFile = true;
        }


        //Add specific functionality to "enter" for mTitle
        mTitleField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //if user pressed the "done" button
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    //Set focus to mText
                    mTextField.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //Read in a file.
        if(!isNewFile) {
            readFile(initTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text_save){
            saveTextDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readFile(String name) {
        //Only attempt to read valid file names
        if (name.length() > 0) {
            //Read from file
            String path = this.getExternalFilesDir(null) + "/EasyA/" + mQuarter + "/" + mClass;
            String filename = name;
            File file = new File(path, filename);

            if (file.exists()) {
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                FileInputStream in = null;
                try {
                    in = new FileInputStream(file);
                    in.read(bytes);
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String contents = new String(bytes);

                mTextField.setText(contents);
                Toast.makeText(this, "File Loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *  Opens dialog fragment with two spinners to dictate where to save the file
     */
    private void saveTextDialog(){
        SetSaveFragment saveFragment = new SetSaveFragment();
        saveFragment.setSaveListener(new SetSaveFragment.OnSaveListener() {
            @Override
            public void onSave(String quarter, String classname) {
                if(quarter.length() > 0 && classname.length() > 0) {
                    saveText(quarter, classname);
                }
                else{
                    Toast.makeText(TextFileActivity.this, "Please specify a term and class", Toast.LENGTH_LONG).show();
                }
            }
        });
        saveFragment.show(getFragmentManager(), "save_click");
    }

    /**
     *  Executed when pressing the Save button in the save dialog fragment.
     *  Reads all the text in the text field and saves it to the file.
     */
    private void saveText(String quarter, String classname) {
        String title = mTitleField.getText().toString();     //Get title which will be the filename
        String text = mTextField.getText().toString();       //Get text that will be saved to the file

        //Check if title is not valid
        if(title.length() < 1){
            Toast.makeText(this, "Please specify a title", Toast.LENGTH_LONG).show();
            return; //exit without saving
        }

        //Get file object and create file
        String path = this.getExternalFilesDir(null) + "/EasyA/" + quarter + "/" + classname;
        //Create file path if path does not exist
        File filepath = new File(path);
        if(!filepath.exists()){
            filepath.mkdirs();
        }

        String filename = title;
        File file = new File(path, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Write to file
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
                stream.write(text.getBytes());
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();
        } else {
            //File already exists, display toast message to alert user to choose a different title
            Toast.makeText(this, title + " already exists. Please choose another title name.", Toast.LENGTH_LONG).show();
            return; //exit without saving
        }
    }

}
