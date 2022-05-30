package com.sk.ultimateplayerhq.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.appsaint.communication.Api;

public class EventDetailActivity extends BaseActivity {

    private SimpleDraweeView iv_event;
    private TextView tv_vs,tv_time,tv_date,tv_venue,tv_score,tv_goal_score,tv_assists,tv_player_of_match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initUI();

        communication.callGET(Api.EVENT_DETAIL, getIntent().getStringExtra("id"), "EVENT_DETAIL", header);
    }

    private void initUI() {

        iv_event = findViewById(R.id.iv_event);
        tv_vs = findViewById(R.id.tv_vs);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_venue = findViewById(R.id.tv_venue);
        tv_score = findViewById(R.id.tv_score);
        tv_goal_score = findViewById(R.id.tv_goal_score);
        tv_assists = findViewById(R.id.tv_assists);
        tv_player_of_match = findViewById(R.id.tv_player_of_match);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "EVENT_DETAIL") {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray mPlanner_data =data.getJSONArray("mPlanner_data");
            if(mPlanner_data.length()>0) {
                iv_event.setImageURI(mPlanner_data.getJSONObject(0).optString("lineup_image"));
            }
            JSONArray gs_name =data.getJSONArray("gs_name");
            JSONArray assists_name =data.getJSONArray("assists_name");
            StringBuilder gsBuilder = new StringBuilder();
            StringBuilder assistsBuilder = new StringBuilder();
            for(int i=0;i< gs_name.length();i++){
                String name = gs_name.getJSONObject(i).optString("name");
                if(i==0){
                    gsBuilder.append(name);
                }else{
                    gsBuilder.append(", ");
                    gsBuilder.append(name);
                }
            }

            for(int i=0;i< assists_name.length();i++){
                String name = assists_name.getJSONObject(i).optString("name");
                if(i==0){
                    assistsBuilder.append(name);
                }else{
                    assistsBuilder.append(", ");
                    assistsBuilder.append(name);
                }
            }
            JSONObject eventsDetails = data.getJSONObject("eventObj");
            tv_vs.setText(eventsDetails.optString("title"));
            tv_time.setText(data.optString("formate_time"));
            tv_date.setText(data.optString("formate_sdate"));
            tv_venue.setText(eventsDetails.optString("location"));
            tv_score.setText(eventsDetails.isNull("score")?"0":eventsDetails.optString("score"));
            tv_goal_score.setText(gsBuilder);
            tv_assists.setText(assistsBuilder);
            tv_player_of_match.setText(data.optString("pom"));
        }
    }
}