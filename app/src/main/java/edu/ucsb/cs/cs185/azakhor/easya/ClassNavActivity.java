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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
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
                arrayList.add(0,file.getName());

            }
        }




        final String myDataset[];
        arrayList.add(0,"");//the first recycler is hidden behind the appbar so this does not show

        myDataset = arrayList.toArray(new String[arrayList.size()]);
        mAdapter = new RecyclerAdapter(myDataset);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mRecyclerView.getContext(),mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // UserTrailRun trailNametoPass = myDataset[position];
                        String myClassFolder = myDataset[position];


                        Log.d("Click recycler", myClassFolder);

                        if(!myClassFolder.equals("")) {
                            Intent intent = new Intent(view.getContext(), FilesNavActivity.class);
                            intent.putExtra("getFolder", myClassFolder);
                            intent.putExtra("getTerm", currentTermFolder);


                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onItemLongClick(View v, final int myPos){

                       final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();


                        alertDialog.setMessage("Are you sure you want to delete or rename this folder?");




                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {

                            String myClassFolder = myDataset[myPos];


                            public void onClick(DialogInterface dialog, int id) {


                                File myClasses = new File(ClassNavActivity.this.getExternalFilesDir(null) + "/EasyA/" + currentTermFolder + "/" + myClassFolder);
                                deleteDirectory(myClasses);


//                                String myDataset[];
//                                mAdapter.notifyDataSetChanged();
//                                arrayList.remove(myClassFolder);
//                                myDataset = arrayList.toArray(new String[arrayList.size()]);
//                                mAdapter = new RecyclerAdapter(myDataset);
//                                mRecyclerView.setAdapter(mAdapter);
                                recreate();

                            }
                        });


                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Rename", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                alertDialog.hide();

                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

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
       // getMenuInflater().inflate(R.menu.class_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.addNewText) {
            /*

            Intent intent = new Intent(this, TextFileActivity.class);
            //Send NOTHING through the intent
            intent.putExtra(FilesNavActivity.FILENAME_KEY, "");
            intent.putExtra(FilesNavActivity.TERM_KEY, "");
            intent.putExtra(FilesNavActivity.CLASS_KEY, "");
            intent.putExtra("Activity", "class");
            startActivity(intent);
            finish();
            */
        }

        else if( id == R.id.addNewImage){

            dispatchTakePictureIntent();
            // finish();



        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }




    public void dispatchTakePictureIntent() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    55);

        }


       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    55);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }*/

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

                //arrayList.remove(0);
                //arrayList.add(0, photoFile.getAbsolutePath());
                //arrayList.add(0,"");



            }
        }
    }


    private void savePictureDialog(){
        SetSavePicFragment savePicFragment = new SetSavePicFragment();
        savePicFragment.setSavePicListener(new SetSavePicFragment.OnSavePicListener() {
            @Override
            public void onSavePic(String title, String quarter, String classname) {
                if (title.length() > 0 && quarter.length() > 0 && classname.length() > 0) {
                    savePicture(quarter, classname, title);

                } else {
                    Toast.makeText(ClassNavActivity.this, "Please specify a title, term and class", Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle directoryInfo = new Bundle();
        directoryInfo.putString(FilesNavActivity.TERM_KEY, currentTermFolder);
        directoryInfo.putString(FilesNavActivity.CLASS_KEY, "");
        savePicFragment.setArguments(directoryInfo);
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

        final String filename = title + ".jpg";
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

                    Toast.makeText(ClassNavActivity.this, "File "+filename+" Overridden", Toast.LENGTH_SHORT).show();

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






        }

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
                public void onCreateCourse(String quarterName, String courseName) {

                    File dir = new File(getExternalFilesDir(null) + "/EasyA/" + currentTermFolder + "/" + courseName);
                    if (!dir.exists()) {
                        Log.d("Enter new term", dir.getAbsolutePath());
                        dir.mkdirs();
                      //  finish();
                    }
                    else {
                        Toast.makeText(ClassNavActivity.this, "This name is already in use, please enter another", Toast.LENGTH_LONG).show();
                    }
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
