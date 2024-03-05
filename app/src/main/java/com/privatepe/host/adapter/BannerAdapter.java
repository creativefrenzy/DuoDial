package com.privatepe.host.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.privatepe.host.R;
import com.privatepe.host.activity.WebBanner;
import com.privatepe.host.response.Banner.BannerResult;

import java.util.List;
import java.util.Objects;

public class BannerAdapter extends PagerAdapter {
    private List<BannerResult> imageList;
    private LayoutInflater layoutInflater;
    private Context context;

    public BannerAdapter(List<BannerResult> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.item_lab, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);
        Glide.with(context).load(imageList.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.weekly_placeholder).error(R.drawable.weekly_placeholder)).into(imageView);
        Objects.requireNonNull(container).addView(itemView);
           imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, WebBanner.class);
                   intent.putExtra("url",String.valueOf(imageList.get(position).getUrl()));
                   intent.putExtra("name",String.valueOf(imageList.get(position).getName()));

                   Log.e("urlll","urlll ="+imageList.get(position).getUrl());
                   context.startActivity(intent);
               }
           });

        return itemView;


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View)object;
        container.removeView(view);
    }
}
