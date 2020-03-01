package com.hang.doan.readbooks.presenter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.models.StoryDetail;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailImpl implements StoryDetailPresenter {

    public String TAG = "HANG_DEBUG";
    List<StoryDetail> lstStoryDetail;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("storyDetail");

    public StoryDetailImpl() {
        lstStoryDetail = new ArrayList<>();
    }


    @Override
    public List<StoryDetail> downLoadData() {

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String storyName = snapshot.child("storyName").getValue(String.class);
                    String authorLink = snapshot.child("data").child("authorLink").getValue(String.class);
                    String link = snapshot.child("data").child("link").getValue(String.class);
                    //TODO

                    StoryDetail storyDetail = new StoryDetail(storyName, link, authorLink, null);
                    lstStoryDetail.add(storyDetail);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return lstStoryDetail;
    }
}
