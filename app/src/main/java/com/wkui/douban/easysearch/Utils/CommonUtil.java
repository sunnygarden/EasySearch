package com.wkui.douban.easysearch.Utils;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.view.BookDetailActivity;

import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by wkui on 2017/3/4.
 */

public class CommonUtil {
    public static String THEME_CHANGE_BROADCAST = "com.test.example.myapplication.THEME_CHANGE";
    public static String BOOK_TAGS []={"综合","文学","流行","文化","生活"};
    public static String BASE_URL = "https://api.douban.com/v2/";
    public static String SEARCH_BOOK_COUNT = "30";
    public static String getPositionTag(int pos){
        switch (pos){
            case 0:
                return BOOK_TAGS[0];
            case 1:
                return BOOK_TAGS[1];
            case 2:
                return BOOK_TAGS[2];
            case 3:
                return BOOK_TAGS[3];
            case 4:
                return BOOK_TAGS[4];
        }
        return null;
    }
    /**
     * 强制隐藏输入法键盘
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     * @param context
     */
    public static void hideOrShowSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**
     *
     * 描述：是否有网络连接.androidbase中AbWifiUtil中的方法
     * @param context
     * @return
     */
    public static boolean isConnectivity(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telephonyManager
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    public static String authorCombine(Context context,List<String> bookAuthors){
        StringBuilder sb = new StringBuilder();
        for(String author:bookAuthors){
            sb.append(author);
            sb.append("/");
        }
        String bookAuthor = "";
        if(bookAuthors.size()>0) {
            bookAuthor = sb.toString().substring(0, sb.toString().length() - 1);
        }
        return bookAuthor;
    }

    public static void handleScanResult(Context context,String result){
        Log.d("hello","result = "+result);
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(context,context.getResources().getString(R.string.format_not_support),Toast.LENGTH_SHORT).show();
        } else if(result .startsWith("http://")||result.startsWith("https://")){
            searchWeb(context,result);
        }else if((result.length()==13&&result.startsWith("978"))||result.length()==10){
            vibrate(context);
            requestForBook(context,result);
        } else{
            Toast.makeText(context,context.getResources().getString(R.string.format_not_support),Toast.LENGTH_SHORT).show();
        }
    }

    private static void requestForBook(Context context,String result) {
        Intent intent = new Intent(context,BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.BOOK_ISBN,result);
        context.startActivity(intent);
    }


    public static void searchWeb(Context context ,String query)
    {
        Uri uri = Uri.parse(query);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null)
        {
            context.startActivity(intent);
        }

//        Intent intent = new Intent(context, ScanWebActivity.class);
//        intent.putExtra(SCAN_WEB_URL,query);
//        context.startActivity(intent);
    }

    private static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


}
