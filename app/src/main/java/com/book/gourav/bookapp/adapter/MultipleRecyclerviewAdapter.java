package com.book.gourav.bookapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.book.gourav.bookapp.GetterAndSetter.News;
import com.book.gourav.bookapp.R;
import com.book.gourav.bookapp.activity.ImageFull_screen;
import com.book.gourav.bookapp.fragment.BlankFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.book.gourav.bookapp.fragment.ThreeFragment.snakebar;
import static java.security.AccessController.getContext;

public class MultipleRecyclerviewAdapter extends RecyclerView.Adapter<MultipleRecyclerviewAdapter.ViewHolder> {

    Context context;
    List<News> newsList;
    List<String> uid;
    DatabaseReference mref,mcheck,madd,mdel;
    FirebaseAuth mAuth;
    Boolean aBoolean;


    public MultipleRecyclerviewAdapter(Context context, List<News>news,List<String>id)
    {
        this.context= context;
        this.newsList=news;
        this.uid=id;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);

                return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        mAuth=FirebaseAuth.getInstance();
        mref=FirebaseDatabase.getInstance().getReference().child("news");
        holder.postImage.setVisibility(View.GONE);
        final boolean[] isImageFitToScreen = new boolean[1];

        final String key=uid.get(position);

        final News n=newsList.get(position);
        holder.title.setText(n.getTitle());
        holder.date.setText(n.getDate());
        holder.desc.setText(n.getDesc());
        holder.count.setText(n.getCount());
        if(!(n.getImage().equals("0"))){
            holder.postImage.setVisibility(View.VISIBLE);

            Glide.with(context.getApplicationContext()).load(n.getImage()).into(holder.postImage);
        }

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,ImageFull_screen.class);
                i.putExtra("key",n.getImage());
                context.startActivity(i);

//                AppCompatActivity activity = (AppCompatActivity)
//                        v.getContext();
//                Fragment myFragment = new BlankFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("imageurl",n.getImage());
//                myFragment.setArguments(bundle);
//                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.newRelative, myFragment)
//                        .addToBackStack(null)
//                        .commit();

            }
        });


        mcheck=mref.child(key);
        mcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("upVotes").hasChild(mAuth.getCurrentUser().getUid()))
                {
                    holder.clapimage.setImageResource(R.drawable.clapping);

                }

                else
                {
                    holder.clapimage.setImageResource(R.drawable.clapping_white);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.clapimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isNetworkAvaible(context)){

                   mcheck= mref.child(key);
                   aBoolean=true;
                   mcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           if(aBoolean) {

                               if (dataSnapshot.child("upVotes").hasChild(mAuth.getCurrentUser().getUid())) {
                                   n.decreaseup();
                                   holder.count.setText(n.getCount());
                                   mcheck.child("Count").setValue(n.getCount());
                                   mcheck.child("upVotes").child(mAuth.getCurrentUser().getUid()).removeValue();
                                   holder.clapimage.setImageResource(R.drawable.clapping_white);
                                   aBoolean=false;
                               }
                               else {
                                   n.setCount();
                                   holder.count.setText(n.getCount());
                                   mcheck.child("upVotes").child(mAuth.getCurrentUser().getUid()).setValue("0");
                                   mcheck.child("Count").setValue(n.getCount());
                                   holder.clapimage.setImageResource(R.drawable.clapping);
                                   aBoolean=false;
                               }
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });


               }else
               {
                   Snackbar snackbar = Snackbar
                           .make(snakebar, "Check Internet Connection.....", Snackbar.LENGTH_LONG);
                   snackbar.show();

               }
            }
        });






    }

    public static boolean isNetworkAvaible(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected();
    }









    @Override
    public int getItemCount() {
        return newsList.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,count,date;
        public TextView desc;
        public ImageView postImage,clapimage;


        public ViewHolder(View itemView) {
            super(itemView);

            title= itemView.findViewById(R.id.title1);
            desc= itemView.findViewById(R.id.desc1);
            postImage= itemView.findViewById(R.id.newsimage);
            clapimage= itemView.findViewById(R.id.claping);
            count=itemView.findViewById(R.id.count);
            date=itemView.findViewById(R.id.date);



        }
    }

    public void clear()
    {

        final int size = newsList.size();
        newsList.clear();
        notifyItemRangeRemoved(0, size);

    }
    //
    private void delete(int i) {
        newsList.remove(i);
        notifyItemRemoved(i);
    }



}

