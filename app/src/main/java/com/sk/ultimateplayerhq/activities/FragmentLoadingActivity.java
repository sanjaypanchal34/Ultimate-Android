package com.sk.ultimateplayerhq.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.fragments.ChangePasswordFragment;
import com.sk.ultimateplayerhq.fragments.EditEmailFragment;
import com.sk.ultimateplayerhq.fragments.EditProfileFragment;
import com.sk.ultimateplayerhq.fragments.HomeFragment;
import com.sk.ultimateplayerhq.fragments.SearchFragment;
import com.sk.ultimateplayerhq.interfaces.OnBackPressListener;

public class FragmentLoadingActivity extends FragmentActivity implements OnBackPressListener {


    private Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_fragment_loader);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_loader, getFragment());
        transaction.commit();


    }

    private Fragment getFragment() {

        switch (getIntent().getStringExtra("fragment")) {
            case "EDIT_PROFILE":
                toolbar.setTitle("Edit Profile");
                return new EditProfileFragment();
            case "EDIT_EMAIL":
                toolbar.setTitle("Change Email");
                return new EditEmailFragment();
            case "CHANGE_PASSWORD":
                toolbar.setTitle("Change Password");
                return new ChangePasswordFragment();
            case "SEARCH":
                toolbar.setTitle("Search");
                return new SearchFragment();
            default:
                return new HomeFragment();
        }
    }


    @Override
    public void onBackPressClick(boolean b) {
        if (b) {
            onBackPressed();
        }
    }
}
