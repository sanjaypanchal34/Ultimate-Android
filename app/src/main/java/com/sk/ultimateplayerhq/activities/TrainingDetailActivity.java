package com.sk.ultimateplayerhq.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.TrainingSessionAdapter;
import com.sk.ultimateplayerhq.models.TrainigSessionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.appsaint.communication.Api;

public class TrainingDetailActivity extends BaseActivity {

    private List<TrainigSessionModel> list = new ArrayList<>();
    private RecyclerView rv_sessions;
    private TrainingSessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);
        communication.callGET(Api.TRAINING_DETAIL,getIntent().getStringExtra("id"),"TRAINING_DETAIL",header);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rv_sessions = findViewById(R.id.rv_sessions);
        rv_sessions.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TrainingSessionAdapter(context, list, new TrainingSessionAdapter.OnSessionListener() {
            @Override
            public void onSessionClick(TrainigSessionModel model) {
                Intent intent = new Intent(TrainingDetailActivity.this,ImageViewActivity.class);
                intent.putExtra("image",model.getSession_image());
                startActivity(intent);
            }
        });
        rv_sessions.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if(tag == "TRAINING_DETAIL"){
            JSONArray training_sessions = jsonObject.getJSONArray("data");
            list.clear();
            for(int i=0;i<training_sessions.length();i++){
                list.add(new Gson().fromJson(training_sessions.getJSONObject(i).toString(), TrainigSessionModel.class));
            }
            adapter.notifyDataSetChanged();

          (  (TextView)findViewById(R.id. tv_no_data)).setVisibility(adapter.getItemCount() == 0?View.VISIBLE:View.GONE);
        }
    }
}