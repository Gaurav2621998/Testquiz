package com.class10maths.GetterAndSetter;

import android.os.Parcel;

import java.io.Serializable;

public class Result implements Serializable {

    int Quesno;
    String ans;
    private int checkedId = -1;

    public int getSelectedRadioButtonId() {
        return checkedId;
    }

    public void setSelectedRadioButtonId(int checkedId) {
        this.checkedId = checkedId;
    }


    public Result(){

    }

    protected Result(Parcel in) {
        Quesno = in.readInt();
        ans = in.readString();
    }


    public int getQuesno() {
        return Quesno;
    }

    public void setQuesno(int quesno) {
        Quesno = quesno;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }


    public Result(int quesno, String ans) {
        this.Quesno = quesno;
        this.ans = ans;
    }


}
