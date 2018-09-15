package com.class10maths.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.class10maths.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {

    Button startbutton;
    DatabaseReference mref;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.usermobile)
    EditText usermobile;

    FirebaseUser user;
    @BindView(R.id.post)
    Button post;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        startbutton = (Button) findViewById(R.id.startbutton);



        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        final String quizidd=getIntent().getStringExtra("quizid");
         user= FirebaseAuth.getInstance().getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference().child("quizplayers").child(user.getUid());
        // create child with uid

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvaible(DetailsActivity.this)) {
                    if (check()) {
                        // Toast.makeText(DetailsActivity.this, "yes", Toast.LENGTH_SHORT).show();
                        mref.child("username").setValue(username.getText().toString());
                        mref.child("usermobile").setValue(usermobile.getText().toString());
                        SharedPreferences sharedPreferences=getSharedPreferences("updatescore",MODE_PRIVATE);
                        sharedPreferences.edit().putInt("userdetail",1).apply();
                        Intent i = new Intent(DetailsActivity.this, QuizActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("quizid",quizidd);
                        bundle.putInt("flag",0);
                        bundle.putInt("comefrom",0);
                        i.putExtra("bundle",bundle);
                        startActivity(i);
                        finish();

                    }
                }
                else
                {
                    Toast.makeText(DetailsActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public Boolean check() {
        if (username.getText().toString().equals("") && usermobile.getText().toString().equals("")) {


            Toast.makeText(this, "Fill the Details", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if(usermobile.getText().toString().length()==10) {
                return true;
            }
            else
            {
                Toast.makeText(this, "Mobile No. not valid", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public boolean isNetworkAvaible(Context context) {
        ConnectivityManager conMan = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }



}

