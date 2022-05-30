package com.sk.ultimateplayerhq.dialogs;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.activities.AddEventActivity;
import com.sk.ultimateplayerhq.activities.AddTrainingActivity;
import com.sk.ultimateplayerhq.activities.EventDetailActivity;
import com.sk.ultimateplayerhq.activities.TrainingDetailActivity;
import com.sk.ultimateplayerhq.adapters.EventAdapter;
import com.sk.ultimateplayerhq.adapters.PlayerNumberAdapter;
import com.sk.ultimateplayerhq.custom.calender.MyDate;
import com.sk.ultimateplayerhq.fragments.BaseDialogFragment;
import com.sk.ultimateplayerhq.models.EventModel;
import com.sk.ultimateplayerhq.utils.SessionManager;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.appsaint.communication.Api;
import in.appsaint.communication.Communication;
import in.appsaint.communication.OnCommunicationCallBack;

public class EventsOfDateDialog extends BaseDialogFragment implements OnCommunicationCallBack {

    private RecyclerView rv_events;
    private List<EventModel> list;
    private Date date = new Date();
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_events_of_date, container, false);
    }

    public void setData(MyDate date) {
        this.date = date.getDate();
        this.list = date.getEvents();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUi(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUi(View view) {
        ImageButton ib_close = view.findViewById(R.id.ib_close);
        TextView tv_date = view.findViewById(R.id.tv_date);
        tv_date.setText(new SimpleDateFormat("dd\nMMMM\nyyyy", Locale.ENGLISH).format(date));
        rv_events = view.findViewById(R.id.rv_events);
        rv_events.setLayoutManager(new LinearLayoutManager(getActivity()));
       adapter =  new EventAdapter(getActivity(), list, new EventAdapter.OnEventListener() {
            @Override
            public void onEventView(EventModel model, int position) {
                Intent intent;
                if (model.getIs_training() == 1) {
                    intent = new Intent(getActivity(), TrainingDetailActivity.class);
                }else{
                    intent = new Intent(getActivity(), EventDetailActivity.class);

                }
                intent.putExtra("id",String.valueOf(model.getId()));
                startActivity(intent);
            }

            @Override
            public void onEventDelete(EventModel model, int position) {
                MyAlert.show(getActivity(), "Are you sure want to delete?", new MyAlertDialogListener() {
                    @Override
                    public void onPositiveClick() {
                        HashMap<String, String> param = new HashMap<>();
                        communication.callPOST(Api.EVENT_DELETE,String.valueOf(model.getId()),"EVENT_DELETE",param,header);

                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
            }

            @Override
            public void onEventEdit(EventModel model, int position) {
                Intent intent = new Intent(getActivity(),model.getIs_training()==1?AddTrainingActivity.class: AddEventActivity.class);
                intent.putExtra("date",date.getTime());
                intent.putExtra("isEdit",true);
                intent.putExtra("updateId",model.getId());
                startActivity(intent);
            }

           @Override
           public PlayerNumberAdapter setPlayerAdapter(EventModel model, int position) {
               return new PlayerNumberAdapter(getActivity(), model.getPlayerData("unconfirm"), new PlayerNumberAdapter.OnSquadListener() {
                   @Override
                   public void onClick(EventModel.Player model, int position) {

                   }
               },Color.YELLOW);
           }

           @Override
           public PlayerNumberAdapter setAcceptedPlayerAdapter(EventModel model, int position) {
               return new PlayerNumberAdapter(getActivity(), model.getPlayerData("accept"), new PlayerNumberAdapter.OnSquadListener() {
                   @Override
                   public void onClick(EventModel.Player model, int position) {

                   }},Color.GREEN);
           }

           @Override
           public PlayerNumberAdapter setRejectedPlayerAdapter(EventModel model, int position) {
               return new PlayerNumberAdapter(getActivity(), model.getPlayerData("reject"), new PlayerNumberAdapter.OnSquadListener() {
                   @Override
                   public void onClick(EventModel.Player model, int position) {

                   }},Color.RED);
           }
       });
        rv_events.setAdapter(adapter);

        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ((TextView)view.findViewById(R.id.btn_add_training)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTrainingActivity.class);
                intent.putExtra("date",date.getTime());
                startActivity(intent);
            }
        });

        ((TextView)view.findViewById(R.id.btn_add_training)).setVisibility(SessionManager.isPlayerLogin()?View.GONE:View.VISIBLE);
        ((TextView)view.findViewById(R.id.btn_add_event)).setVisibility(SessionManager.isPlayerLogin()?View.GONE:View.VISIBLE);

        ((TextView)view.findViewById(R.id.btn_add_event)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                intent.putExtra("date",date.getTime());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communication = new Communication(getActivity(),this);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                dialog.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);//solves issue with statusbar
                dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            }
        }

    }

    @Override
    public void onResume() {
        HashMap<String, String> param = new HashMap<>();
        param.put("selected_date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH).format(date));
        communication.callGET(Api.EVENT_DATA_FROM_DATE,"?selected_date="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH).format(date),"EVENT_DATA_FROM_DATE",header);
        super.onResume();
    }


    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if(tag == "EVENT_DATA_FROM_DATE"){
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray events = data.getJSONArray("events");
            if(list==null){
                list = new ArrayList<>();
            }else{
                list.clear();
            }
            for(int i = 0;i<events.length();i++){
                list.add(new Gson().fromJson(events.getJSONObject(i).toString(),EventModel.class));
            }

            adapter.notifyDataSetChanged();
        }else if(tag == "EVENT_DELETE"){
            communication.callGET(Api.EVENT_DATA_FROM_DATE,"?selected_date="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH).format(date),"EVENT_DATA_FROM_DATE",header);

        }
    }


}
