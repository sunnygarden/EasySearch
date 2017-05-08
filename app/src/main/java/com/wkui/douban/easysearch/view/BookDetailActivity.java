package com.wkui.douban.easysearch.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.ThemeUtil;
import com.wkui.douban.easysearch.base.BaseActivity;
import com.wkui.douban.easysearch.bean.Book;
import com.wkui.douban.easysearch.presenter.BookPresenter;
import com.wkui.douban.easysearch.viewInterface.IGetBookDetailView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by wkui on 2017/3/5.
 */

public class BookDetailActivity extends BaseActivity implements View.OnClickListener,IGetBookDetailView {

    @BindViews({R.id.content_summary_layout,R.id.content_author_summary_layout,R.id.content_catalog_layout})
    List<LinearLayout> contentList;
    @BindViews({R.id.book_content_text_summary,R.id.book_content_text_author_summary,R.id.book_content_text_catalog})
    List<TextView> mContentTextList;//show text
    @BindViews({R.id.show_more_summary,R.id.show_more_author_summary,R.id.show_more_catalog})
    List<RelativeLayout> mShowMoreList;//show more layout
    @BindViews({R.id.more_shrink_content_summary,R.id.more_shrink_content_author_summary,R.id.more_shrink_content_catalog})
    List<TextView> moreContentList;//show more text
    @BindViews({R.id.spread_summary,R.id.spread_author_summary,R.id.spread_catalog})
    List<ImageView> mImageSpreadList;//spread image
    @BindViews({R.id.shrink_up_summary,R.id.shrink_up_author_summary,R.id.shrink_up_catalog})
    List<ImageView> mImageShrinkUpList;//shrink image

    public static final String BOOK_ID = "book_id";
    public static final String BOOK_ISBN = "book_isbn";

    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 4;// 默认展示最大行数4行
    private static final int SHRINK_UP_STATE_SUMMARY = 1;// 简介收起状态
    private static final int SPREAD_STATE_SUMMARY = 2;// 简介展开状态
    private static int mStateSummary = SHRINK_UP_STATE_SUMMARY;//简介默认收起状态
    private static final int SHRINK_UP_STATE_AUTHOR_SUMMARY = 3;// 作者简介收起状态
    private static final int SPREAD_STATE_AUTHOR_SUMMARY = 4;// 作者简介展开状态
    private static int mStateAuthorSummary = SHRINK_UP_STATE_AUTHOR_SUMMARY;//作者简介默认收起状态
    private static final int SHRINK_UP_STATE_CATALOG = 5;// 目录收起状态
    private static final int SPREAD_STATE_CATALOG = 6;// 目录展开状态
    private static int mStateCatalog = SHRINK_UP_STATE_CATALOG;//目录默认收起状态

    private String bookId;
    private String bookIsbn;
    private String bookTitle;
    private Book responseBook;
    private Activity mContext;
    private BookPresenter bookPresenter;

    @BindView(R.id.book_image_view)
    ImageView bookImageView;
    @BindView(R.id.book_title)
    TextView bookTitleView;
    @BindView(R.id.book_author)
    TextView bookAuthorView;
    @BindView(R.id.book_publisher)
    TextView bookPublisherView;
    @BindView(R.id.book_publish_time)
    TextView bookPublishTimeView;
    @BindView(R.id.rate_number)
    TextView rateNumberView;
    @BindView(R.id.rating_star)
    RatingBar rateBar;
    @BindView(R.id.rating_people)
    TextView ratePeopleView;

