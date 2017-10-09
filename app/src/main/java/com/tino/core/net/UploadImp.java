package com.tino.core.net;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tino.core.ActionCallback;
import com.tino.core.api.Urls;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by root on 17-8-1.
 */
public class UploadImp{

    public static void SaveImageInCosServer(String sig,byte[] image_content,String url,final ActionCallback<String> callback)
    {
        Map<String, String> headers=new IdentityHashMap<>();
        headers.put("Authorization", sig);
        Request.Builder builder = new Request.Builder();
        com.squareup.okhttp.Headers.Builder headerBuilder = new com.squareup.okhttp.Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                headerBuilder.add(key, headers.get(key));}
            builder.headers(headerBuilder.build());}
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),image_content);
        RequestBody requestbody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"op\""),
                        RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),"upload"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"fileContent\""), fileBody)
                .build();
        Request request=  builder.url(url).post(requestbody).build();
        OkHttpClientManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}
            @Override
            public void onResponse(Response response) throws IOException {
                String string = response.body().string().trim();
                callback.onSuccess(string);
            }
        });
    }
    public static void getcosSigFromServer(final ActionCallback<String> callback)
    {
        new OkHttpRequest.Builder().url(Urls.GET_COS_SIG).addParams("a","a").post(new StringCallback(callback));
    }
    public static void getImSigFromServer(String token,String uid,final ActionCallback<String> callback)
    {
        new OkHttpRequest.Builder().url(Urls.GET_IM_SIG).addParams("userId",uid).addParams("token",token).post(new StringCallback(callback));
    }


    public static void uploadblog(String token,String uid,String title,String content,String pic,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.UP_LOAD_BLOG).addParams("token",token)
                .addParams("userId",uid)
                .addParams("title",URLEncoder.encode(title))
                .addParams("content", URLEncoder.encode(content))
                .addParams("pictures",pic)
                .post(new StringCallback(callback));
    }
    public static void uploadTopicblog(String token,String uid,String title,String topicid,String content,String pic,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.UP_LOAD_BLOG).addParams("token",token)
                .addParams("userId",uid)
                .addParams("title",URLEncoder.encode(title))
                .addParams("content",URLEncoder.encode(content))
                .addParams("pictures",pic)
                .addParams("topicId",topicid)
                .post(new StringCallback(callback));
    }

    public static void addReply(String token,String uid,String sid,String content,String pic,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.ADD_REPLY_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("blogId",sid)
                .addParams("content",URLEncoder.encode(content))
                .post(new StringCallback(callback));

    }
    public static void addsubReply(String token,String uid,String listId,String replyTo,String content,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.ADD_SubREPLY_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("listId",listId)
                .addParams("content",URLEncoder.encode(content))
                .addParams("replyToId",replyTo)
                .post(new StringCallback(callback));

    }

    public static void likeBlog(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.LIKE_BLOG_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("blogTime",sid)
                .post(new StringCallback(callback));

    }
    public static void dislikeBlog(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.DISLIKE_BLOG_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("blogTime",sid)

                .post(new StringCallback(callback));

    }
    public static void collectblog(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.COLLECT_BLOG_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("blogId",sid)
                .post(new StringCallback(callback));

    }

    public static void deleteblog(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.DELETE_BLOG_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("blogTime",sid)
                .post(new StringCallback(callback));

    }
    public static void deleteCommentList(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.DELETE_COMMENT_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("listTime",sid)
                .post(new StringCallback(callback));

    }

    public static void uncollectblog(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.UNCOLLECT_BLOG_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("blogId",sid)
                .post(new StringCallback(callback));
    }
    public static void careUser(String token,String uid,String beCared,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.CARE_USER_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("beCared",beCared)
                .post(new StringCallback(callback));
    }
    public static void uncareUser(String token,String uid,String sid,ActionCallback<String> callback){
        new OkHttpRequest.Builder().url(Urls.UNCARE_USER_URL)
                .addParams("token",token)
                .addParams("userId",uid)
                .addParams("beCared",sid)
                .post(new StringCallback(callback));
    }

}
