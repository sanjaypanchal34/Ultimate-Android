package com.sk.ultimateplayerhq;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sk.ultimateplayerhq.utils.SessionManager;

public class AppController extends Application {
    private AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        MultiDex.install(this);
        Fresco.initialize(this);

        SessionManager.init(this);
    }


    public AppController getInstance() {
        return mInstance;
    }
}
