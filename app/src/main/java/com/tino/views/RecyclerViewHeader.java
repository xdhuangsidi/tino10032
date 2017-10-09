package com.tino.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RecyclerViewHeader extends RelativeLayout {
    private boolean mAlreadyAligned;
    private int mCurrentScroll;
    private int mDownScroll;
    private RecyclerView mRecycler;
    private boolean mRecyclerWantsTouchEvent;
    private boolean mReversed;

    private class HeaderItemDecoration extends ItemDecoration {
        private int mHeaderHeight;
        private int mNumberOfChildren;

        public HeaderItemDecoration(LayoutManager layoutManager, int height) {
            if (layoutManager.getClass() == LinearLayoutManager.class) {
                this.mNumberOfChildren = 1;
            } else if (layoutManager.getClass() == GridLayoutManager.class) {
                this.mNumberOfChildren = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                this.mNumberOfChildren = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            this.mHeaderHeight = height;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int value = parent.getChildLayoutPosition(view) < this.mNumberOfChildren ? this.mHeaderHeight : 0;
            if (RecyclerViewHeader.this.mReversed) {
                outRect.bottom = value;
            } else {
                outRect.top = value;
            }
        }
    }

    public static RecyclerViewHeader fromXml(Context context, @LayoutRes int layoutRes) {
        RecyclerViewHeader header = new RecyclerViewHeader(context);
        View.inflate(context, layoutRes, header);
        return header;
    }

    public RecyclerViewHeader(Context context) {
        super(context);
    }

    public RecyclerViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void attachTo(RecyclerView recycler) {
        attachTo(recycler, false);
    }

    public void attachTo(RecyclerView recycler, boolean headerAlreadyAligned) {
        validateRecycler(recycler, headerAlreadyAligned);
        this.mRecycler = recycler;
        this.mAlreadyAligned = headerAlreadyAligned;
        this.mReversed = isLayoutManagerReversed(recycler);
        setupAlignment(recycler);
        setupHeader(recycler);
    }

    private boolean isLayoutManagerReversed(RecyclerView recycler) {
        LayoutManager manager = recycler.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).getReverseLayout();
        }
        if (manager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) manager).getReverseLayout();
        }
        return false;
    }

    private void setupAlignment(RecyclerView recycler) {
        if (!this.mAlreadyAligned) {
            LayoutParams newHeaderParams;
            int gravity = (this.mReversed ? 80 : 48) | 1;
            if (getLayoutParams() != null) {
                newHeaderParams = new LayoutParams(getLayoutParams());
                newHeaderParams.width = -2;
                newHeaderParams.height = -2;
                //newHeaderParams. = gravity;
            } else {
                newHeaderParams = new LayoutParams(-2, -2);
            }
            setLayoutParams(newHeaderParams);
            FrameLayout newRootParent = new FrameLayout(recycler.getContext());
            newRootParent.setLayoutParams(recycler.getLayoutParams());
            ViewParent currentParent = recycler.getParent();
            if (currentParent instanceof ViewGroup) {
                int indexWithinParent = ((ViewGroup) currentParent).indexOfChild(recycler);
                ((ViewGroup) currentParent).removeViewAt(indexWithinParent);
                recycler.setLayoutParams(new LayoutParams(-1, -1));
                newRootParent.addView(recycler);
                newRootParent.addView(this);
                ((ViewGroup) currentParent).addView(newRootParent, indexWithinParent);
            }
        }
    }

    @SuppressLint({"NewApi"})
    private void setupHeader(final RecyclerView recycler) {
        recycler.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewHeader.this.mCurrentScroll = RecyclerViewHeader.this.mCurrentScroll + dy;
                RecyclerViewHeader.this.setTranslationY((float) (-RecyclerViewHeader.this.mCurrentScroll));
            }
        });
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int height = RecyclerViewHeader.this.getHeight();
                if (height > 0) {
                    if (VERSION.SDK_INT >= 16) {
                        RecyclerViewHeader.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        RecyclerViewHeader.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    if (RecyclerViewHeader.this.mAlreadyAligned) {
                        MarginLayoutParams params = (MarginLayoutParams) RecyclerViewHeader.this.getLayoutParams();
                        height = (height + params.topMargin) + params.bottomMargin;
                    }
                    recycler.addItemDecoration(new HeaderItemDecoration(recycler.getLayoutManager(), height), 0);
                }
            }
        });
    }

    private void validateRecycler(RecyclerView recycler, boolean headerAlreadyAligned) {
        LayoutManager layoutManager = recycler.getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("Be sure to call RecyclerViewHeader constructor after setting your RecyclerView's LayoutManager.");
        } else if (layoutManager.getClass() == LinearLayoutManager.class || layoutManager.getClass() == GridLayoutManager.class || (layoutManager instanceof StaggeredGridLayoutManager)) {
            if (layoutManager instanceof LinearLayoutManager) {
                if (((LinearLayoutManager) layoutManager).getOrientation() != 1) {
                    throw new IllegalArgumentException("Currently RecyclerViewHeader supports only VERTICAL orientation LayoutManagers.");
                }
            } else if ((layoutManager instanceof StaggeredGridLayoutManager) && ((StaggeredGridLayoutManager) layoutManager).getOrientation() != 1) {
                throw new IllegalArgumentException("Currently RecyclerViewHeader supports only VERTICAL orientation StaggeredGridLayoutManagers.");
            }
            if (!headerAlreadyAligned) {
                ViewParent parent = recycler.getParent();
                if (parent != null && !(parent instanceof LinearLayout) && !(parent instanceof FrameLayout) && !(parent instanceof RelativeLayout)) {
                    throw new IllegalStateException("Currently, NOT already aligned RecyclerViewHeader can only be used for RecyclerView with a parent of one of types: LinearLayout, FrameLayout, RelativeLayout.");
                }
            }
        } else {
            throw new IllegalArgumentException("Currently RecyclerViewHeader supports only LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager.");
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.mRecyclerWantsTouchEvent = this.mRecycler.onInterceptTouchEvent(ev);
        if (this.mRecyclerWantsTouchEvent && ev.getAction() == 0) {
            this.mDownScroll = this.mCurrentScroll;
        }
        return this.mRecyclerWantsTouchEvent || super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!this.mRecyclerWantsTouchEvent) {
            return super.onTouchEvent(event);
        }
        this.mRecycler.onTouchEvent(MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX(), event.getY() - ((float) (this.mCurrentScroll - this.mDownScroll)), event.getMetaState()));
        return false;
    }
}
