package com.sk.ultimateplayerhq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
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
import com.sk.ultimateplayerhq.adapters.HighlightAdapter;
import com.sk.ultimateplayerhq.models.ChatModel;
import com.sk.ultimateplayerhq.models.HighlightModel;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.appsaint.communication.Api;

public class HighlightActivity extends BaseActivity {

    private final List<HighlightModel> list = new ArrayList<>();
    private RecyclerView rv_highlight;
    private TextView tv_no_data;
    private HighlightAdapter adapter;
    private Pusher pusher;

    Channel channel;
    private int uniqID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight);
        Log.e("AUTH:::",Api.C_HOST+"/pusher/auth");
        HttpAuthorizer authorizer = new HttpAuthorizer(Api.C_HOST+"pusher/auth");
        Map<String, String> secret = new HashMap<>();
        secret.put("secret", Api.CHAT_SECRET.toString());
        secret.put("Authorization", SessionManager.getToken());
        authorizer.setHeaders(secret);
        initUI();

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

        communication.callGET(Api.HIGHLIGHTS, "HIGHLIGHTS", header);
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
                            if (jsonObject.optString("action_type").equalsIgnoreCase("highlights")) {
                                communication.callGET(Api.HIGHLIGHTS, "HIGHLIGHTS2", header);
                            }
                        } catch (JSONException e) {
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
        ((LinearLayout) findViewById(R.id.ln_upload)).setVisibility(SessionManager.isPlayerLogin() ? View.GONE : View.VISIBLE);
        ((TextView) findViewById(R.id.btn_upload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,CreateHighlightActivity.class));
            }
        });

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rv_highlight = findViewById(R.id.rv_highlight);
        tv_no_data = findViewById(R.id.tv_no_data);
        rv_highlight.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new HighlightAdapter(context, list, new HighlightAdapter.OnHighlightListener() {
            @Override
            public void onEditClick(HighlightModel model, int position) {
                Intent intent = new Intent(context,CreateHighlightActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("data",model);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(HighlightModel model, int position) {
                MyAlert.show(context, "Are you sure want to delete?", new MyAlertDialogListener() {
                    @Override
                    public void onPositiveClick() {
                        HashMap<String, String> param = new HashMap<>();
                        param.put("highlight_id", String.valueOf(model.getHighlight_id()));
                        param.put("_method", "delete");
                        communication.callPOST(Api.HIGHLIGHTS_DELETE, "", "HIGHLIGHTS_DELETE", param, header);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
            }

            @Override
            public void onViewClick(HighlightModel model, int position) {
                Intent intent;
                if(model.getType().equalsIgnoreCase("video")){
                    intent = new Intent(context, Highlightvideoplayer.class);
                        intent.putExtra("video",model.getUrl());
                }else{
                    intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("image",model.getUrl());
                }
                startActivity(intent);
            }
        });
        rv_highlight.setAdapter(adapter);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "HIGHLIGHTS") {
            list.clear();
            JSONArray highlight = jsonObject.getJSONObject("data").getJSONArray("highlight");
             uniqID =  jsonObject.getJSONObject("data").getInt("unique");
            for (int i = 0; i < highlight.length(); i++) {
                list.add(new Gson().fromJson(highlight.getJSONObject(i).toString(), HighlightModel.class));
            }

            adapter.notifyDataSetChanged();

            tv_no_data.setVisibility(adapter.getItemCount()==0?View.VISIBLE:View.GONE);
            channel = pusher.subscribePrivate(Api.CHANNEL_NAME.toString()+uniqID, new PrivateChannelEventListener() {
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
        } else if (tag == "HIGHLIGHTS2") {
            list.clear();
            JSONArray highlight = jsonObject.getJSONObject("data").getJSONArray("highlight");
            for (int i = 0; i < highlight.length(); i++) {
                list.add(new Gson().fromJson(highlight.getJSONObject(i).toString(), HighlightModel.class));
            }

            adapter.notifyDataSetChanged();
            tv_no_data.setVisibility(adapter.getItemCount()==0?View.VISIBLE:View.GONE);

        } else if (tag == "HIGHLIGHTS_DELETE") {
            communication.callGET(Api.HIGHLIGHTS, "HIGHLIGHTS2", header);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        communication.callGET(Api.HIGHLIGHTS, "HIGHLIGHTS2", header);
    }


    @Override
    public void onBackPressed() {
        pusher.unsubscribe(Api.CHANNEL_NAME.toString()+uniqID);
        pusher.disconnect();
        super.onBackPressed();
    }
}