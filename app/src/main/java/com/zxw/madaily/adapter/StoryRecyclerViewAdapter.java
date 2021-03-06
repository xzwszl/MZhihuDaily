package com.zxw.madaily.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxw.madaily.DailyApplication;
import com.zxw.madaily.R;
import com.zxw.madaily.entity.LatestNews;
import com.zxw.madaily.entity.Story;
import com.zxw.madaily.http.Utils;
import com.zxw.madaily.tool.DataUtils;
import com.zxw.madaily.view.IndicatorView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class StoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<LatestNews> mNews;

    private static  TopStoryAdapter mTopAdapter;
    private OnItemSelectedLinstener mOnItemSelectedLinstener;
    private Context mContext;
    private Set<Integer> mReadSet;

    public StoryRecyclerViewAdapter(Context context, List<LatestNews> news, OnItemSelectedLinstener linstener, Set<Integer> readSet){
        this.mNews = news;
        this.mOnItemSelectedLinstener = linstener;
        this.mContext = context;
        this.mReadSet = readSet;
    }

    public void addFirstNews(LatestNews news) {

        if (mNews == null) {
            mNews = new ArrayList<>();
        } else {
            mNews.clear();
        }
        mNews.add(news);

        if (mTopAdapter == null && mNews != null && mNews.size()> 0) mTopAdapter = new TopStoryAdapter(mContext, mNews.get(0).getTop_stories(), mReadSet);
        else if (mTopAdapter != null) {
            mTopAdapter.setmTopStories(mNews.get(0).getTop_stories());
            mTopAdapter.notifyDataSetChanged();
        }
    }

    public void addNews(LatestNews news) {

        if (news == null) throw  new NullPointerException();
        if (mNews == null) {
            mNews = new ArrayList<>();
        }

        mNews.add(news);
    }

    public List<LatestNews> getmNews() {
        return mNews;
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_top, parent, false);
            return new TopViewHolder(view);
        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_type, parent, false);
            return new DateViewHodler(view);
        }
    }



    @Override
    public int getItemViewType(int position) {

        if (position == 0) return 0;

        int count = 0;

        for (LatestNews news : mNews) {

            count++;

            if (count == position) return 2;
            else if (count >= position) return 1;

            count += news.getStories() == null ? 0 : news.getStories().size();
        }

        return 1;
    }

    public Object getOjbect(int position){

        int count = 0;

        for(LatestNews news : mNews) {
            count++;

            if (count == position) {
                return news.getDate();
            }

            int size = news.getStories() == null ? 0 : news.getStories().size();

            if (count + size >= position) {
                return news.getStories().get(position - count - 1);
            }

            count += size;
        }

       return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);

        if (type == 0) {

         //   TopViewHolder tholder = (TopViewHolder) holder;

        } else if (type == 1){

            Story story = (Story) getOjbect(position);
            ((StoryViewHolder) holder).mTitle.setText(story.getTitle());
            //   holder.mImage.setImageDrawable(null);
            List<String> urls = story.getImages();

            if (urls!= null && urls.size() > 0 ) {
                ((StoryViewHolder) holder).mImage.setVisibility(View.VISIBLE);
                if (((StoryViewHolder) holder).mImage.getTag() == null || !((StoryViewHolder) holder).mImage.getTag().equals(urls.get(0))) {
                    Utils.loadImage(urls.get(0), ((StoryViewHolder) holder).mImage);
                }
            } else {
                ((StoryViewHolder) holder).mImage.setVisibility(View.GONE);
            }
            if (mReadSet.contains(story.getId())) {
                ((StoryViewHolder) holder).mTitle.setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_read_color));
            } else {
                ((StoryViewHolder) holder).mTitle.setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_color));
            }
            CardView card = (CardView) ((StoryViewHolder) holder).mTitle.getParent().getParent();
            card.setCardBackgroundColor(DailyApplication.mInstance.getAppResource().getColor(R.color.card_back));
        } else {

            String date = (String) getOjbect(position);
            if (position == 1)
                ((DateViewHodler)holder).mDate.setText("今日要闻");
            else
                ((DateViewHodler)holder).mDate.setText(DataUtils.getStoryTitle(date));

            ((DateViewHodler) holder).mDate.setTextColor(DailyApplication.mInstance.getAppResource().getColor(R.color.text_color));
        }
    }



    @Override
    public int getItemCount() {

        if (mNews == null) return 0;

        int count = mNews.size() + 1;

        for (LatestNews news : mNews) {
            count += news.getStories() == null ? 0 :news.getStories().size();
        }

        return count;
    }


    public static class DateViewHodler extends RecyclerView.ViewHolder {

        public final TextView mDate;

        public DateViewHodler(View itemView) {
            super(itemView);

            mDate = (TextView) itemView;
        }
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
        public final IndicatorView mIndicator;

        public TopViewHolder(View itemView) {
            super(itemView);

            mViewPager = (ViewPager) itemView.findViewById(R.id.vp_top);
            mTitle = (TextView) itemView.findViewById(R.id.tv_top_title);
            mIndicator = (IndicatorView) itemView.findViewById(R.id.idv_indicator);
            dealWithViewPager(mViewPager, mTitle);
        }

//        public void update() {
//            mTopAdapter.setmTopStories(mNews.get(0).getTop_stories());
//            mTopAdapter.notifyDataSetChanged();
//        }
        private void dealWithViewPager(final ViewPager vp, final TextView tv) {

            vp.setAdapter(mTopAdapter);
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    mIndicator.setLoacation(i, v);
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
