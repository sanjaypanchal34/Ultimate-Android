package com.sk.ultimateplayerhq.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.sk.ultimateplayerhq.activities.WelcomeActivity;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Communication;
import in.appsaint.communication.OnCommunicationCallBack;

public abstract class BaseFragment extends Fragment implements OnCommunicationCallBack {

    protected FragmentActivity context;
    protected Communication communication;
    protected HashMap<String,String> header = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        header.put("Authorization", SessionManager.getToken());
        communication = new Communication(getActivity(), this);
    }

    @Override
    public void onSuccessCallBack(Object tag, JSONObject jsonObject) throws JSONException {
        if (jsonObject.optInt("status") == 1)
            onSuccessCall(tag, jsonObject, jsonObject.getInt("status") == 1);
        else {
            if(jsonObject.optString("msg").equalsIgnoreCase("Invalid credentials")){
                SessionManager.setLogged(false);
                Intent i = new Intent(context, WelcomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }else{

                MyAlert.show(context, jsonObject.optString("msg"));
            }
        }
    }

    protected abstract void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException;

    protected void onFailCall(Object tag, Throwable t) {

    }


    @Override
    public void onFailCallBack(Object tag, Throwable t) {

    }

    @Override
    public void onConnectionChange(boolean b) {

    }

    @Override
    public void onResume() {

        super.onResume();
    }
}
