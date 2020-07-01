package com.hang.doan.readbooks.models;

import java.util.List;

public class Story {
    private GeneralInformation generalInformation;
    private List<Chapter> chapters;

    public Story() {

    }

    public Story(GeneralInformation generalInformation, List<Chapter> chapters) {
        this.generalInformation = generalInformation;
        this.chapters = chapters;
    }

    public GeneralInformation getInformation() {
        return generalInformation;
    }

    public void setInformation(GeneralInformation generalInformation) {
        this.generalInformation = generalInformation;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
