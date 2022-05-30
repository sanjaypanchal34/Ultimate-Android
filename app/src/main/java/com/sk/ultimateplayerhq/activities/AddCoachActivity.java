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
import com.sk.ultimateplayerhq.models.CoachModel;
import com.sk.ultimateplayerhq.models.SquadModel;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import in.appsaint.communication.Api;

public class AddCoachActivity extends BaseActivity {


    private boolean isEdit;
    private int updateId;
    private EditText edt_name,edt_email;
    private MaterialButton btn_save;
    private CoachModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            updateId = getIntent().getIntExtra("id", -1);
            communication.callGET(Api.COACH_DETAIL, updateId, "COACH_DETAIL", header);
        }


        initUI();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_name.getText().toString().isEmpty()) {
                    edt_name.requestFocus();
                    edt_name.setError("Please Enter Coach Name.");
                } else if (edt_email.getText().toString().isEmpty()) {
                    edt_email.requestFocus();
                    edt_email.setError("Please Enter Coach Email.");
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("name", edt_name.getText().toString());
                    param.put("email", edt_email.getText().toString());
                    if (isEdit) {
                        param.put("id", String.valueOf(updateId));

                        communication.callPOST(Api.COACH_UPDATE, updateId, "COACH_UPDATE", param, header);
                    } else {
                        communication.callPOST(Api.COACH_CREATE, "", "COACH_UPDATE", param, header);
                    }
                }
            }
        });
    }

    private void initUI() {
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        btn_save = findViewById(R.id.btn_save);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "COACH_DETAIL") {
            model = CoachModel.fromJson(jsonObject.getJSONObject("data"));
            edt_name.setText(model.getName());
            edt_email.setText(model.getEmail());
        }else{
            MyAlert.show(AddCoachActivity.this, jsonObject.getString("msg"), false, new MyAlertDialogListener() {
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