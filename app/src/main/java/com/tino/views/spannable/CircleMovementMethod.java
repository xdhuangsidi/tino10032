package com.tino.views.spannable;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.BaseMovementMethod;
import android.text.method.Touch;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.tino.R;
import com.tino.core.TinoApplication;


public class CircleMovementMethod extends BaseMovementMethod {
    public static final int DEFAULT_COLOR = 2131492954;
    public final String TAG;
    private boolean isPassToTv;
    private BackgroundColorSpan mBgSpan;
    private ClickableSpan[] mClickLinks;
    private int mClickableSpanBgClorId;
    private int mTextViewBgColorId;

    public boolean isPassToTv() {
        return this.isPassToTv;
    }

    private void setPassToTv(boolean isPassToTv) {
        this.isPassToTv = isPassToTv;
    }

    public CircleMovementMethod() {
        this.TAG = CircleMovementMethod.class.getSimpleName();
        this.isPassToTv = true;
        this.mTextViewBgColorId = R.color.transparent;
        this.mClickableSpanBgClorId = R.color.transparent;
    }

    public CircleMovementMethod(int clickableSpanBgClorId) {
        this.TAG = CircleMovementMethod.class.getSimpleName();
        this.isPassToTv = true;
        this.mClickableSpanBgClorId = clickableSpanBgClorId;
        this.mTextViewBgColorId = R.color.transparent;
    }

    public CircleMovementMethod(int clickableSpanBgClorId, int textViewBgColorId) {
        this.TAG = CircleMovementMethod.class.getSimpleName();
        this.isPassToTv = true;
        this.mClickableSpanBgClorId = clickableSpanBgClorId;
        this.mTextViewBgColorId = textViewBgColorId;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            int x = (((int) event.getX()) - widget.getTotalPaddingLeft()) + widget.getScrollX();
            int y = (((int) event.getY()) - widget.getTotalPaddingTop()) + widget.getScrollY();
            Layout layout = widget.getLayout();
            int off = layout.getOffsetForHorizontal(layout.getLineForVertical(y), (float) x);
            this.mClickLinks = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan.class);
            if (this.mClickLinks.length > 0) {
                setPassToTv(false);
                Selection.setSelection(buffer, buffer.getSpanStart(this.mClickLinks[0]), buffer.getSpanEnd(this.mClickLinks[0]));
                this.mBgSpan = new BackgroundColorSpan(TinoApplication.getInstance().getResources().getColor(this.mClickableSpanBgClorId));
                buffer.setSpan(this.mBgSpan, buffer.getSpanStart(this.mClickLinks[0]), buffer.getSpanEnd(this.mClickLinks[0]), 33);
            } else {
                setPassToTv(true);
                widget.setBackgroundResource(this.mTextViewBgColorId);
            }
        } else if (action == 1) {
            if (this.mClickLinks.length > 0) {
                this.mClickLinks[0].onClick(widget);
                if (this.mBgSpan != null) {
                    buffer.removeSpan(this.mBgSpan);
                }
            }
            Selection.removeSelection(buffer);
            widget.setBackgroundResource(R.color.transparent);
        } else if (action != 2) {
            if (this.mBgSpan != null) {
                buffer.removeSpan(this.mBgSpan);
            }
            widget.setBackgroundResource(R.color.transparent);
        }
        return Touch.onTouchEvent(widget, buffer, event);
    }
}
