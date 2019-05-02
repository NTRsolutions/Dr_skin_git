package com.sismatix.drskin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.drskin.Fragment.Home;
import com.sismatix.drskin.Fragment.Shop;
import com.sismatix.drskin.Model.Cuntrylist_model;
import com.sismatix.drskin.R;

import java.util.List;

public class Cuntrylist_Adapter extends RecyclerView.Adapter<Cuntrylist_Adapter.MyViewHolder> {

    private Context context;
    private List<Cuntrylist_model> model;

    public Cuntrylist_Adapter(Context context, List<Cuntrylist_model> cartList) {
        this.context = context;
        this.model = cartList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_cuntrylist, viewGroup, false);

        return new Cuntrylist_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            final Cuntrylist_model cuntrylist_model = model.get(position);
     //   holder.tv_category_name.setTypeface(Home.roboto_bold);
        holder.tv_name.setText(cuntrylist_model.getName());
        holder.tv_name.setTypeface(Home.opensans_bold);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.app_logo);
        requestOptions.error(R.drawable.app_logo);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(cuntrylist_model.getFlag()).into(holder.iv_flage);

        holder.lv_nature_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Bundle b=new Bundle();
                        b.putString("vlaue",cuntrylist_model.getVlaue());
                        b.putString("name",cuntrylist_model.getName());
                        Log.e("categotyidd",""+cuntrylist_model.getVlaue());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new Shop();
                        myFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out).replace(R.id.rootLayout, myFragment).addToBackStack(null).commit();
                    }
                }, 1000);
            }
        });


    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name ;
        ImageView iv_flage;
        LinearLayout lv_nature_click;
        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_flage = (ImageView) view.findViewById(R.id.iv_flage);
            lv_nature_click = (LinearLayout) view.findViewById(R.id.lv_nature_click);


        }
    }
}
