package com.hang.doan.readbooks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hang.doan.readbooks.OnItemClickListener;
import com.hang.doan.readbooks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickItemAdapter extends RecyclerView.Adapter<PickItemAdapter.ItemViewHolder> {

    private Context context;
    private List<String> items;

    @Nullable
    private OnItemClickListener onItemClickListener;

    public PickItemAdapter(@NonNull Context context, @NonNull List<String> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pick, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void onItemClick(int i) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(i);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvItem)
        TextView tvItem;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int i) {
            tvItem.setText(items.get(i));
            itemView.setOnClickListener(v -> onItemClick(i));
        }
    }
}
