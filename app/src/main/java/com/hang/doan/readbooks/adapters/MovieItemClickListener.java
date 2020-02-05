package com.hang.doan.readbooks.adapters;

import android.widget.ImageView;

import com.hang.doan.readbooks.models.Movie;

public interface MovieItemClickListener {

    void onMovieClick(Movie movie, ImageView movieImageView); // we will need the imageview to make the shared animation between the two activity

}
