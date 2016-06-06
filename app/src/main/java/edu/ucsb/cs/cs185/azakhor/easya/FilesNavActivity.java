package edu.ucsb.cs.cs185.azakhor.easya;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FilesNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<String> arrayList = new ArrayList<String>();
    String myFolder;


    public final static String FILENAME_KEY = "edu.ucsb.cs.cs185.azakhor.easya.filename";
    public final static String TERM_KEY = "edu.ucsb.cs.cs185.azakhor.easya.term";
    public final static String CLASS_KEY = "edu.ucsb.cs.cs185.azakhor.easya.class";
    String currentClassFolder;
      String currentTermFolder;
    Boolean noFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noFiles= true;

        currentClassFolder  = getIntent().getStringExtra("getFolder");
         currentTermFolder = getIntent().getStringExtra("getTerm");
        getSupportActionBar().setTitle(currentTermFolder + "/" + currentClassFolder);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv2);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        final File allMyFiles= new File(this.getExternalFilesDir(null) + "/EasyA/"+ currentTermFolder + "/" + currentClassFolder);
        if(allMyFiles.isDirectory()){
            Log.d("cameraroll File", allMyFiles.toString());

            for(File file: allMyFiles.listFiles())
            {
                Log.d("allMyFiles", file.getAbsolutePath());
                arrayList.add(0,file.getName());
                noFiles = false;
            }
        }

        myFolder = allMyFiles.getAbsolutePath();



if(noFiles){
    TextView noFilesText = (TextView)findViewById(R.id.noFilesText);
    noFilesText.setVisibility(View.VISIBLE);
}
        final String myDataset[];
        arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show

        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter2(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mRecyclerView.getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // UserTrailRun trailNametoPass = myDataset[position];
                        String myFile = arrayList.get(position);


                        Log.d("Click recycler", currentTermFolder + currentClassFolder + myFile);

//Todo change intent to open
                        Intent intent = new Intent(view.getContext(), TextFileActivity.class);
                        //TODO: Fill in Strings below appropriately with the selected text file
                        intent.putExtra(FILENAME_KEY, myFile);
                        intent.putExtra(TERM_KEY, currentTermFolder);
                        intent.putExtra(CLASS_KEY, currentClassFolder);
                        startActivity(intent);





                    }


                    @Override
                    public void onItemLongClick(View v, final int myPos) {

                        final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();


                        alertDialog.setMessage("Are you sure you want to delete this file?");


                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

                            String myFileDelete = myDataset[myPos];


                            public void onClick(DialogInterface dialog, int id) {


                                File allMyClasses = new File(FilesNavActivity.this.getExternalFilesDir(null) + "/EasyA/" + currentTermFolder + "/" + currentClassFolder + "/" + myFileDelete);
                                if (allMyClasses.exists()) {
                                    allMyClasses.delete();

                                }
                                else Log.d("Delete file", allMyClasses.getAbsolutePath());


//                                String myDataset[];
//                                mAdapter.notifyDataSetChanged();
//                                arrayList.remove(myFileDelete);
//                                myDataset = arrayList.toArray(new String[arrayList.size()]);
//                                mAdapter = new RecyclerAdapter(myDataset);
//                                mRecyclerView.setAdapter(mAdapter);

                                recreate();


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

        Button newTextFileButton = (Button)findViewById(R.id.addNewText);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LOCATION", "Restarting");
        final String myDataset[];
        //arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show

        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
     protected void onPostResume() {
        super.onPostResume();
        Log.d("LOCATION", "PostResuming");
        final String myDataset[];
        //arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show

        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }*/




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.files_nav, menu);
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
            intent.putExtra(FILENAME_KEY, "");
            intent.putExtra(TERM_KEY, currentTermFolder);
            intent.putExtra(CLASS_KEY, currentClassFolder);
            startActivity(intent);

        }
        else if( id == R.id.addNewImage){

            dispatchTakePictureIntent();



        }


        return super.onOptionsItemSelected(item);
    }


    private void dispatchTakePictureIntent() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    55);

        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    55);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d("cameraroll", "dispatch");
            // Create the File where the photo should go

           // savePictureDialog();

            File storageDir = new File(this.getExternalFilesDir(null) + "/EasyApics");




            if (!storageDir.exists()) {
                storageDir.mkdir();
            }
            Log.d("cameraroll", storageDir.getAbsolutePath());
            File photoFile = new File(storageDir,"1");
          /*  try {

               // photoFile = createImageFile();
                // TextView text = (TextView) findViewById(R.id.no_pho);
                //text.setVisibility(View.GONE);
                // listofPhotos.add(0, photoFile.getAbsolutePath());



                Log.d("dispatch add",photoFile.getAbsolutePath());





            } catch (IOException ex) {
                // Error occurred while creating the File

            }*/
//            Log.d("cameraroll", photoFile.getAbsolutePath());
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 55);

                arrayList.remove(0);
                arrayList.add(0, photoFile.getAbsolutePath());
                arrayList.add(0,"");



            }
        }
    }


    private void savePictureDialog(){
        SetSavePicFragment savePicFragment = new SetSavePicFragment();
        savePicFragment.setSavePicListener(new SetSavePicFragment.OnSavePicListener() {
            @Override
            public void onSavePic(String title, String quarter, String classname) {
                if(title.length() > 0 && quarter.length() > 0 && classname.length() > 0) {
                    savePicture(quarter, classname,title);
                }
                else{
                    Toast.makeText(FilesNavActivity.this, "Please specify a title, term and class", Toast.LENGTH_LONG).show();
                }
            }
        });
        savePicFragment.show(getFragmentManager(), "save_pic_click");
    }


    private String savePicture(String quarter, String classname,String title) {
        Log.d("SAVING","Start saving text");
            //Get text that will be saved to the file

        //Check if title is not valid


        //Get file object and create file
        String path = this.getExternalFilesDir(null) + "/EasyA/" + quarter + "/" + classname ;
        //Create file path if path does not exist
        File filepath = new File(path);
        if(!filepath.exists()){
            filepath.mkdirs();
        }

        File myPictureFiletoMove = new File(this.getExternalFilesDir(null) + "/EasyApics/1");

        final String filename = title;
        final File file = new File(path, filename);

        if (!file.exists()) {

                myPictureFiletoMove.renameTo(file);


            //Write to file

            Toast.makeText(this, "File "+filename+" Saved", Toast.LENGTH_SHORT).show();
        } else {
            //Show override confirmation fragment asking for confirmation
            SetOverrideFragment overrideFragment = new SetOverrideFragment();
            overrideFragment.setOverrideListener(new SetOverrideFragment.onOverrideListener() {
                @Override
                public void onOverride() {
                    //Override file
                    File myPictureFiletoMove = new File(getApplicationContext().getExternalFilesDir(null) + "/EasyApics/1");
                    myPictureFiletoMove.renameTo(file);

                    //Write to file
                    FileOutputStream stream = null;

                    Toast.makeText(FilesNavActivity.this, "File "+filename+" Overridden", Toast.LENGTH_SHORT).show();

                }
            });
            overrideFragment.show(getFragmentManager(), "override_click");
        }

        return file.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 55 && resultCode == RESULT_OK)
        {


            Log.d("activityResult", " " + arrayList.size());
            savePictureDialog();



            final String myDataset[];
            //arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show

            myDataset = arrayList.toArray(new String[arrayList.size()]);
            mAdapter = new RecyclerAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);


        }

    }










    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, NotePad.class);
            // intent.putExtra("trailImageURL", myUrlset[position]);

            this.startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
