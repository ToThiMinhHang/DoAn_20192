package com.hang.doan.readbooks.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    private List<Comment> commentList = new ArrayList<Comment>();


    public CommentArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class CommentViewHolder {
        ImageView comment_user_img;
        TextView comment_user_message;
        TextView comment_user_Name;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public void add(Comment object) {
        commentList.add(object);
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CommentViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_comment, parent, false);
            viewHolder = new CommentViewHolder();
            viewHolder.comment_user_img = (ImageView) row.findViewById(R.id.comment_user_img);
            viewHolder.comment_user_message = (TextView) row.findViewById(R.id.comment_user_message);
            viewHolder.comment_user_Name = (TextView) row.findViewById(R.id.comment_user_Name);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CommentViewHolder)row.getTag();
        }
        Comment comment = getItem(position);

        Picasso.with(getContext())
                .load(comment.getComment_user_img())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(viewHolder.comment_user_img);
        viewHolder.comment_user_message.setText(comment.getComment_user_message());
        viewHolder.comment_user_Name.setText(comment.getComment_user_Name());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
