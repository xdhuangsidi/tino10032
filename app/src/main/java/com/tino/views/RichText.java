package com.tino.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tino.R;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RichText extends TextView {
    private ImageGetter asyncImageGetter;
    private int d_h;
    private int d_w;
    private Drawable errorImage;
    private OnImageClickListener onImageClickListener;
    private Drawable placeHolder;
    private HashSet<Target> targets;

    public interface OnImageClickListener {
        void imageClicked(List<String> list, int i);
    }

    private static final class URLDrawable extends BitmapDrawable {
        private Drawable drawable;

        public void draw(Canvas canvas) {
            if (this.drawable != null) {
                this.drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    public RichText(Context context) {
        this(context, null);
    }

    public RichText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.d_w = 200;
        this.d_h = 200;
        this.asyncImageGetter = new ImageGetter() {
            public Drawable getDrawable(String source) {
                final URLDrawable urlDrawable = new URLDrawable();
                SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        urlDrawable.setBounds(resource.getBounds());
                        urlDrawable.setDrawable(resource);
                        RichText.this.setText(RichText.this.getText());
                    }
                };
                RichText.this.addTarget(target);
                Glide.with(RichText.this.getContext()).load(source).placeholder(RichText.this.placeHolder).error(RichText.this.errorImage).into((Target) target);
                return urlDrawable;
            }
        };
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.targets = new HashSet();
        final int[] RichText = new int[]{R.attr.placeHolder, R.attr.errorImage, R.attr.default_width, R.attr.default_height};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, RichText);
        this.placeHolder = typedArray.getDrawable(0);
        this.errorImage = typedArray.getDrawable(1);
        this.d_w = typedArray.getDimensionPixelSize(2, this.d_w);
        this.d_h = typedArray.getDimensionPixelSize(3, this.d_h);
        if (this.placeHolder == null) {
            this.placeHolder = new ColorDrawable(7829368);
        }
        this.placeHolder.setBounds(0, 0, this.d_w, this.d_h);
        if (this.errorImage == null) {
            this.errorImage = new ColorDrawable(7829368);
        }
        this.errorImage.setBounds(0, 0, this.d_w, this.d_h);
        typedArray.recycle();
    }

    public void setRichText(String text) {
        SpannableStringBuilder spannableStringBuilder;
        this.targets.clear();
        Spanned spanned = Html.fromHtml(text, this.asyncImageGetter, null);
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }
        ImageSpan[] imageSpans = (ImageSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final List<String> imageUrls = new ArrayList();
        int size = imageSpans.length;
        for (int i = 0; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);
            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                public void onClick(View widget) {
                    if (RichText.this.onImageClickListener != null) {
                        RichText.this.onImageClickListener.imageClicked(imageUrls, finalI);
                    }
                }
            };
            ClickableSpan[] clickableSpans = (ClickableSpan[]) spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
            if (!(clickableSpans == null || clickableSpans.length == 0)) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end, 33);
        }
        super.setText(spanned);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addTarget(Target target) {
        this.targets.add(target);
    }

    public void setPlaceHolder(Drawable placeHolder) {
        this.placeHolder = placeHolder;
        this.placeHolder.setBounds(0, 0, this.d_w, this.d_h);
    }

    public void setErrorImage(Drawable errorImage) {
        this.errorImage = errorImage;
        this.errorImage.setBounds(0, 0, this.d_w, this.d_h);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
}
