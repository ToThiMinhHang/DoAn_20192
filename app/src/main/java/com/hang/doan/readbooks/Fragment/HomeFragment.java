package com.hang.doan.readbooks.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.hang.doan.readbooks.ui.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements BookItemClickListener {

    private List<Book> lstSlide;
    private ViewPager slidePager;
    private final int MAX_SLIDE = 5;

    private BookAdapter bookAdapter;
    private TabLayout indicator;
    private RecyclerView MoviesRV;

    private Context ct;
    private static final String TAG = "HANG_DEBUG";
    List<Book> lstBook;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_home, container, false);

        indicator = view.findViewById(R.id.indicator);
        MoviesRV = view.findViewById(R.id.Rv_movies);

        ct = getContext();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("storyDetail");

        lstBook = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List <String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Book newBook = new Book();
                    newBook.setName(ds.child("data").child("name").getValue(String.class));
                    newBook.setAuthorLink(ds.child("data").child("authorLink").getValue(String.class));
                    newBook.setImageURL(ds.child("data").child("imgLink").getValue(String.class));
                    newBook.setLink(ds.child("data").child("link").getValue(String.class));

                    lstBook.add(newBook);
                }

                bookAdapter = new BookAdapter(ct,lstBook);

                MoviesRV.setAdapter(bookAdapter);
                MoviesRV.setLayoutManager(new LinearLayoutManager(ct,LinearLayoutManager.HORIZONTAL,false));

                bookAdapter.setOnItemClickListener(HomeFragment.this);


                lstSlide = new ArrayList<>();
                for(int i = 0; i < MAX_SLIDE; i++)
                    lstSlide.add(new Book(lstBook.get(i).getName(), lstBook.get(i).getImageURL(), lstBook.get(i).getAuthorLink(), null, lstBook.get(i).getLink()));
                slidePager = view.findViewById(R.id.slider_pager);
                SliderPagerAdapter adapter = new SliderPagerAdapter(view.getContext(), lstSlide);
                slidePager.setAdapter(adapter);

//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new HomeFragment.SliderTimer(), 4000, 6000);
//


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        myRef.addListenerForSingleValueEvent(eventListener);



        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBookClick(int position) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity

        Intent intent = new Intent(ct, BookDetailActivity.class);
        // send movie information to deatilActivity
        intent.putExtra("name",lstBook.get(position).getName());
        intent.putExtra("authorLink",lstBook.get(position).getAuthorLink());
        intent.putExtra("link",lstBook.get(position).getLink());
        intent.putExtra("imgURL",lstBook.get(position).getImageURL());
        startActivity(intent);

        // i l make a simple test to see if the click works

        // it works great

    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(slidePager.getChildCount() > 0) {
                        if (slidePager.getCurrentItem() < MAX_SLIDE) {
                            slidePager.setCurrentItem(slidePager.getCurrentItem() + 1);
                        } else
                            slidePager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}