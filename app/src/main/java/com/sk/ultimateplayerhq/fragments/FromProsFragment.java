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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class FromProsFragment extends BaseFragment {

    private RecyclerView rv_sessions;
    private List<SessionModel> list = new ArrayList<>();
    private SessionAdapter adapter;
    private GridLayoutManager layoutManager;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

         bundle = getArguments();
        if (bundle == null) {
            return;
        }
        initUI(view);
       /* HashMap<String, String> param = new HashMap<>();
        param.put("category_slug", bundle.getString("slug"));
        param.put("paged", "1");
        communication.callPOST(Api.POST_BY_CATEGORY, "POST_BY_CATEGORY", param);*/
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        rv_sessions = view.findViewById(R.id.rv_sessions);
         layoutManager = new GridLayoutManager(context, 2);
        rv_sessions.setLayoutManager(layoutManager);
        rv_sessions.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
//                HashMap<String, String> param = new HashMap<>();
//                param.put("category_slug", bundle.getString("slug"));
//                param.put("paged", String.valueOf(current_page));
//                communication.callPOST(Api.POST_BY_CATEGORY, "POST_BY_CATEGORY", param);
            }
        });
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
        if (tag == "POST_BY_CATEGORY") {
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                list.add(SessionModel.fromJson(data.getJSONObject(i)));
            }

            adapter.notifyDataSetChanged();
        }
    }
}
