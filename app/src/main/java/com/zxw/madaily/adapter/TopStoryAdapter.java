package com.zxw.madaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zxw.madaily.ContentActivity;
import com.zxw.madaily.entity.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class TopStoryAdapter extends PagerAdapter{

    private List<Story> mTopStories;
    private List<ImageView> mTopViews;

    public TopStoryAdapter(final Context context, List<Story> topStories){
        this.mTopStories = topStories;

        if (topStories != null) {
            mTopViews = new ArrayList<ImageView>();
            for (int i = 0; i < mTopStories.size(); i++){
                ImageView imageview = new ImageView(context);
                imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mTopViews.add(imageview);

                final int finalI = i;
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, ContentActivity.class);
                        intent.putExtra("id", mTopStories.get(finalI).getId());
                        context.startActivity(intent);
                    }
                });

                loadImage(mTopStories.get(i).getImage(), imageview);
            }
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTopStories.get(position % mTopStories.size()).getTitle();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageview = mTopViews.get(position % mTopStories.size());

        ViewParent vp = imageview.getParent();
        if (vp != null) {
            ((ViewGroup)vp).removeView(imageview);
        }

        ((ViewPager) container).addView(imageview);

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
