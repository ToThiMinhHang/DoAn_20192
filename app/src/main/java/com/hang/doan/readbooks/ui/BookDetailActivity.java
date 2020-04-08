package com.hang.doan.readbooks.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private TextView txt_author, txt_name;

    private Context context;


    String storyName;
    String authorName;


    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> arrayList;

    private int index = 0;


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

        Intent intent = getIntent();
        final String id_tac_pham = intent.getExtras().getString("id_tac_pham");
        String id_tac_gia = intent.getExtras().getString("id_tac_gia");
        DatabaseReference authorRef = database.getReference("authorDetail/" + id_tac_gia);
        DatabaseReference bookRef = database.getReference("storyDetail/" + id_tac_pham);

        txt_author = findViewById(R.id.book_detail_tacgia);
        txt_name = findViewById(R.id.book_detail_tentruyen);


        // Read from the database
        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                authorName = dataSnapshot.child("authorName").getValue(String.class);
                txt_author.setText(authorName);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storyName = dataSnapshot.child("generaInformation").child("name").getValue(String.class);
                txt_name.setText(storyName);

                for (DataSnapshot snapshot : dataSnapshot.child("chapters").getChildren()) {
                    Chapter chapter = snapshot.getValue(Chapter.class);
                    index = index + 1;
                    arrayList.add(chapter.getChapterName());
                    adapter.notifyDataSetChanged();
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


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(BookDetailActivity.this, ReadBook.class);
                int pos = parent.getPositionForView(view);
                intent.putExtra("INDEX", String.valueOf(pos));
                intent.putExtra("id_tac_pham", id_tac_pham);
                startActivity(intent);
            }
        });

    }


}
