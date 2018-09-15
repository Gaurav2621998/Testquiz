package com.class10maths.GetterAndSetter;

import java.io.Serializable;

public class Quiz implements Serializable {
    String ans;
    String optionA;
    String optionB;
    String optionC;

    public String getQno() {
        return qno;
    }

    public void setQno(String qno) {
        this.qno = qno;
    }

    String qno;

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getQuesimage() {
        return quesimage;
    }

    public void setQuesimage(String quesimage) {
        this.quesimage = quesimage;
    }

    String optionD;
    String ques;
    String quesimage;

    public Quiz(String ans, String optionA, String optionB, String optionC, String optionD, String ques, String quesimage,String qno) {
        this.ans = ans;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.ques = ques;
        this.quesimage = quesimage;
        this.qno=qno;
    }

    public Quiz(){}
}
