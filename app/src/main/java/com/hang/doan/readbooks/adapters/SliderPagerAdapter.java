package com.hang.doan.readbooks.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hang.doan.readbooks.R;
import com.hang.doan.readbooks.models.Book;
import com.hang.doan.readbooks.ui.BookDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext ;
    private List<Book> mList ;
    private View slideLayout;

    public SliderPagerAdapter(Context mContext, List<Book> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        slideLayout = inflater.inflate(R.layout.slide_item,null);

        ImageView slideImg = slideLayout.findViewById(R.id.slide_item_img);
        TextView author = slideLayout.findViewById(R.id.slide_item_txt_author);
        TextView name = slideLayout.findViewById(R.id.slide_item_txt_name);

        author.setText(mList.get(position).getAuthorLink());
        name.setText(mList.get(position).getName());
        Picasso.with(mContext)
                .load(mList.get(position).getImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(slideImg);

        container.addView(slideLayout);

        final int index = position;
        ImageView slide_btn = slideLayout.findViewById(R.id.slide_item_img);
        slide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(slideLayout.getContext(),BookDetailActivity.class);
                // send movie information to deatilActivity
                intent.putExtra("name",mList.get(index).getName());
                intent.putExtra("authorLink",mList.get(index).getAuthorLink());
                intent.putExtra("link",mList.get(index).getLink());
                intent.putExtra("imgURL",mList.get(index).getImageURL());
                slideLayout.getContext().startActivity(intent);
            }
        });







        return slideLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
