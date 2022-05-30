package com.sk.ultimateplayerhq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.ChatScreen;
import com.sk.ultimateplayerhq.activities.CoachCalenderActivity;
import com.sk.ultimateplayerhq.activities.HighlightActivity;
import com.sk.ultimateplayerhq.activities.MatchDayActivity;
import com.sk.ultimateplayerhq.activities.NewLockerActivity;
import com.sk.ultimateplayerhq.activities.SquadActivity;
import com.sk.ultimateplayerhq.utils.UrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import in.appsaint.communication.Api;

public class CoachOfficeFragment extends BaseFragment{

    private RelativeLayout rl_squad,rl_calender,rl_match_day,rl_white_board,rl_fav,rl_analysis,rl_highlight,rl_chat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coach_office, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);

        rl_squad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SquadActivity.class));
            }
        });

        rl_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CoachCalenderActivity.class));
            }
        });

        rl_match_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MatchDayActivity.class);
                intent.putExtra("url", Api.C_BASE_URL +"matchday-planner-redirect");
                startActivity(intent);
            }
        });

        rl_white_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MatchDayActivity.class);
                intent.putExtra("url",  UrlUtils.get("whiteboard"));
                startActivity(intent);
            }
        });
        rl_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MatchDayActivity.class);
                intent.putExtra("url",   UrlUtils.get("analysis-room"));
                startActivity(intent);
            }
        });
        rl_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), NewLockerActivity.class));
            }
        });

        rl_highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), HighlightActivity.class));
            }
        });rl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getActivity(), ChatScreen.class));
            }
        });
        super.onViewCreated(view, savedInstanceState);

    }

    private void initUI(View view) {
        rl_squad = view.findViewById(R.id.rl_squad);
        rl_calender = view.findViewById(R.id.rl_calender);
        rl_match_day = view.findViewById(R.id.rl_match_day);
        rl_white_board = view.findViewById(R.id.rl_white_board);
        rl_fav = view.findViewById(R.id.rl_fav);
        rl_analysis = view.findViewById(R.id.rl_analysis);
        rl_highlight = view.findViewById(R.id.rl_highlight);
        rl_chat = view.findViewById(R.id.rl_chat);
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {

    }
}
