package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.StoryItemArrayAdapter;
import com.hang.doan.readbooks.models.StoryItem;

public class MyStoryActivity extends AppCompatActivity {

    String user_id;
    FirebaseDatabase database;
    DatabaseReference authorRef;
    DatabaseReference storyRef;

    ListView activity_my_story_lst_story;
    private StoryItemArrayAdapter adapter;
//    private List<StoryItem> storyItemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);
        user_id = getIntent().getExtras().getString("user_id");

        Button activity_my_story_btn_write_new = findViewById(R.id.activity_my_story_btn_write_new);
        activity_my_story_btn_write_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteNewActivity.class);
                // send movie information to deatilActivity
                intent.putExtra("user_id",  user_id);
                startActivity(intent);
            }
        });

        activity_my_story_lst_story = findViewById(R.id.activity_my_story_lst_story);
        adapter = new StoryItemArrayAdapter(getApplicationContext(), R.layout.my_story_item);
        // Here, you set the data in your ListView
        activity_my_story_lst_story.setAdapter(adapter);
//        storyItemList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        authorRef = database.getReference("authorDetail/" + user_id);
        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("lstStory").getChildren()) {
                    String ten_truyen = ds.child("name").getValue(String.class);
                    String id_truyen = ds.child("id").getValue(String.class);
                    final StoryItem storyItem = new StoryItem();
                    storyItem.setStory_name(ten_truyen);
                    storyItem.setStory_id(id_truyen);
                    storyRef = database.getReference("storyDetail/" + id_truyen + "/generalInformation");
                    storyRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String img_link = dataSnapshot.child("imgLink").getValue(String.class);
                            storyItem.setImage_link(img_link);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    adapter.add(storyItem);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
