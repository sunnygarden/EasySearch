package com.wkui.douban.easysearch.viewInterface;

import com.wkui.douban.easysearch.bean.Book;

/**
 * Created by wkui on 2017/3/9.
 */

public interface IGetBookDetailView {

    void onSuccess(Book book);

    void onFailure();
}
