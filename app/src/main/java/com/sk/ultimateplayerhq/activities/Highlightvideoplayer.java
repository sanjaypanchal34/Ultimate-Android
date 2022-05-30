package com.sk.ultimateplayerhq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.custom.FullScreenVideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Highlightvideoplayer extends BaseActivity {

    private VideoView andExoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlightvideoplayer);
        MediaController mediaController = new MediaController(context);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        andExoPlayerView.setMediaController(mediaController);
        andExoPlayerView.setVideoURI(Uri.parse(getIntent().getStringExtra("video")));
        andExoPlayerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                andExoPlayerView.start();
            }
        });
        mediaController.setEnabled(true);


        ((ImageButton)findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {

    }
}