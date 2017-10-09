package com.tino.base;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.tino.R;
import com.tino.adapter.BaseRecyclerAdapter;
import com.tino.core.ActionCallback;
import com.tino.views.haoRefresh.HaoRecyclerView;
import com.tino.views.haoRefresh.LoadMoreListener;
import com.tino.views.haoRefresh.ProgressView;

import java.util.List;


public abstract class BaseRecyclerFragment<T> extends BaseFragment implements ActionCallback<List<T>> {
    protected static final int STATE_LOADMORE = 1;
    protected static final int STATE_REFRESH = 0;
    private BaseRecyclerAdapter<T> adapter;
    protected int currentPage = 8;

    View emptyView;
    protected boolean enableEmptyView = true;
    private boolean enableResumeRefresh = false;

    ImageView iv_empty;
    OnRefreshListener listener = new OnRefreshListener() {
        public void onRefresh() {
            BaseRecyclerFragment.this.toRefresh();
            BaseRecyclerFragment.this.recyclerView.loadMoreComplete();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (BaseRecyclerFragment.this.swipeRefresh != null) {
                        BaseRecyclerFragment.this.swipeRefresh.setRefreshing(false);
                    }
                    if (BaseRecyclerFragment.this.recyclerView != null) {
                        BaseRecyclerFragment.this.recyclerView.refreshComplete();
                    }
                }
            }, 1000);
        }
    };
    protected int pageItemCount = 2000;

    public HaoRecyclerView recyclerView;
    int resumeCount = 0;
    public int state = 0;

    public SwipeRefreshLayout swipeRefresh;

    TextView tv_empty;

    @Override
public void bindview(View view)
{
    emptyView=(View)view.findViewById(R.id.empty_view);
    emptyView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                toRefresh();
        }
    });
    iv_empty=(ImageView)view.findViewById(R.id.iv_default);
    recyclerView=(HaoRecyclerView)view.findViewById(R.id.recyclerView);
    swipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
    tv_empty=(TextView)view.findViewById(R.id.tv_default);

}

    public abstract boolean canLoadmore();

    public abstract void initAction();

    public abstract BaseRecyclerAdapter<T> setAdapter();



    public int setInflateId() {
        return R.layout.fragment_recycler;
    }

    public void init() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.adapter = setAdapter();
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setCanloadMore(false);
        if (canLoadmore()) {
            this.pageItemCount = 15;
        } else {
            this.pageItemCount = 2000;
        }
        initAction();
        initRefreshLayout();
    }

    public void enableResumeRefresh(boolean enableResumeRefresh) {
        this.enableResumeRefresh = enableResumeRefresh;
    }

    public void onResume() {
        super.onResume();
        if (this.resumeCount == 0) {
            this.swipeRefresh.post(new Runnable() {
                public void run() {
                    if (BaseRecyclerFragment.this.swipeRefresh != null) {
                        BaseRecyclerFragment.this.swipeRefresh.setRefreshing(true);
                    }
                }
            });
            this.listener.onRefresh();
        }
        if (this.resumeCount < 2) {
            this.resumeCount++;
        }
        if (this.enableResumeRefresh && this.resumeCount > 1 && this.currentPage == 1) {
            this.swipeRefresh.post(new Runnable() {
                public void run() {
                    if (BaseRecyclerFragment.this.swipeRefresh != null) {
                        BaseRecyclerFragment.this.swipeRefresh.setRefreshing(true);
                    }
                }
            });
            this.listener.onRefresh();
        }
    }

    public void toRefresh() {
        this.state = 0;
        hideDefaultView();
    }

    public void toLoadMore() {
        this.state = 1;
    }

    public void setEmptyView(int drawableId, String text) {
        setEmptyImg(drawableId);
        setEmptyText(text);
    }

    public void setEmptyText(String text) {
        this.tv_empty.setText(text);
    }

    public void setEmptyImg(int drawableId) {
        this.iv_empty.setImageResource(drawableId);
    }

    public void hideDefaultView() {
        this.emptyView.setVisibility(View.GONE);
    }

    private void initRefreshLayout() {
        this.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        this.swipeRefresh.setProgressViewOffset(true, -20, 100);
        ProgressView progressView = new ProgressView(getContext());
        progressView.setIndicatorId(0);

        progressView.setIndicatorColor(getResources().getColor(R.color.titleColorPrimary));
        this.recyclerView.setFootLoadingView(progressView);
        this.swipeRefresh.setOnRefreshListener(this.listener);
        this.recyclerView.setLoadMoreListener(new LoadMoreListener() {
            public void onLoadMore() {
                BaseRecyclerFragment.this.toLoadMore();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (BaseRecyclerFragment.this.recyclerView != null) {
                            BaseRecyclerFragment.this.recyclerView.loadMoreComplete();
                        }
                    }
                }, 1000);
            }
        });
    }

    public void setPageItemCount(int count) {
        this.pageItemCount = count;
    }

    public void showDefaultView() {
        if (this.enableEmptyView) {
            this.emptyView.setVisibility(View.VISIBLE);
        }
    }
    public void setState(int i){
    this.state=i;
}
    public void onSuccess(List<T> datas) {

        if (datas == null || datas.size()  <=0) {
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
        this.currentPage=this.currentPage+8;
        this.adapter.addItems(datas);

    }

    public void onFailure(String errorCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            showToast(message);
        }
        if (this.adapter.getItemCount() <= 0) {
            showDefaultView();
        }
    }

    public void onBefore() {
    }

    public void onAfter() {
    }

    public void inProgress(float progress) {
    }

    public void onError() {
        if (this.adapter.getItemCount() <= 0) {
            showDefaultView();
        }
    }
}
