package com.wkui.douban.easysearch.view;

import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.adapter.SearchAdapter;
import com.wkui.douban.easysearch.base.BaseActivity;
import com.wkui.douban.easysearch.bean.Book;
import com.wkui.douban.easysearch.presenter.BookPresenter;
import com.wkui.douban.easysearch.viewInterface.IGetBookSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wkui on 2017/3/5.
 */

public class BookSearchActivity extends BaseActivity implements IGetBookSearchView {

    @BindView(R.id.main_search_layout)
    SearchView searchView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView recycleView;
    @BindView(R.id.empty_view)
    EmptyLayout mErrorLayout;
    @BindView(R.id.fab)
    FloatingActionButton FAB;

    private SearchAdapter adapter;
    private List<Book> bookList = new ArrayList<>();
    private String keyword;
    private int lastVisibleItem;
    private BookPresenter bookPresenter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        bookPresenter = BookPresenter.getSingletonPresenter();
        initViews();
    }

    @Override
    protected void handleThemeChange() {
        swipeRefresh.setColorSchemeColors(ThemeUtil.getThemeColor());
        StatusBarUtil.setColor(this, ThemeUtil.getThemeColor());
        //applyKitKatTranslucency(ThemeUtil.getThemeColor());
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}};
        int[] colors = new int[]{ThemeUtil.getThemeColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        FAB.setBackgroundTintList(csl);
    }


    /**
     * 初始化视图
     */
    private void initViews() {
        //设置监听
        searchView.setSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearch(String text) {
                keyword = text;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestForData();
            }
        });

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForData();
            }
        });
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(bookList);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestForData();
            }
        });
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //一碰就隐藏软键盘
                CommonUtil.hideSoftInput(BookSearchActivity.this,recyclerView);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addMoreItem(adapter.getItemCount());
                        }
                    },1000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if(firstVisibleItemPosition == 0) {
                    if(FAB.getVisibility()==View.VISIBLE) {
                        FAB.setVisibility(View.GONE);
                    }
                }else{
                    if(FAB.getVisibility()==View.GONE){
                        FAB.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager.scrollToPosition(0);
            }
        });
        handleThemeChange();
    }


    private void requestForData() {
        bookPresenter.getBookByKeyword(this,keyword,0,false);
        adapter.notifyDataSetChanged();
    }


    public void addMoreItem(int start) {
        bookPresenter.getBookByKeyword(this,keyword,start,true);
    }

    @Override
    public void onSuccess(List<Book> books, int bookCount, boolean isLoadMore) {
        if(!isLoadMore) {//第一次加载而不是上拉加载
            adapter.setGetBookCount(books.size());
            adapter.setTotalBookCount(bookCount);
            bookList.clear();
            for (Book book : books) {
                bookList.add(book);
            }
            if (adapter != null) {
                recycleView.setAdapter(adapter);
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }else{
                Log.e(TAG,"adapter==null");
            }
        }else{//上拉加载
            adapter.setGetBookCount(books.size());
            bookList.addAll(books);
            adapter.notifyDataSetChanged();
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onFailure() {
        if(CommonUtil.isConnectivity(BookSearchActivity.this)){
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }else{
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        swipeRefresh.setRefreshing(false);
    }
}

