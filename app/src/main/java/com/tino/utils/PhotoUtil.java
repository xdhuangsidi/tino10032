package com.tino.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import com.alibaba.fastjson.asm.Opcodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoUtil {
    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        int heightRatio = Math.round(((float) height) / ((float) reqHeight));
        int widthRatio = Math.round(((float) width) / ((float) reqWidth));
        if (heightRatio < widthRatio) {
            return heightRatio;
        }
        return widthRatio;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 720, 1280);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 40, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }
    public static byte[] bitmapTobyte(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 40, baos);
        return baos.toByteArray();
    }

    public static void compressImage(String filePath, String fileName) throws FileNotFoundException {
        Bitmap bm = getSmallBitmap(filePath);
        if (bm != null) {
            int degree = readPictureDegree(filePath);
            if (degree != 0) {
                bm = rotateBitmap(bm, degree);
            }
            File path = new File(FileUtils.PIC_PATH);
            if (!path.exists()) {
                path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(new File(FileUtils.PIC_PATH, fileName));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(CompressFormat.JPEG, 100, baos);
            int options = 100;
            while (baos.toByteArray().length / 1024 > 100) {
                baos.reset();
                bm.compress(CompressFormat.JPEG, options, baos);
                options -= 10;
            }
            try {
                out.write(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!bm.isRecycled()) {
                bm.recycle();
            }
            System.gc();
        }
    }

    public static int readPictureDegree(String path) {
        try {
            switch (new ExifInterface(path).getAttributeInt("Orientation", 1)) {
                case 3:
                    return Opcodes.GETFIELD;
                case 6:
                    return 90;
                case 8:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap == null) {
            return bitmap;
        }
        Matrix m = new Matrix();
        m.postRotate((float) degress);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }
}
