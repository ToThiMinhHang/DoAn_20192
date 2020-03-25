package com.hang.doan.readbooks.adapters;

import android.graphics.Movie;
import android.widget.ImageView;

import com.hang.doan.readbooks.models.Book;

public interface BookItemClickListener {

    void onBookClick(int position, int type); // we will need the imageview to make the shared animation between the two activity


}
