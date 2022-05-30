package com.sk.ultimateplayerhq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.PostDetailActivity;
import com.sk.ultimateplayerhq.activities.ShopActivity;
import com.sk.ultimateplayerhq.adapters.SessionAdapter;
import com.sk.ultimateplayerhq.adapters.ShopAdapter;
import com.sk.ultimateplayerhq.models.SessionModel;
import com.sk.ultimateplayerhq.models.ShopModel;
import com.sk.ultimateplayerhq.utils.EndlessRecyclerOnScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class ShopFragment extends BaseFragment {

    private RecyclerView rv_sessions;
    private List<ShopModel> list = new ArrayList<>();
    private ShopAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initUI(view);

        HashMap<String, String> param = new HashMap<>();
        communication.callPOST(Api.GET_PRODUCT_LIST, "GET_PRODUCT_LIST", param);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        SimpleDraweeView iv_banner = view.findViewById(R.id.iv_banner);
        iv_banner.setImageResource(R.drawable.shop);
        iv_banner.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rv_sessions = view.findViewById(R.id.rv_sessions);
         layoutManager = new LinearLayoutManager(context);
        rv_sessions.setLayoutManager(layoutManager);
        adapter = new ShopAdapter(context, list, new ShopAdapter.OnProductClick() {
            @Override
            public void onProductClick(ShopModel model) {
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                intent.putExtra("guid",model.getGuid());
                startActivity(intent);
            }
        });
        rv_sessions.setAdapter(adapter);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "GET_PRODUCT_LIST") {
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                list.add(ShopModel.fromJson(data.getJSONObject(i)));
            }

            adapter.notifyDataSetChanged();
        }
    }
}
