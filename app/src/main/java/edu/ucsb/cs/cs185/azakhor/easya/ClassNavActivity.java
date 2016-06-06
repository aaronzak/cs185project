package edu.ucsb.cs.cs185.azakhor.easya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<String> arrayList = new ArrayList<String>();

    String currentTermFolder;

    Boolean noClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_nav);
        Log.d("open Class", "shia");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noClass = true;


         currentTermFolder = getIntent().getStringExtra("getFolder");
        getSupportActionBar().setTitle(currentTermFolder);
//        TextView myTextView = (TextView)findViewById(R.id.textView2);
//        myTextView.setText(currentTermFolder);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);





        mRecyclerView = (RecyclerView) findViewById(R.id.rv2);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        File allMyClasses= new File(this.getExternalFilesDir(null) + "/EasyA/"+ currentTermFolder);
        Log.d("currentTermFolder" , allMyClasses.getAbsolutePath());
        if(allMyClasses.isDirectory()){
            Log.d("cameraroll", "dir");

            for(File file: allMyClasses.listFiles())
            {
                noClass = false;
                arrayList.add(file.getName());
            }
        }

        if(noClass){
            TextView noClassT = (TextView)findViewById(R.id.noClassText);
            noClassT.setVisibility(View.VISIBLE);
        }


        final String myDataset[];
        arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show

        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter(myDataset);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mRecyclerView.getContext(),mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // UserTrailRun trailNametoPass = myDataset[position];
                        String myClassFolder = arrayList.get(position);


                        Log.d("Click recycler", myClassFolder);


                        Intent intent = new Intent(view.getContext(), FilesNavActivity.class);
                        intent.putExtra("getFolder", myClassFolder);
                        intent.putExtra("getTerm", currentTermFolder);


                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View v, final int myPos){

                       final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();

                        alertDialog.setMessage("Are you sure you want to delete this folder?");

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            String myClassFolder = myDataset[myPos];
                            public void onClick(DialogInterface dialog, int id) {
                                File myClasses = new File(ClassNavActivity.this.getExternalFilesDir(null) + "/EasyA/" + currentTermFolder + "/" + myClassFolder);
                                deleteDirectory(myClasses);
                                String myDataset[];
                                mAdapter.notifyDataSetChanged();
                                arrayList.remove(myClassFolder);
                                myDataset = arrayList.toArray(new String[arrayList.size()]);
                                mAdapter = new RecyclerAdapter(myDataset);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                alertDialog.hide();
                            }
                        });

                        alertDialog.show();
                    }


                }
        ));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.class_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.addNewText) {

            Intent intent = new Intent(this, TextFileActivity.class);
            //Send NOTHING through the intent
            intent.putExtra(FilesNavActivity.FILENAME_KEY, "");
            intent.putExtra(FilesNavActivity.TERM_KEY, "");
            intent.putExtra(FilesNavActivity.CLASS_KEY, "");
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addNewClass) {

            CreateClass createClass = new CreateClass();
            createClass.setTermListener(new CreateClass.onCourseListener() {
                @Override
                public void onCreateCourse(final String courseName) {
                    File dir = new File(getExternalFilesDir(null) + "/EasyA/" + currentTermFolder + "/" + courseName);
                    if (!dir.exists()) {
                        Log.d("Enter new term", dir.getAbsolutePath());
                        dir.mkdirs();
                    }
                    else {
                        Toast.makeText(ClassNavActivity.this, "This name is already in use, please enter another", Toast.LENGTH_LONG).show();
                    }

                    ClassNavActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final String myDataset[];
                            arrayList.add(courseName);
                            myDataset = arrayList.toArray(new String[arrayList.size()]);
                            mAdapter = new RecyclerAdapter(myDataset);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.invalidate();
                        }
                    });
                }
            });
            createClass.show(getFragmentManager(),"create_class_click");

            // Intent intent = new Intent(this, CreateClass.class);
            // intent.putExtra("trailImageURL", myUrlset[position]);
            //intent.putExtra("getCurrentClass",currentTermFolder);

            //this.startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
