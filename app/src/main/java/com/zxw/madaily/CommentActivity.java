package com.zxw.madaily;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zxw.madaily.fragment.CommentFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sony on 2015/8/2.
 */
public class CommentActivity extends AppCompatActivity{

    private int id;
    private CommentFragment mLongFragment;
    private CommentFragment mShortFragment;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private CommentFragmentAdpater adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);



        mViewPager = (ViewPager) findViewById(R.id.vp_comment);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);

    }

    private void init() {

        initView();


        id = getIntent().getIntExtra("id", 0);

        int longComments = getIntent().getIntExtra("long", 0);
        int shortComments =getIntent().getIntExtra("short", 0);

        adpater = new CommentFragmentAdpater(getSupportFragmentManager());

        mLongFragment = new CommentFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("type",0);
        mLongFragment.setArguments(bundle);
        adpater.addFragfment(mLongFragment, "长评论:" + longComments);

        Bundle b = new Bundle();
        b.putInt("id", id);
        b.putInt("type", 1);
        mShortFragment = new CommentFragment();
        mShortFragment.setArguments(b);
        adpater.addFragfment(mShortFragment, "短评论:" + shortComments);

        mViewPager.setAdapter(adpater);

        mTabLayout.setupWithViewPager(mViewPager);
    }


    private class CommentFragmentAdpater extends  FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mFragmentTitles;


        public CommentFragmentAdpater(FragmentManager fm) {
            super(fm);
        }

        public void addFragfment(Fragment fragment, String title) {

            if (mFragments == null) {
                mFragments = new ArrayList<>();
            }

            if (mFragmentTitles == null) {
                mFragmentTitles = new ArrayList<>();
            }

            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments == null ? null : mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitles == null ? "" : mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
