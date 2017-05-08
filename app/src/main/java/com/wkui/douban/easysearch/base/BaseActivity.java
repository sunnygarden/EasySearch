package com.wkui.douban.easysearch.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wkui.douban.easysearch.Utils.ActivityManager;
import com.wkui.douban.easysearch.Utils.CommonUtil;

/**
 * Created by wkui on 2017/3/4.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ThemeChangeBroadcast broadcast;
    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        initBroadCastReceiver();
        ActivityManager.getInstatnce().addActivity(this);
        //StatusBarUtil.setColorNoTranslucent(this,ThemeUtil.getThemeColor());
        initView();
    }

    private void initBroadCastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonUtil.THEME_CHANGE_BROADCAST);
        broadcast = new ThemeChangeBroadcast();
        registerReceiver(broadcast,intentFilter);
    }

    protected abstract void initView();


    class ThemeChangeBroadcast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //StatusBarUtil.setColorNoTranslucent(mActivity,ThemeUtil.getThemeColor());
            handleThemeChange();
        }
    }

    protected abstract void handleThemeChange();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast);
        ActivityManager.getInstatnce().removeActivity(this);
    }

    protected void applyKitKatTranslucency(int color) {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            // mTintManager.setStatusBarTintResource(R.color.red_base);//通知栏所需颜色
            mTintManager.setStatusBarTintColor(color);
        }

    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
