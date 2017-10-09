package com.tino.views.haoRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tino.R;


public class LoadingMoreFooter extends LinearLayout {
    private Context context;
    private LinearLayout end_layout;
    private LinearLayout loading_view_layout;

    public LoadingMoreFooter(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        setGravity(17);
        setLayoutParams(new LayoutParams(-1, -2));
        View view = LayoutInflater.from(context).inflate(R.layout.view_footer_layout, null);
        this.loading_view_layout = (LinearLayout) view.findViewById(R.id.loading_view_layout);
        this.end_layout = (LinearLayout) view.findViewById(R.id.end_layout);
        addFootLoadingView(new ProgressBar(context, null, 16842871));
        TextView textView = new TextView(context);
        textView.setText("已经到底啦~");
        addFootEndView(textView);
        addView(view);
    }

    public void addFootLoadingView(View view) {
        this.loading_view_layout.removeAllViews();
        this.loading_view_layout.addView(view);
    }

    public void addFootEndView(View view) {
        this.end_layout.removeAllViews();
        this.end_layout.addView(view);
    }

    public void setEnd() {
        setVisibility(View.VISIBLE);
        this.loading_view_layout.setVisibility(View.GONE);
        this.end_layout.setVisibility(View.VISIBLE);
    }

    public void setVisible() {
        setVisibility(View.VISIBLE);
        this.loading_view_layout.setVisibility(View.VISIBLE);
        this.end_layout.setVisibility(View.GONE);
    }

    public void setGone() {
        setVisibility(View.GONE);
    }
}
