package com.hang.doan.readbooks.models;

import java.util.ArrayList;
import java.util.List;

public class StoryItem {

    public String getStory_name() {
        return story_name;
    }

    public void setStory_name(String story_name) {
        this.story_name = story_name;
    }

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }

    public List<String> getMucsach() {
        return mucsach;
    }

    public void setMucsach(List<String> mucsach) {
        this.mucsach = mucsach;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    String image_link;
    String story_name;
    String story_id;
    List<String> chapters;
    List<String> mucsach;

    public StoryItem() {
        chapters = new ArrayList<>();
        mucsach = new ArrayList<>();
    }

}
