package com.wkui.douban.easysearch.viewInterface;

import com.wkui.douban.easysearch.bean.Book;

import java.util.List;

/**
 * Created by wkui on 2017/3/9.
 */

public interface IGetBookView {

    void onSuccess(List<Book> books, boolean isLoadMore);

    void onFailure();
}
