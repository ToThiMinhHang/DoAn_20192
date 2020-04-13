package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class GeneralInformation {
    String authorID;
    String imgLink;
    String link;
    String name;
    String status;
    String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

    public List<String> getMucsach() {
        return mucsach;
    }

    public void setMucsach(List<String> mucsach) {
        this.mucsach = mucsach;
    }

    String storyID;
    List<String> mucsach;

    public GeneralInformation() {
        mucsach = new ArrayList<>();
    }


}
