package com.sk.ultimateplayerhq.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.custom.CustomTypefaceSpan;
import com.sk.ultimateplayerhq.utils.SessionManager;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SpannableString ss1=  new SpannableString("Don't have an account? Sign Up");
        ss1.setSpan(new RelativeSizeSpan(0.9f), 23,30, 0); // set size
        ss1.setSpan(new CustomTypefaceSpan("",ResourcesCompat.getFont(this, R.font.teko_s_b)), 23,30, 0); // set size
        TextView tv=  findViewById(R.id.tv_sign_Up);
        tv.setText(ss1);
        findViewById(R.id.tv_coach_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, CoachLoginActivity.class));
                finish();
            }
        });

        findViewById(R.id.tv_player_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, CoachLoginActivity.class);
                intent.putExtra("isPlayerLogin",true);
                startActivity(intent);
                finish();
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
            }
        });
    }


}
