package com.tino.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class EText extends AppCompatEditText {
    DrawableRightListener drawableRightListener;

    public interface DrawableRightListener {
        void onDrawableRightClick(View view);
    }

    public EText(Context context) {
        super(context);
    }

    public EText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawableRightListener(DrawableRightListener drawableRightListener) {
        this.drawableRightListener = drawableRightListener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 1:
                if (this.drawableRightListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[2];
                    if (drawableRight != null && event.getRawX() >= ((float) (getRight() - drawableRight.getBounds().width()))) {
                        this.drawableRightListener.onDrawableRightClick(this);
                        return false;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
