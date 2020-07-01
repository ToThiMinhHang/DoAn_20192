package com.hang.doan.readbooks.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hang.doan.readbooks.models.GeneralInformation;
import com.hang.doan.readbooks.models.Story;

public class StoryRepository {
    private FirebaseDatabase database;
    public StoryRepository() {
        database = FirebaseDatabase.getInstance();
    }

    public String newKey() {
        return database.getReference("storyDetail").push().getKey();
    }

    public void insertOrUpdate(String id, Story story) {
        DatabaseReference ref = database.getReference("storyDetail").child(id);
        GeneralInformation generalInformation = new GeneralInformation();
        generalInformation.setIntroduction(story.getInformation().getIntroduction());
        generalInformation.setStatus(story.getInformation().getStatus());
        generalInformation.setName(story.getInformation().getName());
        generalInformation.setAuthorID(story.getInformation().getAuthorID());
        //generalInformation.setStoryID(story.getInformation().getStoryID());
        generalInformation.setImgLink(story.getInformation().getImgLink());
        //generalInformation.setLink(story.getInformation().getLink());
        generalInformation.setMucsach(story.getInformation().getMucsach());
        generalInformation.setOriginalLanguage(story.getInformation().getOriginalLanguage());
        generalInformation.setTranslatedLanguage(story.getInformation().getTranslatedLanguage());

        ref.child("generalInformation").setValue(generalInformation);
        ref.child("chapters").setValue(story.getChapters());
    }
}
