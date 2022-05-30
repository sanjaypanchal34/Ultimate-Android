package com.sk.ultimateplayerhq.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.models.HighlightModel;
import com.sk.ultimateplayerhq.utils.RealPathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class CreateHighlightActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 2355;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 2033;
    private  boolean isEdit = false;
    private Toolbar toolbar;
    private EditText edt_title;
    private MaterialButton btn_select, btn_save;
    private File uploadFile;
    private SimpleDraweeView iv_high;
    private String media_key;
    private HighlightModel model;
    private ImageButton iv_clear;

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_highlight);
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        model = (HighlightModel) getIntent().getSerializableExtra("data");

        initUI();

        if(isEdit){
            edt_title.setText(model.getName());
            iv_clear.setVisibility(View.VISIBLE);
            if(model.getType().equalsIgnoreCase("video")){
                iv_high.setImageURI(model.getV_thumbnail_url());
            }else {
                iv_high.setImageURI(model.getUrl());
            }
        }else{
            iv_clear.setVisibility(View.GONE);
        }


        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_high.setActualImageResource(0);
                uploadFile = null;
                iv_clear.setVisibility(View.GONE);
            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(context).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


                            // title of the alert dialog
                            alertDialog.setTitle("Select Media");

                            // list of the items to be displayed to
                            // the user in the form of list
                            // so that user can select the item from
                            final String[] listItems = new String[]{"Image", "Video"};

                            // the function setSingleChoiceItems is the function which builds
                            // the alert dialog with the single item selection
                            alertDialog.setItems(listItems, new DialogInterface.OnClickListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        ImagePicker.with(CreateHighlightActivity.this)                         //  Initialize ImagePicker with activity or fragment context
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
                                    } else {
                                        Intent intent = new Intent();
                                        intent.setType("video/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                                    }
                                    dialog.dismiss();
                                }
                            });

                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog customAlertDialog = alertDialog.create();

                            // show the alert dialog when the button is clicked
                            customAlertDialog.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_title.getText().toString().trim().isEmpty()) {
                    edt_title.requestFocus();
                    edt_title.setError("Enter Name");
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("highlightsName", edt_title.getText().toString());


                    if (isEdit) {
                        param.put("edit_highlight_id", String.valueOf(model.getHighlight_id()));
                       if(uploadFile==null){
                           param.put("file_type",model.getType());
                       }else{
                           param.put("fileName", System.currentTimeMillis() + uploadFile.getName());
                           param.put("file_type", isImageFile(uploadFile.getPath()) ? "image" : "video");
                       }

                        communication.callPOST(Api.UPDATE_MEDIA, "", "SEND_MEDIA", param, header);
                    } else if (uploadFile != null) {
                        param.put("fileName", System.currentTimeMillis() + uploadFile.getName());
                        param.put("file_type", isImageFile(uploadFile.getPath()) ? "image" : "video");
                        communication.callPOST(Api.SEND_MEDIA, "", "SEND_MEDIA", param, header);
                    } else {
                        Toast.makeText(context, "Select Media", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initUI() {
        iv_clear = findViewById(R.id.iv_clear);
        toolbar = findViewById(R.id.toolbar);
        edt_title = findViewById(R.id.edt_title);
        btn_select = findViewById(R.id.btn_select);
        btn_save = findViewById(R.id.btn_save);
        iv_high = findViewById(R.id.iv_high);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "SEND_MEDIA") {
            JSONObject data = jsonObject.getJSONObject("data");
            media_key = data.optString("media_key");
            if(uploadFile!=null) {
                communication.uploadAWS(data.optString("url"), "AWS", uploadFile);
            }else {
                onBackPressed();
            }
        } else if (tag == "SEND_MEDIA_LINK") {
            onBackPressed();

        } else if (tag == "AWS") {
            Log.e("FROM", String.valueOf(tag));
           if(isImageFile(media_key)){
               HashMap<String, String> param = new HashMap<>();
               param.put("media_key", media_key);
               param.put("highlight_name", edt_title.getText().toString());
              if(isEdit){
                  param.put("highlight_id", String.valueOf(model.getHighlight_id()));
                  communication.callPOST(Api.UPDATE_MEDIA_LINK, "", "SEND_MEDIA_LINK", param, header);
              }else {
                  communication.callPOST(Api.SEND_MEDIA_LINK, "", "SEND_MEDIA_LINK", param, header);
              }
           }else{
               onBackPressed();
           }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            uploadFile = new File(images.get(0).getPath());
            if (uploadFile.getName().endsWith("jpg") || uploadFile.getName().endsWith("png") || uploadFile.getName().endsWith("jpeg")) {
                iv_high.setImageURI(Uri.fromFile(uploadFile));
                iv_clear.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK && data != null) {
            uploadFile = new File(RealPathUtil.getRealPath(context, data.getData()));
            iv_high.setImageURI(Uri.fromFile(uploadFile));
            iv_clear.setVisibility(View.VISIBLE);

        }
    }
}
