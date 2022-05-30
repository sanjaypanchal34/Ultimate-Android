package com.sk.ultimateplayerhq.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.LoginActivity;
import com.sk.ultimateplayerhq.activities.MainActivity;
import com.sk.ultimateplayerhq.activities.NewPlanActivity;
import com.sk.ultimateplayerhq.activities.PlanDetailActivity;
import com.sk.ultimateplayerhq.activities.WelcomeActivity;
import com.sk.ultimateplayerhq.interfaces.OnLoadFragmentListener;
import com.sk.ultimateplayerhq.models.UserModel;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Api;

public class MyAccountFragment extends BaseFragment implements View.OnClickListener {


    private TextView tv_edit_profile;
    private TextView tv_edit_email;
    private TextView tv_change_password;
    private TextView tv_logout;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_plan;
    private SimpleDraweeView iv_profile;
    private MaterialButton btn_detail,btn_join;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);


        tv_edit_profile.setOnClickListener(this);
        tv_edit_email.setOnClickListener(this);
        tv_change_password.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        btn_detail.setOnClickListener(this);
        btn_join.setOnClickListener(this);


        tv_name.setText(String.format("%s %s", SessionManager.getUser().getFirst_name(), SessionManager.getUser().getLast_name()));
        tv_email.setText(SessionManager.getUser().getUsername());
        super.onViewCreated(view, savedInstanceState);
    }

    private void getUserData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("wp_generate_token", SessionManager.getUser().getWp_generate_token());
        param.put("id", String.valueOf(SessionManager.getUser().getId()));
        communication.callPOST(Api.USER_PROFILE, "PROFILE", param);
    }

    private void initUI(View view) {
        btn_join = view.findViewById(R.id.btn_join);
        btn_detail = view.findViewById(R.id.btn_detail);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_plan = view.findViewById(R.id.tv_plan);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_edit_profile = view.findViewById(R.id.tv_edit_profile);
        tv_edit_email = view.findViewById(R.id.tv_edit_email);
        tv_change_password = view.findViewById(R.id.tv_change_password);
        tv_logout = view.findViewById(R.id.tv_logout);
    }

    private OnLoadFragmentListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (OnLoadFragmentListener) context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_edit_profile) {
            listener.onLoadFragment(new EditProfileFragment(), "EDIT_PROFILE");
        } else if (view.getId() == R.id.tv_edit_email) {
            listener.onLoadFragment(new EditEmailFragment(), "EDIT_EMAIL");
        } else if (view.getId() == R.id.tv_change_password) {
            listener.onLoadFragment(new ChangePasswordFragment(), "CHANGE_PASSWORD");
        } else if (view.getId() == R.id.btn_detail) {
            startActivity(new Intent(context, PlanDetailActivity.class));
        }  else if (view.getId() == R.id.btn_join) {
            startActivity(new Intent(context, NewPlanActivity.class));
        } else if (view.getId() == R.id.tv_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Are you sure want to logout?");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SessionManager.setLogged(false);
                            Intent i = new Intent(context, WelcomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "PROFILE") {
            UserModel model = UserModel.fromJson(jsonObject.getJSONObject("data"));
            model.setWp_generate_token(SessionManager.getUser().getWp_generate_token());
            model.setId(SessionManager.getUser().getId());
            SessionManager.setUser(model);
            tv_name.setText(String.format("%s %s", model.getFirst_name(), model.getLast_name()));
            tv_email.setText(model.getUsername());
            tv_plan.setText(model.getLevel_name());
            iv_profile.setImageURI(model.getProfile_img());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }
}
