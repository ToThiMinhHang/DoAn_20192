package com.hang.doan.readbooks.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ImageViewHolder> {

    Context context ;
    List<Book> mData;
    BookItemClickListener bookItemClickListener;

    private final String TAG = "HANG_DEBUG";

    public BookAdapter(Context context, List<Book> mData) {
        this.context = context;
        this.mData = mData;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.rc_book_item,viewGroup,false);
        return new ImageViewHolder(view);

        }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder myViewHolder, int i) {

        Book movieCurrent = mData.get(i);

        myViewHolder.TvTitle.setText(movieCurrent.getName());
//        Log.d(TAG, "url: " + movieCurrent.getImageURL());

        Picasso.with(context)
                .load(movieCurrent.getImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(myViewHolder.ImgMovie);

    }

    public void setOnItemClickListener(BookItemClickListener listener) {
        bookItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView TvTitle;
        private ImageView ImgMovie;


        public ImageViewHolder(@NonNull View itemView) {

            super(itemView);
            TvTitle = itemView.findViewById(R.id.item_movie_title);
            ImgMovie = itemView.findViewById(R.id.item_movie_img);

            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            if (bookItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    View view = (View) v.getParent();
                    int ret = view.getId();
                    Log.d(TAG, String.valueOf(ret));
                    if (ret == R.id.Rv_movies)
                        bookItemClickListener.onBookClick(position, 0);
                    else
                        bookItemClickListener.onBookClick(position, 1);
                }
            }
        }

    }
}
