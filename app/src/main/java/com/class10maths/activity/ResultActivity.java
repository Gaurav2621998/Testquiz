package com.class10maths.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.class10maths.GetterAndSetter.Quiz;
import com.class10maths.GetterAndSetter.Result;
import com.class10maths.R;
import com.class10maths.adapter.ResultAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.class10maths.activity.Main2Activity.mInterstitialAd;


public class ResultActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.marksgot)
    TextView marksgot;
    @BindView(R.id.totalmarks)
    TextView totalmarks;
    @BindView(R.id.answerrecycler)
    RecyclerView answerrecycler;
    @BindView(R.id.viewanswer)
    Button viewanswer;

    ResultAdapter adapter;
    DatabaseReference mref;
    FirebaseUser user;

    List<Integer> correctpostion = new ArrayList<>();
    @BindView(R.id.retry)
    Button retry;

    int topscore;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);


        if(isNetworkAvaible(this)) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
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

                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            });
        }








        user= FirebaseAuth.getInstance().getCurrentUser();
        final List<Result> resultList = (List<Result>) getIntent().getSerializableExtra("resultlist");
        final List<Quiz> questionlist = (List<Quiz>) getIntent().getSerializableExtra("questionlist");
//        String activity=getIntent().getStringExtra("activity");
//        String quizid=getIntent().getStringExtra("quizid");

        final Bundle bundle = getIntent().getBundleExtra("bundle");
        String activity = bundle.getString("activity");
        final String quizid = bundle.getString("quizid");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Result");
        mref= FirebaseDatabase.getInstance().getReference().child("quiz").child(quizid).child("topplayer");

       // Toast.makeText(this, activity + quizid, Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences("updatescore", MODE_PRIVATE);
        int scoreupdate=preferences.getInt("upscore"+quizid,0);



        if (activity.equals("quiz")) {
            //Toast.makeText(this, quizid, Toast.LENGTH_SHORT).show();
            preferences.edit().putInt(quizid, 1).apply();
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(resultList);
            String ajson = gson.toJson(questionlist);
            editor.putString(quizid + "resultlist", json);
            editor.putString(quizid + "questionlist", ajson);
            editor.commit();


        }

        // Toast.makeText(this, String.valueOf(resultList.size()), Toast.LENGTH_SHORT).show();


        for (int i = 0; i < resultList.size(); i++) {
            Result r = resultList.get(i);
            Quiz q = questionlist.get(i);

            if (r.getAns().equals(q.getAns())) {
                correctpostion.add(i);
                count += 1;
            }

        }


        if(scoreupdate==0)
        {
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    topscore =Integer.parseInt(dataSnapshot.child("score").getValue().toString());
                    updatescore(topscore);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            preferences.edit().putInt("upscore"+quizid,1).apply();

        }

        marksgot.setText(String.valueOf(count));
        totalmarks.setText(String.valueOf(questionlist.size()));

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        answerrecycler.setLayoutManager(layoutManager);
        answerrecycler.setHasFixedSize(true);

        adapter = new ResultAdapter(this, correctpostion, resultList.size());
        answerrecycler.setAdapter(adapter);


        viewanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ResultActivity.this, QuizActivity.class);
                Bundle bundle1 = new Bundle();

                bundle1.putString("quizid", quizid);
                bundle1.putInt("flag", 1);
                bundle1.putInt("comefrom",1);
                i.putExtra("bundle", bundle1);
                startActivity(i);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(ResultActivity.this, QuizActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("quizid", quizid);
                    bundle1.putInt("flag", 0);
                    bundle1.putInt("comefrom",1);
                    i.putExtra("bundle", bundle1);
                    startActivity(i);

            }
        });


    }

    private void updatescore(int topscore) {
        if(count>topscore)
        {
            mref.child("playerid").setValue(user.getUid().toString());
            mref.child("score").setValue(String.valueOf(count));
        }
    }

    public boolean isNetworkAvaible(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i=new Intent(this,Main2Activity.class);
        i.putExtra("comefrom","Result");
        startActivity(i);

    }
}
