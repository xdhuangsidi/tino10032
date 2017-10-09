package com.tino.ui.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;
import com.tencent.imcore.CustomElem;
import com.tino.R;
import com.tino.adapter.ConversationAdapter;
import com.tino.adapter.CustomConversationAdapter;
import com.tino.core.model.Conversation;
import com.tino.core.model.CustomMessage;
import com.tino.core.model.CustomNomalConversation;
import com.tino.core.model.MessageFactory;
import com.tino.core.model.NomalConversation;
import com.tino.im.presenter.ConversationPresenter;
import com.tino.im.presenter.CustomConversationPresenter;
import com.tino.im.view.ConversationView;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * 会话列表界面
 */
public class CustConversationFragment extends Fragment implements ConversationView {

    private final String TAG = "ConversationFragment";

    private View view;
    private List<Conversation> conversationList = new LinkedList<Conversation>();
    private CustomConversationAdapter adapter;
    private CustomConversationPresenter presenter;
    private ListView listView;



    public CustConversationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null){
            view = inflater.inflate(R.layout.fragment_conversation, container, false);
            listView=(ListView)view.findViewById(R.id.list_coversation);
            adapter = new CustomConversationAdapter(getActivity(), R.layout.item_conversation, conversationList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    conversationList.get(position).navToDetail(getActivity());

                }
            });
            presenter = new CustomConversationPresenter(this);
            presenter.getConversation();
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

    }



    @Override
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        for (TIMConversation item:conversationList){

                    CustomNomalConversation c=null;
                    for(TIMMessage message:item.getLastMsgs(1)){

                        if(message.getElement(0) instanceof TIMCustomElem)
                             c=new CustomNomalConversation(item);
                             if(c!=null){
                                 c.setCv(this);
                                 this.conversationList.add(c);
                             }

                    }


        }
        adapter.notifyDataSetChanged();

    }


    @Override
    public void updateMessage(TIMMessage message) {
        // TODO Auto-generated method stub

        if (message == null){
            adapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System){

            return;
        }
          if (!(MessageFactory.getMessage(message) instanceof CustomMessage)) return;
        CustomNomalConversation conversation = new CustomNomalConversation(message.getConversation());
        conversation.setProfile(message.getSenderProfile());
        Iterator<Conversation> iterator =conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (CustomNomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        refresh();


    }






    @Override
    public void removeConversation(String identify) {
        // TODO Auto-generated method stub

    }









}