package edu.ucsb.cs.cs185.azakhor.easya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
public class CreateTerm extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_term);



        Button addNewTermButton = (Button)findViewById(R.id.confirmNewTerm);
        addNewTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText enterTermName = (EditText)findViewById(R.id.enterTermName);

                String newTermString = enterTermName.getText().toString();

                Log.d("Enter new term", newTermString);

                File dir = new File(Environment.getExternalStorageDirectory() + "/EasyA/" + newTermString);
                if (!dir.exists()) {
                    dir.mkdirs();
                    Intent intent = new Intent(v.getContext(),MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Snackbar.make(v, "This name is already in use, please enter another", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

    }
}
