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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.adapter.CommentRecyclerViewAdapter;
import com.zxw.madaily.config.Urls;
import com.zxw.madaily.entity.Comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sony on 2015/8/2.
 */
public class CommentFragment extends Fragment{

    private List<Comment> mComments;
    private Gson gson;
    private CommentRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int id;
    private String type;
    private boolean loading;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_comment, container, false);

        initView(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        Bundle bundle = getArguments();

        id = bundle == null ? 0 : bundle.getInt("id");
        int t = bundle == null ? 0 : bundle.getInt("type");

        type = t == 0 ? Urls.LONG_COMMENT : Urls.SHORT_COMMENT;

        mAdapter = new CommentRecyclerViewAdapter(mComments);
        mRecyclerView.setAdapter(mAdapter);

        setupRecyclerView();


        gson = new Gson();

        loading = true;
        loadData(type);
    }


    private void initView(View root) {

        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_comments);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void setupRecyclerView() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (mComments != null && !loading && dy > 0) {

                    int count = mComments.size();

                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                        loading = true;

                        int commentId = mComments.get(count-1).getId();

                        loadData(type + "/before/" + commentId);
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

    private void loadData(final String commentType) {

        StringRequest commentRequest = new StringRequest(
                Request.Method.GET,
                Urls.BASE_URL + Urls.STORY + id + "/" + commentType,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jb = new JSONObject(response);

                            if (jb.has("comments")) {

                                List<Comment> comments = gson.fromJson(jb.getString("comments"), new TypeToken<List<Comment>>() {
                                }.getType());

                                if (comments != null) {
                                    mAdapter.addComments(comments);

                                    mComments = mAdapter.getmComments();
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (Exception e) {

                        }
                        finally {
                            loading = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading = false;
                    }
                }
        );

        commentRequest.setTag(this.getClass().getName());
        DailyApplication.mInstance.getVolleyQueue().add(commentRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DailyApplication.mInstance.getVolleyQueue().cancelAll(this.getClass().getName());
    }
}
