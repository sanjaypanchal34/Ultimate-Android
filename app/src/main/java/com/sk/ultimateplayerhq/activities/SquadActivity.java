package com.sk.ultimateplayerhq.activities;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.fragments.CoachListFragment;
import com.sk.ultimateplayerhq.fragments.PlayerListFragment;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SquadActivity extends BaseActivity {

    HashMap<String, String> param = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        param.put("Authorization", SessionManager.getToken());


        findViewById(R.id.btn_add_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SquadActivity.this, AddPlayerActivity.class));
            }
        }); findViewById(R.id.btn_add_coach).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SquadActivity.this, AddCoachActivity.class));
            }
        })
        ;


        ((MaterialButton)findViewById(R.id.btn_add_player)).setVisibility(SessionManager.isPlayerLogin()?GONE:View.VISIBLE);
        ((MaterialButton)findViewById(R.id.btn_add_coach)).setVisibility(SessionManager.isPlayerLogin()?GONE:View.VISIBLE);

        initUI();

    }

    private void initUI() {
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        if(SessionManager.isPlayerLogin()){
            tab_layout.removeTabAt(1);
        }
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                if(tab.getPosition()==0){
                    transaction.replace(R.id.manager_player_container, new PlayerListFragment());
                }else{
                    transaction.replace(R.id.manager_player_container, new CoachListFragment());
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_layout.selectTab(tab_layout.getTabAt(0),true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.manager_player_container, new PlayerListFragment());
        transaction.commit();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}