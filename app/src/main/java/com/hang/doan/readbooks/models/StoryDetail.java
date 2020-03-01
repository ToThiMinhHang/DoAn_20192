package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class StoryDetail {

    private String name;
    private String link;
    private String authorLink;
    private List<String> mucSach;
    private String imageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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


    public StoryDetail () {
        mucSach = new ArrayList<>();
    }



    public StoryDetail(String name, String link, String authorLink, List<String> mucSach) {
        this.name = name;
        this.link = link;
        this.authorLink = authorLink;
        this.mucSach = mucSach;
    }

}
