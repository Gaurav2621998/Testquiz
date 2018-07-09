package com.book.gourav.bookapp.GetterAndSetter;

public class Videos {

    String image;

    public String getImage() {
        return image;
    }

    public Videos(String image, String link, String title) {
        this.image = image;
        this.link = link;
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String link;

    public Videos() {
    }

    String title;

}
