package com.zxw.madaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.view.TopSwipeRefreshLayout;

/**
 * Created by xzwszl on 7/23/2015.
 */
public class OtherFragment extends Fragment {

    private TopSwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    pr

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        initView(root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View root){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_refresh);
        mRecyclerView.setLayoutManager(layoutManager);

        mSwipeRefresh = (TopSwipeRefreshLayout) root.findViewById(R.id.srl_refresh);
        mSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.orange)
                , getResources().getColor(R.color.green)
                , getResources().getColor(R.color.blue));

    }

    private void init() {

    }

    private void loadMessage() {

        StringRequest dtRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.THEME + this.getTag(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        dtRequest.setTag(this.getTag());
        DailyApplication.mInstance.getVolleyQueue().add(dtRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getTag());
    }
}
