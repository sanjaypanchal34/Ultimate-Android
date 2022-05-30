package com.sk.ultimateplayerhq.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.interfaces.OnBackPressListener;
import com.sk.ultimateplayerhq.utils.InputUtils;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import in.appsaint.communication.Api;
import in.appsaint.communication.PART;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 2022;
    private File uploadFile;
    private MaterialButton btn_file_pick;
    private TextView tv_file_name;
    private TextInputLayout in_f_name, in_l_name;
    private MaterialButton btn_update;

    private SimpleDraweeView iv_profile;
    private ImageView iv_edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);

        InputUtils.setText(SessionManager.getUser().getFirst_name(), in_f_name);
        InputUtils.setText(SessionManager.getUser().getLast_name(), in_l_name);
        iv_profile.setImageURI(SessionManager.getUser().getProfile_img());
        btn_file_pick.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {

        btn_file_pick = view.findViewById(R.id.btn_file_pick);
        tv_file_name = view.findViewById(R.id.tv_file_name);
        in_f_name = view.findViewById(R.id.in_f_name);
        in_l_name = view.findViewById(R.id.in_l_name);
        btn_update = view.findViewById(R.id.btn_update);
        iv_profile = view.findViewById(R.id.iv_profile);
        iv_edit = view.findViewById(R.id.iv_edit);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            uploadFile = new File(images.get(0).getPath());
            iv_profile.setImageURI(Uri.fromFile(uploadFile));
            tv_file_name.setText(uploadFile.getName());
            HashMap<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(SessionManager.getUser().getId()));
            communication.callPOST(Api.UPDATE_PROFILE_IMAGE, "UPDATE_PROFILE_IMAGE", param, new PART("user-avatar", uploadFile));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_edit) {
            ImagePicker.with(EditProfileFragment.this)                         //  Initialize ImagePicker with activity or fragment context
                    .setCameraOnly(false)               //  Camera mode
                    .setMultipleMode(false)              //  Select multiple images or single image
                    .setFolderMode(true)                //  Folder mode
                    .setShowCamera(true)                //  Show camera button
                    .setDoneTitle("Done")               //  Done button title
                    .setLimitMessage("You have reached selection limit")    // Selection limit message
                    .setMaxSize(10)                     //  Max images can be selected
                    .setSavePath("ImagePicker")         //  Image capture folder name
                    .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                    .setRequestCode(REQUEST_CODE_CHOOSE)                //  Set request code, default Config.RC_PICK_IMAGES
                    .setKeepScreenOn(true)              //  Keep screen on when selecting images
                    .start();
        } else if (view.getId() == R.id.btn_update) {
            if (InputUtils.isEmpty(in_f_name)) {
                InputUtils.setError(in_f_name, InputUtils.enter("First Name"));
            } else if (InputUtils.isEmpty(in_l_name)) {
                InputUtils.setError(in_l_name, InputUtils.enter("Last Name"));
            } else {
                HashMap<String, String> param = new HashMap<>();
                param.put("id", String.valueOf(SessionManager.getUser().getId()));
                param.put("wp_generate_token", SessionManager.getUser().getWp_generate_token());
                param.put("first_name", InputUtils.getText(in_f_name));
                param.put("last_name", InputUtils.getText(in_l_name));
                communication.callPOST(Api.UPDATE_PROFILE, "UPDATE_PROFILE", param);
            }
        }
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "UPDATE_PROFILE") {
            MyAlert.show(context, jsonObject.getString("msg"), false, new MyAlertDialogListener() {
                @Override
                public void onPositiveClick() {
                    listener.onBackPressClick(true);
                }

                @Override
                public void onNegativeClick() {

                }
            });
        } else if (tag == "UPDATE_PROFILE_IMAGE") {
            MyAlert.show(context, jsonObject.getString("msg"), false, new MyAlertDialogListener() {
                @Override
                public void onPositiveClick() {
                    listener.onBackPressClick(false);
                }

                @Override
                public void onNegativeClick() {

                }
            });
        }
    }


    private OnBackPressListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnBackPressListener) context;
    }
}
