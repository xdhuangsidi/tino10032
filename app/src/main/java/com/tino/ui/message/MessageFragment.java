package com.tino.ui.message;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.adapter.BlogViewAdapter;
import com.tino.adapter.ViewsPageAdapter;
import com.tino.base.BaseFragment;
import com.tino.ui.home.BloglistFragement;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends BaseFragment {

    ViewPager viewPager;
    SlidingTabLayout slide_tab;
    @Override
    public void init() {

        List<Fragment> fragmentList = new ArrayList();
        fragmentList.add(new  ConversationFragment());
        fragmentList.add(new CustConversationFragment());
        List<String> titles = new ArrayList();
        titles.add("聊天");
        titles.add("通知");
        viewPager.setAdapter(new BlogViewAdapter(getFragmentManager(), fragmentList, titles));//与主页的适配器功能相同
        this.slide_tab.setViewPager(this.viewPager/*, titles*/);


    }

    @Override
    public int setInflateId() {
        return  R.layout.fragment_friend;
    }

    @Override
    public void bindview(View view) {
        viewPager=(ViewPager)view.findViewById(R.id.viewPager_msg);
        slide_tab=(SlidingTabLayout)view.findViewById(R.id.slide_tab_msg);



    }

}
