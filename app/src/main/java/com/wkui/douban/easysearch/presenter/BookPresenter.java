package com.wkui.douban.easysearch.presenter;

import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.bean.Book;
import com.wkui.douban.easysearch.bean.BooksResponse;
import com.wkui.douban.easysearch.douban.RequestInterface;
import com.wkui.douban.easysearch.viewInterface.IGetBookDetailView;
import com.wkui.douban.easysearch.viewInterface.IGetBookSearchView;
import com.wkui.douban.easysearch.viewInterface.IGetBookView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wkui on 2017/3/9.
 */

public class BookPresenter {

    private static BookPresenter bookPresenter = null;
    private Retrofit retrofit;
    private RequestInterface requestInterface;

    private BookPresenter(){
        retrofit = new Retrofit.Builder()
                .baseUrl(CommonUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestInterface=retrofit.create(RequestInterface.class);

    }

    public static BookPresenter getSingletonPresenter(){
            if(bookPresenter == null){
                synchronized(BookPresenter.class){
                    if(bookPresenter == null){
                        bookPresenter = new BookPresenter();
                    }
                }
            }
        return bookPresenter;
    }

   public void getBookByTag(final IGetBookView iGetBookView, int start, String tag , final boolean isLoadMore){
       Map<String, String> options = new HashMap<String, String>();
       options.put("q", "");
       options.put("tag", tag);
       options.put("start", Integer.toString(start));
       options.put("count", CommonUtil.SEARCH_BOOK_COUNT);
       Call<BooksResponse> call=requestInterface.getBookData(options);
       call.enqueue(new Callback<BooksResponse>() {
           @Override
           public void onResponse(Call<BooksResponse> call,        Response<BooksResponse> response) {
               if(response!=null&&response.body()!=null) {
                   iGetBookView.onSuccess(response.body().getBooks(),isLoadMore);
                   }else{
                   iGetBookView.onFailure();
               }
           }
           @Override
           public void onFailure(Call<BooksResponse> call, Throwable t) {
               iGetBookView.onFailure();
           }
       });
   }

    public void getBookDetailInfoById(final IGetBookDetailView iGetBookDetailView, String bookId) {

        Call<Book> call = requestInterface.getBookDetail(bookId);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response != null && response.body() != null) {
                    iGetBookDetailView.onSuccess(response.body());
                }else{
                    iGetBookDetailView.onFailure();
                }
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                iGetBookDetailView.onFailure();
            }
        });
    }


    public void getBookDetailInfoByIsbn(final IGetBookDetailView iGetBookDetailView, String bookIsbn){

        Call<Book> call = requestInterface.getBookScan(bookIsbn);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response != null && response.body() != null) {
                   iGetBookDetailView.onSuccess(response.body());
                } else {
                    iGetBookDetailView.onFailure();
                }
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                    iGetBookDetailView.onFailure();
            }
        });
    }

    public void getBookByKeyword(final IGetBookSearchView iGetBookSearchView, String keyword, int start, final boolean isLoadmore){

        Map<String, String> options = new HashMap<String, String>();
        options.put("q", keyword);
        options.put("tag", "");
        options.put("start", Integer.toString(start));
        options.put("count", CommonUtil.SEARCH_BOOK_COUNT);
        Call<BooksResponse> call=requestInterface.getBookData(options);
        call.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                if(response!=null&&response.body()!=null) {
                    iGetBookSearchView.onSuccess(response.body().getBooks(),response.body().getTotal(),isLoadmore);
                }else{
                    iGetBookSearchView.onFailure();
                }
                  }
            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                iGetBookSearchView.onFailure();
            }
        });
    }

}
