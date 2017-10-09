package com.tino.views;

import android.content.Context;
import android.support.v7.widget.ListViewCompat;
import android.util.AttributeSet;

public class NoScrollListView extends ListViewCompat {
    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(536870911, MeasureSpec.AT_MOST));
    }
}
