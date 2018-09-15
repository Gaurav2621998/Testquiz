package com.class10maths.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.class10maths.GetterAndSetter.Quiz;
import com.class10maths.GetterAndSetter.Result;
import com.class10maths.R;
import com.class10maths.adapter.QuizAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {

     RecyclerView quizrecycler;

    public static Button submit;
    List<Result> resultList = new ArrayList<>();
     public DatabaseReference mref, ref;

   public static QuizAdapter quizadapter;
    public CountDownTimer waittimer;
    @BindView(R.id.timer)
    TextView timer;
    public long time;
    List<Quiz> questionlist = new ArrayList<>();
    @BindView(R.id.quizTitle)
    TextView quizTitle;
    public String quizid;

    ProgressDialog progressDialog;

    public static SharedPreferences activitypreference;
    int flag=0,flagcomefrom;
    public AdView mAdView;
    @BindView(R.id.backarrow)
    ImageView backarrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);



        activitypreference = getSharedPreferences("updatescore", MODE_PRIVATE);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Quiz......");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
                finish();
            }
        });
        progressDialog.show();


        Bundle bundle=getIntent().getBundleExtra("bundle");
        quizid=bundle.getString("quizid");
        flag=bundle.getInt("flag");
        flagcomefrom=bundle.getInt("comefrom");
         // Toast.makeText(this, userid+quizid+flag, Toast.LENGTH_SHORT).show();

        submit = (Button) findViewById(R.id.submit);

        if (flag == 1) {
            submit.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            backarrow.setVisibility(View.VISIBLE);
            progressDialog.dismiss();

        }

        quizrecycler = (RecyclerView) findViewById(R.id.quizrecycler);



        ref = FirebaseDatabase.getInstance().getReference().child("quiz").child(quizid);
        ref.keepSynced(true);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("quiztitle").getValue().toString();
                String quiztime = dataSnapshot.child("quiztime").getValue().toString();
                quizTitle.setText(title);
                int timeinmin = Integer.parseInt(quiztime);
                time = timeinmin * 60000;
                //timer();
                //Toast.makeText(QuizActivity.this, String.valueOf(time), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        quizrecycler.setLayoutManager(layoutManager);
        quizrecycler.setHasFixedSize(true);


        mref = FirebaseDatabase.getInstance().getReference().child("quiz").child(quizid).child("quizques");
        mref.keepSynced(true);



        if(flagcomefrom==1) {
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                int count = 1;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Quiz quiz = snapshot.getValue(Quiz.class);
                        questionlist.add(quiz);
                        Result r = new Result(count, "xyz");
                        resultList.add(r);
                        count += 1;
                    }
                    quizadapter = new QuizAdapter(QuizActivity.this, resultList, questionlist, flag, quizid);
                    quizrecycler.setAdapter(quizadapter);
                    progressDialog.dismiss();
                    timer();
                    //long mill = 600000;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }
        else
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new QuizAsync().execute();
                }
            }, 3000);




        }


    }

    void timer() {
        long mill = time;
        //Toast.makeText(QuizActivity.this, String.valueOf(mill), Toast.LENGTH_SHORT).show();

        waittimer = new CountDownTimer(mill, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                timer.setText("" + String.valueOf(minutes) + ":" + String.valueOf(seconds));

//                } else {
//                    this.cancel();

//                }


                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                timer.setText("Time Over!");
                Intent i=new Intent(QuizActivity.this,ResultActivity.class);
                i.putExtra("resultlist", (Serializable) resultList);
                i.putExtra("questionlist", (Serializable) questionlist);
                Bundle bundle=new Bundle();
                bundle.putString("activity","quiz");
                bundle.putString("quizid",quizid);
                i.putExtra("bundle",bundle);
                startActivity(i);


            }

        }.start();


    }

    @Override
    public void onBackPressed() {

        if(flag==0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Quit");
            dialog.setCancelable(false);
            dialog.setMessage("Quit from this Quiz Are You Sure ?");
            dialog.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog.show();
        }
        else
        {
            finish();
        }


    }

    private class QuizAsync  extends AsyncTask<Void,Void,Void> {



        @Override
        protected Void doInBackground(Void... voids) {

            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                int count = 1;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Quiz quiz = snapshot.getValue(Quiz.class);
                        questionlist.add(quiz);
                        Result r = new Result(count, "xyz");
                        resultList.add(r);
                        count += 1;
                    }
                    quizadapter = new QuizAdapter(QuizActivity.this, resultList, questionlist, flag,quizid);
                    quizrecycler.setAdapter(quizadapter);


                    //long mill = 600000;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    timer();
                }
            },5000);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}
