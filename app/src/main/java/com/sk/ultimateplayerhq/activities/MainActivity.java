package com.sk.ultimateplayerhq.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.sk.ultimateplayerhq.R;
import com.sk.ultimateplayerhq.adapters.CategoryAdapter;
import com.sk.ultimateplayerhq.adapters.HomeAdapter;
import com.sk.ultimateplayerhq.fragments.ChangePasswordFragment;
import com.sk.ultimateplayerhq.fragments.CoachOfficeFragment;
import com.sk.ultimateplayerhq.fragments.EditProfileFragment;
import com.sk.ultimateplayerhq.fragments.FromProsFragment;
import com.sk.ultimateplayerhq.fragments.HomeFragment;
import com.sk.ultimateplayerhq.fragments.LockerFragment;
import com.sk.ultimateplayerhq.fragments.MyAccountFragment;
import com.sk.ultimateplayerhq.fragments.SessionsFragment;
import com.sk.ultimateplayerhq.fragments.ShopFragment;
import com.sk.ultimateplayerhq.fragments.SubSessionsFragment;
import com.sk.ultimateplayerhq.fragments.WebFragment;
import com.sk.ultimateplayerhq.interfaces.OnLoadFragmentListener;
import com.sk.ultimateplayerhq.models.CategoryModel;
import com.sk.ultimateplayerhq.models.SubCategoryModel;
import com.sk.ultimateplayerhq.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.appsaint.communication.Api;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnLoadFragmentListener, CategoryAdapter.OnCategoryListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private RecyclerView rv_nav_menu;
    private List<CategoryModel> list = new ArrayList<>();
    private CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initDrawer();

        initUI();

        loadFragment(new HomeFragment(), null);
        setMenuVisible(true);
        communication.callPOST(Api.CATEGORY_LIST, "CATEGORY_LIST", new HashMap<>());


      /*  getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                if(f instanceof HomeFragment){
                    setNewTitle("Home");
                    setMenuVisible(true);
                }else if(f instanceof MyAccountFragment){
                    setNewTitle("Profile");
                    setMenuVisible(false);
                }else if(f instanceof ShopFragment){
                    setNewTitle("Shop");
                    setMenuVisible(false);
                }else if(f instanceof LockerFragment){
                    setNewTitle("Locker Room");
                    setMenuVisible(true);
                }else if(f instanceof SessionsFragment){
                   Bundle bundle = f.getArguments();
                    setMenuVisible(false);
                   switch (bundle.getString("slug")){
                       case "session-of-the-week":{ setNewTitle("Session of the week");}break;
                       case "drills-from-pros":{ setNewTitle("Drills From Pros");}break;
                   }
                }
            }
        });*/
    }

    @Override
    protected void onSuccessCall(Object tag, JSONObject jsonObject, boolean b) throws JSONException {
        if (tag == "CATEGORY_LIST") {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray cat_list = data.getJSONArray("cat_list");
            list.clear();
            list.add(new CategoryModel("Home", new ArrayList<>(), false, 0));
            /*list.add(new CategoryModel("Coach Office", new ArrayList<>(), false, 9));*/
            list.add(new CategoryModel("Your Account", new ArrayList<>(), false, 1));
            List<SubCategoryModel> subLockerList = new ArrayList<>();
            subLockerList.add(new SubCategoryModel("UPHQ session locker room", ""));
            subLockerList.add(new SubCategoryModel("Whiteboard Locker room", "whiteboard-locker-room"));
            subLockerList.add(new SubCategoryModel("Analysis Locker room", "analysis-locker-room"));
            list.add(new CategoryModel("Locker Room", subLockerList, false, 6));
            List<SubCategoryModel> subList = new ArrayList<>();
            for (int i = 0; i < cat_list.length(); i++) {
                subList.add(SubCategoryModel.fromJson(cat_list.getJSONObject(i)));
            }
            list.add(new CategoryModel("Drill Library", subList, false, 2));
            list.add(new CategoryModel("Drills From Pros", new ArrayList<>(), false, 3));
            list.add(new CategoryModel("Live", new ArrayList<>(), false, 4));
            list.add(new CategoryModel("Whiteboard room", new ArrayList<>(), false, 7));
            list.add(new CategoryModel("Analysis room", new ArrayList<>(), false, 8));
            list.add(new CategoryModel("Shop", new ArrayList<>(), false, 5));

            adapter.notifyDataSetChanged();
        }
    }

    private void loadFragment(Fragment fragment, String stack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
       /* if (stack != null) {
            transaction.addToBackStack(stack);
        }*/
        transaction.commit();
    }

    private void initUI() {

    }

    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView tv_coach_office = navigationView.findViewById(R.id.tv_coach_office);
        tv_coach_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawers();
                }
                Fragment fragment = new CoachOfficeFragment();
                loadFragment(fragment, "Coach Office");
                setNewTitle("Coach's Office");
                setMenuVisible(false);
            }
        });
        rv_nav_menu = navigationView.findViewById(R.id.rv_nav_menu);
        rv_nav_menu.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CategoryAdapter(context, list, this);
        rv_nav_menu.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (f instanceof HomeFragment) {
                super.onBackPressed();
            } else {
                loadFragment(new HomeFragment(), null);
                setNewTitle("Home");
                setMenuVisible(true);
            }
        }

    }

    @Override
    public void onLoadFragment(Fragment fragment, String stack) {
        Intent intent = new Intent(MainActivity.this, FragmentLoadingActivity.class);
        intent.putExtra("fragment", stack);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(CategoryModel model, int position) {

        switch (model.getPosition()) {
            case 0: {
                loadFragment(new HomeFragment(), null);
                setMenuVisible(true);
                setNewTitle("Home");
            }
            break;
            case 1: {
                loadFragment(new MyAccountFragment(), "home");
                setNewTitle("Profile");
                setMenuVisible(false);
            }
            break;
            case 4: {


                Fragment fragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", UrlUtils.get("hikmet-karaman"));
                fragment.setArguments(bundle);
                loadFragment(fragment, "Live");
                setNewTitle("Live");
                setMenuVisible(false);
             /*   Fragment fragment = new SessionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("slug", "session-of-the-week");
                bundle.putInt("banner", R.drawable.session_of_week);
                fragment.setArguments(bundle);
                loadFragment(fragment, "Session of the week");
                setNewTitle("Session of the week");
                setMenuVisible(false);*/
            }
            break;

            case 3: {
                Fragment fragment = new SessionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("slug", "drills-from-pros");
                bundle.putInt("banner", R.drawable.drill_from_pross);
                fragment.setArguments(bundle);
                loadFragment(fragment, "Session of the week");
                setNewTitle("Drills From Pros");
                setMenuVisible(false);
            }
            break;

            case 5: {
                Fragment fragment = new ShopFragment();
                loadFragment(fragment, "shop");
                setNewTitle("Shop");
                setMenuVisible(false);
                /* https://ultimatehq.iihglobal.co.uk/shop/*/
            }
            break;
            case 7: {
                Fragment fragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", UrlUtils.get("whiteboard"));
                fragment.setArguments(bundle);
                loadFragment(fragment, "Whiteboard room");
                setNewTitle("Whiteboard room");
                setMenuVisible(false);
                /* https://ultimatehq.iihglobal.co.uk/shop/*/
            }
            break;
            case 8: {
                Fragment fragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", UrlUtils.get("analysis-room"));
                fragment.setArguments(bundle);
                loadFragment(fragment, "Analysis room");
                setNewTitle("Analysis room");
                setMenuVisible(false);
                /* https://ultimatehq.iihglobal.co.uk/shop/*/
            }
            break;

            case 9: {
                Fragment fragment = new CoachOfficeFragment();
                loadFragment(fragment, "Coach Office");
                setNewTitle("Coach Office");
                setMenuVisible(false);
                /* https://ultimatehq.iihglobal.co.uk/shop/*/
            }
            break;


        }


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }
    }

    @Override
    public void onSubCategoryClick(SubCategoryModel model) {
        switch (model.getCategory_name()) {
            case "UPHQ session locker room": {
                Fragment fragment = new LockerFragment();
                loadFragment(fragment, model.getCategory_name());
                setNewTitle(model.getCategory_name());

                break;
            }
            case "Analysis Locker room":
            case "Whiteboard Locker room": {
                Fragment fragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", UrlUtils.get(model.getCategory_slug()));
                fragment.setArguments(bundle);
                loadFragment(fragment, model.getCategory_name());
                setNewTitle(model.getCategory_name());

                break;
            }

            default: {
                Fragment fragment = new SubSessionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("slug", model.getCategory_slug());
                fragment.setArguments(bundle);
                loadFragment(fragment, model.getCategory_name());
                setNewTitle(model.getCategory_name());


                break;
            }


        }

        setMenuVisible(false);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.opt_search) {
            Intent intent = new Intent(MainActivity.this, FragmentLoadingActivity.class);
            intent.putExtra("fragment", "SEARCH");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewTitle(String s) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(s);
        }
    }

}