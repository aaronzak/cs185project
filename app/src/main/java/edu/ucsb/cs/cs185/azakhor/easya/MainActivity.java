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
                    arrayList.add(0,file.getName());
                }
            }






            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }






        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);





        final String myDataset[];
        arrayList.add(0,"");//the first recycler is hidden behind the appbar so this does not show
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



                        if(!myTermFolder.equals("")) {
                            Intent intent = new Intent(view.getContext(), ClassNavActivity.class);
                            intent.putExtra("getFolder", myTermFolder);


                            view.getContext().startActivity(intent);
                        }
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


                                File allMyClasses = new File(MainActivity.this.getExternalFilesDir(null) + "/EasyA/" + currentTermFolder);
                                deleteDirectory(allMyClasses);


                              /*  String myDataset[];

                                arrayList.remove(currentTermFolder);
                                myDataset = arrayList.toArray(new String[arrayList.size()]);
                                mAdapter = new RecyclerAdapter(myDataset);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();*/

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
                })
        );




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_in_main);
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
                    Toast.makeText(MainActivity.this, "Please specify a title, term and class", Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle directoryInfo = new Bundle();
        directoryInfo.putString(FilesNavActivity.TERM_KEY, "");
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

                    Toast.makeText(MainActivity.this, "File "+filename+" Overridden", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_in_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main2, menu);
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
            intent.putExtra("Activity", "main");
            startActivity(intent);
            finish();
        }
        else if( id == R.id.addNewImage){

            dispatchTakePictureIntent();
            // finish();



        }


        return super.onOptionsItemSelected(item);
    }

public void refresh(){
    recreate();
}
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Log.d("TEST","Press action");

            final CreateTerm createTerm = new CreateTerm();
            createTerm.setTermListener(new CreateTerm.onTermListener() {
                @Override
                public void onCreateTerm(String quarterName) {
                    File dir = new File(getExternalFilesDir(null) + "/EasyA/" + quarterName);
                    if (!dir.exists()) {
                        dir.mkdirs();
                        getFragmentManager().beginTransaction().hide(createTerm);
                       // refresh();

//                        final String myDataset[];
//                        arrayList.remove(0);
//                        arrayList.add(0,quarterName);
//                        arrayList.add(0," ");//the first recycler is hidden behind the appbar so this does not show
//                        myDataset = arrayList.toArray(new String[arrayList.size()]);
//                        mAdapter = new RecyclerAdapter(myDataset);
//                        mAdapter.notifyDataSetChanged();
//                        mRecyclerView.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(MainActivity.this, "This name is already in use, please enter another", Toast.LENGTH_LONG).show();
                    }

                }
            });
            createTerm.show(getFragmentManager(), "create_term_click");
            //recreate();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_in_main);
        // drawer.mDrawerLayout.closeDrawer(Gravity.END);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
