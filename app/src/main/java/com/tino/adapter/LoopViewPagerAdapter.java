package com.tino.adapter;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.asm.Opcodes;
import com.tino.R;

import java.util.ArrayList;
import java.util.List;

public class LoopViewPagerAdapter extends BaseLoopPagerAdapter {
    private final List<String> mHeroes = new ArrayList();
    private final ViewGroup mIndicators;
    private int mLastPosition;

    public static class ViewHolder {
        ImageView ivBanner;
        TextView tvName;
    }

    public LoopViewPagerAdapter(ViewPager viewPager, ViewGroup indicators) {
        super(viewPager);
        this.mIndicators = indicators;
    }

    public void setList(List<String> heroes) {
        this.mHeroes.clear();
        this.mHeroes.addAll(heroes);
        notifyDataSetChanged();
    }

    private void initIndicators() {
        if (this.mIndicators.getChildCount() != this.mHeroes.size() && this.mHeroes.size() > 1) {
            this.mIndicators.removeAllViews();
            Resources res = this.mIndicators.getResources();
            int size = res.getDimensionPixelOffset(R.dimen.indicator_size);
            int margin = res.getDimensionPixelOffset(R.dimen.indicator_margin);
            for (int i = 0; i < getPagerCount(); i++) {
                ImageView indicator = new ImageView(this.mIndicators.getContext());
                indicator.setAlpha(Opcodes.GETFIELD);
                LayoutParams lp = new LayoutParams(size, size);
                lp.setMargins(margin, 0, 0, 0);
                lp.gravity = Gravity.CENTER;
                indicator.setLayoutParams(lp);
                indicator.setImageDrawable(res.getDrawable(R.drawable.selector_indicator));
                this.mIndicators.addView(indicator);
            }
        }
    }

    public void notifyDataSetChanged() {
        initIndicators();
        super.notifyDataSetChanged();
    }

    public int getPagerCount() {
        return this.mHeroes.size();
    }

    public String getItem(int position) {
        return (String) this.mHeroes.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager, parent, false);
            holder = new ViewHolder();
            holder.ivBanner = (ImageView) convertView.findViewById(R.id.ivBanner);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String picUrl = (String) this.mHeroes.get(position);
        holder.tvName.setText("图片" + position);
        return convertView;
    }

    public void onPageItemSelected(int position) {
        if (VERSION.SDK_INT >= 11) {
            this.mIndicators.getChildAt(this.mLastPosition).setActivated(false);
            this.mIndicators.getChildAt(position).setActivated(true);
        }
        this.mLastPosition = position;
    }
}
