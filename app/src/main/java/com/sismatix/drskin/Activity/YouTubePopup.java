package com.sismatix.drskin.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.sismatix.drskin.R;
import com.sismatix.drskin.Retrofit.Configgg;

public class YouTubePopup extends AppCompatActivity {

    String vidId;
    YouTubePlayer YPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yplayer);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.4));
        //getWindow().setLayout((int) (width*.8),(int) (height*.6));

        vidId= getIntent().getStringExtra("videoId");
        Youtube(vidId);
    }
    private void Youtube(final String vidId) {
        Log.e("vidsYT", "" + vidId);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        final AppCompatActivity activity = (AppCompatActivity) this;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment_mpp, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Configgg.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    YPlay = youTubePlayer;
                    YPlay.loadVideo(vidId);
                    YPlay.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    YPlay.setShowFullscreenButton(true);
                    YPlay.play();
                    //YPlay.getCurrentTimeMillis();

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(activity, "Video Problem", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
