package com.tino.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.tino.R;

import java.util.Arrays;
import java.util.List;

public class ViewPagerHolder extends CommonHolder<String> {
  //  @Bind({2131558766})
    LinearLayout indicators;
    List<String> pics = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});
  //  @Bind({2131558574})
    ViewPager viewPager;
    LoopViewPagerAdapter viewPagerAdapter;

    public ViewPagerHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.layout_viewpager);
       viewPager = (ViewPager)root.findViewById( R.id.viewPager);
      indicators = (LinearLayout) root.findViewById( R.id.indicators);

    }

    public void bindData(String s) {
        if (this.viewPager.getAdapter() == null) {
            this.viewPagerAdapter = new LoopViewPagerAdapter(this.viewPager, this.indicators);
            this.viewPager.setAdapter(this.viewPagerAdapter);
            this.viewPager.addOnPageChangeListener(this.viewPagerAdapter);
            this.viewPagerAdapter.setList(this.pics);
            return;
        }
        this.viewPagerAdapter.setList(this.pics);
    }
}
