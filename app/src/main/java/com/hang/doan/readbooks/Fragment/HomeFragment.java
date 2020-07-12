package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.BookAdapter;
import com.hang.doan.readbooks.adapters.BookItemClickListener;
import com.hang.doan.readbooks.adapters.SliderPagerAdapter;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.models.BookID;
import com.hang.doan.readbooks.models.Comment;
import com.hang.doan.readbooks.ui.BookDetailActivity;
import com.hang.doan.readbooks.ui.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements BookItemClickListener {

    private List<Book> lstSlide;

    private ViewPager slidePager;
    private final int MAX_SLIDE = 5;

    private BookAdapter bookFavoriteAdapter;
    private BookAdapter bookNewAdapter;
    private BookAdapter bookBuyAdapter;
    private BookAdapter bookReadAdapter;
    private TabLayout indicator;
    private RecyclerView BooksFavoriteRV;
    private RecyclerView BooksNewRV;
    private RecyclerView BooksbuyRV;
    private RecyclerView BooksReadRV;

    private Context ct;
    private static final String TAG = "HANG_DEBUG";
    List<Book> lstFavoriteBook;
    List<Book> lstNewBook;
    List<Book> lstBuyBook;
    List<Book> lstReadBook;


    List<BookID> slideID;
    List<BookID> newID;
    List<BookID> favoriteID;
    List<BookID> bookBuyID;
    List<BookID> bookReadID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_home, container, false);

        slideID = new ArrayList<>();
        newID = new ArrayList<>();
        favoriteID = new ArrayList<>();
        bookBuyID = new ArrayList<>();
        bookReadID = new ArrayList<>();

        indicator = view.findViewById(R.id.indicator);
        BooksFavoriteRV = view.findViewById(R.id.Rv_movies);
        BooksNewRV = view.findViewById(R.id.Rv_movies2);
        BooksbuyRV = view.findViewById(R.id.Rv_movies3);
        BooksReadRV = view.findViewById(R.id.Rv_movies4);

        ct = getContext();

        lstFavoriteBook = new ArrayList<>();
        lstNewBook = new ArrayList<>();
        lstSlide = new ArrayList<>();
        lstBuyBook = new ArrayList<>();
        lstReadBook = new ArrayList<>();


        slidePager = view.findViewById(R.id.slider_pager);
        final SliderPagerAdapter adapter = new SliderPagerAdapter(view.getContext(), lstSlide);
        slidePager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // new book
        bookNewAdapter = new BookAdapter(ct, lstNewBook);
        BooksNewRV.setAdapter(bookNewAdapter);
        BooksNewRV.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false));
        bookNewAdapter.setOnItemClickListener(HomeFragment.this);
        bookNewAdapter.notifyDataSetChanged();

        ///favorite
        bookFavoriteAdapter = new BookAdapter(ct, lstFavoriteBook);
        BooksFavoriteRV.setAdapter(bookFavoriteAdapter);
        BooksFavoriteRV.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false));
        bookFavoriteAdapter.setOnItemClickListener(HomeFragment.this);
        bookFavoriteAdapter.notifyDataSetChanged();

        ///bought
        bookBuyAdapter = new BookAdapter(ct, lstBuyBook);
        BooksbuyRV.setAdapter(bookBuyAdapter);
        BooksbuyRV.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false));
        bookBuyAdapter.setOnItemClickListener(HomeFragment.this);
        bookBuyAdapter.notifyDataSetChanged();

        ///read
        bookReadAdapter = new BookAdapter(ct, lstReadBook);
        BooksReadRV.setAdapter(bookReadAdapter);
        BooksReadRV.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false));
        bookReadAdapter.setOnItemClickListener(HomeFragment.this);
        bookReadAdapter.notifyDataSetChanged();


        BooksNewRV.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        BooksFavoriteRV.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        BooksReadRV.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        BooksbuyRV.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });




        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Slide = database.getReference("HomeScreen/Slide");
        DatabaseReference New = database.getReference("HomeScreen/New");
        DatabaseReference Favorite = database.getReference("HomeScreen/Favorite");
        String src = "authorDetail/" + FirebaseAuth.getInstance().getUid() + "/lstBuy";
        Log.d(TAG, "src onCreateView: " + src);
        DatabaseReference Buy = database.getReference(src);

        final DatabaseReference bookDetail = database.getReference("storyDetail");

        Slide.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String slide_id_tac_pham = ds.getValue(String.class);
                    slideID.add(new BookID(slide_id_tac_pham));
                    bookDetail.child(slide_id_tac_pham).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book newBook = new Book();
                            newBook.setId_tac_pham(dataSnapshot.child("generalInformation").child("storyID").getValue(String.class));
                            newBook.setName(dataSnapshot.child("generalInformation").child("name").getValue(String.class));
                            newBook.setImageURL(dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class));
                            newBook.setId_tac_gia(dataSnapshot.child("generalInformation").child("authorID").getValue(String.class));
                            lstSlide.add(newBook);
