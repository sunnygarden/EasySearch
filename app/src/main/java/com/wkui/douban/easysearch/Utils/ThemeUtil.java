package com.wkui.douban.easysearch.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.base.MyApplication;

/**
 * Created by wkui on 2017/3/4.
 */

public class ThemeUtil {

    public static int defalutThemeColor = MyApplication.mContext.getResources().getColor(R.color.green);
    public static int transparentColor = Color.TRANSPARENT;

    private static Context context = MyApplication.mContext;

    /**
     * Get the theme color which saved in sharedPref
     * @return
     */
    public static int getThemeColor(){
        SharedPreferences pref = context.getSharedPreferences("ThemeColor",context.MODE_PRIVATE);
        return pref.getInt("themeColor",defalutThemeColor);
    }

    /**
     * Set the theme color save in sharedPref
     * @return
     */
    public static void setThemeColor( int color){
        SharedPreferences.Editor editor = context.getSharedPreferences("ThemeColor",context.MODE_PRIVATE).edit();
        editor.putInt("themeColor",color);
        editor.commit();
    }

    public static void setThemePosition(int position){
        SharedPreferences.Editor editor = context.getSharedPreferences("ThemeColor",context.MODE_PRIVATE).edit();
        editor.putInt("position",position);
        editor.commit();
    }

    public static int getThemePosition(){
        SharedPreferences pref = context.getSharedPreferences("ThemeColor",context.MODE_PRIVATE);
        return pref.getInt("position",0);
    }
}
