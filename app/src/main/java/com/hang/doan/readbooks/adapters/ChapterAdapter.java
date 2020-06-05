package com.hang.doan.readbooks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Chapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private Context context;
    private List<Chapter> chapters;

    public ChapterAdapter(Context context, List<Chapter> chapters) {
        this.context = context;
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    class ChapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvChapterName)
        TextView tvName;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int i) {
            Chapter chapter = chapters.get(i);
            String text = (i + 1) + ".  " + chapter.getChapterName();
            tvName.setText(text);
        }
    }
}
