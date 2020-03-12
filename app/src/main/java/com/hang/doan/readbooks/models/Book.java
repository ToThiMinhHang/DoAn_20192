package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private String authorLink;
    private String imageURL;
    private String link;
    private List<String> mucSach;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAuthorLink() {
        return authorLink;
    }

    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }

    public List<String> getMucSach() {
        return mucSach;
    }

    public void setMucSach(List<String> mucSach) {
        this.mucSach = mucSach;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public Book() {
        mucSach = new ArrayList<>();
    }



    public Book(String name, String imgLink, String authorLink, List<String> mucSach, String link) {
        this.name = name;
        this.imageURL = imgLink;
        this.authorLink = authorLink;
        this.mucSach = mucSach;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
