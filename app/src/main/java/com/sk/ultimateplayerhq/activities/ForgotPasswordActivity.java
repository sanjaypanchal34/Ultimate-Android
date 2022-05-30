package com.sk.ultimateplayerhq.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.utils.InputUtils;

import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Api;

public class ForgotPasswordActivity extends BaseActivity {

    private TextInputLayout in_email;
    private MaterialButton btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forogt_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        initUI();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InputUtils.isNotValidEmail(in_email)) {
                    InputUtils.setError(in_email, InputUtils.enterValid("Email"));
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("login", InputUtils.getText(in_email));
                    communication.callPOST(Api.LOGIN, "FORGOT_PASSWORD", param);
                }
            }
        });
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) {

    }

    private void initUI() {
        in_email = findViewById(R.id.in_email);
        btn_send = findViewById(R.id.btn_send);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}