package com.tino.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import uk.co.senab.photoview.PhotoView;

public class SmoothImageView extends PhotoView {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_TRANSFORM_IN = 1;
    private static final int STATE_TRANSFORM_OUT = 2;
    private int mBgAlpha = 0;
    private final int mBgColor = ViewCompat.MEASURED_STATE_MASK;
    private Bitmap mBitmap;
    private int mOriginalHeight;
    private int mOriginalLocationX;
    private int mOriginalLocationY;
    private int mOriginalWidth;
    private Paint mPaint;
    private Matrix mSmoothMatrix;
    private int mState = 0;
    private TransformListener mTransformListener;
    private boolean mTransformStart = false;
    private Transfrom mTransfrom;

    private class LocationSizeF implements Cloneable {
        float height;
        float left;
        float top;
        float width;

        private LocationSizeF() {
        }

        public String toString() {
            return "[left:" + this.left + " top:" + this.top + " width:" + this.width + " height:" + this.height + "]";
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public interface TransformListener {
        void onTransformComplete(int i);
    }

    private class Transfrom {
        LocationSizeF endRect;
        float endScale;
        LocationSizeF rect;
        float scale;
        LocationSizeF startRect;
        float startScale;

        private Transfrom() {
        }

        void initStartIn() {
            this.scale = this.startScale;
            try {
                this.rect = (LocationSizeF) this.startRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        void initStartOut() {
            this.scale = this.endScale;
            try {
                this.rect = (LocationSizeF) this.endRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public SmoothImageView(Context context) {
        super(context);
        inits();
    }

    public SmoothImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits();
    }

    public SmoothImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inits();
    }

    private void inits() {
        this.mSmoothMatrix = new Matrix();
        this.mPaint = new Paint();
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setStyle(Style.FILL);
    }

    public void setOriginalInfo(int width, int height, int locationX, int locationY) {
        this.mOriginalWidth = width;
        this.mOriginalHeight = height;
        this.mOriginalLocationX = locationX;
        this.mOriginalLocationY = locationY;
        this.mOriginalLocationY -= getStatusBarHeight(getContext());
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(c.getField("status_bar_height").get(c.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return statusBarHeight;
        }
    }

    public void transformIn() {
        this.mState = 1;
        this.mTransformStart = true;
        invalidate();
    }

    public void transformOut() {
        this.mState = 2;
        this.mTransformStart = true;
        invalidate();
    }

    private void initTransform() {
        if (getDrawable() != null) {
            if (this.mBitmap == null || this.mBitmap.isRecycled()) {
                this.mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            }
            if (this.mTransfrom == null && getWidth() != 0 && getHeight() != 0) {
                float startScale;
                float endScale;
                this.mTransfrom = new Transfrom();
                float xSScale = ((float) this.mOriginalWidth) / ((float) this.mBitmap.getWidth());
                float ySScale = ((float) this.mOriginalHeight) / ((float) this.mBitmap.getHeight());
                if (xSScale > ySScale) {
                    startScale = xSScale;
                } else {
                    startScale = ySScale;
                }
                this.mTransfrom.startScale = startScale;
                float xEScale = ((float) getWidth()) / ((float) this.mBitmap.getWidth());
                float yEScale = ((float) getHeight()) / ((float) this.mBitmap.getHeight());
                if (xEScale < yEScale) {
                    endScale = xEScale;
                } else {
                    endScale = yEScale;
                }
                this.mTransfrom.endScale = endScale;
                this.mTransfrom.startRect = new LocationSizeF();
                this.mTransfrom.startRect.left = (float) this.mOriginalLocationX;
                this.mTransfrom.startRect.top = (float) this.mOriginalLocationY;
                this.mTransfrom.startRect.width = (float) this.mOriginalWidth;
                this.mTransfrom.startRect.height = (float) this.mOriginalHeight;
                this.mTransfrom.endRect = new LocationSizeF();
                float bitmapEndWidth = ((float) this.mBitmap.getWidth()) * this.mTransfrom.endScale;
                float bitmapEndHeight = ((float) this.mBitmap.getHeight()) * this.mTransfrom.endScale;
                this.mTransfrom.endRect.left = (((float) getWidth()) - bitmapEndWidth) / 2.0f;
                this.mTransfrom.endRect.top = (((float) getHeight()) - bitmapEndHeight) / 2.0f;
                this.mTransfrom.endRect.width = bitmapEndWidth;
                this.mTransfrom.endRect.height = bitmapEndHeight;
                this.mTransfrom.rect = new LocationSizeF();
            }
        }
    }

    private void getCenterCropMatrix() {
        if (getDrawable() != null) {
            float scale;
            if (this.mBitmap == null || this.mBitmap.isRecycled()) {
                this.mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            }
            float xScale = ((float) this.mOriginalWidth) / ((float) this.mBitmap.getWidth());
            float yScale = ((float) this.mOriginalHeight) / ((float) this.mBitmap.getHeight());
            if (xScale > yScale) {
                scale = xScale;
            } else {
                scale = yScale;
            }
            this.mSmoothMatrix.reset();
            this.mSmoothMatrix.setScale(scale, scale);
            this.mSmoothMatrix.postTranslate(-(((((float) this.mBitmap.getWidth()) * scale) / 2.0f) - ((float) (this.mOriginalWidth / 2))), -(((((float) this.mBitmap.getHeight()) * scale) / 2.0f) - ((float) (this.mOriginalHeight / 2))));
        }
    }

    private void getBmpMatrix() {
        if (getDrawable() != null && this.mTransfrom != null) {
            if (this.mBitmap == null || this.mBitmap.isRecycled()) {
                this.mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            }
            this.mSmoothMatrix.setScale(this.mTransfrom.scale, this.mTransfrom.scale);
            this.mSmoothMatrix.postTranslate(-(((this.mTransfrom.scale * ((float) this.mBitmap.getWidth())) / 2.0f) - (this.mTransfrom.rect.width / 2.0f)), -(((this.mTransfrom.scale * ((float) this.mBitmap.getHeight())) / 2.0f) - (this.mTransfrom.rect.height / 2.0f)));
        }
    }

    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            if (this.mState == 1 || this.mState == 2) {
                if (this.mTransformStart) {
                    initTransform();
                }
                if (this.mTransfrom == null) {
                    super.onDraw(canvas);
                    return;
                }
                if (this.mTransformStart) {
                    if (this.mState == 1) {
                        this.mTransfrom.initStartIn();
                    } else {
                        this.mTransfrom.initStartOut();
                    }
                }
                if (this.mTransformStart) {
                    Log.d("Dean", "mTransfrom.startScale:" + this.mTransfrom.startScale);
                    Log.d("Dean", "mTransfrom.startScale:" + this.mTransfrom.endScale);
                    Log.d("Dean", "mTransfrom.scale:" + this.mTransfrom.scale);
                    Log.d("Dean", "mTransfrom.startRect:" + this.mTransfrom.startRect.toString());
                    Log.d("Dean", "mTransfrom.endRect:" + this.mTransfrom.endRect.toString());
                    Log.d("Dean", "mTransfrom.rect:" + this.mTransfrom.rect.toString());
                }
                this.mPaint.setAlpha(this.mBgAlpha);
                canvas.drawPaint(this.mPaint);
                int saveCount = canvas.getSaveCount();
                canvas.save();
                getBmpMatrix();
                canvas.translate(this.mTransfrom.rect.left, this.mTransfrom.rect.top);
                canvas.clipRect(0.0f, 0.0f, this.mTransfrom.rect.width, this.mTransfrom.rect.height);
                canvas.concat(this.mSmoothMatrix);
                getDrawable().draw(canvas);
                canvas.restoreToCount(saveCount);
                if (this.mTransformStart) {
                    this.mTransformStart = false;
                    startTransform(this.mState);
                    return;
                }
                return;
            }
            this.mPaint.setAlpha(255);
            canvas.drawPaint(this.mPaint);
            super.onDraw(canvas);
        }
    }

    private void startTransform(final int state) {
        if (this.mTransfrom != null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            PropertyValuesHolder scaleHolder;
            PropertyValuesHolder leftHolder;
            PropertyValuesHolder topHolder;
            PropertyValuesHolder widthHolder;
            PropertyValuesHolder heightHolder;
            PropertyValuesHolder alphaHolder;
            if (state == 1) {
                scaleHolder = PropertyValuesHolder.ofFloat("scale", new float[]{this.mTransfrom.startScale, this.mTransfrom.endScale});
                leftHolder = PropertyValuesHolder.ofFloat("left", new float[]{this.mTransfrom.startRect.left, this.mTransfrom.endRect.left});
                topHolder = PropertyValuesHolder.ofFloat("top", new float[]{this.mTransfrom.startRect.top, this.mTransfrom.endRect.top});
                widthHolder = PropertyValuesHolder.ofFloat("width", new float[]{this.mTransfrom.startRect.width, this.mTransfrom.endRect.width});
                heightHolder = PropertyValuesHolder.ofFloat("height", new float[]{this.mTransfrom.startRect.height, this.mTransfrom.endRect.height});
                alphaHolder = PropertyValuesHolder.ofInt("alpha", new int[]{0, 255});
                valueAnimator.setValues(new PropertyValuesHolder[]{scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder});
            } else {
                scaleHolder = PropertyValuesHolder.ofFloat("scale", new float[]{this.mTransfrom.endScale, this.mTransfrom.startScale});
                leftHolder = PropertyValuesHolder.ofFloat("left", new float[]{this.mTransfrom.endRect.left, this.mTransfrom.startRect.left});
                topHolder = PropertyValuesHolder.ofFloat("top", new float[]{this.mTransfrom.endRect.top, this.mTransfrom.startRect.top});
                widthHolder = PropertyValuesHolder.ofFloat("width", new float[]{this.mTransfrom.endRect.width, this.mTransfrom.startRect.width});
                heightHolder = PropertyValuesHolder.ofFloat("height", new float[]{this.mTransfrom.endRect.height, this.mTransfrom.startRect.height});
                alphaHolder = PropertyValuesHolder.ofInt("alpha", new int[]{255, 0});
                valueAnimator.setValues(new PropertyValuesHolder[]{scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder});
            }
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public synchronized void onAnimationUpdate(ValueAnimator animation) {
                    SmoothImageView.this.mTransfrom.scale = ((Float) animation.getAnimatedValue("scale")).floatValue();
                    SmoothImageView.this.mTransfrom.rect.left = ((Float) animation.getAnimatedValue("left")).floatValue();
                    SmoothImageView.this.mTransfrom.rect.top = ((Float) animation.getAnimatedValue("top")).floatValue();
                    SmoothImageView.this.mTransfrom.rect.width = ((Float) animation.getAnimatedValue("width")).floatValue();
                    SmoothImageView.this.mTransfrom.rect.height = ((Float) animation.getAnimatedValue("height")).floatValue();
                    SmoothImageView.this.mBgAlpha = ((Integer) animation.getAnimatedValue("alpha")).intValue();
                    SmoothImageView.this.invalidate();
                    ((Activity) SmoothImageView.this.getContext()).getWindow().getDecorView().invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (state == 1) {
                        SmoothImageView.this.mState = 0;
                    }
                    if (SmoothImageView.this.mTransformListener != null) {
                        SmoothImageView.this.mTransformListener.onTransformComplete(state);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }
            });
            valueAnimator.start();
        }
    }

    public void setOnTransformListener(TransformListener listener) {
        this.mTransformListener = listener;
    }
}
