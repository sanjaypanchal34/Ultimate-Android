package com.sk.ultimateplayerhq.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.AddCoachActivity;
import com.sk.ultimateplayerhq.activities.AddPlayerActivity;
import com.sk.ultimateplayerhq.adapters.CoachAdapter;
import com.sk.ultimateplayerhq.models.CoachModel;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class CoachListFragment extends BaseFragment{
    List<CoachModel> squadList = new ArrayList<>();
    private RecyclerView rv_team;
    private CoachAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rv_team = view.findViewById(R.id.rv_team);
        rv_team.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CoachAdapter(getActivity(), squadList, new CoachAdapter.OnSquadListener() {
            @Override
            public void onEditClick(CoachModel model, int position) {
                Intent intent = new Intent(getActivity(), AddCoachActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("id",model.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(CoachModel model, int position) {
                MyAlert.show(getActivity(), String.format("Are you sure want to delete %s?", model.getName()), new MyAlertDialogListener() {
                    @Override
                    public void onPositiveClick() {
                        communication.callPOST(Api.COACH_DELETE,model.getId(),"COACH_DELETE",new HashMap<>(),header);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
            }
        });
        rv_team.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);

    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "COACH") {
            JSONArray data = jsonObject.getJSONArray("data");
            squadList.clear();
            for(int i=0;i<data.length();i++){
                squadList.add(CoachModel.fromJson(data.getJSONObject(i)));
            }
            adapter.notifyDataSetChanged();
        }else  if (tag == "COACH_DELETE") {
            getSquad();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSquad();
    }



    private void getSquad() {
        communication.callGET(Api.COACH, "COACH", header);
    }

}
