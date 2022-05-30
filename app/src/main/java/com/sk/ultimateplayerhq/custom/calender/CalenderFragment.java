package com.sk.ultimateplayerhq.custom.calender;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.fragments.BaseFragment;
import com.sk.ultimateplayerhq.models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.appsaint.communication.Api;

public class CalenderFragment extends BaseFragment implements CustomCalenderView.OnMonthChangeListener {

    private TextView tv_month;
    private RecyclerView rv_calender;
    Calendar cal;
    Calendar calMin;
    Calendar calMax;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private CalenderAdapter adapter;
    private Calendar selected;
    private MyDate chooseDate;
    private ImageView iv_back_month, iv_next_month;
    private final List<MyDate> dayValueInCells = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cal = (Calendar) getArguments().getSerializable("cal");
        calMin = (Calendar) getArguments().getSerializable("min");
        calMax = (Calendar) getArguments().getSerializable("max");
        selected = (Calendar) getArguments().getSerializable("selected");
        initUI(view);


        iv_next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNextMonth();
                }
            }
        });
        iv_back_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPreviousMonth();
                }
            }
        });

        HashMap<String, String> param = new HashMap<>();
        param.put("month", String.valueOf(cal.get(Calendar.MONTH)));
        param.put("year", String.valueOf(cal.get(Calendar.YEAR)));
        communication.callGET(Api.EVENTS, "?month=" + (cal.get(Calendar.MONTH) + 1) + "&year=" + cal.get(Calendar.YEAR), "EVENTS", header);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(View view) {
        tv_month = view.findViewById(R.id.tv_month);
        iv_back_month = view.findViewById(R.id.iv_back_month);
        iv_next_month = view.findViewById(R.id.iv_next_month);
        rv_calender = view.findViewById(R.id.rv_calender);
        rv_calender.setLayoutManager(new GridLayoutManager(getActivity(), 7));



    }

    private void addtoList(Calendar cal, List<EventModel> tempList) throws ParseException {
        dayValueInCells.clear();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            List<EventModel> eventOfDate = new ArrayList<>();
            boolean isEventIn=false;
            boolean isTrainingIn=false;
            for (EventModel model : tempList) {
                Date compareDate = startFormat.parse(model.getStart());
                assert compareDate != null;
                if (compareFormat.format(compareDate).equals(compareFormat.format(mCal.getTime()))) {
                    if(model.getIs_training()==1){
                        isTrainingIn = true;
                    }else{
                        isEventIn = true;
                    }
                    eventOfDate.add(model);
                }
            }
            MyDate myDate = new MyDate(mCal.getTime(), isSelected(mCal), eventOfDate,isEventIn,isTrainingIn);
            if (mCal.getTime().equals(selected.getTime())) {
                chooseDate = myDate;
            }
            dayValueInCells.add(myDate);
            mCal.add(Calendar.DAY_OF_MONTH, 1);

            SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
            String month_name = month_date.format(cal.getTime());
            tv_month.setText(month_name);
        }

        adapter = new CalenderAdapter(getActivity(), dayValueInCells, cal, calMin,
                calMax);
        adapter.setListener(new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate date) {
                adapter.notifyDataSetChanged();
                chooseDate = date;
                if (listener != null) {
                    listener.onDateSelect(chooseDate == null ? new MyDate(selected.getTime(), true, new ArrayList<>(), false, false) : chooseDate);
                }
            }

            @Override
            public void onNextMonth() {
                if (listener != null) {
                    listener.onNextMonth();
                }
            }

            @Override
            public void onPreviousMonth() {
                if (listener != null) {
                    listener.onPreviousMonth();
                }
            }
        });
        rv_calender.setAdapter(adapter);

    }

    private boolean isSelected(Calendar mCal) {
        if (mCal == null) {
            return false;
        } else {
            return false;
        }

    }

    private OnCalenderListener listener;

    public void setListener(OnCalenderListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSetMonth(Calendar cal) {
        this.cal = cal;
        /*List<MyDate> dayValueInCells = new ArrayList<MyDate>();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(new MyDate(mCal.getTime(), isSelected(mCal)));
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        adapter = new CalenderAdapter(getActivity(), dayValueInCells, cal, calMin, calMax);
        adapter.setListener(new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate date) {
                adapter.notifyDataSetChanged();
                chooseDate = date;
                if (listener != null) {
                    listener.onDateSelect(chooseDate == null ? new MyDate(selected.getTime(), true) : chooseDate);
                }
            }

            @Override
            public void onNextMonth() {
                if (listener != null) {
                    listener.onNextMonth();
                }
            }

            @Override
            public void onPreviousMonth() {
                if (listener != null) {
                    listener.onPreviousMonth();
                }
            }
        });
        rv_calender.setAdapter(adapter);

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        String month_name = month_date.format(cal.getTime());
        tv_month.setText(month_name);*/


        HashMap<String, String> param = new HashMap<>();
        param.put("month", String.valueOf(cal.get(Calendar.MONTH)));
        param.put("year", String.valueOf(cal.get(Calendar.YEAR)));
        communication.callGET(Api.EVENTS, "?month=" + (cal.get(Calendar.MONTH) + 1) + "&year=" + cal.get(Calendar.YEAR), "EVENTS", header);

    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "EVENTS") {
            JSONArray data = jsonObject.getJSONArray("data");
            List<EventModel> tempList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                tempList.add(EventModel.fromJson(data.getJSONObject(i)));
            }
            try {
                addtoList(cal, tempList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
