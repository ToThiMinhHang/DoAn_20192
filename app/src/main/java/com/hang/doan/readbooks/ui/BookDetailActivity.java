package com.hang.doan.readbooks.ui;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hang.doan.readbooks.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView BookImg;
    private TextView txt_author,txt_name;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_item);
        // ini views
        iniViews();

    }

    void iniViews() {
        String author = getIntent().getExtras().getString("authorLink");
        txt_author = findViewById(R.id.txt_book_detail_author);
        txt_author.setText(author);

        String imageResourceURL = getIntent().getExtras().getString("imgURL");

        BookImg = findViewById(R.id.img_book_detail);
        Picasso.with(context)
                .load(imageResourceURL)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(BookImg);

        String name = getIntent().getExtras().getString("name");
        txt_name = findViewById(R.id.txt_book_detail_name);
        txt_name.setText(name);

    }


}
