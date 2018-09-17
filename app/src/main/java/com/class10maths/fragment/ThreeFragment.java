package com.class10maths.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.class10maths.GetterAndSetter.News;
import com.class10maths.R;
import com.class10maths.adapter.MultipleRecyclerviewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThreeFragment extends Fragment {

    public ThreeFragment() {
        // Required empty public constructor
    }

    MultipleRecyclerviewAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    MultipleRecyclerviewAdapter multipleRecyclerviewAdapter;
    RecyclerView recyclerView;
    List<News> news=new ArrayList<>();
    List<String>key=new ArrayList<>();
    DatabaseReference mref;
    View v;
    public static ConstraintLayout snakebar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_three, container, false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        this.v=view;
        load();
    }
    public void load(){
        multipleRecyclerviewAdapter=new MultipleRecyclerviewAdapter(getContext(),news,key);
        multipleRecyclerviewAdapter.clear();
        snakebar=(ConstraintLayout)v.findViewById(R.id.sankebar);
        recyclerView= v.findViewById(R.id.news_recycler);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mref= FirebaseDatabase.getInstance().getReference().child("news");
        mref.keepSynced(true);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                news.clear();
                key.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    News n=snapshot.getValue(News.class);
                    String id=snapshot.getKey().toString();
                    key.add(id);
                    news.add(n);
                }

                adapter= new MultipleRecyclerviewAdapter(getContext(),news,key);
                recyclerView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);




    }



}
