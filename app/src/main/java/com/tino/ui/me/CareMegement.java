package com.tino.ui.me;

import com.tino.adapter.BaseRecyclerAdapter;
import com.tino.adapter.UserListAdapter;
import com.tino.base.BaseRecyclerFragment;
import com.tino.core.AccountImpl;
import com.tino.core.model.User;
import com.tino.utils.CommonUtils;

/**
 * Created by root on 17-8-5.
 */

public class CareMegement extends BaseRecyclerFragment<User> {
    UserListAdapter adapter;
    @Override
    public boolean canLoadmore() {
        return true;
    }
    @Override
    public BaseRecyclerAdapter<User> setAdapter() {
        this.adapter = new UserListAdapter();

        this.adapter.SetContext(getActivity());

        return this.adapter;
    }
    @Override
    public void initAction() {


    }


    public void toRefresh() {
        super.toRefresh();
        adapter.dataList.clear();
        currentPage=0;
        AccountImpl.getCareME(CommonUtils.gettoken(getActivity()),CommonUtils.getUid(getActivity()),0,this);

    }
    public void toLoadMore() {
        super.toLoadMore();
        final String id=getActivity().getSharedPreferences("login_data", 0).getString("uid", "");
        final String token=getActivity().getSharedPreferences("login_data", 0).getString("token", "");
        AccountImpl.getCareME(token,id,currentPage+8,this);

    }
}
