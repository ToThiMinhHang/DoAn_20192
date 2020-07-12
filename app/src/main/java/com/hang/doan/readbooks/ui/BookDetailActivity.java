package com.hang.doan.readbooks.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Fragment.AccountFragment;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.CommentArrayAdapter;
import com.hang.doan.readbooks.models.Chapter;
import com.hang.doan.readbooks.models.Comment;
import com.hang.doan.readbooks.models.StoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BookDetailActivity extends AppCompatActivity {

    final String TAG = "HANG_DEBUG";
    private ImageView BookImg;
    private TextView txt_author, txt_name;

    private Context context;

    String id_tac_pham;

    String storyName;
    String authorID;
    String imageResourceURL;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    List<Chapter> chapters = new ArrayList<>();

    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> arrayList;
    TextView book_detail_tinhtrang;

    private int index = 0;


    View viewNewPost;
    TextView user_name;
    EditText user_edit;
    ImageButton btn_comment;
    TextView book_detail_mucsach;

    CommentArrayAdapter commnetAdapter;
    ListView listView;
    List<Comment> comments;

    List<Integer> chapterIDbuyed = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // ini views

        Intent intent = getIntent();
        id_tac_pham = intent.getExtras().getString("storyID");


        DatabaseReference bookRef = database.getReference("storyDetail/" + id_tac_pham);

        txt_author = findViewById(R.id.book_detail_tacgia);
        txt_name = findViewById(R.id.book_detail_tentruyen);
        book_detail_tinhtrang = findViewById(R.id.book_detail_tinhtrang);
        book_detail_mucsach = findViewById(R.id.book_detail_mucsach);

        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storyName = dataSnapshot.child("generalInformation").child("name").getValue(String.class);
                txt_name.setText(storyName);
                imageResourceURL = dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class);
                authorID = dataSnapshot.child("generalInformation").child("authorID").getValue(String.class);
                String status =  dataSnapshot.child("generalInformation").child("status").getValue(String.class);
                book_detail_tinhtrang.setText(status);

                for (DataSnapshot snapshot : dataSnapshot.child("chapters").getChildren()) {
                    Chapter chapter = snapshot.getValue(Chapter.class);
                    chapters.add(chapter);
                    index = index + 1;
                    arrayList.add(chapter.getChapterName());
                    adapter.notifyDataSetChanged();
                }

                for (DataSnapshot snapshot : dataSnapshot.child("generalInformation").child("mucsach").getChildren()) {
                    String mucsach = snapshot.getValue(String.class);
                    book_detail_mucsach.setText(book_detail_mucsach.getText().toString()  + mucsach+ "\n");
                }

                DatabaseReference authorRef = database.getReference("authorDetail/" + authorID);

                BookImg = findViewById(R.id.img_book_detail);
                Picasso.with(context)
                        .load(imageResourceURL)
                        .placeholder(R.drawable.ic_placeholder)
                        .fit()
                        .centerCrop()
                        .into(BookImg);

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

        list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //if (chapterIDbuyed.contains(position)) {

                    Intent intent = new Intent(BookDetailActivity.this, ReadBook.class);
                    int pos = parent.getPositionForView(view);
                    intent.putExtra("INDEX", String.valueOf(pos));
                    intent.putExtra("storyID", id_tac_pham);
                    startActivity(intent);
//                }
//                else {
////                    Toast.makeText(getApplicationContext(), "Mua truyen di may", Toast.LENGTH_SHORT).show();
//                    Bundle data = new Bundle();
//
//                    Intent intent = new Intent(BookDetailActivity.this, PaymentActivity.class);
//                    data.putString("storyID", storyID);
//                    data.putString("storyName", storyName);
//                    data.putString("userID", AccountFragment.userID);
//                    data.putInt("storyChapter", position);
//                    data.putString("storyChapterName", chapters.get(position).getChapterName());
//                    data.putInt("storyChapterPrice", Integer.parseInt(chapters.get(position).getPrice()));
//                    //Log.d(TAG, "storyChapterPrice: " + chapters.get(position).getPrice());
//                    intent.putExtras(data);
//
//                    startActivity(intent);
//                }
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

        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        FirebaseDatabase databasePost = FirebaseDatabase.getInstance();
        DatabaseReference comment_ref = databasePost.getReference("comment/" + id_tac_pham);
        comments = new ArrayList<>();
//        comment_ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Comment comment = snapshot.getValue(Comment.class);
//
//                    comments.add(comment);
//                    commnetAdapter.add(comment);
//                    commnetAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//
//        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    comments.add(comment);
                    commnetAdapter.add(comment);
                    commnetAdapter.notifyDataSetChanged();
                //}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        comment_ref.addChildEventListener(childEventListener);


//        buy = findViewById(R.id.btn_momo);
//        buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle data = new Bundle();
//                String KEY_ENVIRONMENT = "key_environment";
//                int environment = 1;//developer default - Production environment = 2
//
//                Intent intent = new Intent(BookDetailActivity.this, PaymentActivity.class);
//                data.putInt(KEY_ENVIRONMENT, 1);
//                data.putString("storyID", storyID);
//                data.putString("authorID", authorID);
//                data.putInt("storyChapter", 0);
//                intent.putExtras(data);
//
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
        super.onBackPressed();

    }


}
