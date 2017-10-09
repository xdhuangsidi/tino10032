package com.tino.core.net;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.reflect.Field;

public class ImageUtils {

    public static class ImageSize {
        int height;
        int width;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public String toString() {
            return "ImageSize{width=" + this.width + ", height=" + this.height + '}';
        }
    }

    public static ImageSize getImageSize(InputStream imageStream) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
        int width = srcSize.width;
        int height = srcSize.height;
        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;
        if (width <= reqWidth || height <= reqHeight) {
            return 1;
        }
        return Math.max(Math.round(((float) width) / ((float) reqWidth)), Math.round(((float) height) / ((float) reqHeight)));
    }

    public static ImageSize getImageViewSize(View view) {
        ImageSize imageSize = new ImageSize(2,2);
        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);
        return imageSize;
    }

    private static int getExpectHeight(View view) {
        int height = 0;
        if (view == null) {
            return 0;
        }
        LayoutParams params = view.getLayoutParams();
        if (!(params == null || params.height == -2)) {
            height = view.getWidth();
        }
        if (height <= 0 && params != null) {
            height = params.height;
        }
        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");
        }
        if (height <= 0) {
            height = view.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        return height;
    }

    private static int getExpectWidth(View view) {
        int width = 0;
        if (view == null) {
            return 0;
        }
        LayoutParams params = view.getLayoutParams();
        if (!(params == null || params.width == -2)) {
            width = view.getWidth();
        }
        if (width <= 0 && params != null) {
            width = params.width;
        }
        if (width <= 0) {
            width = getImageViewFieldValue(view, "mMaxWidth");
        }
        if (width <= 0) {
            width = view.getContext().getResources().getDisplayMetrics().widthPixels;
        }
        return width;
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue <= 0 ) {
                return 0;
            }
            return fieldValue;
        } catch (Exception e) {
            return 0;
        }
    }
}
