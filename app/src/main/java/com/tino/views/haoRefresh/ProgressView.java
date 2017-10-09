package com.tino.views.haoRefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.tino.R;
import com.wang.avi.indicators.BallBeatIndicator;
import com.wang.avi.indicators.BallClipRotateIndicator;
import com.wang.avi.indicators.BallClipRotateMultipleIndicator;
import com.wang.avi.indicators.BallClipRotatePulseIndicator;
import com.wang.avi.indicators.BallGridBeatIndicator;
import com.wang.avi.indicators.BallGridPulseIndicator;
import com.wang.avi.indicators.BallPulseIndicator;
import com.wang.avi.indicators.BallPulseRiseIndicator;
import com.wang.avi.indicators.BallPulseSyncIndicator;
import com.wang.avi.indicators.BallRotateIndicator;
import com.wang.avi.indicators.BallScaleIndicator;
import com.wang.avi.indicators.BallScaleMultipleIndicator;
import com.wang.avi.indicators.BallScaleRippleIndicator;
import com.wang.avi.indicators.BallScaleRippleMultipleIndicator;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.wang.avi.indicators.BallTrianglePathIndicator;
import com.wang.avi.indicators.BallZigZagDeflectIndicator;
import com.wang.avi.indicators.BallZigZagIndicator;
import com.wang.avi.indicators.BaseIndicatorController;
import com.wang.avi.indicators.BaseIndicatorController.AnimStatus;
import com.wang.avi.indicators.CubeTransitionIndicator;
import com.wang.avi.indicators.LineScaleIndicator;
import com.wang.avi.indicators.LineScalePartyIndicator;
import com.wang.avi.indicators.LineScalePulseOutIndicator;
import com.wang.avi.indicators.LineScalePulseOutRapidIndicator;
import com.wang.avi.indicators.LineSpinFadeLoaderIndicator;
import com.wang.avi.indicators.PacmanIndicator;
import com.wang.avi.indicators.SemiCircleSpinIndicator;
import com.wang.avi.indicators.SquareSpinIndicator;
import com.wang.avi.indicators.TriangleSkewSpinIndicator;

public class ProgressView extends View {
    public static final int BallBeat = 17;
    public static final int BallClipRotate = 2;
    public static final int BallClipRotateMultiple = 5;
    public static final int BallClipRotatePulse = 3;
    public static final int BallGridBeat = 26;
    public static final int BallGridPulse = 1;
    public static final int BallPulse = 0;
    public static final int BallPulseRise = 6;
    public static final int BallPulseSync = 16;
    public static final int BallRotate = 7;
    public static final int BallScale = 12;
    public static final int BallScaleMultiple = 15;
    public static final int BallScaleRipple = 20;
    public static final int BallScaleRippleMultiple = 21;
    public static final int BallSpinFadeLoader = 22;
    public static final int BallTrianglePath = 11;
    public static final int BallZigZag = 9;
    public static final int BallZigZagDeflect = 10;
    public static final int CubeTransition = 8;
    public static final int DEFAULT_SIZE = 30;
    public static final int LineScale = 13;
    public static final int LineScaleParty = 14;
    public static final int LineScalePulseOut = 18;
    public static final int LineScalePulseOutRapid = 19;
    public static final int LineSpinFadeLoader = 23;
    public static final int Pacman = 25;
    public static final int SemiCircleSpin = 27;
    public static final int SquareSpin = 4;
    public static final int TriangleSkewSpin = 24;
    private boolean mHasAnimation;
    int mIndicatorColor;
    BaseIndicatorController mIndicatorController;
    int mIndicatorId;
    Paint mPaint;

    public @interface Indicator {
    }

    public ProgressView(Context context) {
        super(context);
        init(null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorView);
        this.mIndicatorId = a.getInt(0, 0);
        this.mIndicatorColor = a.getColor(0, -1);
        a.recycle();
        this.mPaint = new Paint();
        this.mPaint.setColor(this.mIndicatorColor);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
        applyIndicator();
    }

