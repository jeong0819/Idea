package com.example.realp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class imageSlideAdapter extends PagerAdapter {

    private ArrayList<String> imageUrlList;
    private Context context ;


    public imageSlideAdapter(Context context,ArrayList<String> imageUrlList ){

        this.context = context;
        this.imageUrlList = imageUrlList;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.imageslide,null);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageSlideV);
        Glide.with(context).load(imageUrlList.get(position)).into(imageView);
        container.addView(v);

        return v;
    }

    @Override
    public boolean isViewFromObject( View view, Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }

}