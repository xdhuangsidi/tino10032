package com.tino.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class CommonHolder<T> extends ViewHolder {
    OnNotifyChangeListener listener;

    public interface OnNotifyChangeListener {
        void onNotify();
    }

    public abstract void bindData(T t);

    public CommonHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));

    }

    public CommonHolder(View itemView) {
        super(itemView);

    }

    public Context getContext() {
        return this.itemView.getContext();
    }

    public void setOnNotifyChangeListener(OnNotifyChangeListener listener) {
        this.listener = listener;
    }

    public void notifyChange() {
        this.listener.onNotify();
    }
}
