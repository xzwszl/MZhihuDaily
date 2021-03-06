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
import android.view.animation.Animation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zxw.madaily.ContentActivity;
import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.MainActivity;
import com.zxw.madaily.R;
import com.zxw.madaily.adapter.OtherRecyclerViewAdapter;
import com.zxw.madaily.adapter.StoryRecyclerViewAdapter;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.db.TableHandler;
import com.zxw.madaily.entity.DetailTheme;
import com.zxw.madaily.entity.Story;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.ViewUtils;
import com.zxw.madaily.view.TopSwipeRefreshLayout;

import java.util.List;

/**
 * Created by xzwszl on 7/23/2015.
 */
public class OtherFragment extends Fragment {

    private TopSwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private OtherRecyclerViewAdapter mOAdapter;
    private Gson gson;

    private boolean loading;
    private String title;

    private TableHandler mTableHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        initView(root);

        return root;
    }

    @Nullable
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        String t = bundle.getString("title");

        if (t != null) {
            title = t;
            getActivity().setTitle(title);
        }
        init();
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
        gson = new Gson();
        setupSwipeRefresh();
        setupRecyclerView();

        mTableHandler = new TableHandler(getActivity(), TableHandler.TABLE_THEMES);
        loadData("", false);
    }

    private void setupSwipeRefresh() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                loadData("", true);
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void setupRecyclerView() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                List<Story> stories = mOAdapter.getStories();
                if (stories != null && !loading && dy > 0) {

                    int count = stories.size();

                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == count) {
                        loading = true;

                        int id = stories.get(count - 1).getId();

                        loadData("/before/" + id, true);
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

    public void refreshView() {
        if (mOAdapter != null) {
            mOAdapter.notifyDataSetChanged();
        }
    }

    private void loadData(final String before, boolean loadFromNetwork) {

        loading = true;
        StringRequest dtRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.THEME + this.getTag() + before,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if ( pareseResponse(before, response) && before.equals("")) {
                            mTableHandler.updateContentByIdy(OtherFragment.this.getTag(),response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ViewUtils.showMessage(getActivity(), getString(R.string.network_error));
                        loading = false;
                    }
                }
        );
        dtRequest.setTag(this.getTag());

        if (Utils.isWifiAvailable(getActivity())) {
            DailyApplication.mInstance.getVolleyQueue().add(dtRequest);
        } else if (before.equals("")) {

            String content = null;
            if (!loadFromNetwork) {
                // 优先使用数据库
                content = mTableHandler.findContentById(this.getTag());

                if (!TextUtils.isEmpty(content)) {
                    pareseResponse("", content);
                    return;
                }
            }

            DailyApplication.mInstance.getVolleyQueue().add(dtRequest);
        }
    }

    private boolean  pareseResponse(String before, String response) {
        try {
            final DetailTheme dt = gson.fromJson(response, DetailTheme.class);
            if (mOAdapter == null) {
                mOAdapter = new OtherRecyclerViewAdapter(dt, new StoryRecyclerViewAdapter.OnItemSelectedLinstener() {
                    @Override
                    public void select(View view, int position) {
                        Intent intent = new Intent(getActivity(), ContentActivity.class);
                        intent.putExtra("id", dt.getStories().get(position-2).getId());
                        ((MainActivity) getActivity()).updateReadSet(dt.getStories().get(position - 2).getId());
                        mOAdapter.notifyItemChanged(position);
                        startActivity(intent);
                    }
                }, ((MainActivity)getActivity()).getReadSet());
                mRecyclerView.setAdapter(mOAdapter);
            } else {
                if (dt != null){
                    if (before.equals("")) {
                        mOAdapter.setmDetailTheme(dt);
                    } else if ( dt.getStories() != null) {
                        mOAdapter.addStories(dt.getStories());
                        mOAdapter.notifyDataSetChanged();
                    }
                }
            }
            loading = false;
            return true;
        } catch (JsonSyntaxException e) {
            // do nothing
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getTag());
    }
}
