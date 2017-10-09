package com.tino.ui.find;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.tino.R;
import com.tino.adapter.ViewsPageAdapter;
import com.tino.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class Findragment extends BaseFragment {

    ViewPager viewPager;
    SlidingTabLayout slide_tab;
    @Override
    public void init() {
/*        String[] titles = new String[]{"事件", "墙"};
        List<View> views = new ArrayList();
        View view_bookinfo = View.inflate(getActivity(), R.layout.inc_recycler_view, null);
        View view_comments = View.inflate(getActivity(), R.layout.inc_recycler_view, null);

        views.add(view_bookinfo);
        views.add(view_comments);
        viewPager.setAdapter(new ViewsPageAdapter(views, titles));
        this.slide_tab.setViewPager(this.viewPager, titles);*/

    }

    @Override
    public int setInflateId() {
        return  R.layout.fragment_friend;
    }

    @Override
    public void bindview(View view) {
 /*       viewPager=(ViewPager)view.findViewById(R.id.view_pager_find);
        slide_tab=(SlidingTabLayout)view.findViewById(R.id.slide_tab_find);
*/


    }

}
