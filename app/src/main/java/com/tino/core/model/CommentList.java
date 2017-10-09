package com.tino.core.model;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Set;



/**
 * 一级回复类
 * @author 小妖
 *
 */

public class CommentList implements Serializable {

    private String listTime; // 主键 -- 回复时间
    

    private Blog blogTime; // 帖子ID, 多对一, 外键. 不生成外键
    

    private User commenter; // 回复人, 多对一, 外键
    

    private String content; // 内容

    private Set<Comment> comments;// 所有回复
    
    private String photo; // 图片

    public CommentList() {
        super();
    }

    public CommentList(String listTime) {
        super();
        this.listTime = listTime;
    }

    public String getListTime() {
        return listTime;
    }

    public void setListTime(String listTime) {
        this.listTime = listTime;
    }

    public Blog getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(Blog blogTime) {
        this.blogTime = blogTime;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

}
