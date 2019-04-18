package com.sismatix.drskin.Fragment;

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
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
//import com.dolphinwebsolution.gotido.Model.sliderimage_model;
//import com.dolphinwebsolution.gotido.R;
import com.sismatix.drskin.Model.sliderimage_model;
import com.sismatix.drskin.R;

import java.util.List;


class SlidingImage_Adapter extends PagerAdapter {
    Context context;
    private List<sliderimage_model> sliderimage_models;
    private LayoutInflater inflater;
    String screen;

    public SlidingImage_Adapter(Context context, List<sliderimage_model> sliderimage_models) {

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
        view = inflater.inflate(R.layout.sliding_row, container, false);
        final sliderimage_model sm = sliderimage_models.get(position);


        image = (ImageView) view.findViewById(R.id.imageview);
        progressBar_slider_home = (ProgressBar)view.findViewById(R.id.progress_location_slider);

        //Glide.with(context).load(sm.getLocation_image()).into(image);

        image.setVisibility(View.VISIBLE);
        progressBar_slider_home.setVisibility(View.VISIBLE);

       // Glide.with(context).load(sliderimage_models.get(position).getLocation_image()).into(image);
/*

       Glide.with(context).load(sm.getLocation_image()).asBitmap().fallback(R.drawable.placeholder).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    image.setBackground(drawable);
                    progressBar_slider_home.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);

                }
            }
        });
*/

        /*Glide.with(context)
                .load(sm.getLocation_image())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //   progress_myacc.setVisibility(View.GONE);
                        progressBar_slider_home.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // progress_myacc.setVisibility(View.GONE);
                        progressBar_slider_home.setVisibility(View.GONE);
                        return false;}})
                .placeholder(R.drawable.placeholder)
                .fallback(R.drawable.placeholder)
                .into(image);*/


        // Picasso.with(context).load(imageModel.getUrl()).fit().into(image);
        ((ViewPager) container).addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }
}