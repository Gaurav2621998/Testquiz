package com.class10maths.fragment;

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
import com.class10maths.R;
import com.class10maths.adapter.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment {
    View v;

    RecyclerView recyclerView;
    List<Books> books=new ArrayList<Books>();
    RecyclerView.Adapter adapter;
    RecyclerViewAdapter recyclerViewAdapter;
    DatabaseReference mref;

    public OneFragment()
    {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_one, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        this.v=view;
        recyclerViewAdapter=new RecyclerViewAdapter(getContext(),books);
        recyclerViewAdapter.clear();
        recyclerView = v.findViewById(R.id.book_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        //StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mref= FirebaseDatabase.getInstance().getReference().child("books");
        mref.keepSynced(true);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String image=snapshot.child("image").getValue().toString();
                    String name=snapshot.child("name").getValue().toString();
                    String type=snapshot.child("type").getValue().toString();
                    Books book=new Books(image,name,type);
                    books.add(book);
                }
                adapter=new RecyclerViewAdapter(getContext(),books);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

}