//                            Log.d(TAG, "slide: " + newBook.getName());
                            adapter.notifyDataSetChanged();
                            newBook.setId_tac_gia(dataSnapshot.child("generalInformation").child("authorID").getValue(String.class));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        New.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id_tac_pham = ds.getValue(String.class);
                    newID.add(new BookID(id_tac_pham));
                    bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book newBook = new Book();
                            newBook.setName(dataSnapshot.child("generalInformation").child("name").getValue(String.class));
                            newBook.setImageURL(dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class));
                            newBook.setId_tac_gia(dataSnapshot.child("generalInformation").child("authorID").getValue(String.class));

                            lstNewBook.add(newBook);
//                            Log.d(TAG, "new: " + newBook.getName());
                            bookNewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Favorite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id_tac_pham = ds.getValue(String.class);
                    favoriteID.add(new BookID(id_tac_pham));
                    bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book newBook = new Book();
                            newBook.setName(dataSnapshot.child("generalInformation").child("name").getValue(String.class));
                            newBook.setImageURL(dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class));
                            newBook.setId_tac_gia(dataSnapshot.child("generalInformation").child("authorID").getValue(String.class));
                            lstFavoriteBook.add(newBook);
//                            Log.d(TAG, "favorite: " + newBook.getName());
                            bookFavoriteAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //read_log
        if(HomeActivity.read_log != null) {
            try {

                for (int i = 0; i < HomeActivity.read_log.length(); i++) {
                    JSONObject object = null;

                    object = HomeActivity.read_log.getJSONObject(i);

                    String storyID = object.getString("storyID");
                    String imgLink = object.getString("imgLink");
                    String storyName = object.getString("storyName");

                    Book newBook = new Book();
                    newBook.setName(storyName);
                    newBook.setId_tac_pham(storyID);
                    newBook.setImageURL(imgLink);
                    lstReadBook.add(newBook);

                    bookReadAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        Buy.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String id_tac_pham = ds.getValue(String.class);
//                    Log.d(TAG, "Buy onDataChange: " + id_tac_pham);
//                    bookBuyID.add(new BookID(id_tac_pham));
//                    bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Book newBook = new Book();
//                            newBook.setName(dataSnapshot.child("generalInformation").child("name").getValue(String.class));
//                            newBook.setImageURL(dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class));
//                            newBook.setId_tac_gia(dataSnapshot.child("generalInformation").child("authorID").getValue(String.class));
//                            lstBuyBook.add(newBook);
////                            Log.d(TAG, "favorite: " + newBook.getName());
//                            bookBuyAdapter.notifyDataSetChanged();
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id_tac_pham = dataSnapshot.getKey();
                bookBuyID.add(new BookID(id_tac_pham));
                bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Book newBook = new Book();
                        newBook.setName(dataSnapshot.child("generalInformation").child("name").getValue(String.class));
                        newBook.setImageURL(dataSnapshot.child("generalInformation").child("imgLink").getValue(String.class));
                        newBook.setId_tac_gia(dataSnapshot.child("generalInformation").child("authorID").getValue(String.class));
                        lstBuyBook.add(newBook);
//                            Log.d(TAG, "favorite: " + newBook.getName());
                        bookBuyAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        Buy.addChildEventListener(childEventListener);


        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBookClick(int position, int type) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity

        Intent intent = new Intent(ct, BookDetailActivity.class);
        // send movie information to deatilActivity
        if (type == 0) {
            intent.putExtra("storyID", String.valueOf(favoriteID.get(position).getId_tac_pham()));
            intent.putExtra("user_", String.valueOf(favoriteID.get(position).getId_tac_pham()));
            //intent.putExtra("imgURL", lstFavoriteBook.get(position).getImageURL());
//            Log.d(TAG, "id_tac_pham: "+ favoriteID.get(position).getId_tac_pham());
        } else if (type == 1) {
            intent.putExtra("storyID", String.valueOf(newID.get(position).getId_tac_pham()));
            intent.putExtra("imgURL", lstNewBook.get(position).getImageURL());
//            Log.d(TAG, "id_tac_pham: "+ newID.get(position).getId_tac_pham());
        } else if (type == 2) {
            intent.putExtra("storyID", String.valueOf(bookBuyID.get(position).getId_tac_pham()));
            intent.putExtra("imgURL", lstBuyBook.get(position).getImageURL());
//            Log.d(TAG, "id_tac_pham: "+ newID.get(position).getId_tac_pham());
        }
        else if (type == 3) {
            intent.putExtra("storyID", String.valueOf(bookReadID.get(position).getId_tac_pham()));
            intent.putExtra("imgURL", lstReadBook.get(position).getImageURL());
//            Log.d(TAG, "id_tac_pham: "+ newID.get(position).getId_tac_pham());
        }

        startActivity(intent);

    }

//    class SliderTimer extends TimerTask {
//
//        @Override
//        public void run() {
//
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (slidePager.getChildCount() > 0) {
//                        if (slidePager.getCurrentItem() < MAX_SLIDE) {
//                            slidePager.setCurrentItem(slidePager.getCurrentItem() + 1);
//                        } else
//                            slidePager.setCurrentItem(0);
//                    }
//                }
//            });
//        }
//    }

}