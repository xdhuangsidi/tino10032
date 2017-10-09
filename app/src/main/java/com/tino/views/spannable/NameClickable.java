package com.tino.views.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

public class NameClickable extends ClickableSpan implements OnClickListener {
    private final ISpanClick mListener;
    private int mPosition;

    public NameClickable(ISpanClick l, int position) {
        this.mListener = l;
        this.mPosition = position;
    }

    public void onClick(View widget) {
        this.mListener.onClick(this.mPosition);
    }

    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
      //  ds.setColor(BookApplication.getInstance().getResources().getColor(R.color.colorPrimary));
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
