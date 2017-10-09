package com.tino.utils;

import android.util.Log;

public class LogUtils {
    public static final String LOGTAG = "leanchat";
    public static boolean debugEnabled;

    private static String getDebugInfo() {
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        return trace[2].getClassName() + " " + trace[2].getMethodName() + "()" + ":" + trace[2].getLineNumber() + " ";
    }

    private static String getLogInfoByArray(String[] infos) {
        StringBuilder sb = new StringBuilder();
        for (String info : infos) {
            sb.append(info);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void i(String... s) {
        if (debugEnabled) {
            Log.i(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
        }
    }

    public static void e(String... s) {
        if (debugEnabled) {
            Log.e(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
        }
    }

    public static void d(String... s) {
        if (debugEnabled) {
            Log.d(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
        }
    }

    public static void v(String... s) {
        if (debugEnabled) {
            Log.v(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
        }
    }

    public static void w(String... s) {
        if (debugEnabled) {
            Log.w(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
        }
    }

    public static void logException(Throwable tr) {
        if (debugEnabled) {
            Log.e(LOGTAG, getDebugInfo(), tr);
        }
    }
}
