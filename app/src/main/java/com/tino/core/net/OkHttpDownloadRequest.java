package com.tino.core.net;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class OkHttpDownloadRequest extends OkHttpGetRequest {
    private String destFileDir;
    private String destFileName;

    protected OkHttpDownloadRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, String destFileName, String destFileDir) {
        super(url, tag, params, headers);
        this.destFileName = destFileName;
        this.destFileDir = destFileDir;
    }

    public void invokeAsyn(final ResultCallback callback) {
        prepareInvoked(callback);
        this.mOkHttpClient.newCall(this.request).enqueue(new Callback() {
            public void onFailure(Request request, IOException e) {
                OkHttpDownloadRequest.this.mOkHttpClientManager.sendFailResultCallback(request, e, callback);
            }

            public void onResponse(Response response) {
                try {
                    OkHttpClientManager.getInstance().sendSuccessResultCallback(OkHttpDownloadRequest.this.saveFile(response, callback), callback);
                } catch (IOException e) {
                    e.printStackTrace();
                    OkHttpClientManager.getInstance().sendFailResultCallback(response.request(), e, callback);
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return separatorIndex < 0 ? path : path.substring(separatorIndex + 1, path.length());
    }

    public <T> T invoke(Class<T> cls) throws IOException {
        return (T)saveFile(this.mOkHttpClient.newCall(this.request).execute(), null);
    }

    public String saveFile(Response response, ResultCallback callback) throws IOException {
        Throwable th;
        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            File dir = new File(this.destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, this.destFileName);
            FileOutputStream fos2 = new FileOutputStream(file);
            while (true) {
                try {
                    int len = is.read(buf);
                    if (len == -1) {
                        break;
                    }
                    sum += (long) len;
                    fos2.write(buf, 0, len);
                    if (callback != null) {
                        final long finalSum = sum;
                        final ResultCallback resultCallback = callback;
                        this.mOkHttpClientManager.getDelivery().post(new Runnable() {
                            public void run() {
                                resultCallback.inProgress((((float) finalSum) * 1.0f) / ((float) total));
                            }
                        });
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fos = fos2;
                }
            }
            fos2.flush();
            String absolutePath = file.getAbsolutePath();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (fos2 != null) {
                try {
                    fos2.close();
                } catch (IOException e2) {
                }
            }
            return absolutePath;
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e4) {
                }
            }
           return  null;
        }
    }
}