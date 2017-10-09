package com.tino.core.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class OkHttpDisplayImgRequest extends OkHttpGetRequest {
    private int errorResId;
    private ImageView imageview;

    protected OkHttpDisplayImgRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, ImageView imageView, int errorResId) {
        super(url, tag, params, headers);
        this.imageview = imageView;
        this.errorResId = errorResId;
    }

    private void setErrorResId() {
        this.mOkHttpClientManager.getDelivery().post(new Runnable() {
            public void run() {
                OkHttpDisplayImgRequest.this.imageview.setImageResource(OkHttpDisplayImgRequest.this.errorResId);
            }
        });
    }

    public void invokeAsyn(final ResultCallback callback) {
        prepareInvoked(callback);
        this.mOkHttpClient.newCall(this.request).enqueue(new Callback() {
            public void onFailure(Request request, IOException e) {
                OkHttpDisplayImgRequest.this.setErrorResId();
                OkHttpDisplayImgRequest.this.mOkHttpClientManager.sendFailResultCallback(request, e, callback);
            }

            public void onResponse(Response response) {
                InputStream is = null;
                try {
                    is = response.body().byteStream();
                    int inSampleSize = ImageUtils.calculateInSampleSize(ImageUtils.getImageSize(is), ImageUtils.getImageViewSize(OkHttpDisplayImgRequest.this.imageview));
                    try {
                        is.reset();
                    } catch (IOException e) {
                        is = OkHttpDisplayImgRequest.this.getInputStream().body().byteStream();
                    }
                    Options ops = new Options();
                    ops.inJustDecodeBounds = false;
                    ops.inSampleSize = inSampleSize;
                    final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
                    OkHttpDisplayImgRequest.this.mOkHttpClientManager.getDelivery().post(new Runnable() {
                        public void run() {
                            OkHttpDisplayImgRequest.this.imageview.setImageBitmap(bm);
                        }
                    });
                    OkHttpDisplayImgRequest.this.mOkHttpClientManager.sendSuccessResultCallback(OkHttpDisplayImgRequest.this.request, callback);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                        }
                    }
                } catch (Exception e3) {
                    OkHttpDisplayImgRequest.this.setErrorResId();
                    OkHttpDisplayImgRequest.this.mOkHttpClientManager.sendFailResultCallback(OkHttpDisplayImgRequest.this.request, e3, callback);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e4) {
                        }
                    }
                } catch (Throwable th) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e5) {
                        }
                    }
                }
            }
        });
    }

    private Response getInputStream() throws IOException {
        return this.mOkHttpClient.newCall(this.request).execute();
    }

    public <T> T invoke(Class<T> cls) throws IOException {
        return null;
    }
}
