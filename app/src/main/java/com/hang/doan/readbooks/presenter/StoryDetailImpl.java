package com.hang.doan.readbooks.presenter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.models.Book;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailImpl implements StoryDetailPresenter {

    public String TAG = "HANG_DEBUG";
    List<Book> lstBook;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("storyDetail");

    public StoryDetailImpl() {
        lstBook = new ArrayList<>();
    }


    @Override
    public List<Book> downLoadData() {

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String storyName = snapshot.child("storyName").getValue(String.class);
                    String authorLink = snapshot.child("data").child("authorLink").getValue(String.class);
                    String imglink = snapshot.child("data").child("imgLink").getValue(String.class);
                    String link = snapshot.child("data").child("link").getValue(String.class);
                    //TODO

                    Book book = new Book(storyName, imglink, authorLink, null, link);
                    lstBook.add(book);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return lstBook;
    }
}
