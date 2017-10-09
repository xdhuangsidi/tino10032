package com.tino.core.model;

import java.io.Serializable;
import java.util.Set;



/**
 * 话题类
 * @author 小妖
 *
 */

public class Topic implements Serializable{

    private String topicTime;// 创建时间 -- 主键
    

    private Set<Blog> blogs;// 包含的帖子, 一对多
    
    private String theme;// 话题的主题

    public String getTopicTime() {
        return topicTime;
    }

    public void setTopicTime(String topicTime) {
        this.topicTime = topicTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Set<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(Set<Blog> blogs) {
        this.blogs = blogs;
    }

}