    @BindView(R.id.layout_wait)
    CoordinatorLayout waitLayout;
    @BindView(R.id.activity_detail)
    CoordinatorLayout showDetailLayout;
    @BindView(R.id.img_error_layout)
    ImageView img;
    @BindView(R.id.tv_error_layout)
    TextView tv;
    @BindView(R.id.animProgress)
    ProgressBar animProgress;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_book_detail);
        mContext = this;
        ButterKnife.bind(mContext);
        bookPresenter = BookPresenter.getSingletonPresenter();
        initViews();
        getData();
    }

    private void initViews(){
        //wait to load layout
        Toolbar wait_toolbar = (Toolbar) findViewById(R.id.wait_toolbar);
        wait_toolbar.setBackgroundColor(ThemeUtil.getThemeColor());
        StatusBarUtil.setColor(mContext,ThemeUtil.getThemeColor(),80);
        //applyKitKatTranslucency(ThemeUtil.getThemeColor());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        collapsingToolbarLayout.setBackgroundColor(ThemeUtil.getThemeColor());
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(bookTitle);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        mShowMoreList.get(0).setOnClickListener(this);
        mShowMoreList.get(1).setOnClickListener(this);
        mShowMoreList.get(2).setOnClickListener(this);
    }

    private void getData(){
        Intent intent = getIntent();
        bookId = intent.getStringExtra(BOOK_ID);
        bookIsbn = intent.getStringExtra(BOOK_ISBN);
        if (bookId != null) {
            bookPresenter.getBookDetailInfoById(this,bookId);
        }
        if (bookIsbn != null) {
            bookPresenter.getBookDetailInfoByIsbn(this,bookIsbn);
        }
    }

    @Override
    protected void handleThemeChange() {

    }

    private void connectFail() {
        tv.setText(R.string.tip_network_error);
        img.setBackgroundResource(R.drawable.page_icon_network);
        img.setVisibility(View.VISIBLE);
        animProgress.setVisibility(View.GONE);
    }

    private void noData(){
        tv.setText(R.string.error_view_no_data);
        img.setBackgroundResource(R.drawable.page_icon_empty);
        img.setVisibility(View.VISIBLE);
        animProgress.setVisibility(View.GONE);
    }

    private void getBackGroundColor(String image) {
        Glide.with(this).load(image).asBitmap().into(target);//获取背景色用
    }


    private void setUpBookDetailData(Book book) {
        bookTitle = book.getTitle();
        String bookAuthor = CommonUtil.authorCombine(this, book.getAuthor());
        String bookPublisher = book.getPublisher();
        String bookPublishTime = book.getPubdate();
        if (!TextUtils.isEmpty(bookAuthor)) {
            bookAuthorView.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(bookPublisher)) {
            bookPublisherView.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(bookPublishTime)) {
            bookPublishTimeView.setVisibility(View.VISIBLE);
        }
        String bookImage = book.getImages().getLarge();
        String ratingGrade = book.getRating().getAverage();
        int ratingPeople = book.getRating().getNumRaters();
        String bookSummary = book.getSummary();
        if (bookSummary.length() != 0)
            contentList.get(0).setVisibility(View.VISIBLE);
        String bookAuthorSummary = book.getAuthor_intro();
        if (bookAuthorSummary.length() != 0)
            contentList.get(1).setVisibility(View.VISIBLE);
        String bookCatalog = book.getCatalog();
        if (!TextUtils.isEmpty(bookCatalog) && bookCatalog.length() != 0 && !bookCatalog.startsWith("null")) {
            contentList.get(2).setVisibility(View.VISIBLE);
        }
        Glide.with(this).load(bookImage).into(bookImageView);
        bookTitleView.setText(bookTitle);
        bookAuthorView.setText(this.getString(R.string.book_author, bookAuthor));
        bookPublisherView.setText(this.getString(R.string.book_publisher, bookPublisher));
        bookPublishTimeView.setText(this.getString(R.string.book_publisher_time, bookPublishTime));
        rateNumberView.setText(ratingGrade);
        if (ratingPeople >= 10) {
            rateBar.setRating(Float.parseFloat(ratingGrade) / 2);
            ratePeopleView.setText(this.getString(R.string.rate_people_count, ratingPeople));
        } else {
            rateBar.setRating(0);
            ratePeopleView.setText(getString(R.string.not_enough_comment_people));
        }
        //字数大于100个时显示--伸缩按钮
        if (bookSummary.length() <= 100) {
            mContentTextList.get(0).setText(bookSummary);
        } else {
            mContentTextList.get(0).setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
            mShowMoreList.get(0).setVisibility(View.VISIBLE);
            mContentTextList.get(0).setText(bookSummary);
        }

        if (bookAuthorSummary.length() <= 100) {
            mContentTextList.get(1).setText(bookAuthorSummary);
        } else {
            mContentTextList.get(1).setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
            mShowMoreList.get(1).setVisibility(View.VISIBLE);
            mContentTextList.get(1).setText(bookAuthorSummary);
        }

        if (bookCatalog.length() <= 100) {
            mContentTextList.get(2).setText(bookCatalog);
        } else {
            mContentTextList.get(2).setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
            mShowMoreList.get(2).setVisibility(View.VISIBLE);
            mContentTextList.get(2).setText(bookCatalog);
        }
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            int defaultColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
            Palette palette = Palette.from(bitmap).generate();
            int color = palette.getDominantColor(defaultColor);
            int i = 0;
            while (color == defaultColor&&i<=5) {
                switch (i) {
                    case 0:
                        color = palette.getMutedColor(defaultColor);
                        i++;
                        break;
                    case 1:
                        color = palette.getVibrantColor(defaultColor);
                        i++;
                        break;
                    case 2:
                        color = palette.getDarkMutedColor(defaultColor);
                        i++;
                        break;
                    case 3:
                        color = palette.getDarkMutedColor(defaultColor);
                        i++;
                        break;
                    case 4:
                        color = palette.getLightMutedColor(defaultColor);
                        i++;
                        break;
                    case 5:
                        color = palette.getLightVibrantColor(defaultColor);
                        i++;
                        break;
                    default:
                        break;
                }
            }
            collapsingToolbarLayout.setContentScrimColor(color);
            collapsingToolbarLayout.setBackgroundColor(color);
            collapsingToolbarLayout.setStatusBarScrimColor(color);
            StatusBarUtil.setTransparentForImageView(mContext,bookImageView);
            waitLayout.setVisibility(View.GONE);
            //StatusBarUtil.setColorNoTranslucent(BookDetailActivity.this,color);
            showDetailLayout.setVisibility(View.VISIBLE);//加载好了再显示
            setUpBookDetailData(responseBook);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_more_summary: {
                if (mStateSummary == SPREAD_STATE_SUMMARY) {
                    mContentTextList.get(0).setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mContentTextList.get(0).requestLayout();
                    mImageShrinkUpList.get(0).setVisibility(View.GONE);
                    mImageSpreadList.get(0).setVisibility(View.VISIBLE);
                    moreContentList.get(0).setText(getResources().getString(R.string.get_more));
                    mStateSummary = SHRINK_UP_STATE_SUMMARY;
                } else if (mStateSummary == SHRINK_UP_STATE_SUMMARY) {
                    mContentTextList.get(0).setMaxLines(Integer.MAX_VALUE);
                    mContentTextList.get(0).requestLayout();
                    mImageShrinkUpList.get(0).setVisibility(View.VISIBLE);
                    mImageSpreadList.get(0).setVisibility(View.GONE);
                    mStateSummary = SPREAD_STATE_SUMMARY;
                    moreContentList.get(0).setText(getResources().getString(R.string.shrink_up));
                }
                break;
            }
            case R.id.show_more_author_summary: {
                if (mStateAuthorSummary == SPREAD_STATE_AUTHOR_SUMMARY) {
                    mContentTextList.get(1).setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mContentTextList.get(1).requestLayout();
                    mImageShrinkUpList.get(1).setVisibility(View.GONE);
                    mImageSpreadList.get(1).setVisibility(View.VISIBLE);
                    moreContentList.get(1).setText(getResources().getString(R.string.get_more));
                    mStateAuthorSummary = SHRINK_UP_STATE_AUTHOR_SUMMARY;
                } else if (mStateAuthorSummary == SHRINK_UP_STATE_AUTHOR_SUMMARY) {
                    mContentTextList.get(1).setMaxLines(Integer.MAX_VALUE);
                    mContentTextList.get(1).requestLayout();
                    mImageShrinkUpList.get(1).setVisibility(View.VISIBLE);
                    mImageSpreadList.get(1).setVisibility(View.GONE);
                    mStateAuthorSummary = SPREAD_STATE_AUTHOR_SUMMARY;
                    moreContentList.get(1).setText(getResources().getString(R.string.shrink_up));
                }
                break;
            }
            case R.id.show_more_catalog: {
                if (mStateCatalog == SPREAD_STATE_CATALOG) {
                    mContentTextList.get(2).setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mContentTextList.get(2).requestLayout();
                    mImageShrinkUpList.get(2).setVisibility(View.GONE);
                    mImageSpreadList.get(2).setVisibility(View.VISIBLE);
                    moreContentList.get(2).setText(getResources().getString(R.string.get_more));
                    mStateCatalog = SHRINK_UP_STATE_CATALOG;
                } else if (mStateCatalog == SHRINK_UP_STATE_CATALOG) {
                    mContentTextList.get(2).setMaxLines(Integer.MAX_VALUE);
                    mContentTextList.get(2).requestLayout();
                    mImageShrinkUpList.get(2).setVisibility(View.VISIBLE);
                    mImageSpreadList.get(2).setVisibility(View.GONE);
                    mStateCatalog = SPREAD_STATE_CATALOG;
                    moreContentList.get(2).setText(getResources().getString(R.string.shrink_up));
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onSuccess(Book book) {
        responseBook = book;
        getBackGroundColor(responseBook.getImages().getLarge());
    }

    @Override
    public void onFailure() {
        if (!CommonUtil.isConnectivity(mContext)) {
            connectFail();
        }else{
            noData();
        }
    }
}
