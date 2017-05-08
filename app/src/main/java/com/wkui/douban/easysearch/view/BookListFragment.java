package com.wkui.douban.easysearch.view;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.adapter.BookListAdapter;
import com.wkui.douban.easysearch.bean.Book;
import com.wkui.douban.easysearch.presenter.BookPresenter;
import com.wkui.douban.easysearch.viewInterface.IGetBookView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wkui on 2017/3/5.
 */

public class BookListFragment extends Fragment implements IGetBookView {

    SwipeRefreshLayout swipeRefresh;

    RecyclerView recycleView;

    EmptyLayout mErrorLayout;

    FloatingActionButton FAB;


    private List<Book> bookList = new ArrayList<>();
    private BookListAdapter adapter;
    private int position;
    private String tag;
    private int start = 0;
    private int lastVisibleItem;

    private GridLayoutManager layoutManager;
    private View view;
    private BookPresenter bookPresenter;

    private Activity mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_list,container,false);
        mContext = this.getActivity();
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFruits();
            }
        });
        layoutManager = new GridLayoutManager(mContext,3);
        layoutManager.setSmoothScrollbarEnabled(true);
        recycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setLayoutManager(layoutManager);
        adapter = new BookListAdapter(bookList);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            addMoreItem(start+adapter.getItemCount());
                        }
                    },1000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if(firstVisibleItemPosition == 0) {//reach the top
                    if(FAB.getVisibility()==View.VISIBLE) {//FAB is visible
                        FAB.setVisibility(View.GONE);//change status
                    }
                }else{//not the top
                    if(FAB.getVisibility()==View.GONE){//FAB is gone
                        FAB.setVisibility(View.VISIBLE);//change status
                    }
                }
            }
        });
        FAB = (FloatingActionButton) view.findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager.scrollToPosition(0);
            }
        });
        changeTheme();
        return view;
    }

    public void addMoreItem(int start) {
        bookPresenter.getBookByTag(this,start,tag,true);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("position");
            MainActivity activity = (MainActivity) mContext;
            activity.setThemeChangeListener(new ThemeChangerListener() {
                @Override
                public void onThemeChange() {
                    changeTheme();
                }
            },position);
        }
        tag= CommonUtil.getPositionTag(position);
        bookPresenter = BookPresenter.getSingletonPresenter();
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        bookPresenter.getBookByTag(this,start,tag,false);
    }

    private void changeTheme(){
        swipeRefresh.setColorSchemeColors(ThemeUtil.getThemeColor());
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked}};
        int[] colors = new int[]{ThemeUtil.getThemeColor()};
        ColorStateList csl = new ColorStateList(states, colors);
        FAB.setBackgroundTintList(csl);
    }


    private void refreshFruits() {
        Random random = new Random();
        start = random.nextInt(100);
        bookPresenter.getBookByTag(this,start,tag,false);
    }

    @Override
    public void onSuccess(List<Book> books, boolean isLoadMore) {
        if(!isLoadMore) {//第一次加载而不是上拉加载
            adapter.setGetBookCount(books.size());
            bookList.clear();
            for (Book book : books) {
                bookList.add(book);
            }
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null) {
                        recycleView.setAdapter(adapter);
                        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }
                }
            });
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
        if(CommonUtil.isConnectivity(mContext)){
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }else{
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        swipeRefresh.setRefreshing(false);
    }


    public interface ThemeChangerListener{
        void onThemeChange();
    }
}
