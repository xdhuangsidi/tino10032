package com.tino.ui.message;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;
import com.tencent.imcore.CustomElem;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.adapter.ConversationAdapter;
import com.tino.core.model.Conversation;
import com.tino.core.model.CustomMessage;
import com.tino.core.model.CustomNomalConversation;
import com.tino.core.model.MessageFactory;
import com.tino.core.model.NomalConversation;
import com.tino.im.presenter.ConversationPresenter;
import com.tino.im.utils.SDKmanager;
import com.tino.im.view.ConversationView;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;




/**
 * 会话列表界面
 */
public class ConversationFragment extends Fragment implements ConversationView {

    private final String TAG = "ConversationFragment";

    private View view;
    private List<Conversation> conversationList = new LinkedList<Conversation>();
    private ConversationAdapter adapter;
    private ConversationPresenter presenter;
    private ListView listView;



    public ConversationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(view==null){
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            listView=(ListView)view.findViewById(R.id.list_coversation);
            adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    conversationList.get(position).navToDetail(getActivity());

                }
            });
           listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                               int position, long id) {
                    removeConversation(conversationList.get(position).identify);
                    return true;
                }
            });
            presenter = new ConversationPresenter(this);
            presenter.getConversation();
            ((MainActivity)getActivity()).tv_count.setVisibility(View.VISIBLE);
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(1);
            registerForContextMenu(listView);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }


        return view;



    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
    /**
     * 刷新
     */
    @Override
    public void  refresh() {
        Collections.sort(conversationList);
        adapter.notifyDataSetChanged();
        if(getActivity()!=null)
        ((MainActivity)getActivity()).setMsgUnread(getTotalUnreadNum());

    }



    @Override
    public void initView(List<TIMConversation> conversationList) {
        // TODO Auto-generated method stub

        this.conversationList.clear();
        boolean isCustom=false;
        for (TIMConversation item:conversationList){
            switch (item.getType()){
                case C2C:
                case Group:
                    NomalConversation c=new NomalConversation(item);
                    for (TIMMessage message : item.getLastMsgs(1)) {

                        if (message.getElement(0) instanceof TIMCustomElem){
                            isCustom=true;
                        }

                    }
                   if(!isCustom){
                        c.setCv(this);
                        this.conversationList.add(c);
                   }

                    break;

            }
        }
        ((MainActivity)getActivity()).setMsgUnread(getTotalUnreadNum());
        adapter.notifyDataSetChanged();

    }


    @Override
    public void updateMessage(TIMMessage message) {
        // TODO Auto-generated method stub

        if (message == null){
           // adapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System){
            // groupManagerPresenter.getGroupManageLastMessage();
            return;
        }

        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        if(message.getElement(0) instanceof TIMCustomElem)return;

        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator =conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (NomalConversation) c;

                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        if(!message.isSelf())
               refresh();


    }



    private int getTotalUnreadNum(){
        int num = 0;
        for (Conversation conversation : conversationList){

            num += conversation.getUnreadNum();
        }
        return num;
    }


    @Override
    public void removeConversation(String identify) {
        // TODO Auto-generated method stub
        Iterator<Conversation> iterator = conversationList.iterator();
        while(iterator.hasNext()){
            Conversation conversation = iterator.next();
            if (conversation.getIdentify()!=null&&conversation.getIdentify().equals(identify)){
                iterator.remove();
                SDKmanager.delConversation(identify);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "删除该聊天成功", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }









}