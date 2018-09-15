package com.class10maths.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.class10maths.GetterAndSetter.Quiz;
import com.class10maths.GetterAndSetter.QuizInfo;
import com.class10maths.GetterAndSetter.Result;

import com.class10maths.R;
import com.class10maths.activity.DetailsActivity;
import com.class10maths.activity.Main2Activity;
import com.class10maths.activity.QuizActivity;
import com.class10maths.activity.ResultActivity;
import com.class10maths.fragment.QuizFragment;
import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.class10maths.fragment.QuizFragment.alertDialog;
import static com.class10maths.fragment.QuizFragment.mView;
import static com.class10maths.fragment.QuizFragment.sss;

public class QAdapter extends RecyclerView.Adapter<QAdapter.ViewHolder> {

    Context context;

    List<QuizInfo> quizInfoList;

    DatabaseReference mref;
    Date date;


    public QAdapter(Context context, List<QuizInfo> quizInfoList) {
        this.context = context;
        this.quizInfoList = quizInfoList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final QuizInfo info = quizInfoList.get(position);


        holder.quesname.setText(info.getQuiztitle());
        holder.questime.setText(info.getQuiztime()+" min");
        holder.quescount.setText(info.getQuescount()+ " Ques");
        SimpleDateFormat spf1=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            date = spf1.parse(info.getStarttime());
            if(System.currentTimeMillis()>date.getTime())
            {
                holder.starttime.setVisibility(View.GONE);

            }
            else
            {
                holder.starttime.setText("Start On:"+info.getStarttime());
                holder.startQuiz.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



//        Date date1=new Date();
//        String ddd=spf1.format(date1);
//




        holder.startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final QuizInfo info1=quizInfoList.get(position);

                int c=sss.getInt(info1.getQuizid(),0);
                int checkdetail=sss.getInt("userdetail",0);
               // Toast.makeText(context,String.valueOf(c)+info1.getQuizid() , Toast.LENGTH_SHORT).show();
                if(c==0)
                {
                    if(checkdetail==0)
                    {

                        Intent intent=new Intent(context, DetailsActivity.class);
                        intent.putExtra("quizid",info1.getQuizid());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);


////                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                         alertDialog.setTitle("Your title");
////                        LayoutInflater Inflater=LayoutInflater.from(context);
////                        View myView = Inflater.inflate(R.layout.user_details, null);
////                        alertDialog.setView(myView);
//                        alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                ViewGroup viewGroup=(ViewGroup)mView.getParent();
//                                viewGroup.removeView(mView);
//
//                            }
//                        });
//                        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if(isNetworkConnected())
//                                {
//
//                                    sss.edit().putInt("userdetail",1).apply();
//                                    Intent i = new Intent(context, QuizActivity.class);
//                                    Bundle bundle=new Bundle();
//                                    bundle.putString("quizid",info1.getQuizid());
//                                    bundle.putInt("flag",0);
//                                    i.putExtra("bundle",bundle);
//                                    context.startActivity(i);
//
//
//                                }
//
//                            }
//                        });
//
//                        AlertDialog dialog = alertDialog.create();
//                        dialog.show();
                    }
                    else
                    {
                        if (isNetworkConnected())
                        {
                            Intent i = new Intent(context, QuizActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("quizid",info1.getQuizid());
                            bundle.putInt("flag",0);
                            bundle.putInt("comefrom",0);
                            i.putExtra("bundle",bundle);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        }

                    }
                }
                else
                {

                    Gson gson=new Gson();
                    Type type = new TypeToken<List<Result>>(){}.getType();
                    Type type1 = new TypeToken<List<Quiz>>(){}.getType();

                    String json=sss.getString(info1.getQuizid()+"resultlist","");
                    String ajson=sss.getString(info1.getQuizid()+"questionlist","");


                    List<Result>resultList=gson.fromJson(json,type);
                    List<Quiz>questionlist=gson.fromJson(ajson,type1);

                    Intent intent=new Intent(context,ResultActivity.class);
                    intent.putExtra("resultlist", (Serializable) resultList);
                    intent.putExtra("questionlist", (Serializable) questionlist);
                    Bundle bundle=new Bundle();
                    bundle.putString("activity","quiz");
                    bundle.putString("quizid",info1.getQuizid());
                    intent.putExtra("bundle",bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }

            }
        });


    }

    private void alertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Your title");
        LayoutInflater Inflater=LayoutInflater.from(context);
        View myView = Inflater.inflate(R.layout.user_details,null);
        alertDialog.setView(myView);
        alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(isNetworkConnected())
                {


                }

            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }

    @Override
    public int getItemCount() {
        return quizInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView questime,quesname,starttime,quescount;
        public CardView startQuiz;

        public ViewHolder(View itemView) {
            super(itemView);

                questime=itemView.findViewById(R.id.quiztime);
                quesname=itemView.findViewById(R.id.quizname);
                starttime=itemView.findViewById(R.id.starttime);
                startQuiz=itemView.findViewById(R.id.startQuiz);
                quescount=itemView.findViewById(R.id.quescount);

        }


    }

    public void clear()
    {
        final int size = quizInfoList.size();
        quizInfoList.clear();
        notifyItemRangeRemoved(0, size);

    }

    private void delete(int i) {
        quizInfoList.remove(i);
        notifyItemRemoved(i);
    }


}
