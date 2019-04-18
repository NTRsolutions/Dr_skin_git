package com.sismatix.drskin.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.drskin.Model.sliderimage_model;
import com.sismatix.drskin.R;


import java.util.List;

public class SlideingImageAdapter extends PagerAdapter {
    Context context;
    private List<sliderimage_model> sliderimage_models;
    private LayoutInflater inflater;
    String screen;

    public SlideingImageAdapter(Context context, List<sliderimage_model> sliderimage_models) {

        this.context = context;
        this.sliderimage_models = sliderimage_models;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {

        return sliderimage_models.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final ImageView image;
        final ProgressBar progressBar_slider_home;

        View view = null;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.slidingrow, container, false);
        final sliderimage_model sm = sliderimage_models.get(position);


        image = (ImageView) view.findViewById(R.id.imageview);
        //progressBar_slider_home = (ProgressBar)view.findViewById(R.id.progress_slider);

        //Glide.with(context).load(sm.getLocation_image()).into(image);

        image.setVisibility(View.VISIBLE);
        //progressBar_slider_home.setVisibility(View.VISIBLE);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.app_logo);
        requestOptions.error(R.drawable.app_logo);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(sm.getProd_image()).into(image);

        //Glide.with(context).load(sm.getProd_image()).into(image);

        // Picasso.with(context).load(imageModel.getUrl()).fit().into(image);
        ((ViewPager) container).addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }
}