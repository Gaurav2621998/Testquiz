package com.class10maths.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.class10maths.R;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    Context context;
    List<Integer> correctposition;
    int count;

    public ResultAdapter(Context context, List<Integer> correctposition,int count) {
        this.context = context;
        this.correctposition = correctposition;
        this.count=count;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.quesno.setText(String.valueOf(position+1)+".");
        if(correctposition.contains(position)){
            holder.quesresult.setImageResource(R.drawable.correct);
        }

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView quesno;

        ImageView quesresult;



        public ViewHolder(View itemView) {
            super(itemView);

            quesno=itemView.findViewById(R.id.quesno);
            quesresult=itemView.findViewById(R.id.quesresult);



        }
    }
}
