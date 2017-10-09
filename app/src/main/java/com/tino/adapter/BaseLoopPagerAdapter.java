package com.tino.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseLoopPagerAdapter extends PagerAdapter implements OnPageChangeListener, OnTouchListener, Runnable {
    private static final int DEFAULT_DELAY_MILLIS = 5000;
    private int mChildCount;
    private int mDelayMillis = 5000;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final List mList = new ArrayList();
    private boolean mRunning;
    private final ViewPager mViewPager;
    private final List<View> mViews = new LinkedList();

    public abstract Object getItem(int i);

    public abstract int getPagerCount();

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public abstract void onPageItemSelected(int i);

    public BaseLoopPagerAdapter(ViewPager viewPager) {
        this.mViewPager = viewPager;
        this.mViewPager.setOnTouchListener(this);
    }

    public void notifyDataSetChanged() {
        int fixedCount = getPagerCount();
        if (fixedCount > 0) {
            if (fixedCount == 1) {
                if (fixedCount != this.mList.size()) {
                    this.mList.clear();
                    this.mList.add(getItem(0));
                }
                if (fixedCount != this.mViews.size()) {
                    this.mViews.clear();
                    this.mViews.add(null);
                }
            } else if (fixedCount > 1) {
                int i;
                if (fixedCount + 2 != this.mList.size()) {
                    this.mList.clear();
                    this.mList.add(getItem(fixedCount - 1));
                    for (i = 0; i < fixedCount; i++) {
                        this.mList.add(getItem(i));
                    }
                    this.mList.add(getItem(0));
                }
                if (fixedCount + 2 != this.mViews.size()) {
                    this.mViews.clear();
                    for (i = 0; i < this.mList.size(); i++) {
                        this.mViews.add(null);
                    }
                }
            }
            super.notifyDataSetChanged();
            this.mChildCount = getCount();
            if (this.mViewPager.getCurrentItem() == 0 && this.mChildCount != 1) {
                this.mViewPager.setCurrentItem(1, false);
            }
            stop();
            start();
        }
    }

    public void setDelayMillis(int delayMillis) {
        this.mDelayMillis = delayMillis;
        if (delayMillis <= 0) {
            this.mDelayMillis = 5000;
        }
    }

    public void start() {
        if (!this.mRunning) {
            post();
            this.mRunning = true;
        }
    }

    public void stop() {
        if (this.mRunning) {
            this.mHandler.removeCallbacks(this);
            this.mRunning = false;
        }
    }

    public final boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == 0) {
            stop();
        } else if (event.getAction() == 1) {
            start();
        }
        return false;
    }

    public final void run() {
        int currentPosition = this.mViewPager.getCurrentItem();
        if (currentPosition > 0 && currentPosition < this.mList.size() - 1) {
            if (currentPosition + 1 == this.mList.size() - 1) {
                currentPosition = 1;
            } else {
                currentPosition++;
            }
            this.mViewPager.setCurrentItem(currentPosition, true);
            post();
        }
    }

    private void post() {
        this.mHandler.postDelayed(this, (long) this.mDelayMillis);
    }

    public final Object instantiateItem(ViewGroup container, int position) {
        int fixedPosition = 0;
        if (position == 0) {
            fixedPosition = getPagerCount() - 1;
        } else if (position == this.mList.size() - 1) {
            fixedPosition = 0;
        } else if (position > 0 && position < this.mList.size() - 1) {
            fixedPosition = position - 1;
        }
        if (this.mViews.get(position) == null) {
            this.mViews.set(position, getView(fixedPosition, (View) this.mViews.get(position), container));
        }
        container.addView((View) this.mViews.get(position));
        return this.mViews.get(position);
    }

    public final int getItemPosition(Object object) {
        if (this.mChildCount <= 0) {
            return super.getItemPosition(object);
        }
        this.mChildCount--;
        return -2;
    }

    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public final int getCount() {
        return this.mList.size();
    }

    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public final void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public final void onPageSelected(int position) {
        if (position > 0 && position < this.mList.size() - 1) {
            onPageItemSelected(position - 1);
        }
    }

    public final void onPageScrollStateChanged(int state) {
        if (state == 0 && this.mList.size() > 3) {
            if (this.mViewPager.getCurrentItem() == 0) {
                this.mViewPager.setCurrentItem(this.mList.size() - 2, false);
            } else if (this.mViewPager.getCurrentItem() == this.mList.size() - 1) {
                this.mViewPager.setCurrentItem(1, false);
            }
        }
    }
}
