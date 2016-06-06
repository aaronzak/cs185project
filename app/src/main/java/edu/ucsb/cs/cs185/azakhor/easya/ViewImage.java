package edu.ucsb.cs.cs185.azakhor.easya;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

/**
 * Created by AaronZak on 6/6/16.
 */
public class ViewImage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        String myPic = getIntent().getStringExtra("PicToShow");


        ImageView imV = (ImageView)findViewById(R.id.fullScreen);
        Bitmap myBitmap = BitmapFactory.decodeFile(myPic);

        imV.setImageBitmap(myBitmap);
    }
}
