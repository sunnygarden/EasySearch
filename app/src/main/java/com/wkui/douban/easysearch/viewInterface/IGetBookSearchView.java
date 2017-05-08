package com.wkui.douban.easysearch.viewInterface;

import com.wkui.douban.easysearch.bean.Book;

import java.util.List;

/**
 * Created by wkui on 2017/3/9.
 */

public interface IGetBookSearchView {

    void onSuccess(List<Book> books,int bookCount, boolean isLoadMore);

    void onFailure();
}
