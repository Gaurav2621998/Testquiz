package com.class10maths.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.class10maths.GetterAndSetter.Books;
import com.class10maths.R;
import com.class10maths.activity.Main3Activity;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Books> Book;


    public RecyclerViewAdapter(Context context, List<Books> TempList) {

        this.Book = TempList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_details, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final Books books=Book.get(position);
       holder.Bname.setText(books.getBname());
        Glide.with(context.getApplicationContext()).load(books.getBimage()).into(holder.BImage);

       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(context, Main3Activity.class);
               String name=books.getBname();
               String type=books.getType();
               i.putExtra("key",name);
               i.putExtra("type",type);
               context.startActivity(i);
           }
       });



    }

    @Override
    public int getItemCount() {

        return Book.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Bname;
        public CircleImageView BImage;
        public CardView cardView;


        public ViewHolder(View itemView) {

            super(itemView);
            cardView= itemView.findViewById(R.id.card_view);
            Bname = itemView.findViewById(R.id.BookName);
            BImage=(CircleImageView)itemView.findViewById(R.id.bookimage);


        }
    }

    public void clear()
    {
        final int size = Book.size();
        Book.clear();
        notifyItemRangeRemoved(0, size);

    }

    private void delete(int i) {
        Book.remove(i);
        notifyItemRemoved(i);
    }
}
