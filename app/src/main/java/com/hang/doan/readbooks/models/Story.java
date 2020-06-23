package com.hang.doan.readbooks.models;

import java.util.List;

public class Story {
    private GeneralInformation information;
    private List<Chapter> chapters;

    public Story() {

    }

    public Story(GeneralInformation information, List<Chapter> chapters) {
        this.information = information;
        this.chapters = chapters;
    }

    public GeneralInformation getInformation() {
        return information;
    }

    public void setInformation(GeneralInformation information) {
        this.information = information;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
