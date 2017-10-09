package com.tino.core.model;


import java.io.Serializable;
import java.net.URLDecoder;

/**
 * 二级回复类
 * @author 小妖
 *
 */

public class Comment implements Serializable {

    private String replyTime; // 回复时间
    

    private User replyer; // 回复人
    

    private User replyTo; // 被回复人
    

    private CommentList listTime;
    

    private String content; // 内容
    private String photo; // 图片

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public User getReplyer() {
        return replyer;
    }

    public void setReplyer(User replyer) {
        this.replyer = replyer;
    }

    public User getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(User replyTo) {
        this.replyTo = replyTo;
    }

    public String getContent() {
        return URLDecoder.decode(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public CommentList getListTime() {
        return listTime;
    }

    public void setListTime(CommentList listTime) {
        this.listTime = listTime;
    }

}
