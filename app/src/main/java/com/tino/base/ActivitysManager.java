package com.tino.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

public class ActivitysManager {
    private static ActivitysManager instanse;
    private static Stack<Activity> mStack;

    private ActivitysManager() {
        mStack = new Stack();
    }

    public static ActivitysManager getInstanse() {
        if (instanse == null) {
            instanse = new ActivitysManager();
        }
        return instanse;
    }

    public void add(Activity mActivity) {
        mStack.add(mActivity);
    }

    public Activity getTopActivity() {
        return (Activity) mStack.lastElement();
    }

    public void killTopActivity() {
        killActivity(getTopActivity());
    }

    public void killActivity(Activity activity) {
        if (activity != null) {
            mStack.remove(activity);
            activity.finish();
        }
    }

    public void killAllActivity() {
        Iterator it = mStack.iterator();
        while (it.hasNext()) {
            ((Activity) it.next()).finish();
        }
        mStack.clear();
    }

    public void exitApp(Context mContext) {
        killAllActivity();
        ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).restartPackage(mContext.getPackageName());
        System.exit(0);
    }
}
