package com.hang.doan.readbooks.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.CommentArrayAdapter;
import com.hang.doan.readbooks.models.Chapter;
import com.hang.doan.readbooks.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BookDetailActivity extends AppCompatActivity {

    final String TAG = "HANG_DEBUG";
    private ImageView BookImg;
    private TextView txt_author, txt_name;

    private Context context;


    String storyName;
    String authorID;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> arrayList;

    private int index = 0;


    View viewNewPost;
    TextView user_name;
    EditText user_edit;
    ImageButton btn_comment;



    CommentArrayAdapter commnetAdapter;
    ListView listView;
    List<Comment> comments;

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

        DatabaseReference bookRef = database.getReference("storyDetail/" + id_tac_pham);

        txt_author = findViewById(R.id.book_detail_tacgia);
        txt_name = findViewById(R.id.book_detail_tentruyen);



        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storyName = dataSnapshot.child("generalInformation").child("name").getValue(String.class);
                txt_name.setText(storyName);
                authorID = dataSnapshot.child("generalInformation").child("authorID").getValue(String.class);

                for (DataSnapshot snapshot : dataSnapshot.child("chapters").getChildren()) {
                    Chapter chapter = snapshot.getValue(Chapter.class);
                    index = index + 1;
                    arrayList.add(chapter.getChapterName());
                    adapter.notifyDataSetChanged();
                }

                DatabaseReference authorRef = database.getReference("authorDetail/" + authorID);

                // Read from the database
                authorRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String authorName = dataSnapshot.child("authorName").getValue(String.class);
                        txt_author.setText(authorName);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

                txt_author.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BookDetailActivity.this, AuthorDetailActivity.class);
                        intent.putExtra("id_tac_gia", authorID);
                        startActivity(intent);
                    }
                });
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


        //Post new comment here
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        viewNewPost = findViewById(R.id.new_comment);
        user_name = (TextView) viewNewPost.findViewById(R.id.comment_new_user_Name);
        user_edit = (EditText) viewNewPost.findViewById(R.id.comment_new_user_message);
        user_name.setText(currentUser.getDisplayName());
        ImageView user_img = viewNewPost.findViewById(R.id.comment_new_user_img);
        Picasso.with(this)
                .load(currentUser.getPhotoUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(user_img);
        btn_comment = viewNewPost.findViewById(R.id.comment_new_btn_post);
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Comment cmt = new Comment();
                cmt.setComment_user_Name(currentUser.getDisplayName());
                cmt.setComment_user_message(user_edit.getText().toString());
                cmt.setComment_user_img(currentUser.getPhotoUrl().toString());
                FirebaseDatabase databasePost = FirebaseDatabase.getInstance();
                DatabaseReference comment_ref = databasePost.getReference("comment/" + id_tac_pham);
                String key = comment_ref.push().getKey();
                comment_ref.child(key).setValue(cmt);

                user_edit.setText("");
            }
        });



        //display comment here
        listView = (ListView) findViewById(R.id.comment);
        commnetAdapter = new CommentArrayAdapter(getApplicationContext(), R.layout.activity_comment);
        listView.setAdapter(commnetAdapter);

        FirebaseDatabase databasePost = FirebaseDatabase.getInstance();
        DatabaseReference comment_ref = databasePost.getReference("comment/" + id_tac_pham);
        comments = new ArrayList<>();
        comment_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);

                    comments.add(comment);
                    commnetAdapter.add(comment);
                    commnetAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
