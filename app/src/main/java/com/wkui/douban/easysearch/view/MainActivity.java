package com.wkui.douban.easysearch.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.ActivityManager;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.adapter.BookViewpagerAdapter;
import com.wkui.douban.easysearch.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.search_input)
    EditText searchInput;
    @BindView(R.id.scan_btn)
    ImageView scanImage;
    @BindView(R.id.open_drawer)
    ImageView openDraw;
    @BindView(R.id.nav_view)
    NavigationView nav;

    private BookViewpagerAdapter mViewPagerAdapter;
    FragmentManager fragmentManager;
    private View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        handleThemeChange();
    }
    @Override
    protected void handleThemeChange() {
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}
        };
        StatusBarUtil.setColorForDrawerLayout(MainActivity.this,mDrawerLayout, ThemeUtil.getThemeColor(),80);;
        int[] colors = new int[]{ThemeUtil.getThemeColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        nav.setItemIconTintList(csl);
        TextView mail = (TextView) headerLayout.findViewById(R.id.mail);
        TextView userName = (TextView)headerLayout.findViewById(R.id.username);
        mail.setTextColor(ThemeUtil.getThemeColor());
        userName.setTextColor(ThemeUtil.getThemeColor());
        tablayout.setSelectedTabIndicatorColor(ThemeUtil.getThemeColor());
        tablayout.setTabTextColors(getResources().getColor(R.color.gray),ThemeUtil.getThemeColor());
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main_layout);
        layout.setBackgroundColor(ThemeUtil.getThemeColor());
        mViewPagerAdapter.notifyDataSetChanged();
        for(BookListFragment.ThemeChangerListener listener : mListener) {
            if (listener != null) {
                listener.onThemeChange();
            }
        }
    }


    private void initViews(){
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_item_main:
                        Intent intent = new Intent(MainActivity.this,NavMenuHomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_item_theme:
                        Intent intent1 = new Intent(MainActivity.this,NavMenuThemeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_item_setting:
                        showToast(MainActivity.this.getResources().getString(R.string.toast_click_hint));
                        break;
                    case R.id.nav_item_about:
                        Intent intent2 = new Intent(MainActivity.this,NavMenuAboutActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        headerLayout = nav.inflateHeaderView(R.layout.activity_main_nav_header);
        searchInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Intent searchIntent = new Intent(MainActivity.this,BookSearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                }
                return true;
            }
        });
        openDraw.setOnClickListener(this);
        scanImage.setOnClickListener(this);
        fragmentManager = this.getSupportFragmentManager();
        mViewPagerAdapter = new BookViewpagerAdapter(fragmentManager);
        viewpager.setAdapter(mViewPagerAdapter);
        viewpager.setOffscreenPageLimit(4);
        viewpager.addOnPageChangeListener(this);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.scan_btn:
                Intent scanIntent = new Intent(MainActivity.this,BookScanActivity.class);
                startActivity(scanIntent);
                break;
            case R.id.open_drawer:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    private BookListFragment.ThemeChangerListener[] mListener = new BookListFragment.ThemeChangerListener[5];
    public void setThemeChangeListener(BookListFragment.ThemeChangerListener listener,int i){
        mListener[i] = listener;
    }

    public static BookListFragment newInstance(int position, String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("position", position);
        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //重写onKeyDown方法，判断是否点击退出键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                handleClick();
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
      *此变量用来计算点的次数，初始值为false，第一点设置为true，如果第二次点就为false，退出
      *如果没点就不做操作
      */
    private boolean isDoubleClick;
    Timer timer = new Timer();
    private void handleClick() {
        System.out.println(isDoubleClick);
        if (!isDoubleClick) {
            isDoubleClick = true;
            //第二次点击，用到了TimerTask
            showToast(this.getResources().getString(R.string.toast_quit_hint));

            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    isDoubleClick = false;
                }
            }, 2000);
        } else {
            finish();
            ActivityManager.getInstatnce().finishAllActivities();
        }

    }

    private void showToast(String string) {
        View toastView = getLayoutInflater().inflate(R.layout.toast_layout,null);
        LinearLayout layout = (LinearLayout) toastView.findViewById(R.id.toast_quit_layout);
        TextView text = (TextView) toastView.findViewById(R.id.toast_text);
        text.setText(string);
        layout.setBackgroundResource(R.drawable.toast_radius);
        GradientDrawable myGrad =(GradientDrawable)layout.getBackground();
        myGrad.setColor(ThemeUtil.getThemeColor());
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0,50);
        toast.setView(toastView);
        toast.show();
    }
}
