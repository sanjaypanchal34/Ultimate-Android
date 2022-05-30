package com.sk.ultimateplayerhq.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LongSparseArray;


import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Communication;
import in.appsaint.communication.OnCommunicationCallBack;

public abstract class BaseActivity extends AppCompatActivity implements OnCommunicationCallBack {

    protected Communication communication;
    protected Context context;
    private MenuItem item;
    protected HashMap<String, String> header = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        header.put("Authorization", SessionManager.getToken());
        communication = new Communication(this, this);

        if(getSupportActionBar()!=null){

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(getSupportActionBar()!=null){

            getMenuInflater().inflate(R.menu.logo_main, menu);
            item = menu.findItem(R.id.opt_search);
        }
        return true;
    }



    public void setMenuVisible(boolean b){
        if(item!=null){
            item.setVisible(b);
        }
    }

    @Override
    public void onSuccessCallBack(Object tag, JSONObject jsonObject) throws JSONException {
        //TODO - ADD ALERT HERE
        if (tag == "AWS"){
            onSuccessCall(tag, jsonObject, true);
        }else if (tag == "POST_COMMENTS") {
            onSuccessCall(tag, jsonObject, jsonObject.getInt("status") == 1);
        }else if (jsonObject.optInt("status") == 1) {
            onSuccessCall(tag, jsonObject, jsonObject.getInt("status") == 1);
        } else {

            if(jsonObject.optString("msg").equalsIgnoreCase("Invalid credentials")){
                SessionManager.setLogged(false);
                Intent i = new Intent(context, WelcomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }else{

                MyAlert.show(context, jsonObject.optString("msg"));
            }
        }
        /*Log.e(tag+"::::",jsonObject.toString());
        if (tag.equals("Auth") && jsonObject.optInt("status") == 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(jsonObject.optString("message"));
            dialog.setPositiveButton("Retry", null);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }else if (jsonObject.optInt("status") == 0){
            onFailCall(tag,null);
        }*/
    }

    protected abstract void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException;

    protected void onFailCall(Object tag, Throwable t) {

    }

    @Override
    public void onFailCallBack(Object tag, Throwable t) {
        onFailCall(tag, t);
    }

    @Override
    public void onConnectionChange(boolean b) {

    }
}
