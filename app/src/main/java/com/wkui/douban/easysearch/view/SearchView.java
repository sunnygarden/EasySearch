package com.wkui.douban.easysearch.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.ThemeUtil;

/**
 * Created by wkui on 2017/3/5.
 */
public class SearchView extends LinearLayout {

    private EditText etInput;
    private ImageView ivDelete;
    private ImageView backImage;
    private ImageView searchImage;
    private Context mContext;
    private SearchViewListener mListener;

    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.activity_search_title, this);
        initViews();
    }

    private void initViews() {
        LinearLayout searchView = (LinearLayout) findViewById(R.id.search_view);
        searchView.setBackgroundColor(ThemeUtil.getThemeColor());
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        backImage = (ImageView) findViewById(R.id.search_back_image_view);
        searchImage = (ImageView) findViewById(R.id.search_image);


        ivDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etInput.setText("");
                ivDelete.setVisibility(GONE);
            }
        });
        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
        backImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.hideSoftInput(mContext,backImage);
                ((Activity) mContext).finish();
            }
        });
        searchImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyStartSearching(etInput.getText().toString());
            }
        });
    }

    private void notifyStartSearching(String text){
        if(!TextUtils.isEmpty(text)){
            if (mListener != null) {
                mListener.onSearch(text);
            }
        }
        CommonUtil.hideOrShowSoftInput(mContext);
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
//                if (mListener != null) {
//                    mListener.onSearch(etInput.getText().toString());
//                }
            } else {
                ivDelete.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public interface SearchViewListener {
        void onSearch(String text);
    }

}
