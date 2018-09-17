package com.class10maths.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.class10maths.GetterAndSetter.Videos;
import com.class10maths.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Videos>videos ;
    List<String>key;

    DatabaseReference mref;

    public VideoRecyclerViewAdapter(Context context, List<Videos> TempList,List<String> keylist) {

        this.videos = TempList;
        this.context = context;
        this.key=keylist;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final Videos v=videos.get(position);


       holder.title.setText(v.getTitle());
       Glide.with(context.getApplicationContext()).load(v.getImage()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=key.get(position);
                mref=FirebaseDatabase.getInstance().getReference().child("videos").child(k).child("link");
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String link=dataSnapshot.getValue().toString();
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        context.startActivity(appIntent);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });

    }

    @Override
    public int getItemCount() {

        return videos.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView imageView;



        public ViewHolder(View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.video_title);
            imageView = itemView.findViewById(R.id.img);



        }
    }

    public void clear()
    {
        final int size = videos.size();
        videos.clear();
        notifyItemRangeRemoved(0, size);

    }
    private void delete(int i) {
        videos.remove(i);
        notifyItemRemoved(i);
    }
}
