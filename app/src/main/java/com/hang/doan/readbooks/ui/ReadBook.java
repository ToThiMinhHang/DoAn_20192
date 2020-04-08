package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.R;

public class ReadBook extends AppCompatActivity {
    final String TAG = "HANG_DEBUG";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("storyDetail");

    String id;
    String id_tac_pham;

    String data;
    String chapterName;

    TextView read_book_chapter_name;
    TextView read_book_data;
    ImageButton read_book_btn_back;
    ImageButton read_book_btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_item);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        id = (String) bundle.get("INDEX");
        id_tac_pham = (String) bundle.get("id_tac_pham");

        read_book_chapter_name = findViewById(R.id.read_book_chapter_name);
        read_book_data = findViewById(R.id.read_book_data);

        read_book_btn_back = findViewById(R.id.read_book_btn_back);
        read_book_btn_next = findViewById(R.id.read_book_btn_next);


        reloadData();

        read_book_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(id) > 0) {
                    int id_new = Integer.parseInt(id) - 1;
                    id = String.valueOf(id_new);
                    reloadData();
                }
            }
        });

        read_book_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_new = Integer.parseInt(id) + 1;
                id = String.valueOf(id_new);
                reloadData();
            }
        });


    }

    private void reloadData() {
        if (Integer.parseInt(id) <= 0) {
            read_book_btn_back.setVisibility(View.INVISIBLE);
        }
        else {
            read_book_btn_back.setVisibility(View.VISIBLE);
        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.child(id_tac_pham).child("chapters").child(id).child("data").getValue(String.class);
                chapterName = dataSnapshot.child(id_tac_pham).child("chapters").child(id).child("chapterName").getValue(String.class);

                if (data != null && chapterName != null) {
                    read_book_chapter_name.setText(chapterName);
                    read_book_data.setText(data);
                } else {
                    read_book_chapter_name.setText("end chap");
                    read_book_data.setText("Hết rồi");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
