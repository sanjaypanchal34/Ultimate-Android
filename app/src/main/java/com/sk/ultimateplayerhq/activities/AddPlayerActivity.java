package com.sk.ultimateplayerhq.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.SquadModel;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import in.appsaint.communication.Api;

public class AddPlayerActivity extends BaseActivity {

    private boolean isEdit;
    private int updateId;
    private EditText edt_name, edt_email, edt_appearances, edt_goals, edt_assists, edt_mom, edt_phone,
            edt_jersey_num,
            edt_address;
    private Spinner edt_position,edt_kit_color;
    private MaterialButton btn_save;
    private final HashMap<String, String> header = new HashMap<>();
    private SquadModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        header.put("Authorization", SessionManager.getToken());
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            updateId = getIntent().getIntExtra("id", -1);
            communication.callGET(Api.SQUAD_DETAIL, updateId, "SQUAD_DETAIL", header);
        }


        initUI();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_name.getText().toString().isEmpty()) {
                    edt_name.requestFocus();
                    edt_name.setError("Please Enter Player Name.");
                } else if (edt_position.getSelectedItemPosition()==0) {
                    edt_position.requestFocus();
                    MyAlert.show(AddPlayerActivity.this,"Please Enter Position.");
                } else if (edt_kit_color.getSelectedItemPosition() == 0) {
                    edt_kit_color.requestFocus();
                    MyAlert.show(AddPlayerActivity.this,"Please Enter Kit Color.");
                } else if (edt_jersey_num.getText().toString().isEmpty()) {
                    edt_jersey_num.requestFocus();
                    edt_jersey_num.setError("Please Enter Position Number.");
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("name", edt_name.getText().toString());
                    param.put("email", edt_email.getText().toString());
                    param.put("position", edt_position.getSelectedItem().toString());
                    param.put("appearances", edt_appearances.getText().toString());
                    param.put("goals", edt_goals.getText().toString());
                    param.put("assists", edt_assists.getText().toString());
                    param.put("mom", edt_mom.getText().toString());
                    param.put("phone", edt_phone.getText().toString());
                    param.put("kit_color", edt_kit_color.getSelectedItem().toString());
                    param.put("address", edt_address.getText().toString());
                    param.put("jersey_number", edt_jersey_num.getText().toString());
                    if (isEdit) {
                        param.put("id", String.valueOf(updateId));

                        communication.callPOST(Api.SQUAD_UPDATE, updateId, "SQUAD_UPDATE", param, header);
                    } else {
                        communication.callPOST(Api.SQUAD_CREATE, "", "SQUAD_UPDATE", param, header);
                    }
                }
            }
        });
    }

    private void initUI() {
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_position = findViewById(R.id.edt_position);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddPlayerActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.positions)){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.parseColor("#50ffffff"));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                tv.setBackgroundColor(getResources().getColor(R.color.purple));
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view =  super.getView(position, convertView, parent);

                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.parseColor("#50ffffff"));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        edt_position.setAdapter(arrayAdapter);
        edt_appearances = findViewById(R.id.edt_appearances);
        edt_goals = findViewById(R.id.edt_goals);
        edt_assists = findViewById(R.id.edt_assists);
        edt_mom = findViewById(R.id.edt_mom);
        edt_phone = findViewById(R.id.edt_phone);
        edt_kit_color = findViewById(R.id.edt_kit_color);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(AddPlayerActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.kit_colors)){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.parseColor("#50ffffff"));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                tv.setBackgroundColor(getResources().getColor(R.color.purple));
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view =  super.getView(position, convertView, parent);

                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.parseColor("#50ffffff"));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        colorAdapter.setDropDownViewResource(R.layout.item_spinner);
        edt_kit_color.setAdapter(colorAdapter);
        edt_jersey_num = findViewById(R.id.edt_jersey_num);
        edt_address = findViewById(R.id.edt_address);
        btn_save = findViewById(R.id.btn_save);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "SQUAD_DETAIL") {
            model = SquadModel.fromJson(jsonObject.getJSONObject("data"));
            edt_name.setText(model.getName());
            edt_email.setText(model.getEmail());
            edt_position.setSelection(Arrays.asList(getResources().getStringArray(R.array.positions)).indexOf(model.getPosition()));
            edt_appearances.setText(model.getAppearances());
            edt_goals.setText(model.getGoals());
            edt_assists.setText(model.getAssists());
            edt_mom.setText(model.getMom());
            edt_phone.setText(model.getPhone());
            edt_address.setText(model.getAddress());
            edt_kit_color.setSelection(Arrays.asList(getResources().getStringArray(R.array.kit_colors)).indexOf(model.getKit_color()));
            edt_jersey_num.setText(model.getJersey_number());
        }else{
            MyAlert.show(AddPlayerActivity.this, jsonObject.getString("msg"), false, new MyAlertDialogListener() {
                @Override
                public void onPositiveClick() {
                    onBackPressed();
                }

                @Override
                public void onNegativeClick() {

                }
            });
        }
    }
}