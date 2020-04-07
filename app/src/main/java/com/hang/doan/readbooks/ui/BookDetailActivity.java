package com.hang.doan.readbooks.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Fragment.HomeFragment;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.BookAdapter;
import com.hang.doan.readbooks.adapters.SliderPagerAdapter;
import com.hang.doan.readbooks.models.Author;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.models.Chapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class BookDetailActivity extends AppCompatActivity {

    final String TAG = "HANG_DEBUG";
    private ImageView BookImg;
    private TextView txt_author,txt_name;
    private Context context;
    String storyName;
    String authorName;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("authorDetail");

    DatabaseReference dsChuong = database.getReference("storyDetail");



    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> arrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // ini views
        String imageResourceURL = getIntent().getExtras().getString("imgURL");

        BookImg = findViewById(R.id.img_book_detail);
        Picasso.with(context)
                .load(imageResourceURL)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(BookImg);

        String name = getIntent().getExtras().getString("name");
        txt_name = findViewById(R.id.book_detail_tentruyen);
        txt_name.setText(name);
        storyName = name;



        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Author author_temp = snapshot.getValue(Author.class);
//                    Log.d("HANG_DEBUG",author_temp.getAuthorName());
                    for (int i = 0; i < author_temp.getLstStory().size(); i++) {        // listHS.size() là lấy ra kích cỡ của listHS
                        if(author_temp.getLstStory().get(i).trim().equals(storyName.trim())) {
                            authorName = author_temp.getAuthorName();

                            txt_author = findViewById(R.id.book_detail_tacgia);
                            txt_author.setText(authorName);

                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




        list = (ListView) findViewById(R.id.book_detail_lst_dschuong);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);


        // Read from the database
        dsChuong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d(TAG, snapshot.child("generaInformation").child("name").toString());
                    if(snapshot.child("generaInformation").child("name").getValue(String.class).trim().equals(storyName.trim())) {
                        GenericTypeIndicator<List<Chapter>> t = new GenericTypeIndicator<List<Chapter>>() {};
                        Log.d(TAG, storyName.trim());
                        List<Chapter> chapters = snapshot.child("chapters").getValue(t);
                        for(int i = 0; i < chapters.size(); i++) {
                            arrayList.add(chapters.get(i).getChapterName());
                            Log.d(TAG, arrayList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        return;
                    }
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
