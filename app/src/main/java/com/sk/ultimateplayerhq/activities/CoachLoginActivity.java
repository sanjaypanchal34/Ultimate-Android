package com.sk.ultimateplayerhq.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.UserModel;
import com.sk.ultimateplayerhq.utils.InputUtils;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Api;

public class CoachLoginActivity extends BaseActivity {

    private static final String TAG = "LOGIN";
    private boolean isPlayerLogin;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_login);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                         token = task.getResult();

                    }
                });

        isPlayerLogin = getIntent().getBooleanExtra("isPlayerLogin",false);
        EditText edt_email = findViewById(R.id.edt_email);
        EditText edt_pass = findViewById(R.id.edt_pass);
        TextView btn_login = findViewById(R.id.tv_login);
        TextView tv_or = findViewById(R.id.tv_or);
        TextView btn_create = findViewById(R.id.tv_create_acc);
        TextView btn_forgot_pass = findViewById(R.id.tv_forgot_password);

        if (isPlayerLogin) {
            btn_create.setVisibility(View.GONE);
            tv_or.setVisibility(View.GONE);
        }

        btn_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoachLoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoachLoginActivity.this, RegisterActivity.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_email.getText().toString().isEmpty()){
                    edt_email.requestFocus();
                    Toast.makeText(context, "Please Enter Username/Email.", Toast.LENGTH_SHORT).show();
                }else if(edt_pass.getText().toString().isEmpty()){
                    edt_pass.requestFocus();
                    Toast.makeText(context, "Please Enter Password.", Toast.LENGTH_SHORT).show();
                }else if(isPlayerLogin && token==null){
                    Toast.makeText(context, "Firebase Token Not Found.", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> param = new HashMap<>();
                    param.put("email", edt_email.getText().toString());
                    param.put("username", edt_email.getText().toString());
                    param.put("password",edt_pass.getText().toString());
                    if(isPlayerLogin && token!=null){
                        param.put("firebase_token",token);
                    }
                    communication.callPOST(isPlayerLogin?Api.PLAYER_LOGIN:Api.LOGIN, isPlayerLogin?"PLAYER_LOGIN":"LOGIN", param);
                }
            }
        });


        ((ImageButton)findViewById(R.id.ib_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,WelcomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if(tag=="LOGIN"){
            try {
                UserModel model = UserModel.fromJson(jsonObject.getJSONObject("data"));
                SessionManager.setUser(model);
                HashMap<String, String> param = new HashMap<>();
                param.put("user_id", String.valueOf(model.getId()));
                param.put("token", model.getWp_generate_token());
                communication.callPOST(Api.VERIFY_TOKEN,"VERIFY_TOKEN",param);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else  if(tag=="PLAYER_LOGIN"){
            SessionManager.setToken(String.format("Bearer %s", jsonObject.getString("data")));
            SessionManager.setNewUserID(jsonObject.getInt("id"));
            SessionManager.setRole(jsonObject.getString("role"));
            SessionManager.setLogged(true);
            SessionManager.setIsPlayerLogin(true);
            startActivity(new Intent(CoachLoginActivity.this, PlayerDashboardActivity.class));
            finish();

        }else if(tag=="VERIFY_TOKEN"){
            try {
                SessionManager.setToken(String.format("Bearer %s", jsonObject.getString("data")));
                SessionManager.setNewUserID(jsonObject.getInt("id"));
                SessionManager.setLogged(true);
                SessionManager.setIsPlayerLogin(false);
                startActivity(new Intent(CoachLoginActivity.this, MainActivity.class));
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}