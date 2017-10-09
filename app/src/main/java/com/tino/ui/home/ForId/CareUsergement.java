package com.tino.ui.home.ForId;

import com.tino.adapter.BaseRecyclerAdapter;
import com.tino.adapter.UserListAdapter;
import com.tino.base.BaseRecyclerFragment;
import com.tino.core.AccountImpl;
import com.tino.core.model.User;
import com.tino.ui.home.UserDetialActivity;
import com.tino.ui.home.UserDetialFromIdActivity;

/**
 * Created by root on 17-8-5.
 */

public class CareUsergement extends BaseRecyclerFragment<User> {
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
        AccountImpl.getCareUsers("",((UserDetialFromIdActivity)getActivity()).user.getUserId(),0,this);

    }
    public void toLoadMore() {
        super.toLoadMore();
        AccountImpl.getCareUsers("",((UserDetialFromIdActivity)getActivity()).user.getUserId(),currentPage+8,this);

    }
}
