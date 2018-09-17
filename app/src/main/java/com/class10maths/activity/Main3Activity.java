package com.class10maths.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.class10maths.GetterAndSetter.Unit;
import com.class10maths.R;
import com.class10maths.adapter.UnitRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {


    UnitRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<Unit>units=new ArrayList<>();

    public static String s;
    UnitRecyclerViewAdapter unitRecyclerViewAdapter;

    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarunit);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        unitRecyclerViewAdapter=new UnitRecyclerViewAdapter(this,units);
        unitRecyclerViewAdapter.clear();
          Intent intent=getIntent();
        s=intent.getStringExtra("key");
        String type=intent.getStringExtra("type");
        FloatingActionButton request_button=(FloatingActionButton) findViewById(R.id.request);
        request_button.setVisibility(View.VISIBLE);
        if(type.equals("0"))
        {
            request_button.setVisibility(View.GONE);
        }
        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Main3Activity.this);
                dialog.setTitle("Purchase");
                dialog.setMessage(R.string.contactdetails );

                dialog.setCancelable(false);
                dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
            }
        });

        recyclerView= findViewById(R.id.unit_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mref= FirebaseDatabase.getInstance().getReference().child("books").child(s).child("units");
        mref.keepSynced(true);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                units.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Unit unit=snapshot.getValue(Unit.class);
                    units.add(unit);

                }
                try {
                    adapter = new UnitRecyclerViewAdapter(Main3Activity.this, units);
                    recyclerView.setAdapter(adapter);
                }
                catch (Exception e)
                {}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
       finish();
    }
}
