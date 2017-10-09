package com.tino.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;


import com.tino.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends Adapter<ViewHolder> implements CommonHolder.OnNotifyChangeListener {
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_HEAD = 0;
    protected boolean animationsLocked = false;
    public List<T> dataList = new ArrayList();
    private boolean delayEnterAnimation = true;
    private boolean enableHead = false;
    CommonHolder headHolder;
    private int lastAnimatedPosition = -1;

    static class HeadHolder extends CommonHolder<Void> {
        public HeadHolder(View itemView) {
            super(itemView);

        }

        public void bindData(Void aVoid) {
        }
    }

    public abstract CommonHolder<T> setViewHolder(ViewGroup viewGroup);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
       if (enableHead&&position==0) {
          return this.headHolder;
        }
         System.out.println("on createViewHolder in baseAdapter");



        return setViewHolder(parent);
    }
@Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        runEnterAnimation(holder.itemView, position);
       if (!this.enableHead) {
        System.out.println("bind the data in base Adapter");
            ((CommonHolder) holder).bindData(this.dataList.get(position));
        } else if(position!=0) {
           ((CommonHolder) holder).bindData(this.dataList.get(position-1 ));
        }
        ((CommonHolder) holder).setOnNotifyChangeListener(this);
    }
    @Override
    public int getItemCount() {
       if (this.enableHead) {
            return this.dataList.size() + 1;
       }
        return this.dataList.size();
    }
    @Override
    public int getItemViewType(int position) {
       if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void runEnterAnimation(View view, int position) {
        if (!this.animationsLocked && position > this.lastAnimatedPosition) {
            this.lastAnimatedPosition = position;
            view.setTranslationY((float) DensityUtil.dip2px(view.getContext(), 100.0f));
            view.setAlpha(0.0f);
            view.animate().translationY(0.0f).alpha(1.0f).setStartDelay(this.delayEnterAnimation ? (long) (position * 20) : 0).setInterpolator(new DecelerateInterpolator(2.0f)).setDuration(500).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    BaseRecyclerAdapter.this.animationsLocked = true;
                }
            }).start();
        }
    }

    public void onNotify() {
        notifyDataSetChanged();
    }

    public void setDataList(List<T> datas) {
        this.dataList.clear();
        if (datas != null) {
            this.dataList.addAll(datas);
            System.out.println("bind the T");
        }

        this.notifyDataSetChanged();
    }

    public void addItemsAtFront(List<T> datas) {
        if (datas != null) {
            this.dataList.addAll(0, datas);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> datas) {
        if (datas != null) {
            this.dataList.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void addItem(T data) {
        if (data != null) {
            this.dataList.add(data);
            notifyDataSetChanged();
        }
    }

    public void deletItem(T data) {
        this.dataList.remove(data);
        Log.d("deletItem: ", this.dataList.remove(data) + "");
        notifyDataSetChanged();
    }

    public void setEnableHead(boolean ifEnable) {
        this.enableHead = ifEnable;
    }

    public void setHeadHolder(CommonHolder headHolder1) {
        this.enableHead = true;
        this.headHolder = headHolder1;
    }

    public void setHeadHolder(View itemView) {
        this.enableHead = true;
        this.headHolder = new HeadHolder(itemView);
        notifyItemInserted(0);
    }

    public CommonHolder getHeadHolder() {
        return this.headHolder;
    }
}
