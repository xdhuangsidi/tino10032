package com.tino.core.net;

import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Map;

public class OkHttpPostRequest extends OkHttpRequest {
    private static final int TYPE_BYTES = 3;
    private static final int TYPE_FILE = 4;
    private static final int TYPE_PARAMS = 1;
    private static final int TYPE_STRING = 2;
    private final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    private final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    private byte[] bytes;
    private String content;
    private File file;
    private int type = 0;

    protected OkHttpPostRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, String content, byte[] bytes, File file) {
        super(url, tag, params, headers);
        this.content = content;
        this.bytes = bytes;
        this.file = file;
        validParams();
    }

    private void validParams() {
        int count = 0;
        if (!(this.params == null || this.params.isEmpty())) {
            this.type = 1;
            count = 0 + 1;
        }
        if (this.content != null) {
            this.type = 2;
            count++;
        }
        if (this.bytes != null) {
            this.type = 3;
            count++;
        }
        if (this.file != null) {
            this.type = 4;
            count++;
        }
        if (count <= 0 || count > 1) {
            throw new IllegalArgumentException("the params , content , file , bytes must has one and only one .");
        }
    }

    protected Request buildRequest() {
        if (TextUtils.isEmpty(this.url)) {
            throw new IllegalArgumentException("url can not be empty!");
        }
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, this.headers);
        builder.url(this.url).tag(this.tag).post(this.requestBody);
        return builder.build();
    }

    protected RequestBody buildRequestBody() {
        switch (this.type) {
            case 1:
                FormEncodingBuilder builder = new FormEncodingBuilder();
                addParams(builder, this.params);
                return builder.build();
            case 2:
                return RequestBody.create(this.MEDIA_TYPE_STRING, this.content);
            case 3:
                return RequestBody.create(this.MEDIA_TYPE_STREAM, this.bytes);
            case 4:
                return RequestBody.create(this.MEDIA_TYPE_STREAM, this.file);
            default:
                return null;
        }
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback) {
        return new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            public void onRequestProgress(long bytesWritten, long contentLength) {
                final long j = bytesWritten;
                final long j2 = contentLength;
                OkHttpPostRequest.this.mOkHttpClientManager.getDelivery().post(new Runnable() {
                    public void run() {
                        callback.inProgress((((float) j) * 1.0f) / ((float) j2));
                    }
                });
            }
        });
    }

    private void addParams(FormEncodingBuilder builder, Map<String, String> params) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be null .");
        } else if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, (String) params.get(key));
            }
        }
    }
}
