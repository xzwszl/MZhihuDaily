package com.zxw.madaily;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity{

    // private ImageView mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init(){
        initView();

        loadingMessage();
    }

    private void initView(){

        ///  mLoading = (ImageView) findViewById(R.id.iv_loading);

    }

    private void loadingMessage(){
        JsonObjectRequest jor;
        jor = new JsonObjectRequest(
                Request.Method.GET,
                "",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jor.setTag(this.getLocalClassName());
        DailyApplication.mInstance.getVolleyQueue().add(jor);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getLocalClassName());

    }
}
