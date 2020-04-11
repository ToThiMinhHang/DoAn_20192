package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Fragment.HomeFragment;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.BookAdapter;
import com.hang.doan.readbooks.adapters.BookItemClickListener;
import com.hang.doan.readbooks.adapters.SliderPagerAdapter;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.models.BookID;

import java.util.ArrayList;
import java.util.List;

public class AuthorDetailActivity extends AppCompatActivity implements BookItemClickListener {
    final String TAG = "HANG_DEBUG";

    private BookAdapter bookAdapter;
    private RecyclerView listBook_RV;
    List<Book> lstBook;
    List<BookID> bookID = new ArrayList<>();

    DatabaseReference bookDetail;
    FirebaseDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_detail);


        lstBook = new ArrayList<>();
        bookAdapter = new BookAdapter(this, lstBook);
        listBook_RV = findViewById(R.id.author_Rv_movies);
        listBook_RV.setAdapter(bookAdapter);
        bookAdapter.setOnItemClickListener(AuthorDetailActivity.this);
        listBook_RV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Intent intent = getIntent();
        String id_tac_gia = intent.getExtras().getString("id_tac_gia");

        database = FirebaseDatabase.getInstance();
        DatabaseReference authorRef = database.getReference("authorDetail/" + id_tac_gia);

        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String authorName = dataSnapshot.child("authorName").getValue(String.class);
                TextView author_userName = findViewById(R.id.author_userName);
                author_userName.setText(authorName);

                for (DataSnapshot ds : dataSnapshot.child("lstStory").getChildren()) {

                    String id_tac_pham = ds.child("id").getValue(String.class);
                    bookID.add(new BookID(id_tac_pham));
                    if (!id_tac_pham.contains(".")) {
                        bookDetail = database.getReference("storyDetail/" + id_tac_pham);
                        bookDetail.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Book newBook = new Book();
                                newBook.setName(dataSnapshot.child("generalInformation").child("name").getValue(String.class));
                                newBook.setImageURL(dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class));
                                newBook.setId_tac_pham(dataSnapshot.child("generalInformation").child("storyID").getValue(String.class));

                                lstBook.add(newBook);
//                            Log.d(TAG, "new: " + newBook.getName());
                                bookAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBookClick(int position, int type) {
        Log.d(TAG, "id_tac_pham" + bookID.get(position).getId_tac_pham());

        Intent intent = new Intent(this, BookDetailActivity.class);
        // send movie information to deatilActivity
        intent.putExtra("id_tac_pham", bookID.get(position).getId_tac_pham());
        intent.putExtra("imgURL", lstBook.get(position).getImageURL());


        startActivity(intent);
    }
}
