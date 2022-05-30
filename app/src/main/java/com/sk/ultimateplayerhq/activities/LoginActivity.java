package com.sk.ultimateplayerhq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.UserModel;
import com.sk.ultimateplayerhq.utils.InputUtils;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Api;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private TextInputLayout in_email, inPassword;
    private MaterialButton btn_login, btn_register;
    private TextView tv_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();


//        InputUtils.setText("himanshu", in_email);
//        InputUtils.setText("himanshu123", inPassword);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
    }

    private void initUI() {
        in_email = findViewById(R.id.in_email);
        inPassword = findViewById(R.id.inPassword);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) {
        try {
            UserModel model = UserModel.fromJson(jsonObject.getJSONObject("data"));
            SessionManager.setUser(model);
            SessionManager.setLogged(true);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            if (InputUtils.isEmpty(in_email)) {
                InputUtils.setError(in_email, InputUtils.enterValid("Email"));
            } else if (InputUtils.isEmpty(inPassword)) {
                InputUtils.setError(inPassword, InputUtils.enter("Password"));
            } else {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", InputUtils.getText(in_email));
                param.put("password", InputUtils.getText(inPassword));
                communication.callPOST(Api.LOGIN, "LOGIN", param);
            }
        } else if (id == R.id.tv_forgot_password) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        } else if (id == R.id.btn_register) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }
}