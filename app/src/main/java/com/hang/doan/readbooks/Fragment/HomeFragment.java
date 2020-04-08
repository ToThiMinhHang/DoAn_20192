package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.adapters.BookAdapter;
import com.hang.doan.readbooks.adapters.BookItemClickListener;
import com.hang.doan.readbooks.adapters.SliderPagerAdapter;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.models.BookID;
import com.hang.doan.readbooks.ui.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements BookItemClickListener {

    private List<Book> lstSlide;
    private ViewPager slidePager;
    private final int MAX_SLIDE = 5;

    private BookAdapter bookFavoriteAdapter;
    private BookAdapter bookNewAdapter;
    private TabLayout indicator;
    private RecyclerView BooksFavoriteRV;
    private RecyclerView BooksNewRV;

    private Context ct;
    private static final String TAG = "HANG_DEBUG";
    List<Book> lstFavoriteBook;
    List<Book> lstNewBook;


    List<BookID> slideID;
    List<BookID> newID;
    List<BookID> favoriteID;

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

        indicator = view.findViewById(R.id.indicator);
        BooksFavoriteRV = view.findViewById(R.id.Rv_movies);
        BooksNewRV = view.findViewById(R.id.Rv_movies2);

        ct = getContext();

        lstFavoriteBook = new ArrayList<>();
        lstNewBook = new ArrayList<>();
        lstSlide = new ArrayList<>();


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


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Slide = database.getReference("HomeScreen/slide");
        DatabaseReference New = database.getReference("HomeScreen/New");
        DatabaseReference Favorite = database.getReference("HomeScreen/Favorite");

        final DatabaseReference bookDetail = database.getReference("storyDetail");


        Slide.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Long id_tac_gia = ds.child("id_tac_gia").getValue(Long.class);
                    Long id_tac_pham = ds.child("id_tac_pham").getValue(Long.class);
                    slideID.add(new BookID(id_tac_gia, id_tac_pham));
                    bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book newBook = new Book();
                            newBook.setName(dataSnapshot.child("generaInformation").child("name").getValue(String.class));
                            newBook.setImageURL(dataSnapshot.child("generaInformation").child("imgLink").getValue(String.class));
                            lstSlide.add(newBook);
//                            Log.d(TAG, "slide: " + newBook.getName());
                            adapter.notifyDataSetChanged();

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
                    Long id_tac_gia = ds.child("id_tac_gia").getValue(Long.class);
                    Long id_tac_pham = ds.child("id_tac_pham").getValue(Long.class);
                    newID.add(new BookID(id_tac_gia, id_tac_pham));
                    bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book newBook = new Book();
                            newBook.setName(dataSnapshot.child("generaInformation").child("name").getValue(String.class));
                            newBook.setImageURL(dataSnapshot.child("generaInformation").child("imgLink").getValue(String.class));

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
                    Long id_tac_gia = ds.child("id_tac_gia").getValue(Long.class);
                    Long id_tac_pham = ds.child("id_tac_pham").getValue(Long.class);
                    favoriteID.add(new BookID(id_tac_gia, id_tac_pham));
                    bookDetail.child(String.valueOf(id_tac_pham)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book newBook = new Book();
                            newBook.setName(dataSnapshot.child("generaInformation").child("name").getValue(String.class));
                            newBook.setImageURL(dataSnapshot.child("generaInformation").child("imgLink").getValue(String.class));

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
            intent.putExtra("id_tac_pham", String.valueOf(favoriteID.get(position).getId_tac_pham()));
            intent.putExtra("id_tac_gia", String.valueOf(favoriteID.get(position).getId_tac_gia()));
            intent.putExtra("imgURL", lstFavoriteBook.get(position).getImageURL());
//            Log.d(TAG, "id_tac_pham: "+ favoriteID.get(position).getId_tac_pham());
        } else {
            intent.putExtra("id_tac_pham", String.valueOf(newID.get(position).getId_tac_pham()));
            intent.putExtra("id_tac_gia", String.valueOf(newID.get(position).getId_tac_gia()));
            intent.putExtra("imgURL", lstNewBook.get(position).getImageURL());
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