package com.book.gourav.bookapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.book.gourav.bookapp.GetterAndSetter.Videos;
import com.book.gourav.bookapp.R;
import com.book.gourav.bookapp.adapter.VideoRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {

    public TwoFragment() {
        // Required empty public constructor
    }
    ImageView imageview_micro;
    View v;
    RecyclerView recyclerView;
    VideoRecyclerViewAdapter adapter;
    List<Videos>videos=new ArrayList<>();
    List<String>key=new ArrayList<>();
    VideoRecyclerViewAdapter videoRecyclerViewAdapter;

    DatabaseReference mref;


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_two, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        this.v=view;
        videoRecyclerViewAdapter=new VideoRecyclerViewAdapter(this.getActivity(),videos,key);
        videoRecyclerViewAdapter.clear();
        recyclerView= view.findViewById(R.id.video_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mref= FirebaseDatabase.getInstance().getReference().child("videos");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                videos.clear();
                key.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Videos v=snapshot.getValue(Videos.class);
                    videos.add(v);
                    String k=snapshot.getKey().toString();
                    key.add(k);

                }
                adapter=new VideoRecyclerViewAdapter(getContext(),videos,key);
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
