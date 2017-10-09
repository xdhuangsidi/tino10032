package com.tino.core.net;

import android.text.TextUtils;
import android.util.Pair;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

public abstract class OkHttpRequest {
    protected Map<String, String> headers;

    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient = this.mOkHttpClientManager.getOkHttpClient();
    protected Map<String, String> params;
    protected Request request;
    protected RequestBody requestBody;
    protected String tag;
    protected String url;

    public static class Builder {
        private byte[] bytes;
        private String content;
        private String destFileDir;
        private String destFileName;
        private int errorResId = -1;
        private File file;
        private Pair<String, File>[] files;
        private Map<String, String> headers;
        private ImageView imageView;
        private Map<String, String> params;
        private String tag;
        private String url;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val) {
            if (this.params == null) {
                this.params = new IdentityHashMap();
            }
            this.params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val) {
            if (this.headers == null) {
                this.headers = new IdentityHashMap();
            }
            this.headers.put(key, val);
            return this;
        }

        public Builder files(Pair<String, File>... files) {
            this.files = files;
            return this;
        }

        public Builder destFileName(String destFileName) {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir) {
            this.destFileDir = destFileDir;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder errResId(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public <T> T get(Class<T> clazz) throws IOException {
            return new OkHttpGetRequest(this.url, this.tag, this.params, this.headers).invoke(clazz);
        }

        public OkHttpRequest get(ResultCallback callback) {
            OkHttpRequest request = new OkHttpGetRequest(this.url, this.tag, this.params, this.headers);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T post(Class<T> clazz) {
            return null;
           // return new OkHttpPostRequest(this.url, this.tag, this.params, this.headers, this.content, this.bytes, this.file).invoke(clazz);
        }

        public OkHttpRequest post(ResultCallback callback) {
            System.out.println("begin connect to net");
            OkHttpRequest request = new OkHttpPostRequest(this.url, this.tag, this.params, this.headers, this.content, this.bytes, this.file);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest upload(ResultCallback callback) {
            OkHttpRequest request = new OkHttpUploadRequest(this.url, this.tag, this.params, this.headers, this.files);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T upload(Class<T> clazz) throws IOException {
            return new OkHttpUploadRequest(this.url, this.tag, this.params, this.headers, this.files).invoke(clazz);
        }

        public OkHttpRequest download(ResultCallback callback) {
            OkHttpRequest request = new OkHttpDownloadRequest(this.url, this.tag, this.params, this.headers, this.destFileName, this.destFileDir);
            request.invokeAsyn(callback);
            return request;
        }

        public String download()throws IOException {
            return (String) new OkHttpDownloadRequest(this.url, this.tag, this.params, this.headers, this.destFileName, this.destFileDir).invoke(String.class);
        }

        public void displayImage(ResultCallback callback) {
            new OkHttpDisplayImgRequest(this.url, this.tag, this.params, this.headers, this.imageView, this.errorResId).invokeAsyn(callback);
        }
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    protected OkHttpRequest(String url, String tag, Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
    }

    protected void prepareInvoked(ResultCallback callback) {
        this.requestBody = buildRequestBody();
        this.requestBody = wrapRequestBody(this.requestBody, callback);
        this.request = buildRequest();
    }

    public void invokeAsyn(ResultCallback callback) {
        prepareInvoked(callback);
        //this.mOkHttpClientManager.execute(this.request, callback);
        this.mOkHttpClientManager.execute(this.request,callback);


    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, ResultCallback callback) {
        return requestBody;
    }

    public <T> T invoke(Class<T> clazz) throws IOException {
        return (T)this.mOkHttpClientManager.execute(buildRequest(), (Class) clazz);
    }

    protected void appendHeaders(com.squareup.okhttp.Request.Builder builder, Map<String, String> headers) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be empty!");
        }
        com.squareup.okhttp.Headers.Builder headerBuilder = new com.squareup.okhttp.Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                headerBuilder.add(key, (String) headers.get(key));
            }
            builder.headers(headerBuilder.build());
        }
    }

    public void cancel() {
        if (!TextUtils.isEmpty(this.tag)) {
            this.mOkHttpClientManager.cancelTag(this.tag);
        }
    }
}
