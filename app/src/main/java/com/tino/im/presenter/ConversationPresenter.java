package com.tino.im.presenter;

import android.util.Log;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.imcore.CustomElem;
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
public class ConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;

    public ConversationPresenter(ConversationView view){
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
            if(((TIMMessage) data).getElement(0) instanceof TIMCustomElem)return;
            view.updateMessage(msg);
        }

    }



    public void getConversation(){
        List<TIMConversation> list = TIMManager.getInstance().getConversionList();
        final List<TIMConversation> result = new ArrayList<TIMConversation>();
        for (final TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System) continue;
            if(CommonUtils.getUid().equals(conversation.getPeer()))continue;

            result.add(conversation);




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
