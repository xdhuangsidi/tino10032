package com.tino.core.model;


import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by root on 17-8-3.
 */


import java.net.URLDecoder;
import java.util.List;
import java.util.Set;



/**
 * 帖子实体类
 * @author 小妖
 *
 */

public class Blog implements Serializable{

    private String blogTime; // 发布时间 -- 主键


    private User owner; // 发布人


    private Topic topic; // 所属话题, 多对一, 外键


    private Set<CommentList> commentList; // 回复, 一对多, CommentList储存外键


    private String content; // 内容


    private String pictures; // 帖子图片, json储存url
    private boolean islike;
    private boolean iscollect;
    private String title; // 题目
    private int commentCounts; // 评论数
    private int likeCounts; // 点赞数
    private List<String> likeUser;
    private List<String> collectUser;

    public List<User> getLikeUserInfo() {
        return likeUserInfo;
    }

    public void setLikeUserInfo(List<User> likeUserInfo) {
        this.likeUserInfo = likeUserInfo;
    }

    private List<User> likeUserInfo;
    public boolean isIslike() {
        return islike;
    }

    public void setIslike(boolean islike) {
        this.islike = islike;
    }

    public boolean isIscollect() {
        return iscollect;
    }

    public void setIscollect(boolean iscollect) {
        this.iscollect = iscollect;
    }

    public List<String> getCollectUser() {
        return collectUser;
    }

    public void setCollectUser(List<String> collectUser) {
        this.collectUser = collectUser;
    }

    public List<String> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<String> likeUser) {
        this.likeUser = likeUser;
    }

    public Blog() {}

    public Blog(String blogTime) {
        this.blogTime = blogTime;
    }

    public String getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(String blogTime) {
        this.blogTime = blogTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return URLDecoder.decode(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return URLDecoder.decode(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Set<CommentList> getCommentList() {
        return commentList;
    }

    public void setCommentList(Set<CommentList> commentList) {
        this.commentList = commentList;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public int getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(int likeCounts) {
        this.likeCounts = likeCounts;
    }
    public void setlikeAndCollect(String uid){
        for(String s:this.getCollectUser()){
            if(uid.equals(s))this.iscollect=true;
        }
        for(String s:this.getLikeUser()){
            if(uid.equals(s))this.islike=true;
        }
    }    public void setlike(String uid){

        for(User s:this.getLikeUserInfo()){
            if(uid.equals(s.getUserId()))this.islike=true;
        }
    }

}
