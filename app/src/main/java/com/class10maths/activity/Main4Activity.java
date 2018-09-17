package com.class10maths.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.class10maths.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.voghdev.pdfviewpager.library.PDFViewPager;

import static com.class10maths.activity.Main2Activity.mInterstitialAd;
import static com.class10maths.activity.Main3Activity.s;

public class Main4Activity extends AppCompatActivity {

    DatabaseReference mref;

    private WebView webView;


    public static ProgressDialog pDialog;
    private static ProgressDialog progressDialog;
    public static final int progress_bar_type = 0;


    FirebaseDatabase firebaseDatabase;
    public String a;

    public String pdflocation;
    public static NotificationManager mNotifyManager;
    public static NotificationCompat.Builder mBuilder;
    public static String unit;
    PDFViewPager pdfViewPager;
    public static SharedPreferences pref;
    private static final int MEGABYTE = 1024 * 1024;
    FileOutputStream fos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

         pref=this.getSharedPreferences("Status",Context.MODE_PRIVATE);
        Intent intent = getIntent();
        unit = intent.getStringExtra("key");

        if(isNetworkAvaible(this)) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();

            } else {
               // Toast.makeText(this, "not", Toast.LENGTH_SHORT).show();
                open();
            }
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    open();
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    open();
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    open();
                }
            });
        }
        else{

            open();
        }



    }

    public void open()
    {
        mref = FirebaseDatabase.getInstance().getReference().child("books").
                child(s).child("units").child(unit);
        mref.keepSynced(true);

        final Integer size=pref.getInt(s+unit+"size",0);

        // Toast.makeText(this, String.valueOf(size), Toast.LENGTH_SHORT).show();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Pdf Loading......");
        progressDialog.show();
        try{
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + s+unit+".pdf");  // -> filename = maven.pdf
                    //      File pdfFile=new File(getExternalFilesDir("BookDesk"),s+unit+".pdf");
                    File pdfFile = new File(getCacheDir() + "Book" + s + unit + ".pdf");
                   // Toast.makeText(Main4Activity.this, pdfFile.getAbsolutePath()+"1", Toast.LENGTH_LONG).show();
                    if (pdfFile.exists() && size == pdfFile.length()) {
                        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP)
                        {
                            progressDialog.dismiss();


                            Toast.makeText(Main4Activity.this, "Update your os version", Toast.LENGTH_SHORT).show();
                            finish();

// try {
//                                File file = new File(getCacheDir(), "Book" + s + unit + ".pdf");
//                                Toast.makeText(Main4Activity.this, file.getAbsolutePath() + "2", Toast.LENGTH_SHORT).show();
//
//                                Uri uri = FileProvider.getUriForFile(Main4Activity.this, "com.class10maths", pdfFile);
//
//                               // Uri uri=Uri.parse("/data/data/com.class10maths/cacheBook"+s+unit+".pdf");
//                                Toast.makeText(Main4Activity.this, uri.toString()+"3", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_VIEW);
//                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                intent.setDataAndType(uri, "application/pdf");
//                                startActivity(intent);
//                            }
//                            catch(Exception e)
//                            {
//                                Toast.makeText(Main4Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else {
                            progressDialog.dismiss();
                            PDFViewPager pdfViewPager = new PDFViewPager(Main4Activity.this,pdfFile.getAbsolutePath() );
                            setContentView(pdfViewPager);
                        }
                    } else {
                        if ((isNetworkAvaible(Main4Activity.this))) {
                            progressDialog.dismiss();
                            new DownloadFile().execute(dataSnapshot.child("link").getValue().toString(), s + unit + ".pdf");
                        } else {
                            Toast.makeText(Main4Activity.this, "Check Network Connection", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){}


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pDialog.dismiss();
                        finish();
                    }
                });
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    public static void onProgress(String s) {
        pDialog.setProgress(Integer.parseInt(s));


    }

    public static void Preference(int totalSize) {
        pref.edit().putInt(s+unit+"size",totalSize).apply();
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }


        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
//            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//            File folder = new File(extStorageDirectory, "testthreepdf");

    //        File folder=new File(getExternalFilesDir("BookDesk"),fileName);
            File folder=new File(getCacheDir()+"Book"+fileName);
            //folder.mkdir();

            //File pdfFile = new File(folder, fileName);



            try{
                folder.createNewFile();
              //  pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            //FileDownloader.downloadFile(fileUrl, pdfFile);
            FileDownloader.downloadFile(fileUrl,folder);


            return null;
        }

        public void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            //File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + s+unit+".pdf");  // -> filename = maven.pdf
      //     File pdf=new File(getExternalFilesDir("BookDesk"),s+unit+".pdf");
            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP)
            {
                Toast.makeText(Main4Activity.this, "Update your os Version", Toast.LENGTH_SHORT).show();
                finish();
            }
           else {
                try {
                    File pdf = new File(getCacheDir() + "Book" + s + unit + ".pdf");
                    pref.edit().putInt(s + unit, 1).apply();
                    PDFViewPager pdfViewPager = new PDFViewPager(Main4Activity.this, pdf.getAbsolutePath());
//            pdfViewPager.getScrollIndicators();

                   // Toast.makeText(Main4Activity.this, pdf.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    setContentView(pdfViewPager);
                } catch (Exception e) {
                }
            }
        }
    }
    protected String getPdfPathOnSDCard() {
        File f = new File(Environment.getExternalStorageDirectory()+"/BookDesk/"+ s+unit+".pdf");
        Toast.makeText(this, f.toString(), Toast.LENGTH_SHORT).show();
        return f.toString();
    }



    public static boolean isNetworkAvaible(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }



        @Override
        public void onBackPressed() {
            finish();
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        }
}



