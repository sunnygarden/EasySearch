package com.wkui.douban.easysearch.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.Utils.LogUtil;
import com.wkui.douban.easysearch.Utils.ThemeColor;
import com.wkui.douban.easysearch.Utils.ThemeUtil;

import java.util.List;

/**
 * Created by wkui on 2017/3/6.
 */

public class ThemeSetAdapter extends RecyclerView.Adapter<ThemeSetAdapter.ViewHolder>{

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

   private List<ThemeColor> mThemeList;

    private int mPosition = ThemeUtil.getThemePosition();


    public ThemeSetAdapter (List<ThemeColor> themeList){
        mThemeList = themeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_theme_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mPosition = (int)holder.cardView.getTag();
                    mOnItemClickListener.onItemClick(view,mPosition);

                }
            }
        });//设置点击事件
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ThemeColor themeColor = mThemeList.get(position);
        LogUtil.e("hello","themeColor.getColor()="+themeColor.getColor());
        holder.showColor.setBackgroundColor(themeColor.getColor());
        holder.cardView.setTag(position);
        if(themeColor.isChosen()) {
            holder.isChoosen.setVisibility(View.VISIBLE);
        }else{
            holder.isChoosen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mThemeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView showColor;
        ImageView isChoosen;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.theme_card_view);
            showColor = (ImageView) itemView.findViewById(R.id.them_color);
            isChoosen = (ImageView) itemView.findViewById(R.id.theme_choose);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public int getPosition(){
        return mPosition;
    }


}
