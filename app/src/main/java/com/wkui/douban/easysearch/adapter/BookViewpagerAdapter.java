package com.wkui.douban.easysearch.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.view.MainActivity;

/**
 * Created by wkui on 2017/3/6.
 */

public class BookViewpagerAdapter  extends FragmentStatePagerAdapter {

    private String[] mTitles;

    public BookViewpagerAdapter(FragmentManager fm) {
        super(fm);
        this.mTitles = CommonUtil.BOOK_TAGS;
    }

    @Override public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override public Fragment getItem(int position) {

        return MainActivity.newInstance(position,mTitles[position]);
    }

    @Override public int getCount() {
        return mTitles.length;
    }
}
