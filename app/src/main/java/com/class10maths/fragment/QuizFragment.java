package com.class10maths.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.class10maths.GetterAndSetter.Books;
import com.class10maths.GetterAndSetter.QuizInfo;
import com.class10maths.R;
import com.class10maths.adapter.QAdapter;
import com.class10maths.adapter.RecyclerViewAdapter;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuizFragment extends Fragment {
    View v;

    DatabaseReference mref;
    public static View mView;

    RecyclerView quiz;
    List<QuizInfo>quizInfoList=new ArrayList<>();
    QAdapter adapter;

    public static int fireflag=0;
    public static Builder alertDialog;
    public static LayoutInflater Inflater;

    public static InterstitialAd mInterstitialAd;

    public static SharedPreferences sss;



    public QuizFragment()
    {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_quiz, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        this.v=view;


        sss=getContext().getSharedPreferences("updatescore",MODE_PRIVATE);

        alertDialog=new AlertDialog.Builder(getActivity());
        mView=getActivity().getLayoutInflater().inflate(R.layout.user_details,null);
        alertDialog.setView(mView);

        quiz=(RecyclerView)v.findViewById(R.id.quiz);

        mref= FirebaseDatabase.getInstance().getReference().child("quiz");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        quiz.setLayoutManager(layoutManager);

        quiz.setHasFixedSize(true);
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizInfoList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    QuizInfo info=snapshot.getValue(QuizInfo.class);
                    quizInfoList.add(info);
                }

                adapter=new QAdapter(getActivity().getApplicationContext(),quizInfoList);
                quiz.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

//    public static void alertDialog() {
//        alertDialog.setTitle("Your title");
//       // View myView = Inflater.inflate(R.layout.user_details,null);
//
//        //alertDialog.setView(myView);
//        alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                remove
//
//            }
//        });
//        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//               // if(isNetworkConnected())
//                {
//
//
//                }
//
//            }
//        });
//
//        AlertDialog dialog = alertDialog.create();
//        dialog.show();
//    }

}
