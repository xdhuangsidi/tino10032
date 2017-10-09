package com.tino.views.haoRefresh;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class  HaoRecyclerView extends RecyclerView {
    private boolean canloadMore;
    private boolean isLoadingData;
    private ListView list;
    private LoadMoreListener loadMoreListener;
    private LoadingMoreFooter loadingMoreFooter;
    private Adapter mAdapter;
    private Context mContext;
    private AdapterDataObserver mDataObserver;
    private Adapter mFooterAdapter;

    public HaoRecyclerView(Context context) {
        this(context, null);
    }

    public HaoRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HaoRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.canloadMore = true;
        this.isLoadingData = false;
        this.mDataObserver = new AdapterDataObserver() {
            public void onChanged() {
                HaoRecyclerView.this.mFooterAdapter.notifyDataSetChanged();
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                HaoRecyclerView.this.mFooterAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                HaoRecyclerView.this.mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                HaoRecyclerView.this.mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                HaoRecyclerView.this.mFooterAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                HaoRecyclerView.this.mFooterAdapter.notifyItemMoved(fromPosition, toPosition);
            }
        };
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LoadingMoreFooter footView = new LoadingMoreFooter(this.mContext);
        addFootView(footView);
        footView.setGone();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (this.mFooterAdapter != null && (this.mFooterAdapter instanceof FooterAdapter)) {
            ((FooterAdapter) this.mFooterAdapter).setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (this.mFooterAdapter != null && (this.mFooterAdapter instanceof FooterAdapter)) {
            ((FooterAdapter) this.mFooterAdapter).setOnItemLongClickListener(listener);
        }
    }

    public void addFootView(LoadingMoreFooter view) {
        this.loadingMoreFooter = view;
    }

    public void setFootLoadingView(View view) {
        if (this.loadingMoreFooter != null) {
            this.loadingMoreFooter.addFootLoadingView(view);
        }
    }

    public void setFootEndView(View view) {
        if (this.loadingMoreFooter != null) {
            this.loadingMoreFooter.addFootEndView(view);
        }
    }

    public void refreshComplete() {
        if (this.loadingMoreFooter != null) {
            this.loadingMoreFooter.setGone();
        }
        this.isLoadingData = false;
    }

    public void loadMoreComplete() {
        if (this.loadingMoreFooter != null) {
            this.loadingMoreFooter.setGone();
        }
        this.isLoadingData = false;
    }

    public void loadMoreEnd() {
        if (this.loadingMoreFooter != null) {
            this.loadingMoreFooter.setEnd();
        }
    }

    public void setCanloadMore(boolean flag) {
        this.canloadMore = flag;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        this.mFooterAdapter = new FooterAdapter(this, this.loadingMoreFooter, adapter);
      //  super.setAdapter(this.mFooterAdapter);
        super.setAdapter(adapter);
        this.mAdapter.registerAdapterDataObserver(this.mDataObserver);
    }

    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (/*state == 0 &&*/ this.loadMoreListener != null /*&& !this.isLoadingData && this.canloadMore*/) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = last(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                if (this.loadingMoreFooter != null) {
                    this.loadingMoreFooter.setVisible();
                }
                this.isLoadingData = true;
                this.loadMoreListener.onLoadMore();
            }
        }
    }

    private int last(int[] lastPositions) {
        int i = 0;
        int last = lastPositions[0];
        int length = lastPositions.length;
        while (i < length) {
            int value = lastPositions[i];
            if (value > last) {
                last = value;
            }
            i++;
        }
        return last;
    }
}
