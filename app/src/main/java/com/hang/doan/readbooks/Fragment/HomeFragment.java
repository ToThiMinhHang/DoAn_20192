package com.hang.doan.readbooks.Fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.hang.doan.readbooks.adapters.MovieAdapter;
import com.hang.doan.readbooks.adapters.MovieItemClickListener;
import com.hang.doan.readbooks.adapters.SliderPagerAdapter;
import com.hang.doan.readbooks.models.Movie;
import com.hang.doan.readbooks.models.Slide;
import com.hang.doan.readbooks.models.StoryDetail;
import com.hang.doan.readbooks.ui.HomeActivity;
import com.hang.doan.readbooks.ui.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements MovieItemClickListener {

    private List<Slide> lstSlides;
    private List<Movie> lstBooks;
    private MovieAdapter movieAdapter;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV;
    private EditText editTimKiem;

    private Context ct;
    private static final String TAG = "HANG_DEBUG";
    List<StoryDetail> lstStoryDetail;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("storyDetail");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        sliderpager = view.findViewById(R.id.slider_pager) ;
        indicator = view.findViewById(R.id.indicator);
        MoviesRV = view.findViewById(R.id.Rv_movies);

        ct = getContext();

        // prepare a list of slides ..
        lstSlides = new ArrayList<>() ;
        lstSlides.add(new Slide(R.drawable.slide1,"Slide Title \nmore text here"));
        lstSlides.add(new Slide(R.drawable.slide2,"Slide Title \nmore text here"));
        lstSlides.add(new Slide(R.drawable.slide1,"Slide Title \nmore text here"));
        lstSlides.add(new Slide(R.drawable.slide2,"Slide Title \nmore text here"));
        SliderPagerAdapter adapter = new SliderPagerAdapter(ct,lstSlides);
        sliderpager.setAdapter(adapter);
        // setup timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderpager,true);

        // Recyclerview Setup
        // ini data

        lstBooks  = new ArrayList<>();
//        lstBooks .add(new Movie("Moana",R.drawable.moana,R.drawable.spidercover));
//        lstBooks .add(new Movie("Black P",R.drawable.blackp,R.drawable.spidercover));
//        lstBooks .add(new Movie("The Martian",R.drawable.themartian));
//        lstBooks .add(new Movie("The Martian",R.drawable.themartian));
//        lstBooks .add(new Movie("The Martian",R.drawable.themartian));
//        lstBooks .add(new Movie("The Martian",R.drawable.themartian));

        movieAdapter = new MovieAdapter(ct,lstBooks ,this);
        MoviesRV.setAdapter(movieAdapter);
        MoviesRV.setLayoutManager(new LinearLayoutManager(ct,LinearLayoutManager.HORIZONTAL,false));

        //my code
        downloadDataFireBase();
        movieAdapter.notifyDataSetChanged();

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        // here we send movie information to detail activity
        // also we ll create the transition animation between the two activity

        Intent intent = new Intent(ct, MovieDetailActivity.class);
        // send movie information to deatilActivity
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("imgURL",movie.getThumbnail());
        intent.putExtra("imgCover",movie.getCoverPhoto());
        // lets crezte the animation
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                movieImageView,"sharedName");

        startActivity(intent,options.toBundle());

        // i l make a simple test to see if the click works

        Toast.makeText(ct,"item clicked : " + movie.getTitle(),Toast.LENGTH_LONG).show();
        // it works great


    }

    private void addNewItemsToRecyclerview(StoryDetail storyDetail) {
        lstBooks .add(new Movie(storyDetail.getName(),R.drawable.moana,R.drawable.spidercover));
    }

    private void downloadDataFireBase() {
//        lstStoryDetail = new ArrayList<>();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String storyName = snapshot.child("storyName").getValue(String.class);
                    String authorLink = snapshot.child("data").child("authorLink").getValue(String.class);
                    String link = snapshot.child("data").child("link").getValue(String.class);
                    //TODO

                    StoryDetail storyDetail = new StoryDetail(storyName, link, authorLink, null);
                    addNewItemsToRecyclerview(storyDetail);
//                    lstStoryDetail.add(storyDetail);

                    //lstBooks.add(new Movie(storyName, R.drawable.themartian));

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }




    class SliderTimer extends TimerTask {

        @Override
        public void run() {

//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (sliderpager.getCurrentItem()<lstSlides.size()-1) {
//                        sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1);
//                    }
//                    else
//                        sliderpager.setCurrentItem(0);
//                }
//            });


        }
    }

}