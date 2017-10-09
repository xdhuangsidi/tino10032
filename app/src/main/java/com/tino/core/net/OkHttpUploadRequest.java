package com.tino.core.net;

import android.util.Pair;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.net.URLConnection;
import java.util.Map;

public class OkHttpUploadRequest extends OkHttpPostRequest {
    private Pair<String, File>[] files;

    protected OkHttpUploadRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, Pair<String, File>[] files) {
        super(url, tag, params, headers, null, null, null);
        this.files = files;
    }

    private void addParams(MultipartBuilder builder, Map<String, String> params) {

        if (builder == null) {
            throw new IllegalArgumentException("builder can not be null .");
        } else if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                String[] strArr = new String[]{"Content-Disposition", "form-data; name=\"" + key + "\""};
                builder.addPart(Headers.of(strArr), RequestBody.create(null,key));
            }
        }
    }

    public RequestBody buildRequestBody() {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        addParams(builder, this.params);
        if (this.files != null) {
            for (Pair<String, File> filePair : this.files) {
                String fileKeyName = filePair.first;
                File file = filePair.second;
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileKeyName + "\"; filename=\"" + file.getName()/*hava changd it*/+ "\""), RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
            }
        }
        return builder.build();
    }

    private String guessMimeType(String path) {
        String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(path);
        if (contentTypeFor == null) {
            return "application/octet-stream";
        }
        return contentTypeFor;
    }
}
