package edu.ucsb.cs.cs185.azakhor.easya;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    List<String> arrayList = new ArrayList<String>();

    @Override
    protected void onResume() {

        super.onResume();

        mAdapter.notifyDataSetChanged();
        Log.d("onResume", " new");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Boolean noFolders = true;


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("EasyA");


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {



            // Should we show an explanation?

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    55);

           /* try {
                Thread.sleep(5000);                 //1000 milliseconds is one second.

            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/





            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {



            // Should we show an explanation?

            File dir = new File(this.getExternalFilesDir(null) + "/EasyA");
            if (!dir.exists()) {
                dir.mkdirs();
            }


            File allMyTerms= new File(this.getExternalFilesDir(null) + "/EasyA");
            if(allMyTerms.isDirectory()){
                int n = allMyTerms.listFiles().length;
                Log.d("cameraroll"," " + n );


                for(File file: allMyTerms.listFiles())
                {
                    Log.d("dirExists", file.getName());
                    Log.d("dirExists", dir.getAbsolutePath());
                    noFolders=false;
                    arrayList.add(file.getName());
                }
            }
           /* try {
                Thread.sleep(5000);                 //1000 milliseconds is one second.

            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/





            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }


        if(noFolders){
            TextView noFoldersText = (TextView)findViewById(R.id.noFoldersText);
            noFoldersText.setVisibility(View.VISIBLE);

        }



        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);





        final String myDataset[];
        arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show
        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mRecyclerView.getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // UserTrailRun trailNametoPass = myDataset[position];
                        String myTermFolder = myDataset[position];

                        Log.d("main"," click");


                        Log.d("Click recycler", myTermFolder);



                        Intent intent = new Intent(view.getContext(), ClassNavActivity.class);
                        intent.putExtra("getFolder", myTermFolder);




                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View v, final int myPos){

                        Log.d("main","long click");

                        final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();


                        alertDialog.setMessage("Would you like to delete or rename this folder?");




                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

                            String currentTermFolder = myDataset[myPos];


                            public void onClick(DialogInterface dialog, int id) {

                                Log.d("dialog to delete", currentTermFolder);


                                File allMyClasses = new File(MainActivity.this.getExternalFilesDir(null) + "/EasyA/" + currentTermFolder );
                                deleteDirectory(allMyClasses);


                                String myDataset[];
                                mAdapter.notifyDataSetChanged();
                                arrayList.remove(currentTermFolder);
                                myDataset = arrayList.toArray(new String[arrayList.size()]);
                                mAdapter = new RecyclerAdapter(myDataset);
                                mRecyclerView.setAdapter(mAdapter);


                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Rename", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                alertDialog.hide();

                            }
                        });




                        alertDialog.show();



                    }
                })
        );




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
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.addNewText) {

            Intent intent = new Intent(this, TextFileActivity.class);
            //Send NOTHING through the intent
            intent.putExtra(FilesNavActivity.FILENAME_KEY, "");
            intent.putExtra(FilesNavActivity.TERM_KEY, "");
            intent.putExtra(FilesNavActivity.CLASS_KEY, "");
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, CreateTerm.class);
            // intent.putExtra("trailImageURL", myUrlset[position]);
            this.startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
