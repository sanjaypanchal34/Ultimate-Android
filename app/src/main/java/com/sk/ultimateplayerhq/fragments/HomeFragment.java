package com.sk.ultimateplayerhq.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.PostDetailActivity;
import com.sk.ultimateplayerhq.adapters.HomeAdapter;
import com.sk.ultimateplayerhq.interfaces.OnLoadFragmentListener;
import com.sk.ultimateplayerhq.models.HomeModel;
import com.sk.ultimateplayerhq.models.SessionModel;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class HomeFragment extends BaseFragment {
    private RecyclerView rv_home;
    private HomeAdapter adapter;
    private List<HomeModel> list = new ArrayList<>();
    private String app_banner_url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);

        communication.callPOST(Api.HOME_SCREEN, "HOME", new HashMap<>());
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        rv_home = view.findViewById(R.id.rv_home);
        rv_home.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeAdapter(getActivity(), list, new HomeAdapter.OnHomeAdapterListener() {
            @Override
            public void onSessionClick(SessionModel model) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", String.valueOf(model.getID()));
                startActivity(intent);
            }
        });
        rv_home.setAdapter(adapter);
    }

    private OnLoadFragmentListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (OnLoadFragmentListener) context;
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        app_banner_url = jsonObject.optString("app_banner_url");
        SessionManager.setHomeBanner(app_banner_url);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray latest = data.getJSONArray("latest");
        JSONArray popular = data.getJSONArray("popular");
        list.clear();
        List<SessionModel> latestList = new ArrayList<>();
        List<SessionModel> popularList = new ArrayList<>();
        for (int i = 0; i < latest.length(); i++) {
            latestList.add(SessionModel.fromJson(latest.getJSONObject(i)));
        }

        for (int i = 0; i < popular.length(); i++) {
            popularList.add(SessionModel.fromJson(popular.getJSONObject(i)));
        }


        Log.e("LATEST:::", String.valueOf(popularList.size()));
        list.add(new HomeModel("Latest Sessions", latestList));
        list.add(new HomeModel("Latest Sessions", latestList));
        list.add(new HomeModel("Most Popular Sessions", popularList));


        adapter.notifyDataSetChanged();
    }
}
