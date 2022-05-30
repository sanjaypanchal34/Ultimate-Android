package com.sk.ultimateplayerhq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.PostDetailActivity;
import com.sk.ultimateplayerhq.adapters.SessionAdapter;
import com.sk.ultimateplayerhq.models.SessionModel;
import com.sk.ultimateplayerhq.utils.EndlessRecyclerOnScrollListener;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class SearchFragment extends BaseFragment {

    private RecyclerView rv_sessions;
    private List<SessionModel> list = new ArrayList<>();
    private SessionAdapter adapter;
    private GridLayoutManager layoutManager;
    private TextInputEditText edt_search;
    private EndlessRecyclerOnScrollListener scrollListener;
    private boolean next_page = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(v.getText().toString().trim().isEmpty()){
                        list.clear();
                        scrollListener.reset();
                        adapter.notifyDataSetChanged();
                    }else{
                        list.clear();
                        scrollListener.reset();
                        adapter.notifyDataSetChanged();
                        HashMap<String, String> param = new HashMap<>();
                        param.put("search", v.getText().toString().trim());
                        param.put("paged", "1");
                        communication.callPOST(Api.SEARCH, "SEARCH", param);
                    }
                    return true;
                }
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        edt_search = view.findViewById(R.id.edt_search);
        rv_sessions = view.findViewById(R.id.rv_sessions);
        layoutManager = new GridLayoutManager(context, 2);
        rv_sessions.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if(next_page) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("search", edt_search.getText().toString().trim());
                    param.put("paged", String.valueOf(current_page));
                    communication.callPOST(Api.SEARCH, "SEARCH", param);
                }
            }
        };
        rv_sessions.addOnScrollListener(scrollListener);
        adapter = new SessionAdapter(context, list, new SessionAdapter.OnSessionListener() {
            @Override
            public void onSessionClick(SessionModel model) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", String.valueOf(model.getID()));
                startActivity(intent);
            }
        });
        rv_sessions.setAdapter(adapter);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "SEARCH") {
            JSONArray data = jsonObject.getJSONArray("data");
            next_page  = jsonObject.getBoolean("next_page");
            for (int i = 0; i < data.length(); i++) {
                list.add(SessionModel.fromJson(data.getJSONObject(i)));
            }

            adapter.notifyDataSetChanged();
        }
    }
}
