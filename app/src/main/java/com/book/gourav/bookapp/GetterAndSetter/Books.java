package com.book.gourav.bookapp.GetterAndSetter;

import java.util.List;

public class Books {
    public String getType() {
        return type;
    }

    private  String type;
    private String bname;


    public String getBimage() {
        return bimage;
    }

    public void setBimage(String bimage) {
        this.bimage = bimage;
    }

    private String bimage;

    public Books(){}

    public Books(String image,String name,String type) {
        this.bname=name;
        this.bimage=image;
        this.type=type;
    }


    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }





}
