package com.hang.doan.readbooks.adapters;

import android.graphics.Movie;
import android.widget.ImageView;

import com.hang.doan.readbooks.models.Book;

public interface BookItemClickListener {

    void onBookClick(Book book, ImageView movieImageView); // we will need the imageview to make the shared animation between the two activity

}
