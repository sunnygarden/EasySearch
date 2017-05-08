package com.wkui.douban.easysearch.view;

import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.base.BaseActivity;

/**
 * Created by wkui on 2017/3/7.
 */

public class NavMenuAboutActivity extends BaseActivity {

    Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.nav_about_toolbar);
        toolbar.setTitle(getResources().getString(R.string.nav_menu_about));
        toolbar.setBackgroundColor(ThemeUtil.getThemeColor());
        applyKitKatTranslucency(ThemeUtil.getThemeColor());
        //StatusBarUtil.setColor(this,ThemeUtil.getThemeColor());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView text = (TextView) findViewById(R.id.nav_about_text);
        text.setText(this.getString(R.string.nav_menu_about_text));
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void handleThemeChange() {
        toolbar.setBackgroundColor(ThemeUtil.getThemeColor());
        //StatusBarUtil.setColor(this,ThemeUtil.getThemeColor());
        applyKitKatTranslucency(ThemeUtil.getThemeColor());
    }
}
