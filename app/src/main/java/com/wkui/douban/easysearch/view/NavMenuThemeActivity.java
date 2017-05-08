package com.wkui.douban.easysearch.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.ThemeColor;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.adapter.ThemeSetAdapter;
import com.wkui.douban.easysearch.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wkui on 2017/3/6.
 */

public class NavMenuThemeActivity extends BaseActivity {

    private List<ThemeColor> themeColorList = new ArrayList<>();
    private ThemeSetAdapter adapter ;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_theme);
        toolbar = (Toolbar) findViewById(R.id.theme_toolbar);
        toolbar.setTitle(getResources().getString(R.string.nav_menu_theme_title));
        toolbar.setBackgroundColor(ThemeUtil.getThemeColor());
        StatusBarUtil.setColor(NavMenuThemeActivity.this,ThemeUtil.getThemeColor());
        //applyKitKatTranslucency(ThemeUtil.getThemeColor());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.theme_confirm);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter!=null){
                    int colorPosition = adapter.getPosition();
                    ThemeColor color = themeColorList.get(colorPosition);
                    ThemeUtil.setThemePosition(colorPosition);
                    ThemeUtil.setThemeColor(color.getColor());
                    // ActivityManager.getInstatnce().removeActivity(NavMenuThemeActivity.this);
                    // ActivityManager.getInstatnce().refreshAllActivity();
                    Intent intent = new Intent(CommonUtil.THEME_CHANGE_BROADCAST);
                    sendBroadcast(intent);
                    finish();
                }

            }
        });
        initThemeColor();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.theme_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ThemeSetAdapter(themeColorList);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ThemeSetAdapter.OnRecyclerViewItemClickListener(){

            @Override
            public void onItemClick(View view,int position) {
                for (ThemeColor themeColor : themeColorList) {
                    themeColor.setChosen(false);
                }
                themeColorList.get(position).setChosen(true);
                toolbar.setBackgroundColor(themeColorList.get(position).getColor());
                StatusBarUtil.setColor(NavMenuThemeActivity.this,themeColorList.get(position).getColor());
                //applyKitKatTranslucency(themeColorList.get(position).getColor());
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void handleThemeChange() {

    }

    private void initThemeColor() {
        ThemeColor green = new ThemeColor(getResources().getColor(R.color.green));
        themeColorList.add(green);
        ThemeColor red = new ThemeColor(getResources().getColor(R.color.red));
        themeColorList.add(red);
        ThemeColor orange = new ThemeColor(getResources().getColor(R.color.orange));
        themeColorList.add(orange);
        ThemeColor yellow = new ThemeColor(getResources().getColor(R.color.yellow));
        themeColorList.add(yellow);
        ThemeColor blue = new ThemeColor(getResources().getColor(R.color.blue));
        themeColorList.add(blue);
        ThemeColor gray = new ThemeColor(getResources().getColor(R.color.gray));
        themeColorList.add(gray);
        ThemeColor purple = new ThemeColor(getResources().getColor(R.color.purple));
        themeColorList.add(purple);

        ThemeColor pink = new ThemeColor(getResources().getColor(R.color.pink));
        themeColorList.add(pink);

        ThemeColor brown = new ThemeColor(getResources().getColor(R.color.brown));
        themeColorList.add(brown);
    }
}
