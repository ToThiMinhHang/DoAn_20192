package com.hang.doan.readbooks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Comment;
import com.hang.doan.readbooks.models.StoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoryItemArrayAdapter extends ArrayAdapter<StoryItem> {

    private List<StoryItem> storyList = new ArrayList<StoryItem>();

    public StoryItemArrayAdapter(Context context, int resource) {
        super(context, resource);
    }
    static class StoryViewHolder {
        ImageView story_image;
        TextView story_name;
        TextView last_chapter;
    }

    @Override
    public int getCount() {
        return storyList.size();
    }

    @Override
    public void add(StoryItem object) {
        storyList.add(object);
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StoryViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.my_story_item, parent, false);
            viewHolder = new StoryViewHolder();
            viewHolder.story_image = (ImageView) row.findViewById(R.id.my_story_item_img);
            viewHolder.story_name = (TextView) row.findViewById(R.id.my_story_item_name);
            viewHolder.last_chapter = (TextView) row.findViewById(R.id.my_story_item_name_last_chapter_name);
            row.setTag(viewHolder);
        } else {
            viewHolder = (StoryViewHolder)row.getTag();
        }
        StoryItem storyItem = getItem(position);

        Picasso.with(getContext())
                .load(storyItem.getImage_link())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(viewHolder.story_image);
        viewHolder.story_name.setText(storyItem.getStory_name());
//        viewHolder.last_chapter.setText(storyItem.getChapters().get(storyItem.getChapters().size()-1));
        return row;
    }

}
