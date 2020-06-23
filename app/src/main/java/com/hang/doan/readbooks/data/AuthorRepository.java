package com.hang.doan.readbooks.data;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Callback;
import com.hang.doan.readbooks.models.Author;
import com.hang.doan.readbooks.models.AuthorListStoryPost;
import com.hang.doan.readbooks.models.Story;
import com.hang.doan.readbooks.utils.Transform;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class AuthorRepository {
    private FirebaseDatabase database;

    public AuthorRepository() {
        database = FirebaseDatabase.getInstance();
    }

    public void save(Author author) {
        DatabaseReference ref = database.getReference("authorDetail").push();
        ref.setValue(author);
    }

    public void addStory(Author author, AuthorListStoryPost post, Callback<Boolean> callback) {
        DatabaseReference ref = database.getReference("authorDetail").child(author.getAuthorID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.removeEventListener(this);
                if (!dataSnapshot.exists()) {
                    createNewAuthorWithStory(author, post);
                    callback.callback(true);
                } else {
                    addStoryToExistedAuthor(author, post, callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.callback(false);
            }
        });
    }

    private void createNewAuthorWithStory(Author author, AuthorListStoryPost post) {
        DatabaseReference ref = database.getReference("authorDetail").child(author.getAuthorID());
        author.setLstStory(Collections.singletonList(post));
        ref.setValue(author);
    }

    private void addStoryToExistedAuthor(Author author, AuthorListStoryPost post, Callback<Boolean> callback) {
        DatabaseReference ref = database.getReference("authorDetail").child(author.getAuthorID()).child("lstStory");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.removeEventListener(this);
                if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                    ref.setValue(Collections.singletonList(post));
                    callback.callback(true);
                    return;
                }
                List<AuthorListStoryPost> list = Transform.transform(dataSnapshot.getChildren(), snapshot -> snapshot.getValue(AuthorListStoryPost.class));
                list.add(post);
                ref.setValue(list);
                callback.callback(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.callback(false);
            }
        });
    }
}
