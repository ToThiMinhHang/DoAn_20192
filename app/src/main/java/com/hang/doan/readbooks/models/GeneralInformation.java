package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class GeneralInformation {

    private String storyID;
    private List<String> mucsach;
    private String authorID;
    private String imgLink;
    private String link;
    private String name;
    private String status;
    private String introduction;
    private String originalLanguage;
    private String translatedLanguage;

    public GeneralInformation() {
        mucsach = new ArrayList<>();
    }

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

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTranslatedLanguage() {
        return translatedLanguage;
    }

    public void setTranslatedLanguage(String translatedLanguage) {
        this.translatedLanguage = translatedLanguage;
    }
}
