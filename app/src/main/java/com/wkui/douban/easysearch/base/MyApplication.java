package com.wkui.douban.easysearch.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by wkui on 2017/3/4.
 */

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
