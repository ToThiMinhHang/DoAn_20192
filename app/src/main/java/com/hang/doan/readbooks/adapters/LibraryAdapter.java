package com.hang.doan.readbooks.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.utils.GlideApp;
import com.hang.doan.readbooks.utils.Metrics;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.StoryViewHolder> {

    private Context context;
    private List<Book> books;

    private String keyword = "";
    private List<Book> filtered = new ArrayList<>();

    public LibraryAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
        Collections.copy(filtered, books);
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story_library, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public void updateUI() {
        filter(keyword);
    }

    public void filter(String keyword) {
        this.keyword = keyword;
        filtered.clear();
        for (Book book : books) {
            if (book.search(keyword)) {
                filtered.add(book);
            }
        }

        super.notifyDataSetChanged();
    }

    class StoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvAuthor)
        TextView tvAuthor;
        @BindView(R.id.imgStory)
        ImageView imgStory;

        StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int i) {
            setMargin(i);
            Book book = filtered.get(i);
            tvTitle.setText(book.getName());
            tvAuthor.setText(book.getAuthorName());

            //GlideApp.with(context).load(book.getImageURL()).centerCrop().error(R.color.colorAccent).into(imgStory);
            //Log.d("HANG_DEBUG", "bind: " +book.getImageURL());
            Picasso.with(context)
                    .load(book.getImageURL())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(imgStory);
        }

        private void setMargin(int i) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int space = Metrics.dp(context, 15);
            if (i % 2 == 0) {
                params.setMarginStart(space);
                params.setMarginEnd(Metrics.dp(context, 7));
            } else {
                params.setMarginEnd(space);
                params.setMarginStart(Metrics.dp(context, 7));
            }
            itemView.setLayoutParams(params);
        }
    }
}
