package com.tino.core.model;


import java.io.Serializable;
import java.util.List;

/**
 * 用户信息类
 * @author 小妖
 *
 */

public class User implements Serializable {

    private String userId; // 用户id 一般为手机号
    private String avatar; // 头像url
    private String userName; // 昵称
    private String brief; // 用户身份简介
    private String signature; // 签名
    private String gender; // 性别
    private String backPhoto; // 背景图
    private String institute; // 学院
    private String school; // 学校
    private String profession; // 专业
    private String grade; // 年级
    private String password;
    private String token;

    
    public User() {
    }

    
    public User(String userId) {
        super();
        this.userId = userId;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBackPhoto() {
        return backPhoto;
    }

    public void setBackPhoto(String backPhoto) {
        this.backPhoto = backPhoto;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


   

    /*public String toJson(){
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();
        list.add(brief);
        list.add(gender);
        list.add(grade);
        list.add(institute);
    }*/
    
}
