package com.sk.ultimateplayerhq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.CommentAdapter;
import com.sk.ultimateplayerhq.models.CommentModel;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class PostDetailActivity extends BaseActivity {

    private TextView tv_title,tv_count,no_comments;
    private SimpleDraweeView iv_thumb;
    private String video_id;
    private RecyclerView rv_comments;
    private CommentAdapter adapter;
    private List<CommentModel> list = new ArrayList<>();
    private TextView tv_add_comment;
    private EditText edt_comment;
    private MaterialButton btn_send;
    private ImageView iv_like;
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initUI();

        iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video_id != null) {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("id", video_id);
                    startActivity(intent);
                }
            }
        });

        tv_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_send.setVisibility(View.VISIBLE);
                edt_comment.setVisibility(View.VISIBLE);
            }
        });

        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> param = new HashMap<>();
                param.put("drill_id", getIntent().getStringExtra("id"));
                param.put("user_id", String.valueOf(SessionManager.getUser().getId()));
                param.put("type", isLiked?"unlike":"like");
                communication.callPOST(Api.LIKE_DISLIKE_POST, "LIKE_DISLIKE_POST", param);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_comment.getText().toString().trim().isEmpty()){
                    edt_comment.setError("Enter Comment");
                    edt_comment.requestFocus();
                }else{
                    btn_send.setVisibility(View.GONE);
                    edt_comment.setVisibility(View.GONE);
                    HashMap<String, String> param = new HashMap<>();
                    param.put("drill_id", getIntent().getStringExtra("id"));
                    param.put("user_id", String.valueOf(SessionManager.getUser().getId()));
                    param.put("comment_content", edt_comment.getText().toString().trim());
                    communication.callPOST(Api.ADD_COMMENT, "ADD_COMMENT", param);
                    edt_comment.setText("");
                }
            }
        });
        HashMap<String, String> param = new HashMap<>();
        param.put("drill_id", getIntent().getStringExtra("id"));
        param.put("user_id", String.valueOf(SessionManager.getUser().getId()));
        communication.callPOST(Api.POST_DETAIL, "POST_DETAIL", param);
        communication.callPOST(Api.POST_COMMENTS, "POST_COMMENTS", param);
    }

    private void initUI() {
        iv_like = findViewById(R.id.iv_like);
        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommentAdapter(context, list, new CommentAdapter.OnNotificationListener() {
            @Override
            public void onNotificationClick(CommentModel model) {

            }
        });
        rv_comments.setAdapter(adapter);
        tv_add_comment = findViewById(R.id.tv_add_comment);
        tv_title = findViewById(R.id.tv_title);
        no_comments = findViewById(R.id.no_comments);
        tv_count = findViewById(R.id.tv_count);
        iv_thumb = findViewById(R.id.iv_thumb);
        edt_comment = findViewById(R.id.edt_comment);
        btn_send = findViewById(R.id.btn_send);

        tv_count.setText(getResources().getString(R.string.comments_d,adapter.getItemCount()));
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "POST_DETAIL") {
            JSONObject data = jsonObject.getJSONObject("data");

            video_id = data.getString("video_id");
            tv_title.setText(data.getString("post_title"));
            iv_thumb.setImageURI(data.getString("thumbnail_url"));
            isLiked = data.getInt("favourite") == 1;
            iv_like.setImageResource(data.getInt("favourite") == 1?R.drawable.ic_baseline_favorite_24:R.drawable.ic_baseline_favorite_border_24);
        } else if (tag == "POST_COMMENTS") {
            JSONArray data = jsonObject.getJSONArray("data");
            list.clear();
            for (int i = 0; i < data.length(); i++) {
                list.add(CommentModel.fromJson(data.getJSONObject(i)));
            }
            adapter.notifyDataSetChanged();
            tv_count.setText(getResources().getString(R.string.comments_d,adapter.getItemCount()));
            no_comments.setVisibility(adapter.getItemCount() == 0?View.VISIBLE:View.GONE);
        }else if(tag == "ADD_COMMENT"){
            HashMap<String, String> param = new HashMap<>();
            param.put("drill_id", getIntent().getStringExtra("id"));
            communication.callPOST(Api.POST_COMMENTS, "POST_COMMENTS", param);
        }else if(tag == "LIKE_DISLIKE_POST"){
            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            isLiked = !isLiked;
            iv_like.setImageResource(isLiked?R.drawable.ic_baseline_favorite_24:R.drawable.ic_baseline_favorite_border_24);
        }
    }
}