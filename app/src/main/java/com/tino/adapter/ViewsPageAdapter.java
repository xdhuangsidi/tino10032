package com.tino.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewsPageAdapter extends PagerAdapter {
    String[] titles;
    List<View> views;

    public ViewsPageAdapter(List<View> views) {
        this.views = views;
    }

    public ViewsPageAdapter(List<View> views, String[] titles) {
        this.views = views;
        this.titles = titles;
    }

    public int getCount() {
        return this.views.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) this.views.get(position));
    }

    public CharSequence getPageTitle(int position) {
        if (this.titles == null || this.titles.length < position) {
            return super.getPageTitle(position);
        }
        return this.titles[position];
    }

    public Object instantiateItem(ViewGroup container, int position) {
        container.addView((View) this.views.get(position));
        return this.views.get(position);
    }
}
