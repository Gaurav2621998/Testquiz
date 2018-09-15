package com.class10maths.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.class10maths.GetterAndSetter.Quiz;
import com.class10maths.GetterAndSetter.Result;
import com.class10maths.R;
import com.class10maths.activity.ResultActivity;

import java.io.Serializable;
import java.util.List;

import static com.class10maths.activity.QuizActivity.submit;


public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    Context context;
    int count;
    List<Result> resultList;
    int score=0;
    List<Quiz>questionlist;
    String quizid;

    int flag;
    private RadioGroup lastCheckedRadioGroup = null;


    public QuizAdapter(Context context, List<Result> results,List<Quiz>questionlist,int flag,String quizid) {
        this.context = context;
        this.resultList = results;
        this.questionlist=questionlist;
        this.flag=flag;
        this.quizid=quizid;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.quesimage.setVisibility(View.GONE);
        Quiz q=questionlist.get(position);

        Result r=resultList.get(position);



        //holder.setIsRecyclable(false);


        holder.quesno.setText("Q."+q.getQno());
        holder.ques.setText(q.getQues());
        holder.optionA.setText(q.getOptionA());
        holder.optionB.setText(q.getOptionB());
        holder.optionC.setText(q.getOptionC());
        holder.optionD.setText(q.getOptionD());
        if(flag==1)
        {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.ans.setText(q.getAns());
            holder.optionA.setClickable(false);
            holder.optionB.setClickable(false);
            holder.optionC.setClickable(false);
            holder.optionD.setClickable(false);
        }

        if(!(q.getQuesimage().equals("0")))
        {
            holder.quesimage.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).load(q.getQuesimage()).into(holder.quesimage);

        }
        holder.radioGroup.setTag(position);

        if(r.getSelectedRadioButtonId()!=-1)
        {
            holder.radioGroup.check(r.getSelectedRadioButtonId());
        }
        else
        {
            holder.radioGroup.clearCheck();
        }

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                notifyDataSetChanged();
//                notifyItemRangeChanged(0,resultList.size()-1);


                 Result r1=resultList.get(position);
                int clickpos3= (int) group.getTag();

                resultList.get( clickpos3).setSelectedRadioButtonId(checkedId);

                if(r1.getSelectedRadioButtonId()!=-1 && position==clickpos3)
                {
                    //Toast.makeText(context, String.valueOf(clickpos3)+":"+String.valueOf(position), Toast.LENGTH_SHORT).show();

                        RadioButton rb=(RadioButton)group.findViewById(checkedId);
                        String option= (String) rb.getText();
                        Result s=resultList.get(position);
                        s.setAns(option);
                        //Toast.makeText(context, String.valueOf(s.getQuesno()), Toast.LENGTH_SHORT).show();

                }



            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(context, quizid, Toast.LENGTH_SHORT).show();

                //activitypreference.edit().putBoolean(quizid,false);
                Intent i=new Intent(context,ResultActivity.class);
                i.putExtra("resultlist", (Serializable) resultList);
                i.putExtra("questionlist", (Serializable) questionlist);
                Bundle bundle=new Bundle();
                bundle.putString("activity","quiz");
                bundle.putString("quizid",quizid);
                i.putExtra("bundle",bundle);
                context.startActivity(i);


//                for(int i=0;i<10;i++){esult
//                    Result a=resultList.get(i);
//
//                    Toast.makeText(context, a.getAns().toString(), Toast.LENGTH_SHORT).show();
//
//
//                }
//                Toast.makeText(context, String.valueOf(score), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return questionlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView quesimage;
        public TextView quesno,ques,ans;
        public RadioGroup radioGroup;
        public RadioButton optionA,optionB,optionC,optionD;
        public LinearLayout linearLayout;




        public ViewHolder(View itemView) {
            super(itemView);




            quesimage = itemView.findViewById(R.id.quesimage);
            radioGroup=itemView.findViewById(R.id.radiogroup);
            quesno=itemView.findViewById(R.id.quesno);
            ques=itemView.findViewById(R.id.ques);
            optionA=itemView.findViewById(R.id.optionA);
            optionB=itemView.findViewById(R.id.optionB);
            optionC=itemView.findViewById(R.id.optionC);
            optionD=itemView.findViewById(R.id.optionD);
            ans=itemView.findViewById(R.id.ans);
            linearLayout=itemView.findViewById(R.id.anslayout);

        }


    }




}
