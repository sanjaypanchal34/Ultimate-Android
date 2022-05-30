package com.sk.ultimateplayerhq.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageViewActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);



        ((SimpleDraweeView)findViewById(R.id.iv_image)).setImageURI(getIntent().getStringExtra("image"));
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
