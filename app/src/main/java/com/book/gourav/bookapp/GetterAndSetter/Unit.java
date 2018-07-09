package com.book.gourav.bookapp.GetterAndSetter;

public class Unit {
    public String unit;
    public String title;
    public String link;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String type;

    public Unit(){}

    public Unit(String unit, String title, String link,String type) {
        this.unit = unit;
        this.title = title;
        this.type=type;
        this.link = link;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

