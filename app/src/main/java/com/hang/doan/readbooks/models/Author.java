package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Author {
    String authorName;
    List<String> lstStory;
    List<String> lstStoryLink;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<String> getLstStory() {
        return lstStory;
    }

    public void setLstStory(List<String> lstStory) {
        this.lstStory = lstStory;
    }

    public List<String> getLstStoryLink() {
        return lstStoryLink;
    }

    public void setLstStoryLink(List<String> lstStoryLink) {
        this.lstStoryLink = lstStoryLink;
    }


    public  Author() {
        lstStory = new ArrayList<>();
        lstStoryLink = new ArrayList<>();
    }
}
