package com.tino.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tino.R;
import com.tino.base.BaseActivity;
import com.tino.views.HackyViewPager;
import com.tino.views.SmoothImageView;

import java.util.ArrayList;


import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;



public class PhotoViewActivity extends BaseActivity {
    private int clickPosition = 0;
    private int mHeight;
    private int mScreenX;
    private int mScreenY;
    private int mWidth;

    private ArrayList<String> photoUrls = new ArrayList();

    HackyViewPager viewPager;
    @Override
    public void bindView()
    {
      viewPager=(HackyViewPager)findViewById(R.id.view_pager);
    }
    private class PhotoPagerAdapter extends PagerAdapter {
        private ArrayList<String> urls = new ArrayList();

        public PhotoPagerAdapter(ArrayList<String> list) {
            this.urls = list;
        }

        public int getCount() {
            return this.urls.size();
        }

        public Object instantiateItem(ViewGroup container, int position) {
            final SmoothImageView photoView = new SmoothImageView(container.getContext());
            photoView.setOriginalInfo(PhotoViewActivity.this.mWidth, PhotoViewActivity.this.mHeight, PhotoViewActivity.this.mScreenX, PhotoViewActivity.this.mScreenY);
            photoView.transformIn();
            String imgUrl = (String) this.urls.get(position);

            Glide.with(PhotoViewActivity.this.mContext).load(imgUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
            container.addView(photoView, -1, -1);
            photoView.setOnTransformListener(new SmoothImageView.TransformListener() {
                public void onTransformComplete(int mode) {
                    if (mode == 2) {
                        PhotoViewActivity.this.finish();
                        PhotoViewActivity.this.overridePendingTransition(0, 0);
                    }
                }
            });
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                public void onPhotoTap(View view, float v, float v2) {
                    photoView.transformOut();
                }
            });
            return photoView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public static void showWithParams(Context context, ArrayList<String> photoUrls, int clickPosition, int width, int height, int screenX, int screenY) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra("photos", photoUrls);
        intent.putExtra("position", clickPosition);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("screenX", screenX);
        intent.putExtra("screenY", screenY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    public int setInflateId() {
        return R.layout.activity_photo_view;
    }

    public void init() {
        Intent intent = getIntent();
        this.photoUrls = intent.getStringArrayListExtra("photos");
        if (this.photoUrls.size() == 0) {
            finish();
        }
        this.clickPosition = intent.getIntExtra("position", 0);
        if (this.clickPosition >= this.photoUrls.size()) {
            this.clickPosition = this.photoUrls.size();
        }
        this.mWidth = intent.getIntExtra("width", 0);
        this.mHeight = intent.getIntExtra("height", 0);
        this.mScreenX = intent.getIntExtra("screenX", 0);
        this.mScreenY = intent.getIntExtra("screenY", 0);
        this.viewPager.setAdapter(new PhotoPagerAdapter(this.photoUrls));
        this.viewPager.setCurrentItem(this.clickPosition);
    }

    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, 0);
    }
}
