package com.sk.ultimateplayerhq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class LockerFragment extends BaseFragment {

    private RecyclerView rv_sessions;
    private List<SessionModel> list = new ArrayList<>();
    private SessionAdapter adapter;
    private GridLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locker_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", String.valueOf(SessionManager.getUser().getId()));
        communication.callPOST(Api.LOCKER_ROOM, "LOCKER_ROOM", param);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        rv_sessions = view.findViewById(R.id.rv_sessions);
        layoutManager = new GridLayoutManager(context, 2);
        rv_sessions.setLayoutManager(layoutManager);
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
        if (tag == "LOCKER_ROOM") {
            JSONArray data = jsonObject.getJSONArray("data");
            list.clear();
            for (int i = 0; i < data.length(); i++) {
                list.add(SessionModel.fromJson(data.getJSONObject(i)));
            }

            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", String.valueOf(SessionManager.getUser().getId()));
        communication.callPOST(Api.LOCKER_ROOM, "LOCKER_ROOM", param);
    }
}
