package com.tino.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.text.TextUtils;

import com.tino.core.TinoApplication;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class CommonUtils {
    public static void savetoken(String token,Context ct){
        SharedPreferences.Editor editor = ct.getSharedPreferences("login_data", 0).edit();
        editor.putString("token",token);
        editor.commit();
    }
    public static void saveUid(String uid,Context ct){
        SharedPreferences.Editor editor = ct.getSharedPreferences("login_data", 0).edit();
        editor.putString("uid",uid);
        editor.commit();
    }
    public static void saveAvatar(String avatar,Context ct){
        SharedPreferences.Editor editor = ct.getSharedPreferences("login_data", 0).edit();
        editor.putString("avatar",avatar);
        editor.commit();
    }
    public static String getAvatar(Context ct){
        return ct.getSharedPreferences("login_data", 0).getString("avatar", "");
    }
    public static String getUid(Context ct){
        return ct.getSharedPreferences("login_data", 0).getString("uid", "");
    }
    public static String getUid(){
        return TinoApplication.getInstance().getSharedPreferences("login_data", 0).getString("uid", "");
    }
    public static String gettoken(Context ct){
        return ct.getSharedPreferences("login_data", 0).getString("token", "");
    }
    public static String gettoken(){
        return TinoApplication.getInstance().getSharedPreferences("login_data", 0).getString("token", "");
    }



    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }



    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        }
        return false;
    }

    public static String getFormatDate(long timemillis) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(timemillis));
    }

    public static String decodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        int i = 0;
        while (i < chars.length) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = '\u0000';
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[(i + 2) + j]);
                    if (('0' > ch || ch > '9') && ('a' > ch || ch > 'f')) {
                        cc = '\u0000';
                        break;
                    }
                    cc = (char) ((Character.digit(ch, 16) << ((3 - j) * 4)) | cc);
                }
                if (cc > '\u0000') {
                    i += 5;
                    sb.append(cc);
                    i++;
                }
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public static String encodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 'Ā') {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 15, 16));
                sb.append(Character.forDigit((c >>> 8) & 15, 16));
                sb.append(Character.forDigit((c >>> 4) & 15, 16));
                sb.append(Character.forDigit(c & 15, 16));
            }
        }
        return sb.toString();
    }

/*    public static String convertTime(int time) {
        int second = (time / CloseFrame.NORMAL) % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(((time / CloseFrame.NORMAL) / 60) % 60), Integer.valueOf(second)});
    }*/

    public static boolean isUrlUsable(String url) {
        URL url2;
        Throwable th;
        if (isEmpty(url)) {
            return false;
        }
        HttpURLConnection connt = null;
        try {
            URL urlTemp = new URL(url);
            try {
                connt = (HttpURLConnection) urlTemp.openConnection();
                connt.setRequestMethod("HEAD");
                if (connt.getResponseCode() == 200) {
                    connt.disconnect();
                    return true;
                }
                connt.disconnect();
                return false;
            } catch (Exception e) {
                url2 = urlTemp;
                connt.disconnect();
                return false;
            } catch (Throwable th2) {
                th = th2;
                url2 = urlTemp;
                connt.disconnect();
                throw th;
            }
        } catch (Exception e2) {
            connt.disconnect();
            return false;
        } catch (Throwable th3) {
            th = th3;
            connt.disconnect();
            return false;
        }
    }

    public static boolean isUrl(String url) {
        return Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$").matcher(url).matches();
    }

    public static int getToolbarHeight(Context context) {
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{16843499});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0.0f);
        styledAttributes.recycle();
        return toolbarHeight;
    }
}
