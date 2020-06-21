package com.hang.doan.readbooks.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        ref.setValue(story);
    }
}
