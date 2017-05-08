package com.wkui.douban.easysearch.Utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wkui on 2017/3/4.
 */

public class ActivityManager {

    private ActivityManager(){}

    private static ActivityManager activityManager;

    public static ActivityManager getInstatnce(){
        if(activityManager == null){
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    public static List<Activity> activities = new ArrayList<>();

    public  void addActivity(Activity activity){
        activities.add(activity);
    }

    public  void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public  void finishAllActivities(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
