package com.zxw.madaily.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zxw.madaily.R;
import com.zxw.madaily.entity.DetailTheme;
import com.zxw.madaily.http.Utils;

import java.util.List;

/**
 * Created by sony on 2015/7/23.
 */
public class OtherRecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private DetailTheme mDetailTheme;
    private  StoryRecyclerViewAdapter.OnItemSelectedLinstener linstener;

    public OtherRecyclerViewAdapter(DetailTheme detailTheme, StoryRecyclerViewAdapter.OnItemSelectedLinstener linstener){
        this.mDetailTheme = detailTheme;
        this.linstener = linstener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);

            final StoryRecyclerViewAdapter.StoryViewHolder holder =  new  StoryRecyclerViewAdapter.StoryViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linstener != null) {
                        linstener.select(v, holder.getLayoutPosition());
                    }
                }
            });
            return holder;
        } else if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_top, parent, false);
            return new StoryRecyclerViewAdapter.StoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);

        if (type == 1) {
            StoryRecyclerViewAdapter.StoryViewHolder svh =(StoryRecyclerViewAdapter.StoryViewHolder) holder;
            List<String> urls = mDetailTheme.getStories().get(position-1).getImages();

            if (urls!= null && urls.size() >0) {

                svh.mImage.setVisibility(View.VISIBLE);
                Utils.loadImage(urls.get(0), svh.mImage);
            } else {
                svh.mImage.setVisibility(View.GONE);
            }
            svh.mTitle.setText(mDetailTheme.getStories().get(position-1).getTitle());
        } else if (type == 0) {
            StoryRecyclerViewAdapter.StoryViewHolder ovh = (StoryRecyclerViewAdapter.StoryViewHolder) holder;
            Utils.loadImage(mDetailTheme.getBackground(), ovh.mImage);
            ovh.mTitle.setText(mDetailTheme.getDescription());
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) return 0;
        else if (position >= 1) return 1;
        return 2;
    }

    @Override
    public int getItemCount() {
        return  mDetailTheme!=null && mDetailTheme.getStories() != null ? mDetailTheme.getStories().size()+1 : 1;
    }
}
