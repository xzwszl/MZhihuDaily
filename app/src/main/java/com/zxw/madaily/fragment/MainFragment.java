package com.zxw.madaily.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.adapter.StoryRecyclerViewAdapter;
import com.zxw.madaily.adapter.TopStoryAdapter;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.entity.LatestNews;
import com.zxw.madaily.view.TopScroller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class MainFragment extends Fragment{

    private RecyclerView mNewsRV;
    private ViewPager mTopVP;
    private SwipeRefreshLayout mRefresh;
    private StoryRecyclerViewAdapter mStoryRecyclerViewAdapter;
    private TopStoryAdapter mTopStoryAdapter;

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


        mRefresh = (SwipeRefreshLayout) root.findViewById(R.id.srl_refresh);

        mRefresh.setColorSchemeColors(
                getResources().getColor(R.color.orange)
                ,getResources().getColor(R.color.green)
                ,getResources().getColor(R.color.blue));


        mTopVP = (ViewPager) root.findViewById(R.id.vp_top);

        try {

            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            TopScroller scroller = new TopScroller(getActivity(), new AccelerateInterpolator());

            field.set(mTopVP, scroller);

            Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal",Integer.class, Boolean.class, Boolean.class, Integer.class );

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void loadingData() {

        StringRequest storyRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.NEWS + Urls.LASTEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mLn = gson.fromJson(response, LatestNews.class);

                        mStoryRecyclerViewAdapter = new StoryRecyclerViewAdapter(mLn.getStories());

                        mNewsRV.setAdapter(mStoryRecyclerViewAdapter);

                        mTopStoryAdapter = new TopStoryAdapter(mLn.getTop_stories());

                        mTopVP.setAdapter(mTopStoryAdapter);

                        mTopVP.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                mTopVP.setCurrentItem(((mTopVP.getCurrentItem()+1) % mTopVP.getAdapter().getCount()));

                                mTopVP.postDelayed(this, 3000);
                            }
                        }, 1000);
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
