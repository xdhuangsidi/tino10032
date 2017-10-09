package com.tino.im.presenter;

import android.util.Log;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tino.im.event.MessageEvent;
import com.tino.im.view.ConversationView;
import com.tino.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * 会话界面逻辑
 */
public class CustomConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;

    public CustomConversationPresenter(ConversationView view){
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听

        this.view = view;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            if(data==null)return;
            TIMMessage msg = (TIMMessage) data;
            if(msg.isSelf())return;
            if(!(((TIMMessage) data).getElement(0) instanceof TIMCustomElem))return;
            view.updateMessage(msg);
        }

    }



    public void getConversation(){
        List<TIMConversation> list = TIMManager.getInstance().getConversionList();
        List<TIMConversation> result = new ArrayList<TIMConversation>();
        for (final TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System) continue;
            if(CommonUtils.getUid().equals(conversation.getPeer()))continue;
            for (TIMMessage m:conversation.getLastMsgs(1) ) {
                if(!m.isSelf())result.add(conversation);
            }

          /*  conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        view.updateMessage(timMessages.get(0));

                    }

                }
            });*/

        }
        view.initView(result);
    }

    /**
     * 删除会话
     *
     * @param type 会话类型
     * @param id 会话对象id
     */
    public boolean delConversation(TIMConversationType type, String id){
        return TIMManager.getInstance().deleteConversationAndLocalMsgs(type, id);
    }


}
