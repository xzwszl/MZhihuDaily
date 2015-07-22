package com.zxw.madaily.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxw.madaily.entity.Story;

import java.util.List;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class TopStoryAdapter extends PagerAdapter{

    private List<Story> mTopStories;
    public TopStoryAdapter(List<Story> topStories){
        this.mTopStories = topStories;
    }
    @Override
    public int getCount() {

        return mTopStories == null ? 0 : mTopStories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageview = new ImageView(container.getContext());
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewPager) container).addView(imageview);
        loadImage(mTopStories.get(position).getImage(), imageview);
        return imageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((View)object);
    }

    private void loadImage(String url, ImageView image){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(url, image, options);
    }
}
