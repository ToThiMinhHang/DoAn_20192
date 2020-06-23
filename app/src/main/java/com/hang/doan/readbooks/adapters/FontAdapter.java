package com.hang.doan.readbooks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hang.doan.readbooks.OnItemClickListener;
import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Font;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    private Context context;
    private List<Font> fonts;

    private OnItemClickListener onItemClickListener;

    public FontAdapter(Context context, List<Font> fonts) {
        this.context = context;
        this.fonts = fonts;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_font, parent, false);
        return new FontViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return fonts.size();
    }

    class FontViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvFont)
        TextView tvFont;

        FontViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int i) {
            Font font = fonts.get(i);
            tvFont.setTypeface(font.getRegular(context));
            tvFont.setText(font.getName());
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(i);
                }
            });
        }
    }
}
