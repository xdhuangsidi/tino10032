package com.tino.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.tencent.TIMConversationType;
import com.tino.R;
import com.tino.core.ActionCallback;
import com.tino.core.model.Blog;
import com.tino.core.model.Comment;
import com.tino.core.model.CommentList;
import com.tino.core.net.UploadImp;
import com.tino.ui.home.UserDetialActivity;
import com.tino.ui.message.ChatActivity;
import com.tino.utils.CommonHelper;
import com.tino.utils.CommonUtils;
import com.tino.utils.TimeUtils;
import com.tino.views.AppNoScrollerListView;
import com.tino.views.CircleImageView;
import com.tino.views.CommentEditorController;
import com.tino.views.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;




public class ShareItemHolder extends CommonHolder<CommentList> {
    private static final int TYPE_ARTICLE = 1;
    private static final int TYPE_SAY = 0;
  //  @Bind({2131558613})
    CircleImageView avatar;
    List<Comment> commentList;
    ShareCommentAdapter commentsAdapter;
    CommentEditorController editorController;
   // @Bind({2131558722})
    ImageButton ib_comment;
   // @Bind({2131558755})
    ImageButton ib_like,ib_delete;
    private ShareListAdapter.ItemClick itemClick1;

    ImageView iv_pic;

    AppNoScrollerListView lv_comments;
    PhotoAdapter photoAdapter;

    NoScrollGridView photo_gridview;

    RelativeLayout rl_article;

    RelativeLayout rl_say;
    TextView tv_content;

    TextView tv_name;

    TextView tv_status;

    TextView tv_time;

    TextView tv_title;

    TextView tv_type;
    Blog blog;
    ShareListAdapter.CommentReflashCallback  listener;
private void bindView(View view)
{
    avatar = (CircleImageView)view.findViewById( R.id.avatar);
    tv_name = (TextView) view.findViewById( R.id.tv_name);
    tv_time = (TextView) view.findViewById( R.id.tv_time);
    photo_gridview = (NoScrollGridView) view.findViewById( R.id.photo_gridview);
    rl_article =(RelativeLayout) view.findViewById(R.id.rl_article);
    iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
    rl_say = (RelativeLayout) view.findViewById(R.id.rl_say);
    tv_content = (TextView)view.findViewById(R.id.tv_content);
    tv_title = (TextView) view.findViewById( R.id.tv_title);
    lv_comments = (AppNoScrollerListView) view.findViewById(R.id.lv_comments);
    ib_comment = (ImageButton) view.findViewById( R.id.ib_comment);
    ib_like = (ImageButton) view.findViewById(R.id.ib_like);
    tv_status = (TextView)view.findViewById( R.id.tv_status);
    tv_type = (TextView) view.findViewById( R.id.tv_type);
    ib_delete=(ImageButton)view.findViewById(R.id.ib_delete_comment);
}
    public ShareItemHolder(View view, CommentEditorController editorController) {
        super(view);
        this.editorController = editorController;
        bindView(view);
    }
    public ShareItemHolder(View view, Context ct, ShareListAdapter.ItemClick itemClick,
                           CommentEditorController editorController
    ,ShareListAdapter.CommentReflashCallback listener,Blog blog) {
        super(view);
        this.editorController = editorController;
        this.itemClick1 = itemClick;
        this.listener=listener;
        this.blog=blog;
        bindView(view);

        context=ct;
    }
     Context context;

    public ShareItemHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_share);
        bindView(root);

    }

    public void bindData(final CommentList shareBean) {
        Glide.with(context).load(shareBean.getCommenter().getAvatar()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(this.avatar);
        this.tv_name.setText(shareBean.getCommenter().getUserName());
        this.tv_status.setText( "评论 "+shareBean.getComments().size() );
        this.tv_time.setText(TimeUtils.getListTime(Long.parseLong(shareBean.getListTime()) ));
        this.tv_content.setText(shareBean.getContent());
        this.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (ShareItemHolder.this.itemClick1 != null) {
                    ShareItemHolder.this.itemClick1.onItemClick(shareBean);
                }
            }
        });
        this.avatar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(shareBean.getCommenter().getUserId().equals(CommonUtils.getUid()))
                    return;
                UserDetialActivity.navToDetail(context,shareBean.getCommenter());

       /*         ChatActivity.navToChat(ShareItemHolder.this.getContext(),shareBean.getCommenter().getUserId(),
                        TIMConversationType.C2C,
                        shareBean.getCommenter().getAvatar()
                        ,shareBean.getCommenter().getUserName());*/
            }
        });
        commentList=new ArrayList<>();
        for(Comment c:shareBean.getComments()){
            commentList.add(c);
        }
        this.ib_comment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CommonHelper.comment(blog,0, shareBean.getListTime(),
                        shareBean, ShareItemHolder.this.editorController, new CommentEditorController.CommentListener() {
                            @Override
                            public void onSuccess(String str) {
                                listener.onReflash();
                            }
                        });
            }
        });
        if(shareBean.getCommenter().getUserId().equals(CommonUtils.getUid())){
            ib_delete.setVisibility(View.VISIBLE);
            ib_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    UploadImp.deleteCommentList(CommonUtils.gettoken(), CommonUtils.getUid(), shareBean.getListTime(), new ActionCallback<String>() {
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
                            Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                            listener.onReflash();
                            ib_delete.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }


        {
            this.lv_comments.setVisibility(View.VISIBLE);
            this.commentsAdapter = new ShareCommentAdapter(commentList);
            this.lv_comments.setAdapter(this.commentsAdapter);
            this.commentsAdapter.setCommentClickListener(new ShareCommentAdapter.ICommentItemClickListener() {
                public void onItemClick(int commentPosition) {
                    Comment commentItem = commentList.get(commentPosition);

                    CommonHelper.comment(blog,0, shareBean.getListTime(),
                            shareBean,commentItem.getReplyer(), ShareItemHolder.this.editorController, new CommentEditorController.CommentListener() {
                                @Override
                                public void onSuccess(String str) {
                                    listener.onReflash();
                                }
                            });

                }
            });
        }
        this.lv_comments.setOnItemClickListener(null);
        this.lv_comments.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(ShareItemHolder.this.getContext(), "长按事件,应该显示删除按钮", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
