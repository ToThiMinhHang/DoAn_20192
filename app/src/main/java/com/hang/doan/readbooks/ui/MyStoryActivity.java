package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.models.BookID;

import java.util.ArrayList;

public class MyStoryActivity extends AppCompatActivity {

    String user_id;
    FirebaseDatabase database;
    DatabaseReference authorRef;

    ListView activity_my_story_lst_story;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

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
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        // Here, you set the data in your ListView
        activity_my_story_lst_story.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        authorRef = database.getReference("authorDetail/" + user_id);
        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("lstStory").getChildren()) {

                    String ten_tac_pham = ds.child("name").getValue(String.class);
                    arrayList.add(ten_tac_pham);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
