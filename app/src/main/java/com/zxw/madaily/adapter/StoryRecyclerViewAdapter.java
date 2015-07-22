package com.zxw.madaily.adapter;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxw.madaily.R;
import com.zxw.madaily.entity.Story;

import java.util.List;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryRecyclerViewAdapter.StoryHolderView>{

    private List<Story> mStories;

    public StoryRecyclerViewAdapter(List<Story> stories){
        this.mStories = stories;
    }
    @Override
    public StoryHolderView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
        return new StoryHolderView(view);
    }

    @Override
    public void onBindViewHolder(StoryHolderView holder, int position) {
        holder.mTitle.setText(mStories.get(position).getTitle());
     //   holder.mImage.setImageDrawable(null);
        List<String> urls = mStories.get(position).getImages();

        if (urls!= null && urls.size() >0) {

            loadImage(urls.get(0), holder.mImage);
        }
    }

    @Override
    public int getItemCount() {
        return mStories == null ? 0 : mStories.size();
    }

    private void loadImage(String url, ImageView image){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(url, image, options);
    }

    public static class StoryHolderView extends RecyclerView.ViewHolder{

        public final ImageView mImage;
        public final TextView mTitle;

        public StoryHolderView(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.iv_story);
            mTitle = (TextView) itemView.findViewById(R.id.tv_story);
        }
    }
}
