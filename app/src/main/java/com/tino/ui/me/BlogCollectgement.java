package com.tino.ui.me;

import com.tino.adapter.BLogsAdapter;
import com.tino.adapter.BaseRecyclerAdapter;
import com.tino.base.BaseRecyclerFragment;
import com.tino.core.AccountImpl;
import com.tino.core.model.Blog;
import com.tino.ui.home.ShareDetailActivity;
import com.tino.utils.CommonUtils;

import java.util.List;

/**
 * Created by root on 17-8-5.
 */

public class BlogCollectgement extends BaseRecyclerFragment<Blog> {
    BLogsAdapter adapter;
    @Override
    public boolean canLoadmore() {
        return true;
    }
    @Override
    public BaseRecyclerAdapter<Blog> setAdapter() {
        this.adapter = new BLogsAdapter();

        this.adapter.SetContext(getActivity());
        this.adapter.SetItemclick(new BLogsAdapter.ItemClick() {
            @Override
            public void onItemClick(Blog blog) {
                ShareDetailActivity.launch(getActivity(),blog);
                //getActivity().startActivity(getActivity());
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
        AccountImpl.getCollectBlogtList(CommonUtils.gettoken(getActivity()), CommonUtils.getUid(getActivity()), 0, this);
    }

    public void toLoadMore() {
        super.toLoadMore();
        System.out.println("requestPage:+++++++++++++"+(currentPage+8));
        final String id=getActivity().getSharedPreferences("login_data", 0).getString("uid", "");
        final String token=getActivity().getSharedPreferences("login_data", 0).getString("token", "");
        AccountImpl.getCollectBlogtList(token,id,currentPage+8,this);

    }
    @Override
    public void onSuccess(List<Blog> datas) {
        String uid=CommonUtils.getUid();
        if(datas!=null){
            for(Blog blog:datas){
                blog.setIscollect(true);
                blog.setlike(uid);
            }
        }
        hideDefaultView();
        if (datas == null || datas.size() <=0) {
            this.recyclerView.setCanloadMore(false);
        } else {
            this.recyclerView.setCanloadMore(true);
        }
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
}
