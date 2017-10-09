package com.tino.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static String CROP_PIC_PATH = (Environment.getExternalStorageDirectory() + "/bookfarm/tem_pic/croppedImg.png");
    public static String PIC_PATH = (Environment.getExternalStorageDirectory() + "/hsd_app_data/tem_pic/");
    public static String SDPATH = (Environment.getExternalStorageDirectory() + "/hsd_app_data/");

    public static void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        try {
            if (!isFileExist("")) {
                createSDDir("");
            }
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            System.out.print( "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static File getCoverFile() {
        return new File(SDPATH, "book_cover.JPEG");
    }

    public static void saveCover(Bitmap bitmap) {
        saveBitmap(bitmap, "book_cover");
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals("mounted")) {
            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdirs());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    public static void deleteDir() {
        File dir = new File(SDPATH);
        if (dir != null && dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    deleteDir();
                }
            }
            dir.delete();
        }
    }

    public static boolean fileIsExists(String path) {
        try {
            if (new File(path).exists()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
