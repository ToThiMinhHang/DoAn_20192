package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Author {


    private String authorID;
    private String link;
    private String authorName;
    private List<AuthorListStoryPost> lstStory;
    private String imageUser;

    public  Author() {
        lstStory = new ArrayList<>();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<AuthorListStoryPost> getLstStory() {
        return lstStory;
    }

    public void setLstStory(List<AuthorListStoryPost> lstStory) {
        this.lstStory = lstStory;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }
}
