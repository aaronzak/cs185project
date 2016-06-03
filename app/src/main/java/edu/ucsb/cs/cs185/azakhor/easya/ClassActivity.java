package edu.ucsb.cs.cs185.azakhor.easya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AaronZak on 6/1/16.
 */
public class ClassActivity extends AppCompatActivity {



    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<String> arrayList = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class2);

        String currentTermFolder = getIntent().getStringExtra("getFolder");
//        TextView myTextView = (TextView)findViewById(R.id.textView2);
//        myTextView.setText(currentTermFolder);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);





        mRecyclerView = (RecyclerView) findViewById(R.id.rv2);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        File allMyClasses= new File(Environment.getExternalStorageDirectory() + "/EasyA/"+ currentTermFolder);
        if(allMyClasses.isDirectory()){
            Log.d("cameraroll", "dir");

            for(File file: allMyClasses.listFiles())
            {
                arrayList.add(file.getName());
            }
        }

        arrayList.add("yo");


        final String myDataset[];
        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_class) {
            Intent intent = new Intent(this, CreateTerm.class);
            // intent.putExtra("trailImageURL", myUrlset[position]);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
