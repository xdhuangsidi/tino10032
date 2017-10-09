package com.tino.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tino.R;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.model.Blog;
import com.tino.core.model.Comment;
import com.tino.core.model.CommentList;
import com.tino.core.model.CustomMessage;
import com.tino.core.model.User;
import com.tino.core.net.CommonCallback;
import com.tino.core.net.UploadImp;
import com.tino.views.CommentEditorController;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by root on 17-8-24.
 */

public class CommonHelper {

    public static void toggleFavorite(final Blog blog, final ImageButton ib_like, final TextView tv_status) {

        if (blog.isIslike()) {
            blog.setIslike(false);
            Toast.makeText(TinoApplication.getInstance(), "取消点赞", Toast.LENGTH_SHORT).show();
            UploadImp.dislikeBlog(CommonUtils.gettoken(), CommonUtils.getUid(), blog.getBlogTime(),
                    new ActionCallback<String>() {
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
                        public void onSuccess(String s){
                            ib_like.setBackgroundResource(R.mipmap.ic_unfavorit);
                            tv_status.setText(""+(blog.getLikeCounts()-1));
                        }
                    });
        }else {

            blog.setIslike(true);
if(blog.getOwner().getUserId()!=CommonUtils.getUid()){
            Gson gson=new Gson();
            Blog blog1=new Blog();
            blog1.setTitle(blog.getTitle());
            blog1.setBlogTime(blog.getBlogTime());
            blog1.setContent(blog.getContent());
            blog1.setLikeUserInfo(blog.getLikeUserInfo());
            blog1.setOwner(blog.getOwner());
            blog1.setPictures(blog.getPictures());
            blog1.setLikeCounts(blog.getLikeCounts());

            String js=gson.toJson(blog1);

            TIMMessage message = new TIMMessage();
            TIMCustomElem elem = new TIMCustomElem();
            elem.setDesc("#@like@#");
            elem.setData(js.getBytes());
            elem.setExt(Long.toString(System.currentTimeMillis()).getBytes());
            message.addElement(elem);
            CustomMessage msg=new CustomMessage(message);
            TIMManager.getInstance().getConversation(TIMConversationType.C2C
                    ,blog.getOwner().getUserId()).sendMessage(msg.getMessage(), new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    //Toast.makeText(TinoApplication.getInstance(), "send custom successu", Toast.LENGTH_SHORT).show();
                }
            });}
            UploadImp.likeBlog(CommonUtils.gettoken(), CommonUtils.getUid(), blog.getBlogTime(),
                    new ActionCallback<String>() {
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
                            ib_like.setBackgroundResource(R.mipmap.ic_favorit);
                            tv_status.setText(""+(blog.getLikeCounts()+1));
                        }
                    });
        }


    }
    public static void toggleCollect(final Blog blog, final ImageButton ib_collect) {

        if (blog.isIscollect()==true) {
            blog.setIscollect(false);
            UploadImp.uncollectblog(CommonUtils.gettoken(), CommonUtils.getUid(), blog.getBlogTime(),
                    new ActionCallback<String>() {
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
                        public void onSuccess(String s){
                            ib_collect.setImageResource(R.mipmap.icon_uncollect);
                            blog.setIscollect(false);
                            Toast.makeText(TinoApplication.getInstance(), "取消收藏", Toast.LENGTH_SHORT).show();

                        }
                    });
        }else {
            //NotificationUtils.pushShareLike(SPUtils.getString(BookApplication.getInstance(), Constants.USERNAME) + "给你点了赞!", shareBean.getSid(), shareBean.getUid());
            blog.setIscollect(true);
            UploadImp.collectblog(CommonUtils.gettoken(), CommonUtils.getUid(), blog.getBlogTime(),
                    new ActionCallback<String>() {
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
                            ib_collect.setImageResource(R.mipmap.icon_collect);   blog.setIscollect(true);
                            Toast.makeText(TinoApplication.getInstance(), "收藏成功", Toast.LENGTH_SHORT).show();

                        }
                    });
        }


    }

    public static void deleteBlog(final String blogid,final Context ct){
        TextView  tv=new TextView(ct);
        tv.setText("\n      删除操作不可逆，确定删除？");
        new  AlertDialog.Builder(ct)
                .setTitle("删除确认")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(tv)

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UploadImp.deleteblog(CommonUtils.gettoken(),
                                CommonUtils.getUid(), blogid, new ActionCallback<String>() {
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
                                        Intent intent = new Intent();
                                        intent.setAction("tino.reflash_blog");
                                        ct.sendBroadcast(intent);
                                        Toast.makeText(TinoApplication.getInstance(), "删除帖子成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }
    public static void comment(final Blog blog,final int mode, final String lid, final CommentList comment, final CommentEditorController editorController, final CommentEditorController.CommentListener listener) {

        editorController.editTextBodyVisible(0, mode, lid, comment.getCommenter().getUserId(), 0);
        editorController.setCommentListener(new CommentEditorController.CommentListener() {
            public void onSuccess(String str) {

            }
        });
        editorController.mSendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = editorController.mEditText.getText().toString().trim();
                editorController.clearEditText();
                editorController.editTextBodyVisible(View.GONE);
                if(!comment.getCommenter().getUserId().equals(CommonUtils.getUid())) {
                    Gson gson = new Gson();
                    Blog blog1 = new Blog();
                    blog1.setTitle(blog.getTitle());
                    blog1.setBlogTime(blog.getBlogTime());
                    blog1.setContent(blog.getContent());
                    blog1.setLikeUserInfo(blog.getLikeUserInfo());
                    blog1.setOwner(blog.getOwner());
                    blog1.setPictures(blog.getPictures());
                    blog1.setLikeCounts(blog.getLikeCounts());

                    String js = gson.toJson(blog1);

                    TIMMessage message = new TIMMessage();
                    TIMCustomElem elem = new TIMCustomElem();
                    elem.setDesc(content);
                    elem.setData(js.getBytes());
                    elem.setExt(Long.toString(System.currentTimeMillis()).getBytes());
                    message.addElement(elem);
                    CustomMessage msg = new CustomMessage(message);
                    TIMManager.getInstance().getConversation(TIMConversationType.C2C
                            , comment.getCommenter().getUserId()).sendMessage(msg.getMessage(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            //Toast.makeText(TinoApplication.getInstance(), "send custom successu", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                UploadImp.addsubReply(CommonUtils.gettoken()
                        , CommonUtils.getUid()
                        , comment.getListTime(), comment.getCommenter().getUserId()
                        , content
                        , new ActionCallback<String>() {
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
                                  listener.onSuccess("");
                            }
                        });
            }

        });
    }
    public static void comment(final Blog blog,final int mode, final String lid, final CommentList comment, final User replyTo, final CommentEditorController editorController, final CommentEditorController.CommentListener listener) {

        editorController.editTextBodyVisible(0, mode, lid, comment.getCommenter().getUserId(), 0);
        editorController.setCommentListener(new CommentEditorController.CommentListener() {
            public void onSuccess(String str) {

            }
        });
        editorController.mEditText.setHint("回复给"+replyTo.getUserName());
        editorController.mSendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = editorController.mEditText.getText().toString().trim();
                editorController.clearEditText();
                editorController.editTextBodyVisible(View.GONE);

                if(!replyTo.getUserId().equals(CommonUtils.getUid())) {
                    Gson gson = new Gson();
                    Blog blog1 = new Blog();
                    blog1.setTitle(blog.getTitle());
                    blog1.setBlogTime(blog.getBlogTime());
                    blog1.setContent(blog.getContent());
                    blog1.setLikeUserInfo(blog.getLikeUserInfo());
                    blog1.setOwner(blog.getOwner());
                    blog1.setPictures(blog.getPictures());
                    blog1.setLikeCounts(blog.getLikeCounts());

                    String js = gson.toJson(blog1);


                    TIMMessage message = new TIMMessage();
                    TIMCustomElem elem = new TIMCustomElem();
                    elem.setDesc(content);
                    elem.setData(js.getBytes());
                    elem.setExt(Long.toString(System.currentTimeMillis()).getBytes());
                    message.addElement(elem);
                    CustomMessage msg = new CustomMessage(message);
                    TIMManager.getInstance().getConversation(TIMConversationType.C2C
                            , replyTo.getUserId()).sendMessage(msg.getMessage(), new TIMValueCallBack<TIMMessage>() {
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(TinoApplication.getInstance(), "发送失败" + s, Toast.LENGTH_SHORT).show();
                            System.out.println("send custom message fail____" + s);
                        }

                        @Override
                        public void onSuccess(TIMMessage timMessage) {
                            Toast.makeText(TinoApplication.getInstance(), "发送成功", Toast.LENGTH_SHORT).show();
                            System.out.println("send custom message successfuly" + blog.getTitle());
                        }
                    });

                }
                UploadImp.addsubReply(CommonUtils.gettoken()
                        , CommonUtils.getUid()
                        , comment.getListTime(),replyTo.getUserId()
                        , content
                        , new ActionCallback<String>() {
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
                                listener.onSuccess("");
                            }
                        });
            }

        });
    }

    public static void  toggleCare(final ImageView imageView, String becared, boolean iscare,final CommonCallback cb){
        if (iscare){
            UploadImp.uncareUser(CommonUtils.gettoken(), CommonUtils.getUid(),
                    becared, new ActionCallback<String>() {
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
                              imageView.setImageResource(R.drawable.icon_uncare);
                              cb.onSuccess(false);
                              Toast.makeText(TinoApplication.getInstance(), "取消关注", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            UploadImp.careUser(CommonUtils.gettoken(), CommonUtils.getUid(),
                    becared, new ActionCallback<String>() {
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
                            imageView.setImageResource(R.drawable.icon_uncare);
                            cb.onSuccess(true);
                            Toast.makeText(TinoApplication.getInstance(), "已关注", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
}
