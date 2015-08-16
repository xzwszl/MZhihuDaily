package com.zxw.madaily.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zxw.madaily.ContentActivity;
import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.adapter.StoryRecyclerViewAdapter;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.db.DBHandler;
import com.zxw.madaily.db.NewsHandler;
import com.zxw.madaily.db.TableHandler;
import com.zxw.madaily.entity.Content;
import com.zxw.madaily.entity.LatestNews;
import com.zxw.madaily.entity.Story;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.DataUtils;
import com.zxw.madaily.tool.ViewUtils;
import com.zxw.madaily.view.TopSwipeRefreshLayout;

import java.net.ContentHandler;
import java.util.List;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class MainFragment extends Fragment{

    private RecyclerView mNewsRV;
    private TopSwipeRefreshLayout mRefresh;
    private StoryRecyclerViewAdapter mStoryRecyclerViewAdapter;
    private Gson gson;
    private NewsHandler mNewsHandler;
    private TableHandler mTableHandler;

    private boolean loading;

    private DownloadThread mThread;

    public  DownloadThread getDownloadThread() {
        return mThread;
    }

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

        getActivity().setTitle("知乎日报");
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getClass().getName());
    }

    private void init(){
        setupSwipeRefresh();
        setupRecyclerView();
        gson = new Gson();

        mStoryRecyclerViewAdapter = new StoryRecyclerViewAdapter(null, new StoryRecyclerViewAdapter.OnItemSelectedLinstener() {
            @Override
            public void select(View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("id", ((Story) mStoryRecyclerViewAdapter.getOjbect(position)).getId());
                startActivity(intent);
            }
        });

        mNewsRV.setAdapter(mStoryRecyclerViewAdapter);

        if (mStoryRecyclerViewAdapter != null &&mStoryRecyclerViewAdapter.getmNews() !=null) return;

        mNewsHandler = new NewsHandler(getActivity());
        mTableHandler = new TableHandler(getActivity(), TableHandler.TABLE_CONTENT);
        loadData(Urls.LASTEST);
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

    private void setupSwipeRefresh() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(true);
                loadData(Urls.LASTEST);
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void setupRecyclerView() {

        mNewsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (!loading && dy > 0) {

                    int count = mStoryRecyclerViewAdapter.getItemCount();
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mNewsRV.getLayoutManager();
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                        List<LatestNews> news = mStoryRecyclerViewAdapter.getmNews();
                        if (news != null && news.size() > 0) {
                            loadData("before/" + news.get(news.size() - 1).getDate());
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    private void loadData(final String type) {
        loading = true;
        StringRequest storyRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.NEWS + type,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            LatestNews news  = gson.fromJson(response, LatestNews.class);

                            if (news != null) {
                                if (type.equals(Urls.LASTEST)) {
                                    mStoryRecyclerViewAdapter.addFirstNews(news);

                                    if (Utils.isWifiAvailable(getActivity()) && mThread == null) {
                                        mThread = new DownloadThread(news, true);
                                        mThread.start();
                                    }

                                } else {
                                    mStoryRecyclerViewAdapter.addNews(news);
                                }

                                mStoryRecyclerViewAdapter.notifyDataSetChanged();
                                mNewsHandler.insertOrUpdateNewbyDay(news.getDate(),response);

                            }
                        } catch (JsonSyntaxException e) {

                            if (type.equals(Urls.LASTEST))  {
                                loadDateFromDB(type);
                            }
                        }
                        loading = false;
                    }
                },
                new  Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        ViewUtils.showMessage(getActivity(),getString(R.string.network_error));

                        if (type.equals(Urls.LASTEST))  {
                            loadDateFromDB(type);
                        }
                        loading = false;
                    }
                }
        );
        storyRequest.setTag(this.getClass().getName());

        if (Utils.isWifiAvailable(getActivity()) && type.equals(Urls.LASTEST)) {
            DailyApplication.mInstance.getVolleyQueue().add(storyRequest);
        } else {
            if (!loadDateFromDB(type) && !type.equals(Urls.LASTEST)) {
                DailyApplication.mInstance.getVolleyQueue().add(storyRequest);
            }
        }
    }

    private boolean loadDateFromDB(String type) {
        //优先读取数据库
        String content = null;
        if (type.equals(Urls.LASTEST)) {
            content = mNewsHandler.findLastNews();
        } else {
            content = mNewsHandler.findNewsByDay(DataUtils.getBeforeDate(type.substring(7)));
        }

        if (!TextUtils.isEmpty(content)) {
            LatestNews news  = gson.fromJson(content, LatestNews.class);

            if (news != null) {

                if (type.equals(Urls.LASTEST)) {

                    mStoryRecyclerViewAdapter.addFirstNews(news);
                } else {
                    mStoryRecyclerViewAdapter.addNews(news);
                }

                mStoryRecyclerViewAdapter.notifyDataSetChanged();
                loading = false;
                return true;
            }
        }
        return false;
    }

    public void offlineDownload(final String id, boolean isForced) {

        if (!isForced && !Utils.isWifiAvailable(getActivity())) return;
        StringRequest contentRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.NEWS + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTableHandler.insertContentById(id, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        contentRequest.setTag(this.getClass().getName());
        String content = mTableHandler.findContentById(id);
        if (TextUtils.isEmpty(content)) {
            DailyApplication.mInstance.getVolleyQueue().add(contentRequest);
        }
    }

    public class DownloadThread extends Thread {

        private LatestNews mNews;
        private boolean isForced;
        public DownloadThread(LatestNews news,boolean isForced) {
            mNews = news;
            this.isForced = isForced;
        }

        @Override
        public void run() {
            if (mNews == null || mNews.getStories() == null) return;

            for (Story story : mNews.getStories()) {
                offlineDownload(String.valueOf(story.getId()),isForced);
            }
        }
    }
}
