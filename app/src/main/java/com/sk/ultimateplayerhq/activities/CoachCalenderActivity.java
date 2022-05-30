package com.sk.ultimateplayerhq.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.custom.calender.CustomCalenderView;
import com.sk.ultimateplayerhq.custom.calender.MyDate;
import com.sk.ultimateplayerhq.custom.calender.OnCalenderListener;
import com.sk.ultimateplayerhq.dialogs.EventsOfDateDialog;
import com.sk.ultimateplayerhq.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CoachCalenderActivity extends BaseActivity {

    private CustomCalenderView custom_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_calender);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
         custom_calender = findViewById(R.id.custom_calender);

        /*custom_calender.setMaxDate(new Date());*/
        /*custom_calender.setMinDate(minDate);*/
        custom_calender.setSelectedDate(new Date());
        custom_calender.setFragmentManager(getSupportFragmentManager(), new OnCalenderListener() {
            @Override
            public void onDateSelect(MyDate myDate) {
                EventsOfDateDialog dialog = new EventsOfDateDialog();
                dialog.setData(myDate);
                dialog.show(getSupportFragmentManager(),"event");
            }

            @Override
            public void onNextMonth() {

            }

            @Override
            public void onPreviousMonth() {

            }
        });
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {

    }

    @Override
    protected void onResume() {
        if(custom_calender!=null){
         custom_calender.post(new Runnable() {
             @Override
             public void run() {
                 custom_calender.refreshData();
             }
         });
        }
        super.onResume();
    }
}