    public void setIndicatorId(int indicatorId) {
        this.mIndicatorId = indicatorId;
        applyIndicator();
    }

    public void setIndicatorColor(int color) {
        this.mIndicatorColor = color;
        this.mPaint.setColor(this.mIndicatorColor);
        invalidate();
    }

    private void applyIndicator() {
        switch (this.mIndicatorId) {
            case 0:
                this.mIndicatorController = new BallPulseIndicator();
                break;
            case 1:
                this.mIndicatorController = new BallGridPulseIndicator();
                break;
            case 2:
                this.mIndicatorController = new BallClipRotateIndicator();
                break;
            case 3:
                this.mIndicatorController = new BallClipRotatePulseIndicator();
                break;
            case 4:
                this.mIndicatorController = new SquareSpinIndicator();
                break;
            case 5:
                this.mIndicatorController = new BallClipRotateMultipleIndicator();
                break;
            case 6:
                this.mIndicatorController = new BallPulseRiseIndicator();
                break;
            case 7:
                this.mIndicatorController = new BallRotateIndicator();
                break;
            case 8:
                this.mIndicatorController = new CubeTransitionIndicator();
                break;
            case 9:
                this.mIndicatorController = new BallZigZagIndicator();
                break;
            case 10:
                this.mIndicatorController = new BallZigZagDeflectIndicator();
                break;
            case 11:
                this.mIndicatorController = new BallTrianglePathIndicator();
                break;
            case 12:
                this.mIndicatorController = new BallScaleIndicator();
                break;
            case 13:
                this.mIndicatorController = new LineScaleIndicator();
                break;
            case 14:
                this.mIndicatorController = new LineScalePartyIndicator();
                break;
            case 15:
                this.mIndicatorController = new BallScaleMultipleIndicator();
                break;
            case 16:
                this.mIndicatorController = new BallPulseSyncIndicator();
                break;
            case 17:
                this.mIndicatorController = new BallBeatIndicator();
                break;
            case 18:
                this.mIndicatorController = new LineScalePulseOutIndicator();
                break;
            case 19:
                this.mIndicatorController = new LineScalePulseOutRapidIndicator();
                break;
            case 20:
                this.mIndicatorController = new BallScaleRippleIndicator();
                break;
            case 21:
                this.mIndicatorController = new BallScaleRippleMultipleIndicator();
                break;
            case 22:
                this.mIndicatorController = new BallSpinFadeLoaderIndicator();
                break;
            case 23:
                this.mIndicatorController = new LineSpinFadeLoaderIndicator();
                break;
            case 24:
                this.mIndicatorController = new TriangleSkewSpinIndicator();
                break;
            case 25:
                this.mIndicatorController = new PacmanIndicator();
                break;
            case 26:
                this.mIndicatorController = new BallGridBeatIndicator();
                break;
            case 27:
                this.mIndicatorController = new SemiCircleSpinIndicator();
                break;
        }
        this.mIndicatorController.setTarget(this);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(dp2px(30), widthMeasureSpec), measureDimension(dp2px(30), heightMeasureSpec));
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            return specSize;
        }
        if (specMode == Integer.MIN_VALUE) {
            return Math.min(defaultSize, specSize);
        }
        return defaultSize;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!this.mHasAnimation) {
            this.mHasAnimation = true;
            applyAnimation();
        }
    }

    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == View.GONE || v == INVISIBLE) {
                this.mIndicatorController.setAnimationStatus(AnimStatus.END);
            } else {
                this.mIndicatorController.setAnimationStatus(AnimStatus.START);
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIndicatorController.setAnimationStatus(AnimStatus.CANCEL);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIndicatorController.setAnimationStatus(AnimStatus.START);
    }

    void drawIndicator(Canvas canvas) {
        this.mIndicatorController.draw(canvas, this.mPaint);
    }

    void applyAnimation() {
        this.mIndicatorController.initAnimation();
    }

    private int dp2px(int dpValue) {
        return ((int) getContext().getResources().getDisplayMetrics().density) * dpValue;
    }
}
