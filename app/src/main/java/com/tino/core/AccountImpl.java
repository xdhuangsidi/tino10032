package com.tino.core;

import com.google.gson.reflect.TypeToken;
import com.tino.core.api.ApiResponse;
import com.tino.core.api.Urls;
import com.tino.core.model.Blog;
import com.tino.core.model.CommentList;
import com.tino.core.model.User;
import com.tino.core.net.OkHttpRequest;
import com.tino.core.net.StringCallback;
import com.tino.utils.CommonUtils;

import java.util.List;

public class  AccountImpl {
    public static void loginWithPwd(String username, String passwd, ActionCallback<String> callback) {
        new OkHttpRequest.Builder().url(Urls.LOGIN_PWD_URL).addParams("userId", username).addParams("password", passwd).
              post(new StringCallback(callback));
    }
    public static void loginWithAuth(String username,String code,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.LOGIN_AUTH_URL).addParams("userId", username).addParams("code", code).post(new StringCallback(callback));
    }
    public static void changePwd(String uid,String token,String pwd,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.CHANGE_PASS).addParams("userId", uid).addParams("token",token).addParams("password", pwd).post(new StringCallback(callback));
    }
    public static void addinf(String token,String uid,String avatar,String gender,String name,String signature,String school,String institute,String grade,String profession,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.ADD_INFO).addParams("token",token)
                .addParams("userId",uid).addParams("avatar",avatar)
                .addParams("gender",gender).addParams("userName",name)
                .addParams("signature",signature).addParams("school",school)
                .addParams("grade",grade)
                .addParams("institute",institute).addParams("profession",profession)
                .post(new StringCallback(callback))
        ;
    }

    public static void getinfo(String uid,String token,String who, ActionCallback<User> callback){
        new OkHttpRequest.Builder().url(Urls.GET_USER_INFO)
                .addParams("userId",uid)
                .addParams("token",token)
                .addParams("userIdForMsg",who)
                .post(new ResponseCallback(callback, new TypeToken<ApiResponse<User>>() {
        }.getType()));

    }

    public static void getBlogtList(String token, String uid,int i, ActionCallback<List<Blog>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_BLOG_URL).addParams("token", token).addParams("userId", uid).addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<Blog>>>() {
        }.getType()));
    }
    public static void getBlogtListFromTopic(String token, String topicId,String uid,int i, ActionCallback<List<Blog>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_TOPIC_BLOG_URL)
                .addParams("token", token)
                .addParams("userId", uid)
                .addParams("topicId", topicId)
                .addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<Blog>>>() {
        }.getType()));
    }
    public static void getCollectBlogtList(String token, String uid,int i, ActionCallback<List<Blog>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_COLLECT_BLOG_URL).addParams("token", token).addParams("userId", uid).addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<Blog>>>() {
        }.getType()));
    }
    public static void getComeentList(String  i, ActionCallback<List<CommentList>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_COMMENTLIST_URL)
                .addParams("token", CommonUtils.gettoken())
                .addParams("userId", CommonUtils.getUid())
                .addParams("blogTime",i)
                .post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<CommentList>>>() {
        }.getType()));
    }



    public static void getCareBlogtList(String token, String uid,int i, ActionCallback<List<Blog>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_CARE_BLOG_URL).addParams("token", token).addParams("userId", uid).addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<Blog>>>() {
        }.getType()));
    }
    public static void getMyBlogtList(String token, String uid,int i, ActionCallback<List<Blog>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_MY_BLOG_URL).addParams("token", token).addParams("userId", uid).addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<Blog>>>() {
        }.getType()));
    }
    public static void getCareUsers(String token, String uid,int i, ActionCallback<List<User>> callback) {
        new OkHttpRequest.Builder().url(Urls.GET_CARE_USER_URL).addParams("token", token).addParams("userId", uid).addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<User>>>() {
        }.getType()));
    }
    public static void getCareME(String token, String uid,int i, ActionCallback<List<User>> callback) {

        new OkHttpRequest.Builder().url(Urls.GET_CARE_ME_URL).addParams("token", token).addParams("userId", uid).addParams("startPage",i+"").post(new ResponseCallback(callback, new TypeToken<ApiResponse<List<User>>>() {
        }.getType()));
    }



}