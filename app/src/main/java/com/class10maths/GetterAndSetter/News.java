package com.class10maths.GetterAndSetter;

public class News {
    public String title;
    public String desc;

    public String getCount() {
        return Count;
    }

    public void setCount() {
        this.Count = Integer.toString(Integer.parseInt(Count)+1);
    }

    private String Count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String date;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String image;


    public News() {
    }

    public News(String title,String desc,String image,String Count,String date)
    {
            this.date=date;
            this.desc=desc;
            this.title=title;
            this.image=image;
            this.Count=Count;
    }

    public void decreaseup() {
        this.Count = Integer.toString(Integer.parseInt(Count)-1);
    }
}
