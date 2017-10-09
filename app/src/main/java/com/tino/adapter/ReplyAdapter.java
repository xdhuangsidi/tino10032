package com.tino.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.TIMConversationType;
import com.tino.R;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.model.CommentList;
import com.tino.core.net.UploadImp;
import com.tino.ui.message.ChatActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.TimeUtils;
import com.tino.views.CircleImageView;
import com.tino.views.CommentEditorController;
import com.tino.views.NoScrollGridView;

import java.util.List;

public class ReplyAdapter extends BaseRecyclerAdapter<CommentList> {

  private ItemClick click;
 private Context context;
    public CommentEditorController editorController;
    class CommentHolder extends CommonHolder<CommentList> {
        CircleImageView avatar;
        TextView tv_title;
        TextView tv_content;
        TextView tv_name;
        TextView tv_time;
        ImageButton ib_comment;

        NoScrollGridView photo_gridview ;
        public CommentHolder(View root) {
            super(root);//, R.layout.item_comment);
            tv_name = (TextView)root.findViewById( R.id.tv_name);
            tv_time = (TextView) root.findViewById(R.id.tv_time);
            tv_content = (TextView) root.findViewById(R.id.tv_content);
            tv_title=(TextView)root.findViewById(R.id.tv_title);
            avatar = (CircleImageView) root.findViewById(R.id.avatar);
            ib_comment=(ImageButton)root.findViewById(R.id.ib_add_subcomment);
            photo_gridview = (NoScrollGridView) root.findViewById( R.id.photo_gridview);

        }
        @Override
        public void bindData(final CommentList blog) {

            Glide.with(TinoApplication.getContext()).load(blog.getCommenter().getAvatar()).error(R.mipmap.avatar_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(this.avatar);
            this.ib_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editorController.editTextBodyVisible(View.GONE);
                    editorController.clearEditText();
                    editorController.editTextBodyVisible(0,1, "", blog.getCommenter().getUserId(), 0);
                    editorController.setCommentListener(new CommentEditorController.CommentListener() {
                        public void onSuccess(String content) {

                        }
                    });
                    editorController.mSendBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String content =  editorController.mEditText.getText().toString().trim();
                            UploadImp.addsubReply(CommonUtils.gettoken()
                                    , CommonUtils.getUid()
                                    , blog.getListTime(), blog.getCommenter().getUserId()
                                    ,content
                                    ,new ActionCallback<String>() {
                                        @Override
                                        public void inProgress(float f) {

                                        }

                                        @Override
                                        public void onAfter() {

                                        }

                                        @Override
                                        public void onBefore() {

                                        }

                                        @Override
                                        public void onError() {

                                        }

                                        @Override
                                        public void onFailure(String str, String str2) {

                                        }

                                        @Override
                                        public void onSuccess(String s) {

                                        }
                                    });
                        }
                    });
                }
            });
            this.tv_name.setText(blog.getCommenter().getUserName());
            this.tv_content.setText(blog.getContent());
            this.tv_time.setText(TimeUtils.getListTime(Long.parseLong(blog.getListTime())));
            this.tv_title.setVisibility(View.GONE);
            this.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatActivity.navToChat(context, blog.getCommenter().getUserId(),  TIMConversationType.C2C, blog.getCommenter().getAvatar(),blog.getCommenter().getUserName());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(click!=null){
                      //  click.onItemClick(blog);
                    }
                }
            });
            photo_gridview.setVisibility(View.GONE);
           /* List<String> list= JSON.parseObject(blog.get,new  TypeReference<List<String>>() {});
            if(list.size()==0){
                photo_gridview.setVisibility(View.GONE);
            }
            else{
                this.photo_gridview.setVisibility(View.VISIBLE);
                PhotoAdapter photoAdapter = new PhotoAdapter(this.itemView.getContext(),list);
                this.photo_gridview.setAdapter(photoAdapter);
            }*/
        }
    }
    public interface ItemClick {
        void onItemClick(CommentList blog);
    }
    @Override
    public CommonHolder<CommentList> setViewHolder(ViewGroup parent) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_detail, parent, false);
    return new CommentHolder(v);
    }
    public void SetItemclick(ItemClick click){
        this.click=click;
    }
    public void SetContext(Context ct){
        this.context=ct;
    }
}
