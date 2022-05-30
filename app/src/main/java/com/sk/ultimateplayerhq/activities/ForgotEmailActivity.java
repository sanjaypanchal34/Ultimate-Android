package com.sk.ultimateplayerhq.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sk.ultimateplayerhq.R;

import in.appsaint.communication.Api;

public class ForgotEmailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_email);

        Log.e("LOGIN:::", Api.LOGIN.toString());
    }
}