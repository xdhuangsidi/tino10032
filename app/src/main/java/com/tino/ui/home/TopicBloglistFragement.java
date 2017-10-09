package com.tino.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tino.adapter.BLogsAdapter;
import com.tino.adapter.BaseRecyclerAdapter;
import com.tino.adapter.TopicBLogsAdapter;
import com.tino.base.BaseRecyclerFragment;
import com.tino.core.AccountImpl;
import com.tino.core.model.Blog;
import com.tino.ui.TopicActivity;
import com.tino.utils.CommonUtils;

import java.util.List;

/**
 * Created by root on 17-8-5.
 */

public class TopicBloglistFragement extends BaseRecyclerFragment<Blog> {
    TopicBLogsAdapter adapter;


    BroadcastReceiver bReceiver;
    IntentFilter iFilter;

    @Override
    public boolean canLoadmore() {
        return true;
    }



    @Override
    public BaseRecyclerAdapter<Blog> setAdapter() {

        iFilter=new IntentFilter();
        iFilter.addAction("tino.reflash_blog");
        bReceiver=new RefreshReceiver();
        getActivity().registerReceiver(bReceiver, iFilter);


        this.adapter = new TopicBLogsAdapter();

        this.adapter.SetContext(getActivity());
        this.adapter.SetItemclick(new TopicBLogsAdapter.ItemClick() {
            @Override
            public void onItemClick(Blog blog) {
                ShareDetailActivity.launch(getActivity(),blog);

            }
        });

        return this.adapter;
    }
    @Override
    public void initAction() {


    }


    public void toRefresh() {
        super.toRefresh();
        adapter.dataList.clear();
        currentPage=0;
        AccountImpl.getBlogtListFromTopic(CommonUtils.gettoken(getActivity())
                ,((TopicActivity)getActivity()).topicId
                ,CommonUtils.getUid(getActivity()),0,this);

    }
    public void toLoadMore() {
        super.toLoadMore();
        AccountImpl.getBlogtListFromTopic(CommonUtils.gettoken(getActivity())
                ,  ((TopicActivity)getActivity()).topicId
                ,CommonUtils.getUid(getActivity()),this.currentPage+8,this);

    }
    @Override
    public void onSuccess(List<Blog> datas) {
        String uid=CommonUtils.getUid();
        if(datas!=null){
            for(Blog blog:datas){
                   blog.setlikeAndCollect(uid);
            }
        }
        if (datas == null || datas.size() <=0) {
            this.recyclerView.setCanloadMore(false);
        } else {
            this.recyclerView.setCanloadMore(true);
        }

        hideDefaultView();
        if (this.state == 0) {
            this.currentPage = 0;
            if (datas == null || datas.size() == 0) {
                showDefaultView();
            } else {
                hideDefaultView();
            }
            this.adapter.setDataList(datas);
            return;
        }
        this.currentPage+=8;
        this.adapter.addItems(datas);

    }

    public  class RefreshReceiver extends BroadcastReceiver {
        public RefreshReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            toRefresh();
        }

    }

}
