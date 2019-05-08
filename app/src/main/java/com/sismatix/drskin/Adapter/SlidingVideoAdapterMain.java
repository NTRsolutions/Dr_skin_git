package com.sismatix.drskin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.sismatix.drskin.Activity.YPlayer;
import com.sismatix.drskin.Model.slidervideo_model;
import com.sismatix.drskin.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlidingVideoAdapterMain extends RecyclerView.Adapter<SlidingVideoAdapterMain.MyViewHolder> {
    private Context context;
    private List<slidervideo_model> models;
    String video_id, spl, youtubeUrl;
    public static String video_id_pass;

    public SlidingVideoAdapterMain(Context context, List<slidervideo_model> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sliding_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final slidervideo_model slidervideo_model = models.get(position);

        youtubeUrl = slidervideo_model.getProd_video();
        Log.e("yid", "" + youtubeUrl);

//Youtube(holder);
        String tst = getYoutubeID(youtubeUrl);
        video_id_pass = getYoutubeID(slidervideo_model.getProd_video());
        Log.e("videooooiddd", "" + tst);

        Picasso.with(context)
                .load("http://img.youtube.com/vi/" + tst + "/mqdefault.jpg")
                .into(holder.iv_thumb);

        holder.lv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatActivity activity = (AppCompatActivity) v.getContext();

                Log.e("video_id_pass",""+video_id_pass);
                // Toast.makeText(context, video_id_pass, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, YPlayer.class);
                intent.putExtra("videoId", video_id_pass);
                activity.startActivity(intent);
            }
        });

    }

    private String getYoutubeID(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = youtubeUrl;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (youtubeUrl.contains("youtu.be/")) {
                spl = youtubeUrl.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                } else {
                    video_id = spl;
                }
            }
        }
        Log.e("vidid", "" + video_id);
        return video_id;
    }


    @Override
    public int getItemCount() {
        return models.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_thumbnail;
        ImageView iv_thumb;
        View view;

        public MyViewHolder(View view) {
            super(view);
            lv_thumbnail = (LinearLayout) view.findViewById(R.id.lv_thumbnailll);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
//videoView = (VideoView) view.findViewById(R.id.videoView);
        }
    }
}