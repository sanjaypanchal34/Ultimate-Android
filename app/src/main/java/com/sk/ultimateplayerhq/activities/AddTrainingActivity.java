package com.sk.ultimateplayerhq.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.MultiSelectAdapter;
import com.sk.ultimateplayerhq.adapters.MultiSessionAdapter;
import com.sk.ultimateplayerhq.dialogs.MultiSelectDialog;
import com.sk.ultimateplayerhq.models.MultiSelectModel;
import com.sk.ultimateplayerhq.models.PlayerModel;
import com.sk.ultimateplayerhq.models.SessionModel;
import com.sk.ultimateplayerhq.utils.alert.MyAlert;
import com.sk.ultimateplayerhq.utils.alert.MyAlertDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.appsaint.communication.Api;
import in.appsaint.communication.PART;

public class AddTrainingActivity extends BaseActivity {

    private static final int REQUEST_CODE_CHOOSE = 2356;
    private final List<MultiSelectModel<PlayerModel>> list = new ArrayList<>();
    private final List<MultiSelectModel<SessionModel>> sessionList = new ArrayList<>();
    private final List<MultiSelectModel<PlayerModel>> selectedPLayerlist = new ArrayList<>();
    private final List<MultiSelectModel<SessionModel>> selectedSessionlist = new ArrayList<>();
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat yyMMFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    TextView tv_date, tv_players, tv_session, edt_from, edt_to, tv_date_pick_lable, tv_date_pick;
    private long date;
    private Toolbar toolbar;
    private MaterialButton btn_add_session, btn_save;
    private EditText edt_title, edt_location, edt_desc;
    private Calendar fromTime, toTime;
    private RecyclerView rv_players, rv_sessions;
    private MultiSelectAdapter<PlayerModel> fullPlayerAdapter;
    private MultiSelectAdapter<SessionModel> fullSessionAdapter;
    private MultiSessionAdapter selectedSessionAdapter;
    private RadioButton rd_mon, rd_week;
    private String selectedRepeat = "";
    private MaterialButton btn_upload;
    private TextView tv_file_name;
    private File uploadFile;
    private boolean isEdit;
    private int id;
    private SimpleDraweeView iv_logo;
    private ImageButton ib_refresh_high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);
        fromTime = Calendar.getInstance();
        toTime = Calendar.getInstance();
        id = getIntent().getIntExtra("updateId", -1);
        date = getIntent().getLongExtra("date", -1);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
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
        fullSessionAdapter = new MultiSelectAdapter<SessionModel>(context, sessionList) {
            @Override
            protected int getItem() {
                return R.layout.item_multi;
            }

            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(sessionList.get(position).getItem().getThumbnail_url());
                holder.tv.setText(sessionList.get(position).getItem().getPost_title());
                holder.cb.setChecked(sessionList.get(position).isSelected());
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onMultiClick(MultiSelectModel<SessionModel> selected) {
                if (selectedSessionlist.contains(selected)) {
                    selected.setSelected(false);
                    selectedSessionlist.remove(selected);
                } else {
                    selectedSessionlist.add(selected);
                    selected.setSelected(true);
                }

                notifyDataSetChanged();
                selectedSessionAdapter.notifyDataSetChanged();
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

        tv_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog dialog = new MultiSelectDialog();
                dialog.setAdapter(fullSessionAdapter);
                dialog.show(getSupportFragmentManager(), "SessionModel");
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
        })
        ;

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
                } else if (!selectedRepeat.trim().isEmpty() && tv_date_pick.getText().toString().isEmpty()) {
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
                    param.put("player", getPlayer());
                    param.putAll(getSessionParam());
                   /* param.put("event_session", getMatchDay());

                    param.put("session_title", edt_score.getText().toString());
                    param.put("session_image", getGoalScorer());
                    param.put("session_id", getAssists());
                    param.put("session_desc", );*/

                    Log.e("PARAM::", param.toString());
                    if (isEdit) {

                        communication.callPOST(Api.CALENDER_TRAINING_UPDATE, String.valueOf(id), "CALENDER_TRAINING_UPDATE", param, header, new PART("image", uploadFile));
                    } else {
                        param.put("repeat_till_date", selectedRepeat.isEmpty() ? "" : tv_date_pick.getText().toString());
                        param.put("repeat_event", selectedRepeat);
                        communication.callPOST(Api.CALENDER_TRAINIG_CRAETE, "CALENDER_TRAINIG_CRAETE", param, header, new PART("image", uploadFile));
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


        ib_refresh_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communication.callGET(Api.SESSION_LIST, "SESSION_LIST_REFRESH", header);
            }
        });

        communication.callGET(Api.SESSION_LIST, "SESSION_LIST", header);
    }

    private HashMap<String, String> getSessionParam() {
        List<String> session_title = new ArrayList<>();
        List<String> session_image = new ArrayList<>();
        List<String> session_id = new ArrayList<>();
        List<String> session_desc = new ArrayList<>();
        for (int i = 0; i < selectedSessionlist.size(); i++) {
            session_title.add(String.valueOf(selectedSessionlist.get(i).getItem().getPost_title()));
            session_image.add(String.valueOf(selectedSessionlist.get(i).getItem().getThumbnail_url()));
            session_id.add(String.valueOf(selectedSessionlist.get(i).getItem().getID()));
            session_desc.add(String.valueOf(selectedSessionlist.get(i).getItem().getPost_content()));
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("session_title", new JSONArray(session_title).toString());
        param.put("session_image", new JSONArray(session_image).toString());
        param.put("session_id", new JSONArray(session_id).toString());
        param.put("event_session", new JSONArray(session_id).toString());
        param.put("session_desc", new JSONArray(session_desc).toString());
        return param;
    }

    private String getPlayer() {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < selectedPLayerlist.size(); i++) {
            temp.add(String.valueOf(selectedPLayerlist.get(i).getItem().getId()));
        }
        return new JSONArray(temp).toString();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "SESSION_LIST") {
            sessionList.clear();
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                sessionList.add(new MultiSelectModel<SessionModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), SessionModel.class)));
            }

            communication.callGET(Api.EVENT_PLAYER_LIST, "EVENT_PLAYER_LIST", header);

        } else  if (tag == "SESSION_LIST_REFRESH") {
            sessionList.clear();
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                sessionList.add(new MultiSelectModel<SessionModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), SessionModel.class)));
            }
            fullSessionAdapter.notifyDataSetChanged();

        } else if (tag == "EVENT_PLAYER_LIST") {
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                list.add(new MultiSelectModel<PlayerModel>(false, new Gson().fromJson(data.getJSONObject(i).toString(), PlayerModel.class)));
            }
            if (isEdit) {
                communication.callGET(Api.TRAINING_DETAIL_FOR_EDIT, String.valueOf(id), "TRAINING_DETAIL_FOR_EDIT", header);
            }
        } else if (tag == "CALENDER_TRAINING_UPDATE" || tag == "CALENDER_TRAINIG_CRAETE") {
            MyAlert.show(context, jsonObject.optString("msg"), false, new MyAlertDialogListener() {
                @Override
                public void onPositiveClick() {
                    onBackPressed();
                }

                @Override
                public void onNegativeClick() {

                }
            });

        } else if (tag == "TRAINING_DETAIL_FOR_EDIT") {
            JSONObject data = jsonObject.getJSONObject("data");

            JSONObject eventsDetails = data.getJSONObject("eventsDetails");
            edt_title.setText(eventsDetails.optString("title"));
            edt_location.setText(eventsDetails.optString("location"));
            edt_desc.setText(eventsDetails.isNull("description")?"":eventsDetails.optString("description"));
            tv_file_name.setText(eventsDetails.isNull("logo_image") ? "" : new File(eventsDetails.optString("logo_image")).getName());
            iv_logo.setImageURI(eventsDetails.optString("logo_image"));
            edt_from.setText(data.optString("start_time"));
            edt_to.setText(data.optString("end_time"));


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

            JSONArray training_sessions = data.getJSONArray("wp_session_ids");
            JSONArray training_sessions_for_data = data.getJSONArray("training_sessions");
            for (int i = 0; i < training_sessions.length(); i++) {
                SessionModel sessionModel = new SessionModel(Integer.parseInt(training_sessions.getString(i)));
                MultiSelectModel<SessionModel> selectedSession = new MultiSelectModel<SessionModel>(true, sessionModel);
                if (sessionList.contains(selectedSession)) {
                    int pos = sessionList.indexOf(selectedSession);
                    if (pos >= 0) {
                        sessionModel = sessionList.get(pos).getItem();
                        sessionModel.setPost_content(training_sessions_for_data.getJSONObject(i).optString("session_description"));
                        selectedSession.setItem(sessionModel);
                        selectedSessionlist.add(selectedSession);
                    }
                }
            }
            selectedSessionAdapter.notifyDataSetChanged();
        }
    }

    private void initUI() {
        iv_logo = findViewById(R.id.iv_logo);
        toolbar = findViewById(R.id.toolbar);
        tv_date_pick_lable = findViewById(R.id.tv_date_pick_lable);
        tv_date_pick = findViewById(R.id.tv_date_pick);
        rd_mon = findViewById(R.id.rd_mon);
        rd_week = findViewById(R.id.rd_week);
        tv_date = findViewById(R.id.tv_date);
        edt_title = findViewById(R.id.edt_title);
        edt_from = findViewById(R.id.edt_from);
        edt_to = findViewById(R.id.edt_to);
        edt_location = findViewById(R.id.edt_location);
        edt_desc = findViewById(R.id.edt_desc);
        btn_add_session = findViewById(R.id.btn_add_session);
        tv_session = findViewById(R.id.tv_session);
        tv_players = findViewById(R.id.tv_players);
        btn_save = findViewById(R.id.btn_save);
        rv_players = findViewById(R.id.rv_players);
        rv_sessions = findViewById(R.id.rv_sessions);
        btn_upload = findViewById(R.id.btn_upload);
        ib_refresh_high = findViewById(R.id.ib_refresh_high);
        tv_file_name = findViewById(R.id.tv_file_name);
        rv_sessions.setLayoutManager(new LinearLayoutManager(this));
       /* selectedPlayerAdapter = new MultiSelectAdapter<PlayerModel>(context, selectedPLayerlist) {
            @Override
            protected int getItem() {
                return R.layout.item_multi_white;
            }

            @SuppressLint("RestrictedApi")
            @Override
            protected void bind(ViewHolder holder, int position) {
                holder.iv.setImageURI(selectedPLayerlist.get(position).getItem().getProfileUrl());
                holder.tv.setText(selectedPLayerlist.get(position).getItem().getUser().getName());
                holder.cb.setChecked(selectedPLayerlist.get(position).isSelected());
            }

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
            }
        };
        rv_players.setAdapter(selectedPlayerAdapter);*/

        btn_add_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddNewSessionActivity.class));
            }
        });

        selectedSessionAdapter = new MultiSessionAdapter(context, selectedSessionlist, new MultiSessionAdapter.OnSessionListener() {
            @Override
            public void onSessionClick(MultiSelectModel<SessionModel> model) {
                Intent intent = new Intent(context,ImageViewActivity.class);
                intent.putExtra("image",model.getItem().getThumbnail_url());
                startActivity(intent);
            }
        });
        rv_sessions.setAdapter(selectedSessionAdapter);


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddTrainingActivity.this)                         //  Initialize ImagePicker with activity or fragment context
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (isEdit) {
            rd_mon.setVisibility(View.GONE);
            rd_week.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_till_date_lable)).setVisibility(View.GONE);
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