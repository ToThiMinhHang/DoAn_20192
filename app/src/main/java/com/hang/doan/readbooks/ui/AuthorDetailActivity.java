package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.Fragment.AccountFragment;
import com.hang.doan.readbooks.Fragment.HomeFragment;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.BookAdapter;
import com.hang.doan.readbooks.adapters.BookItemClickListener;
import com.hang.doan.readbooks.adapters.SliderPagerAdapter;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.models.BookID;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AuthorDetailActivity extends AppCompatActivity implements BookItemClickListener {
    final String TAG = "HANG_DEBUG";

    private BookAdapter bookAdapter;
    private RecyclerView listBook_RV;
    List<Book> lstBook;
    List<BookID> bookID = new ArrayList<>();

    DatabaseReference bookDetail;
    DatabaseReference followers;
    FirebaseDatabase database;

    Button btn_author_follow;
    String id_tac_gia;

    String NOTIMSG_URL = "https://us-central1-doan20192-33247.cloudfunctions.net/addNoti";
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
        id_tac_gia = intent.getExtras().getString("id_tac_gia");

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

        //ghi vào firebase  user1 be followed by user 2
        btn_author_follow = findViewById(R.id.btn_author_follow);
        btn_author_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followers = database.getReference("followers");
                followers.child(id_tac_gia).child(AccountFragment.userID).setValue("true");

                postNoti();
            }
        });


    }

    // Post Request For JSONObject for Notifications
    public void postNoti() {
        Long tsLong = System.currentTimeMillis()/1000;

        String message = "HANG " + "đã theo dõi bạn";
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("fromUserID", FirebaseAuth.getInstance().getUid());
            object.put("message", message);
            object.put("resiveUserID", id_tac_gia);
            object.put("timestamp", tsLong);
            object.put("type", "follow");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "postNoti: " + object.toString());
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NOTIMSG_URL, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+ response.toString());

//                        resultTextView.setText("String Response : "+ response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
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
