package com.tino.core.model;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.TIMVideoElem;
import com.tino.R;
import com.tino.adapter.ChatAdapter;
import com.tino.adapter.CustomChatAdapter;
import com.tino.core.TinoApplication;
import com.tino.ui.home.ShareDetailActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {


    private String TAG = getClass().getSimpleName();

    private final int TYPE_TYPING = 14;

    private Type type;
    private String desc;
    private String data;
    public String username;
    public String avatarurl;
    public CustomMessage(TIMMessage message){
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        parse(elem.getData());

    }

    public CustomMessage(Type type){
        message = new TIMMessage();
        String data = "";
        JSONObject dataJson = new JSONObject();
        try{
            switch (type){
                case TYPING:
                    dataJson.put("userAction",TYPE_TYPING);
                    dataJson.put("actionParam","EIMAMSG_InputStatus_Ing");
                    data = dataJson.toString();
            }
        }catch (JSONException e){
            Log.e(TAG, "generate json error");
        }
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data.getBytes());
        message.addElement(elem);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void parse(byte[] data){
        type = Type.INVALID;
        try{
            String str = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(str);
            int action = jsonObj.getInt("userAction");
            switch (action){
                case TYPE_TYPING:
                    type = Type.TYPING;
                    this.data = jsonObj.getString("actionParam");
                    if (this.data.equals("EIMAMSG_InputStatus_End")){
                        type = Type.INVALID;
                    }
                    break;
            }

        }catch (IOException | JSONException e){
            Log.e(TAG, "parse json error");

        }
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.MViewHolder viewHolder, Context context) {
        clearView(viewHolder);
        TextView tv = new TextView(TinoApplication.getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(TinoApplication.getContext().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        List<TIMElem> elems = new ArrayList<>();
        for (int i = 0; i < message.getElementCount(); ++i){
            elems.add(message.getElement(i));
            if (message.getElement(i).getType() == TIMElemType.Custom){
                final TIMCustomElem e = ( TIMCustomElem) message.getElement(i);
                tv.setText(e.getDesc()+"\n");
                String s=new String(e.getData());
                if(s.length()>20) {
                    Gson gson = new Gson();
                    Blog blog = gson.fromJson(s, Blog.class);
                    tv.append(blog.getTitle()+"\n"+blog.getContent());
                }



            }
        }
        getBubbleView(viewHolder).addView(tv);
        if(message.isSelf())
            showStatus((ChatAdapter.RightViewHolder) viewHolder);
    }
    public void showMessage0(CustomChatAdapter.MViewHolder viewHolder, final Context context) {
        viewHolder.tv_name.setText(username);
        Glide.with(context)
                .load(avatarurl)
                //.placeholder(R.mipmap.avatar_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewHolder.avatar);

        List<TIMElem> elems = new ArrayList<>();
        for (int i = 0; i < message.getElementCount(); ++i){
            elems.add(message.getElement(i));
            if (message.getElement(i).getType() == TIMElemType.Custom){
                final TIMCustomElem e = ( TIMCustomElem) message.getElement(i);
                viewHolder.tv_content.setText(e.getDesc());
                String s=new String(e.getData());
                if(s.length()>20) {
                    Gson gson = new Gson();
                    final Blog blog = gson.fromJson(s, Blog.class);
                    viewHolder.tv_title.setText(blog.getTitle());
                    if(blog.getTitle()==null||blog.getTitle().length()<=0){
                        viewHolder.tv_title.setText(blog.getContent());
                    }
                    if("#@like@#".equals(e.getDesc())){
                        viewHolder.tv_type.setText("赞了你");
                        viewHolder.tv_content.setVisibility(View.GONE);
                    }
                    else viewHolder.tv_type.setText("评论了你");
                    viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ShareDetailActivity.launch((Activity) context,blog);
                        }
                    });

                }
                if(e.getExt()!=null&&e.getExt().length>10) {
                    viewHolder.tv_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_time.setText(
                            TimeUtils.getListTime(Long.parseLong(new String(e.getExt()))));
                }
            }
        }


    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return null;
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    public enum Type{
        TYPING,
        INVALID,
    }
}
