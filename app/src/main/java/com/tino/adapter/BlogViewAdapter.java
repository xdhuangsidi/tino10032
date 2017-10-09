package com.tino.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.List;

public class BlogViewAdapter extends FragmentPagerAdapter {
    private List<String> list_Title;
    private List<Fragment> list_fragment;

    public BlogViewAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }

    public Fragment getItem(int position) {
        return (Fragment) this.list_fragment.get(position);
    }

    public int getCount() {
        return this.list_Title.size();
    }

    public CharSequence getPageTitle(int position) {
        if (this.list_Title != null) {
            return (CharSequence) this.list_Title.get(position);
        }
        return super.getPageTitle(position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
