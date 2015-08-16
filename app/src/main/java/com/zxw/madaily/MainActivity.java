package com.zxw.madaily;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.zxw.madaily.adapter.ThemeAdapter;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.db.TableHandler;
import com.zxw.madaily.entity.Content;
import com.zxw.madaily.entity.DailyTheme;
import com.zxw.madaily.entity.LatestNews;
import com.zxw.madaily.entity.Theme;
import com.zxw.madaily.fragment.MainFragment;
import com.zxw.madaily.fragment.OtherFragment;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.FileUtils;

import java.security.acl.LastOwnerException;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mThemesListView;
    private NavigationView mNavigationView;
    private ThemeAdapter mThemeAdapter;
    private Gson gson;

    private MainFragment mMainFragment;
    private OtherFragment mOtherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();

    }

    private void init(){
        initView();
        gson = new Gson();
        loadTheme();
    }

    private void initView(){

        mThemesListView = (ListView) findViewById(R.id.lv_themes);

        mThemesListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.nav_header, null));

        // we have a header here
        mThemesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDrawerLayout.closeDrawers();

                int currentPos = mThemeAdapter.getSelectedPos();

                if (position == 0 || position == currentPos) return;

                mThemeAdapter.setSelectedPos(position-1);

                if (position == 1) {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    if (mMainFragment == null) {
                        mMainFragment = new MainFragment();
                    }
                    ft.replace(R.id.container, mMainFragment, "main");
                    ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);

                    ft.commit();

                    return;
                }

                Theme theme = (Theme) mThemeAdapter.getItem(position - 2);

                if (mOtherFragment != null && String.valueOf(theme.getId()).equals(mOtherFragment.getTag())) {
                    return;
                }

                mOtherFragment = new OtherFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("title", theme.getName());
                mOtherFragment.setArguments(bundle);
                ft.replace(R.id.container, mOtherFragment, String.valueOf(theme.getId()));
                ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);

                ft.commit();

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
        }
        ft.replace(R.id.container,mMainFragment,"main");
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = (FragmentManager)getSupportFragmentManager();

        Fragment fragment = fm.findFragmentByTag("main");

        if (fragment != null && fragment.isVisible()) {
            super.onBackPressed();
        } else {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (mMainFragment == null) {
                mMainFragment = new MainFragment();
            }
            ft.replace(R.id.container, mMainFragment, "main");
            ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            ft.commit();

            mThemeAdapter.setSelectedPos(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void loadTheme(){

        String response = FileUtils.getResponse(Urls.LOCAL_THEME);

        if (response != null) {
            parseResponse(response);
            return;
        }

        StringRequest themeRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.THEMES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        parseResponse(response);

                        FileUtils.saveResonse(Urls.LOCAL_THEME,response);


               //         mNavigationView.invalidate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        themeRequest.setTag(this.getLocalClassName());
        DailyApplication.mInstance.getVolleyQueue().add(themeRequest);
    }

    private void parseResponse(String response) {
        DailyTheme dt = gson.fromJson(response, DailyTheme.class);
        mThemeAdapter = new ThemeAdapter(dt.getOthers());
        mThemesListView.setAdapter(mThemeAdapter);
    }

    @Override
    protected void onDestroy() {

        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getLocalClassName());
        super.onDestroy();
    }
}
