package com.tino.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class AppNoScrollerListView extends ListView {
    private static final int INVALID_POINTER = -1;
    private boolean hasDispatched;
    private int horizonal;
    private int mActivePointerId;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingHorizonalScrooled;
    private boolean mIsBeingVerticalScrooled;
    private int mTouchSlop;
    public OnHorizonalTouchEventListener onHorizonalTouchEventListener;
    private int vertical;
    private int whichVerticalScrollFirst;

    public interface OnHorizonalTouchEventListener {
        void onHorizonalTouchEvent();
    }

    public AppNoScrollerListView(Context context) {
        this(context, null);
    }

    public AppNoScrollerListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppNoScrollerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.vertical = 0;
        this.horizonal = 1;
        this.whichVerticalScrollFirst = -1;
        this.hasDispatched = false;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(536870911, MeasureSpec.AT_MOST));
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case 0:
                this.mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                this.hasDispatched = false;
                this.mIsBeingVerticalScrooled = false;
                this.mIsBeingHorizonalScrooled = false;
                this.whichVerticalScrollFirst = -1;
                float initialMotionX = getMotionEventX(ev, this.mActivePointerId);
                float initialMotionY = getMotionEventY(ev, this.mActivePointerId);
                if (initialMotionY != -1.0f) {
                    this.mInitialMotionX = initialMotionX;
                    this.mInitialMotionY = initialMotionY;
                    break;
                }
                return false;
            case 1:
            case 3:
                this.whichVerticalScrollFirst = -1;
                this.mIsBeingHorizonalScrooled = false;
                this.mIsBeingVerticalScrooled = false;
                break;
            case 2:
                if (this.mActivePointerId == -1) {
                    return false;
                }
                if (Math.abs(getMotionEventX(ev, this.mActivePointerId) - this.mInitialMotionX) > ((float) this.mTouchSlop) && !this.mIsBeingHorizonalScrooled && this.whichVerticalScrollFirst == -1) {
                    this.whichVerticalScrollFirst = this.horizonal;
                    this.mIsBeingHorizonalScrooled = true;
                }
                float y = getMotionEventY(ev, this.mActivePointerId);
                if (y != -1.0f) {
                    if (y - this.mInitialMotionY > ((float) this.mTouchSlop) && !this.mIsBeingVerticalScrooled && this.whichVerticalScrollFirst == -1) {
                        this.whichVerticalScrollFirst = this.vertical;
                        this.mIsBeingVerticalScrooled = true;
                        break;
                    }
                }
                return false;
        }
        if (!(action != 2 || this.mIsBeingVerticalScrooled || !this.mIsBeingHorizonalScrooled || this.hasDispatched || this.onHorizonalTouchEventListener == null)) {
            this.onHorizonalTouchEventListener.onHorizonalTouchEvent();
            this.hasDispatched = true;
        }
        return super.onTouchEvent(ev);
    }

    private float getMotionEventX(MotionEvent ev, int activePointerId) {
        int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1.0f;
        }
        return MotionEventCompat.getX(ev, index);
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1.0f;
        }
        return MotionEventCompat.getY(ev, index);
    }

    public void setOnHorizonalTouchEventListener(OnHorizonalTouchEventListener onHorizonalTouchEventListener) {
        this.onHorizonalTouchEventListener = onHorizonalTouchEventListener;
    }
}
