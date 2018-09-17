package com.class10maths.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.class10maths.GetterAndSetter.Unit;

import com.class10maths.R;
import com.class10maths.activity.Main4Activity;
import com.class10maths.activity.Main3Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UnitRecyclerViewAdapter extends RecyclerView.Adapter<UnitRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Unit>unit ;
    FirebaseUser user;
    DatabaseReference mref;
    String value="0";




    public UnitRecyclerViewAdapter(Context context, List<Unit> TempList) {

        this.unit = TempList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       holder.cardView.setEnabled(true);
       holder.bookstatus.setVisibility(View.VISIBLE);
        holder.paid.setVisibility(View.GONE);
        final Unit u=unit.get(position);
        holder.unit.setText(u.getUnit());
        holder.title.setText(u.getTitle());
        final String[] check = {u.getType()};
        SharedPreferences preferences=context.getSharedPreferences("Status",Context.MODE_PRIVATE);
        Integer status=preferences.getInt(Main3Activity.s+u.getUnit(),0);
        if(status.equals(1))
        {
            holder.bookstatus.setVisibility(View.GONE);
        }


        user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid().toString();
        mref= FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value=dataSnapshot.getValue().toString();


                if(check[0].equals("1") && value.equals("1")) {
                    //holder.cardView.setEnabled(false);
                    holder.paid.setVisibility(View.VISIBLE);
                    holder.bookstatus.setVisibility(View.GONE);
                    holder.cardView.setCardBackgroundColor(R.color.card_view);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unit ut=unit.get(position);
                check[0] =ut.getType();
                if(check[0].equals("1") && value.equals("1") ) {


                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Purchase");
                    dialog.setMessage("Contact to that No. 66825414512 for Buy" );

                    dialog.setCancelable(false);
                    dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    }).show();
                }
                else{

                    Intent intent = new Intent(context, Main4Activity.class);
                    String a = u.getUnit();
                    intent.putExtra("key", a);
                     context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return unit.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView unit;
        public TextView title;
        public CardView cardView;
        public ImageView paid;
        public  ImageView bookstatus;


        public ViewHolder(View itemView) {

            super(itemView);

            cardView= itemView.findViewById(R.id.card_view);
            unit = itemView.findViewById(R.id.unit);
            title = itemView.findViewById(R.id.heading);
            paid=itemView.findViewById(R.id.paid);
            bookstatus=itemView.findViewById(R.id.bookstatus);



        }
    }

    public void clear()
    {
        final int size = unit.size();
        unit.clear();
        notifyItemRangeRemoved(0, size);

    }

    private void delete(int i) {
        unit.remove(i);
        notifyItemRemoved(i);
    }
}
