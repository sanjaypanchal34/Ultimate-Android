package com.sk.ultimateplayerhq.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.ChatAdapter;
import com.sk.ultimateplayerhq.dialogs.ChangeGroupNameDialog;
import com.sk.ultimateplayerhq.models.ChatModel;
import com.sk.ultimateplayerhq.utils.RealPathUtil;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.appsaint.communication.Api;

public class ChatScreen extends BaseActivity {

    private static final int REQUEST_CODE_CHOOSE = 2322;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 5623;
    private final List<ChatModel> list = new ArrayList<>();
    Channel channel;
    private RecyclerView rv_chat;
    private ChatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Pusher pusher;
    private EditText edt_text;
    private ImageView iv_attach;
    private Button btn_send;
    private File uploadFile;
    private String media_key;
    private int uniqID;
    private Toolbar toolbar;
    private TextView tv_title;

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        HttpAuthorizer authorizer = new HttpAuthorizer(Api.C_HOST + "pusher/auth");
        Map<String, String> secret = new HashMap<>();
        secret.put("secret", Api.CHAT_SECRET.toString());
        secret.put("Authorization", SessionManager.getToken());
        authorizer.setHeaders(secret);

        toolbar = findViewById(R.id.toolbar);
        tv_title = toolbar.findViewById(R.id.tv_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.opt_edit) {
                    ChangeGroupNameDialog dialog = new ChangeGroupNameDialog();
                    dialog.setTitle(tv_title.getText().toString());
                    dialog.setListener(new ChangeGroupNameDialog.OnNameListener() {
                        @Override
                        public void onChange(String name) {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("name",name);
                            communication.callPOST(Api.CHAT_NAME_UPDATE,"","CHAT_NAME_UPDATE",param,header);
                        }
                    });
                    dialog.show(getSupportFragmentManager(),"name CHnage");
                }
                return false;
            }
        });


        toolbar.getMenu().getItem(0).setVisible(!SessionManager.isPlayerLogin());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initUI();


        communication.callGET(Api.CHAT, "CHAT", header);

        PusherOptions options = new PusherOptions();
        options.setCluster(Api.CHAT_CLUSTER.toString());
        options.setAuthorizer(authorizer);

        pusher = new Pusher(Api.API_KEY_CHAT.toString(), options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);


        edt_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_send.setEnabled(!s.toString().trim().isEmpty());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> param = new HashMap<>();
                param.put("type", "string");
                param.put("message", edt_text.getText().toString());
                edt_text.setText("");
                communication.callPOST(Api.SEND_CHAT_MSG, "", "SEND_CHAT_MSG", param, header);
            }
        });


        iv_attach.setOnClickListener(new View.OnClickListener() {
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
                                        ImagePicker.with(ChatScreen.this)                         //  Initialize ImagePicker with activity or fragment context
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

    }

    void bindToEvents() {
        channel.bind(Api.EVENT_NAME.toString(), new PrivateChannelEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.e("AAA", event.getData());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(event.getData());
                            if (jsonObject.optString("action_type").equalsIgnoreCase("chat")) {
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                SimpleDateFormat ddMMYY = new SimpleDateFormat(
                                        "dd-MM-yyyy HH:mm", Locale.ENGLISH);
                                ChatModel model = new ChatModel(jsonObject.optString("message"), jsonObject.optString("sender_name"), ddMMYY.format(format.parse(jsonObject.optString("msg_time"))), jsonObject.optString("msg_type"),
                                        jsonObject.optString("file_url"), jsonObject.optInt("senderId"), jsonObject.optString("thumbnail_presignedUrl"), jsonObject.optString("jersey_number"), jsonObject.optString("user_role"));
                                list.add(model);
                                adapter.notifyDataSetChanged();
                                layoutManager.scrollToPosition(list.size() - 1);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAuthenticationFailure(String string, Exception ex) {
            }

            @Override
            public void onSubscriptionSucceeded(String string) {
            }
        });
    }

    private void initUI() {
        rv_chat = findViewById(R.id.rv_chat);
        edt_text = findViewById(R.id.edt_text);
        iv_attach = findViewById(R.id.iv_attach);
        btn_send = findViewById(R.id.btn_send);
        layoutManager = new LinearLayoutManager(context);
        rv_chat.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(context, list, new ChatAdapter.OnChatListener() {
            @Override
            public void onViewClick(ChatModel model, int position) {
                if (model.getType() != null && model.getType().equalsIgnoreCase("image")) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("image", model.getUrl());
                    startActivity(intent);
                } else if (model.getType() != null && model.getType().equalsIgnoreCase("video")) {
                    Intent intent = new Intent(context, Highlightvideoplayer.class);
                    intent.putExtra("video", model.getUrl());
                    startActivity(intent);
                }
            }
        });
        rv_chat.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "CHAT") {
            JSONObject data = jsonObject.getJSONObject("data");
            uniqID = data.getInt("unique");
            tv_title.setText(data.optString("chatName"));


            JSONArray chat = data.getJSONArray("chat");
            for (int i = 0; i < chat.length(); i++) {
                Log.e("CHATL::::", chat.getJSONObject(i).toString());
                list.add(new Gson().fromJson(chat.getJSONObject(i).toString(), ChatModel.class));
            }
            channel = pusher.subscribePrivate(Api.CHANNEL_NAME.toString() + uniqID, new PrivateChannelEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    Log.e("BBB", event.getData());
                }

                @Override
                public void onSubscriptionSucceeded(String s) {
                    bindToEvents();
                }

                @Override
                public void onAuthenticationFailure(String s, Exception e) {
                }

            });
            adapter.notifyDataSetChanged();
            layoutManager.scrollToPosition(list.size() - 1);


        } else if (tag == "SEND_CHAT_IMAGE") {
            JSONObject data = jsonObject.getJSONObject("data");
            media_key = data.optString("media_key");
            if (uploadFile != null) {
                communication.uploadAWS(data.optString("url"), "AWS", uploadFile);
            }
        } else if (tag == "SEND_CHAT_VIDEO") {
            JSONObject data = jsonObject.getJSONObject("data");
            media_key = data.optString("media_key");
            if (uploadFile != null) {
                communication.uploadAWS(data.optString("url"), "AWS", uploadFile);
            }
        } else if (tag == "AWS") {
            HashMap<String, String> param = new HashMap<>();
            param.put("media_key", media_key);
            param.put("type", isImageFile(media_key) ? "image" : "video");
            communication.callPOST(Api.SEND_CHAT_MEDIA_LINK, "", "SEND_CHAT_MEDIA_LINK", param, header);
        } else if (tag == "CHAT_NAME_UPDATE") {
            tv_title.setText(jsonObject.getString("data"));
        } else if (tag == "SEND_CHAT_MSG") {
//            edt_text.requestFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HashMap<String, String> param = new HashMap<>();
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            uploadFile = new File(images.get(0).getPath());
            param.put("file_type", "image");
            param.put("file_name", System.currentTimeMillis() + uploadFile.getName());
            communication.callPOST(Api.SEND_CHAT_IMAGE, "", "SEND_CHAT_IMAGE", param, header);
        } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK && data != null) {
            uploadFile = new File(RealPathUtil.getRealPath(context, data.getData()));
            param.put("file_type", "video");
            param.put("file_name", System.currentTimeMillis() + uploadFile.getName());
            communication.callPOST(Api.SEND_CHAT_VIDEO, "", "SEND_CHAT_VIDEO", param, header);

        }
    }

    @Override
    public void onBackPressed() {
        pusher.unsubscribe(Api.CHANNEL_NAME.toString() + uniqID);
        pusher.disconnect();
        super.onBackPressed();
    }
}
