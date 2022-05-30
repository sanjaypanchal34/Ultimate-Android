package com.sk.ultimateplayerhq.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.sk.ultimateplayerhq.R;


public class VideoPlayerActivity extends AppCompatActivity {
    public String mVideoId;
    public VimeoPlayerView vimeoPlayerView;
    private float seekTime = 0;
    private ImageView mClosePlay;
    private boolean isVideoPlaying = false;
    private boolean exitFull = false;
    private RelativeLayout mLayoutMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        initView();

       /* if(!NetChecker.isConnected(this)) {
            Snackbar snackbar = Snackbar
                    .make(mLayoutMain, "No internet", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
*/
        if (getIntent().hasExtra("id")) {
            mVideoId = getIntent().getStringExtra("id");
        }
        Log.d("AllVideo", "AllVideo ID >>  " + mVideoId);
//If video is open. but limit playing at embedded.
        //vimeoPlayerView.initialize(true, mVideoId, "http://uphq.x-minds.org");
        isVideoPlaying = true;
        vimeoPlayerView.setFullscreenVisibility(true);
        Log.e("ID::::",mVideoId);
        vimeoPlayerView.initialize(false, Integer.parseInt(mVideoId), "https://www.ultimateplayerhq.com/");
        vimeoPlayerView.setFullscreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                enterFullScreen();
            }
        });

    }

    private void initView() {
        vimeoPlayerView = findViewById(R.id.vimeoPlayer);
        mClosePlay = findViewById(R.id.close_play);
        mClosePlay.setOnClickListener(closeOnClickListener);
        mLayoutMain=findViewById(R.id.lay_videoplay_main);
    }

    View.OnClickListener closeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("VideoPlayer", "onStart>>>> ");
        if (seekTime > 0) {
            vimeoPlayerView.seekTo(seekTime);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("VideoPlayer", "onPause>>>> ");
        seekTime = vimeoPlayerView.getCurrentTimeSeconds();
        Log.d("VideoPlayer", "onPauseseekTime>>>> " + seekTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("VideoPlayer", "onResume>>>> ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("VideoPlayer", "onStop>>>> ");
    }

    private void exitVideoFullScreen() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void enterFullScreen() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        exitFull = true;

    }


    @Override
    public void onBackPressed() {
        Log.d("ISPLAY", "FULL" + exitFull);

        if (!exitFull) {
            exitVideoFullScreen();
            exitFull = true;
            isVideoPlaying = false;

        } else {
            super.onBackPressed();
            finish();
        }
        if (isVideoPlaying) {
            super.onBackPressed();
            finish();
        }

    }
}