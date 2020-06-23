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
import com.hang.doan.readbooks.models.Notification;
import com.hang.doan.readbooks.utils.GlideApp;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.noti_imgAvatar)
        ImageView imgAvatar;

        @BindView(R.id.tvMessage)
        TextView tvMessage;

        @BindView(R.id.tvTime)
        TextView tvTime;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int i) {
            Notification notification = notifications.get(i);
            //GlideApp.with(context).load(notification.getFromUserImageURL()).into(imgAvatar);
            tvMessage.setText(notification.getMessage());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(notification.getTimestamp());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.US);
            //tvTime.setText(sdf.format(calendar.getTime()));
            Log.d("HANG_DEBUG", "bind: " +notification.getFromUserImageURL());
            Picasso.with(context)
                    .load(notification.getFromUserImageURL())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(imgAvatar);
        }
    }
}
