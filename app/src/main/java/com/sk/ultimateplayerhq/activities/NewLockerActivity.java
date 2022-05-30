package com.sk.ultimateplayerhq.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.fragments.LockerFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class NewLockerActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_loader_with_new_toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_loader_2, new LockerFragment());
       /* if (stack != null) {
            transaction.addToBackStack(stack);
        }*/
        transaction.commit();
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {

    }
}
