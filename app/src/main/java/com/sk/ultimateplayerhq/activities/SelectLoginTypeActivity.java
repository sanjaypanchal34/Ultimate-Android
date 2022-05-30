package com.sk.ultimateplayerhq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sk.ultimateplayerhq.R;

public class SelectLoginTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login_type);

        initUI();
    }

    private void initUI() {
        ImageButton ib_coach = findViewById(R.id.ib_coach);
        ImageButton ib_player = findViewById(R.id.ib_player);


        ib_coach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectLoginTypeActivity.this, CoachLoginActivity.class));
                finish();
            }
        });
                ib_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SelectLoginTypeActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }


}