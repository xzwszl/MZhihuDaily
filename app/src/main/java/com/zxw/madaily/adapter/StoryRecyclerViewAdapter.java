package com.zxw.madaily.adapter;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.view.ViewPager;
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
import com.zxw.madaily.http.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class StoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Story> mStories;
    private List<Story> mTops;
    private static  TopStoryAdapter mTopAdapter;
    private OnItemSelectedLinstener mOnItemSelectedLinstener;

    public StoryRecyclerViewAdapter(List<Story> stories, List<Story> tops, OnItemSelectedLinstener linstener){
        this.mStories = stories;
        this.mTops = tops;
        this.mOnItemSelectedLinstener = linstener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);

            final StoryViewHolder holder = new StoryViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnItemSelectedLinstener != null) {

                        mOnItemSelectedLinstener.select(v, holder.getLayoutPosition());
                    }
                }
            });
            return holder;
        } else if (viewType == 0) {

            if (mTopAdapter == null) mTopAdapter = new TopStoryAdapter(parent.getContext(),mTops);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_top, parent, false);
            return new TopViewHolder(view);
        } else {
            return null;
        }
    }



    @Override
    public int getItemViewType(int position) {

        if (position == 0) return 0;
        return 1;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);

        if (type == 0) {

         //   TopViewHolder tholder = (TopViewHolder) holder;

        } else if (type == 1){
            ((StoryViewHolder) holder).mTitle.setText(mStories.get(position-1).getTitle());
            //   holder.mImage.setImageDrawable(null);
            List<String> urls = mStories.get(position-1).getImages();

            if (urls!= null && urls.size() >0) {

                Utils.loadImage(urls.get(0), ((StoryViewHolder) holder).mImage);
            }
        }
        }


    @Override
    public int getItemCount() {
        return mStories == null ? 1 : mStories.size() + 1;
    }



    public static class StoryViewHolder extends RecyclerView.ViewHolder{

        public final ImageView mImage;
        public final TextView mTitle;

        public StoryViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.iv_story);
            mTitle = (TextView) itemView.findViewById(R.id.tv_story);
        }
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder {

        public final ViewPager mViewPager;
        public final TextView mTitle;

        public TopViewHolder(View itemView) {
            super(itemView);

            mViewPager = (ViewPager) itemView.findViewById(R.id.vp_top);
            mTitle = (TextView) itemView.findViewById(R.id.tv_top_title);

            dealWithViewPager(mViewPager, mTitle);
        }
        private void dealWithViewPager(final ViewPager vp, final TextView tv) {

            vp.setAdapter(mTopAdapter);
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    int aa = 100;
                }

                @Override
                public void onPageSelected(int i) {

                    tv.setText(mTopAdapter.getPageTitle(i));
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

            tv.setText(mTopAdapter.getPageTitle(0));
            vp.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class, int.class);
                        method.setAccessible(true);
                        method.invoke(vp, vp.getCurrentItem() + 1, true, true, 1);

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    vp.postDelayed(this, 5000);
                }
            }, 5000);
        }
    }

    public interface OnItemSelectedLinstener{

        void select(View view, int position);
    }
}
