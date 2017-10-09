package com.tino.im.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.TinoApplication;
import com.tino.core.model.CustomMessage;
import com.tino.core.model.Message;
import com.tino.core.model.MessageFactory;
import com.tino.im.event.MessageEvent;
import com.tino.ui.message.ChatActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * 在线消息通知展示
 */
public class PushUtil implements Observer {

    private static final String TAG = PushUtil.class.getSimpleName();

    private static int pushNum=0;

    private final int pushId=1;

    private static PushUtil instance = new PushUtil();

    private PushUtil() {
        MessageEvent.getInstance().addObserver(this);
    }

    public static PushUtil getInstance(){
        return instance;
    }



    private void PushNotify(TIMMessage msg){
        //系统消息，自己发的消息，程序在前台的时候不通知

        if (msg==null||Foreground.get().isForeground()||
                (msg.getConversation().getType()!= TIMConversationType.Group&&
                        msg.getConversation().getType()!= TIMConversationType.C2C)||
                msg.isSelf()||
                msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify ||
                MessageFactory.getMessage(msg) instanceof CustomMessage) return;

        String senderStr,contentStr;

        msg.getSenderProfile().getFaceUrl();
        if(msg.getSenderProfile().getNickName()!=null&&msg.getSenderProfile().getNickName().length()!=0)
            senderStr =msg.getSenderProfile().getNickName();
        else  senderStr=msg.getSender();
        Message message = MessageFactory.getMessage(msg);
        if (message == null) return;
        contentStr = message.getSummary();


        final RemoteViews rv = new RemoteViews(TinoApplication.getContext().getPackageName(), R.layout.remoteview_notification);
        rv.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.ic_launcher);
        rv.setTextViewText(R.id.remoteview_notification_headline, senderStr);
        rv.setTextViewText(R.id.remoteview_notification_short_message, contentStr);





        Log.d(TAG, "recv msg " + contentStr);

        NotificationManager mNotificationManager = (NotificationManager) TinoApplication.getContext().getSystemService(TinoApplication.getContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TinoApplication.getContext());
        Intent notificationIntent = new Intent(TinoApplication.getContext(), ChatActivity.class);//修改这里  变成直接进入聊天界面
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("identify",msg.getSender());
        notificationIntent.putExtra("type",TIMConversationType.C2C);
        notificationIntent.putExtra("faceUrl",msg.getSenderProfile().getFaceUrl());
        notificationIntent.putExtra("Nickname",msg.getSenderProfile().getNickName());
        PendingIntent intent = PendingIntent.getActivity(TinoApplication.getContext(), 0,
                notificationIntent, 0);
        mBuilder.setContentTitle(senderStr)//设置通知栏标题
                .setContentText(contentStr)
                .setContent(rv)
                .setPriority( NotificationCompat.PRIORITY_MIN)
                .setContentIntent(intent) //设置通知栏点击意图
 //               .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(senderStr+":"+contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        Notification notify = mBuilder.build();
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notify.bigContentView = rv;
        }
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);

        NotificationTarget notificationTarget = new NotificationTarget(
                TinoApplication.getContext(),
                rv,
                R.id.remoteview_notification_icon,
                notify,
                pushId);
        Glide
                .with(TinoApplication.getContext() ) // safer!
                .load( msg.getSenderProfile().getFaceUrl() )
                .asBitmap()
                .into( notificationTarget );


    }

    public static void resetPushNum(){
        pushNum=0;
    }

    public void reset(){
        NotificationManager notificationManager = (NotificationManager)TinoApplication.getContext().getSystemService(TinoApplication.getContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(pushId);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            TIMMessage msg = (TIMMessage) data;
            if (msg != null){
                PushNotify(msg);
            }
        }
    }
}
