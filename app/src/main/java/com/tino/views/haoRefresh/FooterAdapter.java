package com.tino.views.haoRefresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class FooterAdapter extends Adapter<ViewHolder> implements OnClickListener, OnLongClickListener {
    private static final int DEFAULT = 0;
    private static final int FOOTER = -1;
    private Adapter adapter;
    private HaoRecyclerView haoRecyclerView;
    private LoadingMoreFooter loadingMoreFooter;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    private class SimpleViewHolder extends ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public FooterAdapter(HaoRecyclerView haoRecyclerView, LoadingMoreFooter loadingMoreFooter, Adapter adapter) {
        this.haoRecyclerView = haoRecyclerView;
        this.adapter = adapter;
        this.loadingMoreFooter = loadingMoreFooter;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int position) {
                    return (FooterAdapter.this.getItemViewType(position) == -1 || FooterAdapter.this.getItemViewType(position) == -2) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && (lp instanceof StaggeredGridLayoutManager.LayoutParams) && isFooter(holder.getLayoutPosition())) {
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
        }
    }

    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - 1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            return new SimpleViewHolder(this.loadingMoreFooter);
        }
        return this.adapter.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
        if (this.adapter != null && position < this.adapter.getItemCount()) {
//            this.adapter.onBindViewHolder(holder, position);
        }
    }

    public int getItemCount() {
        if (this.adapter != null) {
            return this.adapter.getItemCount() + 1;
        }
        return 1;
    }

    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return -1;
        }
        if (this.adapter == null || position >= this.adapter.getItemCount()) {
            return 0;
        }
        return this.adapter.getItemViewType(position);
    }

    public long getItemId(int position) {
        if (this.adapter == null || position < 0 || position >= this.adapter.getItemCount()) {
            return -1;
        }
        return this.adapter.getItemId(position);
    }

    public void onClick(View view) {
        if (this.onItemClickListener != null) {
            this.onItemClickListener.onItemClick(null, view, this.haoRecyclerView.getChildAdapterPosition(view), 0);
        }
    }

    public boolean onLongClick(View view) {
        if (this.onItemLongClickListener != null) {
            this.onItemLongClickListener.onItemLongClick(null, view, this.haoRecyclerView.getChildAdapterPosition(view), 0);
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
