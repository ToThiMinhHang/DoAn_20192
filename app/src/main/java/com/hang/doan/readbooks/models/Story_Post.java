package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class Story_Post {
    List<Chapter> chapters;

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public GeneralInformation getGeneralInformation() {
        return generalInformation;
    }

    public void setGeneralInformation(GeneralInformation generalInformation) {
        this.generalInformation = generalInformation;
    }

    GeneralInformation generalInformation;

    public Story_Post() {
        chapters = new ArrayList<>();
        generalInformation = new GeneralInformation();
    }
}

