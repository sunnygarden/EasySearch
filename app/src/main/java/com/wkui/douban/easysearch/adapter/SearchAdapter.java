package com.wkui.douban.easysearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.LogUtil;
import com.wkui.douban.easysearch.bean.Book;
import com.wkui.douban.easysearch.view.BookDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wkui on 2017/3/6.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Book> mBooksList;
    private Context mContext;
    private int getBookCount;
    private int totalBookCount;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private static final int TYPE_TOP = 2;  //顶部TopView

    //上拉加载更多
    public static final int  PULL_UP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;
    //无更多加载项
    public static final int NO_MORE_ITEM = 2;
    //上拉加载更多状态-默认为0
    private int load_more_status=0;

    static class SearchItemViewHolder extends RecyclerView.ViewHolder{
        LinearLayout bookView;
        @BindView(R.id.book_search_image)
        ImageView bookImage;
        @BindView(R.id.book_search_title)
        TextView bookTitle;
        @BindView(R.id.rating_describe)
        TextView bookRateDescribe;
        @BindView(R.id.book_search_author)
        TextView bookAuthor;
        @BindView(R.id.book_search_publisher)
        TextView bookPublisher;
        @BindView(R.id.rating_bar)
        RatingBar rateBar;

        public SearchItemViewHolder(View itemView) {
            super(itemView);
            bookView = (LinearLayout) itemView;
            ButterKnife.bind(this,itemView);
        }
    }

    public SearchAdapter(List<Book> booksList){
        mBooksList = booksList;
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder{
        public TextView search_count_tv;

        public TopViewHolder(View itemView) {
            super(itemView);
            search_count_tv = (TextView) itemView.findViewById(R.id.search_book_count);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_list_item, parent, false);
            final SearchItemViewHolder holder = new SearchItemViewHolder(view);
            holder.bookView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Book book = mBooksList.get(position-1);//minus top item
                    Intent intent = new Intent(mContext, BookDetailActivity.class);
                    intent.putExtra(BookDetailActivity.BOOK_ID, book.getId());
                    mContext.startActivity(intent);
                }
            });
            return holder;
        }else if(viewType==TYPE_FOOTER){
            View foot_view=LayoutInflater.from(mContext).inflate(R.layout.recycler_load_more_layout,parent,false);
            BookListAdapter.FootViewHolder footViewHolder = new BookListAdapter.FootViewHolder(foot_view);
            if(getBookCount<Integer.parseInt(CommonUtil.SEARCH_BOOK_COUNT)){
                Log.d("hello","no more item="+getBookCount);
                load_more_status = NO_MORE_ITEM ;
            }else{
                load_more_status = LOADING_MORE ;
            }
            return footViewHolder;
        }else if(viewType==TYPE_TOP){
            View top_view = LayoutInflater.from(mContext).inflate(R.layout.search_count_layout,parent,false);
            TopViewHolder topViewHolder = new TopViewHolder(top_view);
            return topViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SearchAdapter.SearchItemViewHolder) {
            SearchAdapter.SearchItemViewHolder searchItemViewHolder = (SearchAdapter.SearchItemViewHolder)holder;
            Book book = mBooksList.get(position-1);
            searchItemViewHolder.bookTitle.setText(book.getTitle());
            Glide
                    .with(mContext)
                    .load(book.getImages().getLarge())
                    .placeholder(R.drawable.ic_book_subjectcover_default) // can also be a drawable
                    .into(searchItemViewHolder.bookImage);
            searchItemViewHolder.rateBar.setRating(Float.parseFloat(book.getRating().getAverage())/2);
            searchItemViewHolder.bookRateDescribe.setText(mContext.getString(R.string.rate_describe_count,book.getRating().getAverage()));
            searchItemViewHolder.bookAuthor.setText(CommonUtil.authorCombine(mContext,book.getAuthor()));
            searchItemViewHolder.bookPublisher.setText(book.getPublisher());
        }else if(holder instanceof BookListAdapter.FootViewHolder){
            BookListAdapter.FootViewHolder footViewHolder=(BookListAdapter.FootViewHolder)holder;
            switch (load_more_status){
                case PULL_UP_LOAD_MORE:
                    footViewHolder.foot_layout.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText(mContext.getString(R.string.pull_to_load_more));
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_layout.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText(mContext.getString(R.string.is_loading_more));
                    break;
                case NO_MORE_ITEM:
                    footViewHolder.foot_layout.setVisibility(View.INVISIBLE);
                    break;
            }
        }else if(holder instanceof TopViewHolder){
            TopViewHolder topViewHolder = (TopViewHolder) holder;
            topViewHolder.search_count_tv.setText(mContext.getString(R.string.search_book_count,totalBookCount));
        }

    }


    @Override
    public int getItemCount() {
        Log.d("hello","getItemCount == "+mBooksList.size());
        return mBooksList.size()+2;//底部上拉刷新
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        //内容不够一页，不显示上拉刷新
        LogUtil.d(LogUtil.TAG,"get Item po="+position+",getIC="+getItemCount());
        if (position -1 == mBooksList.size()) {
            return TYPE_FOOTER;
        } else if(position == 0){
            return TYPE_TOP;
        }else {
            return TYPE_ITEM;
        }
    }



    public void setGetBookCount(int bookCount){
        getBookCount = bookCount;
    }

    public void setTotalBookCount(int bookCount){
        totalBookCount = bookCount;
    }

}
