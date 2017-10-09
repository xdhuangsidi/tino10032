package com.tino.im.view;


import com.tencent.TIMConversation;
import com.tencent.TIMMessage;

import java.util.List;

/**
 * 会话列表界面的接口
 */
public interface ConversationView extends MvpView {

    /**
     * 初始化界面或刷新界面
     */
    void initView(List<TIMConversation> conversationList);


    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    void updateMessage(TIMMessage message);



    public void removeConversation(String identify);


    /**
     * 刷新
     */
    void refresh();




}
