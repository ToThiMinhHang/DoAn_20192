package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private String name;
    private String authorLink;
    private List<String> mucSach;
    private String imageURL;

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



    public Book(String name, String imgLink, String authorLink, List<String> mucSach) {
        this.name = name;
        this.imageURL = imgLink;
        this.authorLink = authorLink;
        this.mucSach = mucSach;
    }

}
