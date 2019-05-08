package com.sismatix.drskin.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.sismatix.drskin.Activity.YPlayer;
import com.sismatix.drskin.Activity.YouTubePopup;
import com.sismatix.drskin.Model.slidervideo_model;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.Configgg;
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

        String vidpic = "http://img.youtube.com/vi/" + tst + "/mqdefault.jpg";

        Log.e("vidpic",""+vidpic);

        Glide.with(context)
                .load(vidpic)
                .into(holder.iv_thumb);

        holder.lv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_video_popup);
                dialog.setTitle("Title...");

                //Youtube(video_id_pass, holder);

                dialog.show();*/

                final AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Log.e("video_id_pass",""+video_id_pass);
                // Toast.makeText(context, video_id_pass, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, YouTubePopup.class);//YPlayer
                intent.putExtra("videoId", video_id_pass);
                activity.startActivity(intent);
            }
        });

    }

    /*private void Youtube(final String video_id_pass, final MyViewHolder holder) {
        Log.e("vidsopop", "" + video_id_pass);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        final AppCompatActivity activity = (AppCompatActivity) context;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_popup, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Configgg.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    holder.YPlayerr = youTubePlayer;
                    holder.YPlayerr.setFullscreen(false);
                    holder.YPlayerr.setShowFullscreenButton(false);
                    holder.YPlayerr.loadVideo(video_id_pass);
                    holder.YPlayerr.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            holder.YPlayerr.pause();
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                    holder.YPlayerr.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    holder.YPlayerr.getCurrentTimeMillis();

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(activity, "Video Problem", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

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


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv_thumbnail;
        ImageView iv_thumb;
        View view;
        public static YouTubePlayer YPlayerr;

        public MyViewHolder(View view) {
            super(view);
            lv_thumbnail = (LinearLayout) view.findViewById(R.id.lv_thumbnailll);
            iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
//videoView = (VideoView) view.findViewById(R.id.videoView);
        }
    }
}