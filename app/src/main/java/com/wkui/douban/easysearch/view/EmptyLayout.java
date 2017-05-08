package com.wkui.douban.easysearch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.CommonUtil;
import com.wkui.douban.easysearch.Utils.LogUtil;

/**
 * Created by wkui on 2017/3/6.
 */

public class EmptyLayout extends LinearLayout implements View.OnClickListener{

	//网络没有连接
	public static final int NETWORK_ERROR = 1;
	//正在加载数据
	public static final int NETWORK_LOADING = 2;
	//没有数据
	public static final int NODATA = 3;
	//加载成功,隐藏加载布局
	public static final int HIDE_LAYOUT = 4;

	public static final int NODATA_ENABLE_CLICK = 5;

	private ProgressBar animProgress;
	private boolean clickEnable = true;
	private final Context context;
	private ImageView img;
	private OnClickListener listener;
	private int mErrorState;
	private String strNoDataContent = "";
	private TextView tv;
	private String TAG = getClass().getSimpleName();

	public EmptyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		View view = View.inflate(context, R.layout.view_error_layout, null);
		img = (ImageView) view.findViewById(R.id.img_error_layout);
		tv = (TextView) view.findViewById(R.id.tv_error_layout);
		animProgress = (ProgressBar) view.findViewById(R.id.animProgress);
		setBackgroundColor(-1);
		setOnClickListener(this);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickEnable) {
					setErrorType(NETWORK_LOADING);
					if (listener != null)
						listener.onClick(v);
				}
			}
		});
		addView(view);
	}

	@Override
	public void onClick(View v) {
		if (clickEnable) {
			 setErrorType(NETWORK_LOADING);
			if (listener != null){
				listener.onClick(v);
			}else{
				LogUtil.e(TAG,"listener null");
			}
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		onSkinChanged();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public void onSkinChanged() {
	}

	public void setErrorType(int i) {
		setVisibility(View.VISIBLE);
		switch (i) {
		case NETWORK_ERROR:
			mErrorState = NETWORK_ERROR;
			if (CommonUtil.isConnectivity(context)) {
				tv.setText(R.string.error_view_load_error_click_to_refresh);
				img.setBackgroundResource(R.drawable.page_icon_failed);
			} else {
				tv.setText(R.string.error_view_network_error_click_to_refresh);
				img.setBackgroundResource(R.drawable.page_icon_network);
			}
			img.setVisibility(View.VISIBLE);
			animProgress.setVisibility(View.GONE);
			clickEnable = true;
			break;
		case NETWORK_LOADING:
			mErrorState = NETWORK_LOADING;
			animProgress.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
			tv.setText(R.string.error_view_loading);
			clickEnable = false;
			break;
		case NODATA:
			mErrorState = NODATA;
			img.setBackgroundResource(R.drawable.page_icon_empty);
			img.setVisibility(View.VISIBLE);
			animProgress.setVisibility(View.GONE);
			setTvNoDataContent();
			clickEnable = true;
			break;
		case HIDE_LAYOUT:
			setVisibility(View.GONE);
			break;
		case NODATA_ENABLE_CLICK:
			mErrorState = NODATA_ENABLE_CLICK;
			img.setBackgroundResource(R.drawable.page_icon_empty);
			img.setVisibility(View.VISIBLE);
			animProgress.setVisibility(View.GONE);
			setTvNoDataContent();
			clickEnable = true;
			break;
		default:
			break;
		}
	}


	public void setOnLayoutClickListener(OnClickListener listener) {
		this.listener = listener;
	}

	public void setTvNoDataContent() {
		if (!strNoDataContent.equals(""))
			tv.setText(strNoDataContent);
		else
			tv.setText(R.string.error_view_no_data);
	}

	@Override
	public void setVisibility(int visibility) {
		if (visibility == View.GONE)
			mErrorState = HIDE_LAYOUT;
		super.setVisibility(visibility);
	}

}
