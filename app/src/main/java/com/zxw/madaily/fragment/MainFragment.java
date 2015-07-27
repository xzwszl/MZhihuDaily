package com.zxw.madaily.fragment;

import android.content.Intent;
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
import com.google.gson.Gson;
import com.zxw.madaily.ContentActivity;
import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.adapter.StoryRecyclerViewAdapter;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.entity.Content;
import com.zxw.madaily.entity.LatestNews;
import com.zxw.madaily.view.TopSwipeRefreshLayout;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class MainFragment extends Fragment{

    private RecyclerView mNewsRV;
    private TopSwipeRefreshLayout mRefresh;
    private StoryRecyclerViewAdapter mStoryRecyclerViewAdapter;
    private Gson gson;

    private LatestNews mLn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getClass().getName());
    }

    private void init(){
        gson = new Gson();
        loadingData();
    }

    private void initView(View root) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mNewsRV = (RecyclerView) root.findViewById(R.id.rv_refresh);
        mNewsRV.setLayoutManager(layoutManager);


        mRefresh = (TopSwipeRefreshLayout) root.findViewById(R.id.srl_refresh);

        mRefresh.setColorSchemeColors(
                getResources().getColor(R.color.orange)
                , getResources().getColor(R.color.green)
                , getResources().getColor(R.color.blue));

    }

    private void loadingData() {

        StringRequest storyRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.NEWS + Urls.LASTEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mLn = gson.fromJson(response, LatestNews.class);

                        mStoryRecyclerViewAdapter = new StoryRecyclerViewAdapter(mLn.getStories(), mLn.getTop_stories(), new StoryRecyclerViewAdapter.OnItemSelectedLinstener() {
                            @Override
                            public void select(View view, int position) {
                                Intent intent = new Intent(getActivity(), ContentActivity.class);
                                intent.putExtra("id", mLn.getStories().get(position-1).getId());
                                startActivity(intent);
                            }
                        });

                        mNewsRV.setAdapter(mStoryRecyclerViewAdapter);
                    }
                },
                new  Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        storyRequest.setTag(this.getClass().getName());
        DailyApplication.mInstance.getVolleyQueue().add(storyRequest);
    }
}
