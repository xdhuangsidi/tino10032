package com.tino.core.model;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tino.R;
import com.tino.core.TinoApplication;
import com.tino.im.utils.SDKmanager;
import com.tino.ui.message.ChatActivity;
import com.tino.ui.message.CustomChatActivity;
import com.tino.views.CircleImageView;


/**
 * 好友或群聊的会话
 */
public class CustomNomalConversation extends Conversation {


    private TIMConversation conversation;

    String nickName="";

    TIMUserProfile profile;
    //最后一条消息
    private Message lastMessage;


    public CustomNomalConversation(TIMConversation conversation){

        profile=conversation.getLastMsgs(1).get(0).getSenderProfile();
        identify = conversation.getPeer();
        this.conversation = conversation;
        type = conversation.getType();



    }

  public void setProfile(TIMUserProfile profile){
        this.profile=profile;
  }
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public String getAvatar() {
        if (profile.getFaceUrl()==null)return "";
       return profile.getFaceUrl();
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        CustomChatActivity.navToChat(context,identify, TIMConversationType.C2C,faceUrl,nickName);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary(){
        if (conversation.hasDraft()){
            TextMessage textMessage = new TextMessage(conversation.getDraft());

            if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft().getTimestamp()){
                return TinoApplication.getContext().getString(R.string.conversation_draft) + textMessage.getSummary();
            }else{
                return lastMessage.getSummary();
            }
        }else{
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
       if(profile.getNickName()!=null){
           return profile.getNickName();
       }
       else return  identify;
    }

     public void setConversationName()
    {

        SDKmanager.searchUserProfile(this.identify, new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                //name=identify;
            }

            @Override
            public void onSuccess(TIMUserProfile arg0) {
                profile = arg0;
                nickName = arg0.getNickName();

            }
        });
    }
    @Override
    public void setAvatarAndUsername(final Context context, final TextView tvName, final CircleImageView imageview){
    SDKmanager.searchUserProfile(this.identify, new TIMValueCallBack<TIMUserProfile>() {
        @Override
        public void onError(int arg0, String arg1) {
            // TODO Auto-generated method stub
            //name=identify;

        }

        @Override
        public void onSuccess(TIMUserProfile arg0) {
            profile = arg0;
            faceUrl=arg0.getFaceUrl();
            nickName=arg0.getNickName();
            Glide.with(context).
                    load(faceUrl)
                    //.placeholder(R.mipmap.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
            tvName.setText(arg0.getNickName());

        }
    });

}
    /**
     * 获取未读消息数量
     */

                /**
                 * 获取未读消息数量
                 */
        @Override
        public long getUnreadNum(){
        if (conversation == null) return 0;
        return conversation.getUnreadMessageNum();
    }

        /**
         * 将所有消息标记为已读
         */
    @Override
    public void readAllMessage(){
        if (conversation != null){
            conversation.setReadMessage();
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        if (conversation.hasDraft()){
            if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft().getTimestamp()){
                return conversation.getDraft().getTimestamp();
            }else{
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType(){
        return conversation.getType();
    }
}
