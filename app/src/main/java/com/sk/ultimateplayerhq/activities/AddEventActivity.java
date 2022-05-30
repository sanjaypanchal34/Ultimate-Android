package com.sk.ultimateplayerhq.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.MultiSelectAdapter;
import com.sk.ultimateplayerhq.dialogs.MultiSelectDialog;
import com.sk.ultimateplayerhq.models.MatchPlannerModel;
import com.sk.ultimateplayerhq.models.MultiSelectModel;
import com.sk.ultimateplayerhq.models.PlayerModel;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.appsaint.communication.Api;
import in.appsaint.communication.PART;

public class AddEventActivity extends BaseActivity {
    private static final int REQUEST_CODE_CHOOSE = 2036;
    private final List<MultiSelectModel<PlayerModel>> list = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> assitsList = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> scorerList = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> playerOfMatchList = new ArrayList<>();
    private final List<MultiSelectModel<MatchPlannerModel>> matchDayList = new ArrayList<>();
    private final List<MultiSelectModel<MatchPlannerModel>> selectedMatchDayList = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> selectedPLayerlist = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> selectedAssistslist = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> selectedScorerlist = new ArrayList<>();
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat yyMMFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private Toolbar toolbar;
    private TextView tv_date, edt_from, edt_to, tv_players, edt_assists, edt_goal_scorer, tv_player_of_match, tv_match_day, tv_date_pick_lable, tv_date_pick;
    private EditText edt_title, edt_location, edt_desc, edt_score;
    private RadioButton rd_mon, rd_week;
    private MaterialButton btn_save;
    private Calendar fromTime, toTime;
    private long date;
    private MultiSelectAdapter<PlayerModel> fullPlayerAdapter, fullAssitsAdapter, fullScorerAdapter, fullPlayerOfMatch;
    private String selectedRepeat = "";
    private MultiSelectAdapter<MatchPlannerModel> fullMatchPlannerAdapter;
    private boolean isEdit;
    private int id;
    private MultiSelectModel<PlayerModel> selectedPlayerOfMatch;
    private MultiSelectModel<MatchPlannerModel> selectedMatchDay;
    private MaterialButton btn_upload;
    private TextView tv_file_name;
    private File uploadFile;
    private SimpleDraweeView iv_logo;
    private Spinner sp_result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        fromTime = Calendar.getInstance();
        toTime = Calendar.getInstance();
        date = getIntent().getLongExtra("date", -1);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        id = getIntent().getIntExtra("updateId", -1);
        initUI();
        tv_date.setText(String.format("Event Date:- %s", new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH).format(new Date(date))));

        edt_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = fromTime.get(Calendar.HOUR_OF_DAY);
                int minute = fromTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_from.setText(String.format(Locale.ENGLISH, "%02d:%02d", selectedHour, selectedMinute));
                        fromTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        fromTime.set(Calendar.MINUTE, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select From");
                mTimePicker.show();
            }
        });
        edt_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = toTime.get(Calendar.HOUR_OF_DAY);
                int minute = toTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_to.setText(String.format(Locale.ENGLISH, "%02d:%02d", selectedHour, selectedMinute));
                        toTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        toTime.set(Calendar.MINUTE, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select To");
                mTimePicker.show();
            }
        });

        fullPlayerAdapter = new MultiSelectAdapter<PlayerModel>(context, list) {
            @Override
            protected int getItem() {
                return R.layout.item_multi;
            }

            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(list.get(position).getItem().getProfileUrl());
                holder.tv.setText(list.get(position).getItem().getUser().getName());
                holder.cb.setChecked(list.get(position).isSelected());
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onMultiClick(MultiSelectModel<PlayerModel> selected) {
                if (selectedPLayerlist.contains(selected)) {
                    selected.setSelected(false);
                    selectedPLayerlist.remove(selected);
                } else {
                    selectedPLayerlist.add(selected);
                    selected.setSelected(true);
                }

                notifyDataSetChanged();

                tv_players.setText(String.format(Locale.ENGLISH, "Selected %d/%d", selectedPLayerlist.size(), fullPlayerAdapter.getItemCount()));
            }
        };
        fullAssitsAdapter = new MultiSelectAdapter<PlayerModel>(context, assitsList) {
            @Override
            protected int getItem() {
                return R.layout.item_multi;
            }

            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(assitsList.get(position).getItem().getProfileUrl());
                holder.tv.setText(assitsList.get(position).getItem().getUser().getName());
                holder.cb.setChecked(assitsList.get(position).isSelected());
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onMultiClick(MultiSelectModel<PlayerModel> selected) {
                if (selectedAssistslist.contains(selected)) {
                    selected.setSelected(false);
                    selectedAssistslist.remove(selected);
                } else {
                    selectedAssistslist.add(selected);
                    selected.setSelected(true);
                }

                notifyDataSetChanged();
                edt_assists.setText(String.format(Locale.ENGLISH, "Selected %d/%d", selectedAssistslist.size(), fullAssitsAdapter.getItemCount()));
            }
        };
        fullScorerAdapter = new MultiSelectAdapter<PlayerModel>(context, scorerList) {
            @Override
            protected int getItem() {
                return R.layout.item_multi;
            }

            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(scorerList.get(position).getItem().getProfileUrl());
                holder.tv.setText(scorerList.get(position).getItem().getUser().getName());
                holder.cb.setChecked(scorerList.get(position).isSelected());
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onMultiClick(MultiSelectModel<PlayerModel> selected) {
                if (selectedScorerlist.contains(selected)) {
                    selected.setSelected(false);
                    selectedScorerlist.remove(selected);
                } else {
                    selectedScorerlist.add(selected);
                    selected.setSelected(true);
                }

                notifyDataSetChanged();
                edt_goal_scorer.setText(String.format(Locale.ENGLISH, "Selected %d/%d", selectedScorerlist.size(), fullScorerAdapter.getItemCount()));
            }
        };


        fullPlayerOfMatch = new MultiSelectAdapter<PlayerModel>(context, playerOfMatchList) {
            @Override
            protected int getItem() {
                return R.layout.item_multi;
            }

            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(playerOfMatchList.get(position).getItem().getProfileUrl());
                holder.tv.setText(playerOfMatchList.get(position).getItem().getUser().getName());
                if (selectedPlayerOfMatch == null) {
                    holder.cb.setChecked(false);
                } else {

                    holder.cb.setChecked(selectedPlayerOfMatch.getItem().getId() == playerOfMatchList.get(position).getItem().getId());
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onMultiClick(MultiSelectModel<PlayerModel> selected) {
                selectedPlayerOfMatch = selected;

                notifyDataSetChanged();
                tv_player_of_match.setText(selectedPlayerOfMatch.getItem().getUser().getName());
            }
        };


        fullMatchPlannerAdapter = new MultiSelectAdapter<MatchPlannerModel>(context, matchDayList) {
            @Override
            protected int getItem() {
                return R.layout.item_multi;
            }

            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(matchDayList.get(position).getItem().getLineup_image());
                holder.tv.setText(matchDayList.get(position).getItem().getLineup_name());
                if (selectedMatchDay == null) {
                    holder.cb.setChecked(false);
                } else {

                    holder.cb.setChecked(selectedMatchDay.getItem().getId() == matchDayList.get(position).getItem().getId());
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onMultiClick(MultiSelectModel<MatchPlannerModel> selected) {
                selectedMatchDay = selected;

                notifyDataSetChanged();
                tv_match_day.setText(selectedMatchDay.getItem().getLineup_name());
            }
        };


        tv_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog dialog = new MultiSelectDialog();
                dialog.setAdapter(fullPlayerAdapter);
                dialog.show(getSupportFragmentManager(), "players");
            }
        });


        tv_match_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog dialog = new MultiSelectDialog();
                dialog.setAdapter(fullMatchPlannerAdapter);
                dialog.show(getSupportFragmentManager(), "planner");
            }
        });

        edt_goal_scorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog dialog = new MultiSelectDialog();
                dialog.setAdapter(fullScorerAdapter);
                dialog.show(getSupportFragmentManager(), "scorer");
            }
        });

        edt_assists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog dialog = new MultiSelectDialog();
                dialog.setAdapter(fullAssitsAdapter);
                dialog.show(getSupportFragmentManager(), "assits");
            }
        });

        tv_player_of_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog dialog = new MultiSelectDialog();
                dialog.setAdapter(fullPlayerOfMatch);
                dialog.show(getSupportFragmentManager(), "matchof");
            }
        });

        tv_date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar newCalendar = Calendar.getInstance();
                final DatePickerDialog StartTime = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tv_date_pick.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                StartTime.getDatePicker().setMinDate(new Date().getTime());
                StartTime.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_title.getText().toString().isEmpty()) {
                    edt_title.requestFocus();
                    edt_title.setError("Please Enter Title.");
                } else if (edt_from.getText().toString().isEmpty()) {
                    edt_from.requestFocus();
                    edt_from.setError("Please Select From Time.");
                } else if (edt_to.getText().toString().isEmpty()) {
                    edt_to.requestFocus();
                    edt_to.setError("Please Select To Time.");
                } else if (edt_location.getText().toString().isEmpty()) {
                    edt_location.requestFocus();
                    edt_location.setError("Please Enter Location.");
                } else if (selectedPLayerlist.size() == 0) {
                    tv_players.requestFocus();
                    tv_players.setError("Please Select Player.");
                    Toast.makeText(context, "Please Select Player.", Toast.LENGTH_SHORT).show();
                } else if (!selectedRepeat.trim().isEmpty()  && tv_date_pick.getText().toString().isEmpty()) {
                    tv_date_pick.requestFocus();
                    tv_date_pick.setError("Please Select Till Date.");
                    Toast.makeText(context, "Please Select Till Date.", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar today = Calendar.getInstance();
                    today.setTime(new Date(date));

                    HashMap<String, String> param = new HashMap<>();
                    param.put("event_start_time", yyMMFormatter.format(today.getTime()));
                    today.add(Calendar.DATE, 1);
                    param.put("event_end_time", yyMMFormatter.format(today.getTime()));
                    param.put("event_title", edt_title.getText().toString());
                    param.put("event_time_from", edt_from.getText().toString());
                    param.put("event_time_to", edt_to.getText().toString());
                    param.put("location", edt_location.getText().toString());
                    param.put("description", edt_desc.getText().toString());
                    param.put("won_or_lost", sp_result.getSelectedItem().toString().toLowerCase());
                    param.put("player", getPlayer());
                    param.put("matchday", getMatchDay());

                    param.put("score", edt_score.getText().toString());
                    param.put("goalscorers", getGoalScorer());
                    param.put("assists", getAssists());
                    param.put("player_of_match", selectedPlayerOfMatch == null ? "" : String.valueOf(selectedPlayerOfMatch.getItem().getId()));
                    Log.e("PARAM::", param.toString());
                    //param.put("image", edt_jersey_num.getText().toString());
                    if (isEdit) {

                        communication.callPOST(Api.CALENDER_MATCH_UPDATE, String.valueOf(id), "CALENDER_MATCH_UPDATE", param, header, new PART("image", uploadFile));
                    } else {
                        param.put("repeat_till_date", selectedRepeat.isEmpty() ? "" : tv_date_pick.getText().toString());
                        param.put("repeat_event", selectedRepeat);
                        communication.callPOST(Api.CALENDER_MATCH_CREATE, "CALENDER_MATCH_CREATE", param, header, new PART("image", uploadFile));
                    }
                }
            }
        });

        rd_mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRepeat.equalsIgnoreCase("repeat_monthly")) {
                    selectedRepeat = "";
                    rd_mon.setChecked(false);
                    tv_date_pick_lable.setVisibility(View.GONE);
                    tv_date_pick.setVisibility(View.GONE);
                } else {
                    selectedRepeat = "repeat_monthly";
                    rd_mon.setChecked(true);
                    rd_week.setChecked(false);
                    tv_date_pick_lable.setVisibility(View.VISIBLE);
                    tv_date_pick.setVisibility(View.VISIBLE);
                }
            }
        });

        rd_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRepeat.equalsIgnoreCase("repeat_weekly")) {
                    selectedRepeat = "";
                    rd_week.setChecked(false);
                    tv_date_pick.setVisibility(View.GONE);
                    tv_date_pick_lable.setVisibility(View.GONE);
                } else {
                    selectedRepeat = "repeat_weekly";
                    rd_week.setChecked(true);
                    rd_mon.setChecked(false);
                    tv_date_pick_lable.setVisibility(View.VISIBLE);
                    tv_date_pick.setVisibility(View.VISIBLE);
                }
            }
        });
        communication.callGET(Api.EVENT_PLAYER_LIST, "EVENT_PLAYER_LIST", header);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private String getAssists() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < selectedAssistslist.size(); i++) {
            temp.add(String.valueOf(selectedAssistslist.get(i).getItem().getId()));
        }
        return new JSONArray(temp).toString();
    }

    private String getGoalScorer() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < selectedScorerlist.size(); i++) {
            temp.add(String.valueOf(selectedScorerlist.get(i).getItem().getId()));
        }
        return new JSONArray(temp).toString();
    }

    private String getMatchDay() {
        if (selectedMatchDay == null) {
            return "";
        } else
            return String.valueOf(selectedMatchDay.getItem().getId());
    }

    private String getPlayer() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < selectedPLayerlist.size(); i++) {
            temp.add(String.valueOf(selectedPLayerlist.get(i).getItem().getId()));
        }
        return new JSONArray(temp).toString();
    }

    private void initUI() {
        iv_logo = findViewById(R.id.iv_logo);
        toolbar = findViewById(R.id.toolbar);
        tv_date = findViewById(R.id.tv_date);
        edt_title = findViewById(R.id.edt_title);
        edt_from = findViewById(R.id.edt_from);
        edt_to = findViewById(R.id.edt_to);
        edt_location = findViewById(R.id.edt_location);
        edt_desc = findViewById(R.id.edt_desc);
        tv_players = findViewById(R.id.tv_players);
        tv_match_day = findViewById(R.id.tv_match_day);
        edt_score = findViewById(R.id.edt_score);
        edt_assists = findViewById(R.id.edt_assists);
        edt_goal_scorer = findViewById(R.id.edt_goal_scorer);
        tv_player_of_match = findViewById(R.id.tv_player_of_match);
        rd_mon = findViewById(R.id.rd_mon);
        rd_week = findViewById(R.id.rd_week);
        tv_date_pick_lable = findViewById(R.id.tv_date_pick_lable);
        tv_date_pick = findViewById(R.id.tv_date_pick);
        btn_save = findViewById(R.id.btn_save);
        btn_upload = findViewById(R.id.btn_upload);
        tv_file_name = findViewById(R.id.tv_file_name);
        sp_result = findViewById(R.id.sp_result);

        ArrayAdapter<String> resultAdapter = new ArrayAdapter<String>(AddEventActivity.this,
                R.layout.item_spinner, getResources().getStringArray(R.array.result)){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.parseColor("#50ffffff"));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                tv.setBackgroundColor(getResources().getColor(R.color.purple));
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view =  super.getView(position, convertView, parent);

                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.parseColor("#50ffffff"));
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        resultAdapter.setDropDownViewResource(R.layout.item_spinner);

        sp_result.setAdapter(resultAdapter);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddEventActivity.this)                         //  Initialize ImagePicker with activity or fragment context
                        .setCameraOnly(false)               //  Camera mode
                        .setMultipleMode(false)              //  Select multiple images or single image
                        .setFolderMode(true)                //  Folder mode
                        .setShowCamera(true)                //  Show camera button
                        .setDoneTitle("Done")               //  Done button title
                        .setLimitMessage("You have reached selection limit")    // Selection limit message
                        .setMaxSize(10)                     //  Max images can be selected
                        .setSavePath("ImagePicker")         //  Image capture folder name
                        .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                        .setRequestCode(REQUEST_CODE_CHOOSE)                //  Set request code, default Config.RC_PICK_IMAGES
                        .setKeepScreenOn(true)              //  Keep screen on when selecting images
                        .start();
            }
        });


        if (isEdit) {
            rd_mon.setVisibility(View.GONE);
            rd_week.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_till_date_lable)).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "CALENDER_MATCH_UPDATE" || tag == "CALENDER_MATCH_CREATE") {
            MyAlert.show(context, jsonObject.optString("msg"), false, new MyAlertDialogListener() {
                @Override
                public void onPositiveClick() {
                    onBackPressed();
                }

                @Override
                public void onNegativeClick() {

                }
            });
        } else if (tag == "EVENT_PLAYER_LIST") {
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                list.add(new MultiSelectModel<PlayerModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), PlayerModel.class)));
                assitsList.add(new MultiSelectModel<PlayerModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), PlayerModel.class)));
                scorerList.add(new MultiSelectModel<PlayerModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), PlayerModel.class)));
                playerOfMatchList.add(new MultiSelectModel<PlayerModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), PlayerModel.class)));
            }

            communication.callGET(Api.MATCH_DAY_PLANNER, "MATCH_DAY_PLANNER", header);

        } else if (tag == "MATCH_DAY_PLANNER") {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray mPlanner_data = data.getJSONArray("mPlanner_data");

            for (int i = 0; i < mPlanner_data.length(); i++) {
                matchDayList.add(new MultiSelectModel<MatchPlannerModel>(false, new Gson().fromJson(mPlanner_data.getJSONObject(i).toString(), MatchPlannerModel.class)));
            }

            if (isEdit) {
                communication.callGET(Api.EVENT_DETAIL_FOR_EDIT, String.valueOf(id), "EVENT_DETAIL_FOR_EDIT", header);
            }

        } else if (tag == "EVENT_DETAIL_FOR_EDIT") {
            JSONObject data = jsonObject.getJSONObject("data");

            JSONObject eventsDetails = data.getJSONObject("eventsDetails");
            edt_title.setText(eventsDetails.optString("title"));
            edt_location.setText(eventsDetails.optString("location"));
            edt_desc.setText(eventsDetails.isNull("description")?"":eventsDetails.optString("description"));
            edt_score.setText(eventsDetails.isNull("score") ? "" : eventsDetails.optString("score"));
            tv_file_name.setText(eventsDetails.isNull("logo_image") ? "" : new File(eventsDetails.optString("logo_image")).getName());
            iv_logo.setImageURI(eventsDetails.optString("logo_image"));
            edt_from.setText(data.optString("start_time"));
            tv_player_of_match.setText(eventsDetails.optString("player_of_match_name"));
            edt_to.setText(data.optString("end_time"));
            if(!eventsDetails.isNull("won_lost")){
                String  retStr = eventsDetails.optString("won_lost").substring(0, 1).toUpperCase() + eventsDetails.optString("won_lost").substring(1);
                sp_result.setSelection(Arrays.asList(getResources().getStringArray(R.array.result)).indexOf(retStr));
            }
            for (MultiSelectModel<MatchPlannerModel> model : matchDayList) {
                if (eventsDetails.optString("match_day").equalsIgnoreCase(String.valueOf(model.getItem().getId()))) {
                    selectedMatchDay = model;
                }
            }
            tv_match_day.setText(selectedMatchDay != null ? selectedMatchDay.getItem().getLineup_name() : "");

            JSONArray player_ids = data.getJSONArray("player_ids");
            for (int i = 0; i < player_ids.length(); i++) {
                PlayerModel player = new PlayerModel(player_ids.getInt(i));
                MultiSelectModel<PlayerModel> selectedPlayer = new MultiSelectModel<PlayerModel>(true, player);
                if (list.contains(selectedPlayer)) {
                    int pos = list.indexOf(selectedPlayer);
                    if (pos >= 0) {
                        list.get(pos).setSelected(true);
                        selectedPLayerlist.add(selectedPlayer);
                    }
                }
            }
            tv_players.setText(String.format(Locale.ENGLISH, "Selected %d/%d", selectedPLayerlist.size(), fullPlayerAdapter.getItemCount()));

            JSONArray assits_ids = data.getJSONArray("assits_ids");
            for (int i = 0; i < assits_ids.length(); i++) {
                PlayerModel player = new PlayerModel(assits_ids.getInt(i));
                MultiSelectModel<PlayerModel> selectedPlayer = new MultiSelectModel<PlayerModel>(true, player);
                if (assitsList.contains(selectedPlayer)) {
                    int pos = assitsList.indexOf(selectedPlayer);
                    if (pos >= 0) {
                        assitsList.get(pos).setSelected(true);
                        selectedAssistslist.add(selectedPlayer);
                    }
                }
            }
            edt_assists.setText(String.format(Locale.ENGLISH, "Selected %d/%d", selectedAssistslist.size(), fullAssitsAdapter.getItemCount()));

            JSONArray mp_ids = data.getJSONArray("mp_ids");
            for (int i = 0; i < mp_ids.length(); i++) {
                PlayerModel player = new PlayerModel(mp_ids.getInt(i));
                MultiSelectModel<PlayerModel> selectedPlayer = new MultiSelectModel<PlayerModel>(true, player);
                if (scorerList.contains(selectedPlayer)) {
                    int pos = scorerList.indexOf(selectedPlayer);
                    if (pos >= 0) {
                        scorerList.get(pos).setSelected(true);
                        selectedScorerlist.add(selectedPlayer);
                    }
                }
            }
            edt_goal_scorer.setText(String.format(Locale.ENGLISH, "Selected %d/%d", selectedScorerlist.size(), fullScorerAdapter.getItemCount()));

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            uploadFile = new File(images.get(0).getPath());
            tv_file_name.setText(uploadFile.getName());
            iv_logo.setImageURI(Uri.fromFile(uploadFile));
        }
    }
}
