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

public class NavMenuHomeActivity extends BaseActivity {

    Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.nav_home_toolbar);
        toolbar.setTitle(getResources().getString(R.string.nav_menu_home));
        toolbar.setBackgroundColor(ThemeUtil.getThemeColor());
        //StatusBarUtil.setColor(this,ThemeUtil.getThemeColor());
        applyKitKatTranslucency(ThemeUtil.getThemeColor());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_light_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView text = (TextView) findViewById(R.id.nav_home_text);
        text.setText(this.getString(R.string.nav_menu_home_text));
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void handleThemeChange() {
        toolbar.setBackgroundColor(ThemeUtil.getThemeColor());
        //StatusBarUtil.setColor(this,ThemeUtil.getThemeColor());
        applyKitKatTranslucency(ThemeUtil.getThemeColor());
    }
}
