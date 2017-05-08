package com.wkui.douban.easysearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
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
import com.wkui.douban.easysearch.bean.Book;
import com.wkui.douban.easysearch.view.BookDetailActivity;

import java.util.List;

/**
 * Created by wkui on 2017/3/6.
 */

public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Book> mBookList;
    private int getBookCount;
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;

    //无更多加载项
    public static final int NO_MORE_ITEM = 2;
    //上拉加载更多状态-默认为0
    private int load_more_status=0;
    private LayoutInflater mInflater;
    private List<String> mTitles=null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    /**
     * Item View Holder
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView fruitImage;
        TextView fruitName;
        RatingBar ratingBar;
        TextView ratingDescribe;
        public ItemViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView;
            fruitImage = (ImageView) itemView.findViewById(R.id.book_image);
            fruitName = (TextView) itemView.findViewById(R.id.book_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            ratingDescribe = (TextView) itemView.findViewById(R.id.rating_describe);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends  RecyclerView.ViewHolder{
        public TextView foot_view_item_tv;
        public LinearLayout foot_layout;
        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
            foot_layout = (LinearLayout) view.findViewById(R.id.foot_layout);
            foot_layout.setVisibility(View.VISIBLE);
        }
    }

    public BookListAdapter(List<Book> bookList){
        mBookList = bookList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_book_item,parent,false);
            final ItemViewHolder holder = new ItemViewHolder(view);
            holder.layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Book book = mBookList.get(position);
                    Intent intent = new Intent(mContext,BookDetailActivity.class);
                    intent.putExtra(BookDetailActivity.BOOK_ID,book.getId());
                    mContext.startActivity(intent);
                }
            });
            return holder;
        } else if(viewType==TYPE_FOOTER){
            View foot_view=LayoutInflater.from(mContext).inflate(R.layout.recycler_load_more_layout,parent,false);
            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
            if(getBookCount<Integer.parseInt(CommonUtil.SEARCH_BOOK_COUNT)){
                Log.d("hello","no more item="+getBookCount);
                load_more_status = NO_MORE_ITEM ;
            }else{
                load_more_status = LOADING_MORE ;
            }
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BookListAdapter.ItemViewHolder) {
            BookListAdapter.ItemViewHolder itemViewHolder = (BookListAdapter.ItemViewHolder)holder;
            Book book = mBookList.get(position);
            itemViewHolder.fruitName.setText(book.getTitle());
            Log.d("hello","data="+Float.parseFloat(book.getRating().getAverage())/2);
            itemViewHolder.ratingBar.setRating(Float.parseFloat(book.getRating().getAverage())/2);
            itemViewHolder.ratingDescribe.setText(book.getRating().getAverage());
            Glide
                    .with(mContext)
                    .load(book.getImages().getLarge())
                    .placeholder(R.drawable.ic_book_subjectcover_default) // can also be a drawable
                    .into(itemViewHolder.fruitImage);
        }else if(holder instanceof BookListAdapter.FootViewHolder){
            BookListAdapter.FootViewHolder footViewHolder=(BookListAdapter.FootViewHolder)holder;
            switch (load_more_status){
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText(mContext.getString(R.string.pull_to_load_more));
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText(mContext.getString(R.string.is_loading_more));
                    break;
                case NO_MORE_ITEM:
                    footViewHolder.foot_layout.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBookList.size()+1;
    }//+1

    /**
     * 进行判断是普通Item视图还是FootView视图
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {//判断layoutManager来更改底部刷新item占几个
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public void setGetBookCount(int bookCount){
        getBookCount = bookCount;
    }
}
