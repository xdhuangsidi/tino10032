package com.tino.im.utils;



import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import java.util.List;

public class SDKmanager {

     /**
    * 登出imsdk
    *
    * @param callBack 登出后回调
    */
   public static void logout(TIMCallBack callBack){
       TIMManager.getInstance().logout(callBack);
   }


   /**
    * 设置我的昵称
    *
    * @param name 昵称
    * @param callBack 回调
    */
   public static void setMyNick(String name, TIMCallBack callBack){
       TIMFriendshipManager.getInstance().setNickName(name, callBack);
   }

    public static void setMyFaceUrl(String name, TIMCallBack callBack){
        TIMFriendshipManager.getInstance().setFaceUrl(name,callBack);

    }
    public static void delConversation(String c){
        TIMManager.getInstance().deleteConversation(TIMConversationType.C2C,c);
    }

   /**
    * 根据id获取用户资料
    *
    * @param id 用户id
    * @param callBack 回调
    */
   public static void searchUserProfile(String id, TIMValueCallBack<TIMUserProfile> callBack)
   {
       TIMFriendshipManager.getInstance().searchFriend(id, callBack);
   }

   /**
    * 获取好友列表
    *
    * @param cb callback回调
    */
   public static void getFriendlist(TIMValueCallBack<List<TIMUserProfile>> cb)
   {

        TIMFriendshipManager.getInstance().getFriendList(cb);
   }
   public static void setAllFriendAllow()
   {
       TIMFriendshipManager.getInstance().setAllowType(TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY, new TIMCallBack()
       {

           @Override
           public void onError(int arg0, String arg1) {
               // TODO Auto-generated method stub

           }

           @Override
           public void onSuccess() {
               // TODO Auto-generated method stub

           }

       });
   }

}
