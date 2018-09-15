package com.class10maths.GetterAndSetter;

public class QuizInfo {

    public String quiztime;
    public String quiztitle;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String starttime;

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }

    public String quizid;

    public QuizInfo(String quiztime, String quiztitle, String starttime, String quescount,String quizid) {
        this.quiztime = quiztime;
        this.quiztitle = quiztitle;
        this.starttime = starttime;
        this.quescount = quescount;
        this.quizid=quizid;
    }

    public QuizInfo() {
    }

    public String getQuiztime() {
        return quiztime;
    }

    public void setQuiztime(String quiztime) {
        this.quiztime = quiztime;
    }

    public String getQuiztitle() {
        return quiztitle;
    }

    public void setQuiztitle(String quiztitle) {
        this.quiztitle = quiztitle;
    }


    public String getQuescount() {
        return quescount;
    }

    public void setQuescount(String quescount) {
        this.quescount = quescount;
    }

    public String quescount;
}
