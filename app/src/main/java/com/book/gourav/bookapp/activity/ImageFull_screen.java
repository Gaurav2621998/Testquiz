package com.book.gourav.bookapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.book.gourav.bookapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.book.gourav.bookapp.fragment.ThreeFragment.snakebar;

public class ImageFull_screen extends AppCompatActivity {

    ImageView imageView;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.imagetoolbar);

        setSupportActionBar(toolbar);
        relativeLayout=(RelativeLayout)findViewById(R.id.imagerelative);
        getSupportActionBar().setTitle("PostImage");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i=getIntent();
        final String image=i.getStringExtra("key");
        imageView= findViewById(R.id.news_image);

        Glide.with(this).load(image).into(imageView);

        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        imageView.setAdjustViewBounds(true);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Button b= findViewById(R.id.download_image);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvaible(ImageFull_screen.this)) {
                    Glide.with(ImageFull_screen.this).asBitmap().load(image).into(new SimpleTarget<Bitmap>(500, 500) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            saveImage(resource);
                        }

                    });
                }
                else
                {
                    Snackbar snackbar = Snackbar
                            .make(relativeLayout, "Check Internet Connection.....", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });


    }



    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + image + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "BookApp");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, savedImagePath.toString(), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        sendBroadcast(mediaScanIntent);
    }

    public static boolean isNetworkAvaible(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
