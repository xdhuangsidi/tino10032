package com.tino.core.net;

import android.text.TextUtils;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

public class OkHttpGetRequest extends OkHttpRequest {
    protected OkHttpGetRequest(String url, String tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    protected Request buildRequest() {
        if (TextUtils.isEmpty(this.url)) {
            throw new IllegalArgumentException("url can not be empty!");
        }
        this.url = appendParams(this.url, this.params);
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, this.headers);
        builder.url(this.url).tag(this.tag);
        return builder.build();
    }

    protected RequestBody buildRequestBody() {
        return null;
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (!(params == null || params.isEmpty())) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append((String) params.get(key)).append("&");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
