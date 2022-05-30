package com.sk.ultimateplayerhq.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.interfaces.OnBackPressListener;
import com.sk.ultimateplayerhq.utils.InputUtils;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.appsaint.communication.Api;

public class EditEmailFragment extends BaseFragment {

    private TextInputLayout in_new_email, in_c_email;
    private MaterialButton btn_update;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_email, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InputUtils.isNotValidEmail(in_new_email)) {
                    InputUtils.setError(in_new_email, InputUtils.enterValid("New Email"));
                }/* else if (InputUtils.isNotValidEmail(in_c_email)) {
                    InputUtils.setError(in_c_email, InputUtils.enterValid("Current Email"));
                } else if (!InputUtils.getText(in_new_email).equals(InputUtils.getText(in_c_email))) {
                    InputUtils.setError(in_c_email, "Current Email Doesn't Match");
                }*/ else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("id", String.valueOf(SessionManager.getUser().getId()));
                    param.put("wp_generate_token", SessionManager.getUser().getWp_generate_token());
                    param.put("user_email", InputUtils.getText(in_new_email));
                    communication.callPOST(Api.UPDATE_EMAIL, "UPDATE_EMAIL", param);
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        in_new_email = view.findViewById(R.id.in_new_email);
        in_c_email = view.findViewById(R.id.in_c_email);
        btn_update = view.findViewById(R.id.btn_update);
    }

    private OnBackPressListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnBackPressListener) context;
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "UPDATE_EMAIL") {
            MyAlert.show(context, jsonObject.getString("msg"), false, new MyAlertDialogListener() {
                @Override
                public void onPositiveClick() {
                    listener.onBackPressClick(true);
                }

                @Override
                public void onNegativeClick() {

                }
            });
        }
    }
}
