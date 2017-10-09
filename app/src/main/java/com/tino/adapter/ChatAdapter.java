package com.tino.adapter;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tino.R;
import com.tino.core.TinoApplication;
import com.tino.core.model.Message;
import com.tino.ui.home.UserDetialFromIdActivity;
import com.tino.utils.CommonUtils;
import com.tino.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.qalsdk.service.QalService.context;


/**
 * 聊天界面adapter
 */
public class ChatAdapter extends Adapter<ViewHolder> {

    private final String TAG = "ChatAdapter";
    Context ct;
    String faceUrl;
    private List<Message> messageList = new ArrayList();
    public ChatAdapter(Context ct,String faceUrl,List<Message> list){
    super();
    this.ct=ct;
    this.faceUrl=faceUrl;
    this.messageList=list;
}

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left,parent,false);

            return new  LeftViewHolder(view);
        }
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right,parent,false);
            return new RightViewHolder(view);
        }
        return null;
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MViewHolder) holder).bindData(this.messageList.get(position));

    }
    public void addMessageList(List<Message> messages) {
        this.messageList.addAll(0, messages);
    }
    public void setMessageList(List<Message> messages) {
        this.messageList=messages;
    }
    public void addMessafe(Message message){
      this.messageList.add(message);
  }



    public int getItemViewType(int position) {
        if (( this.messageList.get(position)).isSelf()) {
            return 1;
        }
        return 0;
    }
    public int getItemCount() {
        return this.messageList.size();
    }


public abstract class MViewHolder<T> extends ViewHolder{
    public String avatarUrl;
    public CircleImageView avatar;
    public TextView systemMessage;
    public RelativeLayout MessageLayout;
    public Message messageContent;
    protected Context ct;
    public MViewHolder(View itemView){
        super(itemView);
        this.ct=itemView.getContext();
    }
    public abstract void bindData(Message o);


}

    public  class LeftViewHolder extends MViewHolder<Message>{

        public LeftViewHolder(View view) {
            super(view);
            this.avatar=(CircleImageView)view.findViewById(R.id.avatar);
            this.avatarUrl=faceUrl;
            Glide.with(view.getContext()).load(faceUrl)
                 //   .placeholder(R.mipmap.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avatar);
            this.systemMessage=(TextView)view.findViewById(R.id.tv_time);
            this.MessageLayout=(RelativeLayout)view.findViewById(R.id.leftMessage);

        }

        public void bindData(final Message o) {
            this.messageContent=o;
            if(o.isSelf()){
                Glide.with(context).load(faceUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avatar);
                messageContent.showMessage(this,ct);
                return;
            }
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDetialFromIdActivity.navToDetail(ct,o.getSenderId());
                }
            });
            messageContent.showMessage(this,ct);
        }
    }

    public class RightViewHolder extends MViewHolder<Message>{
        public ProgressBar sending;
        public ImageView error;
        public RightViewHolder(View view) {
            super(view);

            this.avatar=(CircleImageView)view.findViewById(R.id.avatar);
            this.avatarUrl=CommonUtils.getAvatar(view.getContext());
            Glide.with(ct).load(avatarUrl).placeholder(R.mipmap.avatar_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avatar);
            this.systemMessage=(TextView)view.findViewById(R.id.tv_time);
            this.MessageLayout=(RelativeLayout)view.findViewById(R.id.rightMessage);
            this.sending=(ProgressBar)view.findViewById(R.id.sending);
            this.error=(ImageView)view.findViewById(R.id.sendError);
        }


        public void bindData(Message o) {
            System.out.println("\n\n\n  bind the data               \n\n\n\n");
            this.messageContent=o;
            Glide.with(ct).load(CommonUtils.getAvatar(TinoApplication.getContext())).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avatar);
            messageContent.showMessage(this,ct);


        }

    }
}
