package com.tino.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoGridLayout extends GridLayout {
    public PhotoGridLayout(Context context) {
        super(context);
    }

    public PhotoGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setRowCount(3);
        setColumnCount(3);
    }

    public void setPhoto(List<String> picUrls) {
        if (picUrls != null && picUrls.size() != 0) {
            removeAllViews();
            setColumnCount(6);
            int i;
            ImageView iv;
            LayoutParams lp1;
            switch (picUrls.size()) {
                case 1:
                    setRowCount(1);
                    ImageView imageView = new ImageView(getContext());
                    LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
                    lp.columnSpec = spec(4);
                    imageView.setLayoutParams(lp);
                    addView(imageView);
                    Glide.with(getContext()).load((String) picUrls.get(0)).into(imageView);
                    return;
                case 2:
                    setRowCount(1);
                    for (i = 0; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(3);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 3:
                    setRowCount(1);
                    for (i = 0; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(2);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 4:
                    setRowCount(2);
                    for (i = 0; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(3);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 5:
                    setRowCount(2);
                    for (i = 0; i < 3; i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(2);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    for (i = 3; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(3);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 6:
                    setRowCount(2);
                    for (i = 0; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(2);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 7:
                    setRowCount(3);
                    for (i = 0; i < 3; i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(2);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    for (i = 3; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(3);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 8:
                    setRowCount(3);
                    for (i = 0; i < 6; i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(2);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    for (i = 6; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(3);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                case 9:
                    setRowCount(3);
                    for (i = 0; i < picUrls.size(); i++) {
                        iv = new ImageView(getContext());
                        lp1 = (LayoutParams) iv.getLayoutParams();
                        lp1.columnSpec = spec(2);
                        iv.setLayoutParams(lp1);
                        addView(iv);
                        Glide.with(getContext()).load((String) picUrls.get(i)).into(iv);
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
