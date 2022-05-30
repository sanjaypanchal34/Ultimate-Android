package com.sk.ultimateplayerhq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Api;

public class PlayerDashboardActivity extends  BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_dashboard);

        ((Toolbar)findViewById(R.id.toolbar)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.opt_logout){
                    MyAlert.show(context, "Are you sure want to logout?", new MyAlertDialogListener() {
                        @Override
                        public void onPositiveClick() {
                           communication.callPOST(Api.LOGOUT,"","LOGOUT",new HashMap<>(),header);
                        }

                        @Override
                        public void onNegativeClick() {

                        }
                    });
                }
                return false;
            }
        });
        ((RelativeLayout)findViewById(R.id.rl_calender)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CoachCalenderActivity.class));
            }
        });

        ((RelativeLayout)findViewById(R.id.rl_highlight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, HighlightActivity.class));
            }
        });

        ((RelativeLayout)findViewById(R.id.rl_squad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SquadActivity.class));
            }
        });


        ((RelativeLayout)findViewById(R.id.rl_squad)).setVisibility(SessionManager.getPlayerRole().equalsIgnoreCase("player")?View.VISIBLE:View.INVISIBLE);

        ((RelativeLayout)findViewById(R.id.rl_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChatScreen.class));
            }
        });
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if(tag == "LOGOUT"){
            SessionManager.setLogged(false);
            startActivity(new Intent(context,WelcomeActivity.class));
            finish();
        }
    }
}
