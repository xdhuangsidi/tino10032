package com.tino.core.net;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tino.core.TinoApplication;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private Handler mDelivery;
    private Gson mGson;
    private OkHttpClient mOkHttpClient = new OkHttpClient();

    private class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            this.defaultTrustManager = OkHttpClientManager.this.chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                this.defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException e) {
                this.localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private OkHttpClientManager() {
        this.mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        this.mDelivery = new Handler(Looper.getMainLooper());
        this.mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return this.mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return this.mOkHttpClient;
    }

    public void execute(Request request, ResultCallback callback) {
        if (callback == null) {
            System.out.println("no ResultCallback the object =null  return");
                     return;
         }
        final ResultCallback resCallBack = callback;
        resCallBack.onBefore(request);
        this.mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Request request, IOException e) {

                OkHttpClientManager.this.sendFailResultCallback(request, e, resCallBack);
            }

            public void onResponse(Response response) {
                try {
                    String string = response.body().string().trim();
                    System.out.println(string);
                    if (resCallBack.mType == String.class) {
                        OkHttpClientManager.this.sendSuccessResultCallback(string, resCallBack);
                        return;
                    }
                    System.out.println(resCallBack.mType);


                    OkHttpClientManager.this.sendSuccessResultCallback(OkHttpClientManager.this.mGson.fromJson(string, resCallBack.mType), resCallBack);
                } catch (IOException e) {
                    OkHttpClientManager.this.sendFailResultCallback(response.request(), e, resCallBack);
                } catch (JsonParseException e2) {
                    OkHttpClientManager.this.sendFailResultCallback(response.request(), e2, resCallBack);
                }
            }
        });
    }

    public <T> T execute(Request request, Class<T> clazz) throws IOException {
        return (T)new Gson().fromJson(this.mOkHttpClient.newCall(request).execute().body().string().trim(), (Class) clazz);
    }


    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback) {
        if (callback != null) {
            this.mDelivery.post(new Runnable() {
                public void run() {
                    callback.onError(request, e);
                    callback.onAfter();
                }
            });
        }
    }

    public void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        if (callback != null) {
            this.mDelivery.post(new Runnable() {
                public void run() {

                    callback.onResponse(object);
                    callback.onAfter();
                }
            });
        }
    }

    public void cancelTag(Object tag) {
        this.mOkHttpClient.cancel(tag);
    }

    public void setCertificates(InputStream... certificates) {
        setCertificates(certificates, null, null);
        mOkHttpClient.setHostnameVerifier(new HostnameVerifier(){
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        } );
    }

    private TrustManager[] prepareTrustManager(InputStream... certificates) {
        TrustManager[] trustManagerArr = null;
        if (certificates != null && certificates.length > 0) {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                int length = certificates.length;
                int i = 0;
                int index = 0;
                while (i < length) {
                    InputStream certificate = certificates[i];
                    int index2 = index + 1;
                    keyStore.setCertificateEntry(Integer.toString(index), certificateFactory.generateCertificate(certificate));
                    if (certificate != null) {
                        try {
                            certificate.close();
                        } catch (IOException e) {
                        }
                    }
                    i++;
                    index = index2;
                }
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                trustManagerArr = trustManagerFactory.getTrustManagers();
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            } catch (CertificateException e3) {
                e3.printStackTrace();
            } catch (KeyStoreException e4) {
                e4.printStackTrace();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        return trustManagerArr;
    }

    private KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        KeyManager[] keyManagerArr = null;
        if (!(bksFile == null || password == null)) {
            try {
                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());
                keyManagerArr = keyManagerFactory.getKeyManagers();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            } catch (UnrecoverableKeyException e3) {
                e3.printStackTrace();
            } catch (CertificateException e4) {
                e4.printStackTrace();
            } catch (IOException e5) {
                e5.printStackTrace();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
        }
        return keyManagerArr;
    }

    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
            this.mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e2) {
            e2.printStackTrace();
        }
    }
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }
    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }
}
