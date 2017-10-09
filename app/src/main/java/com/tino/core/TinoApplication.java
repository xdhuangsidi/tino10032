package com.tino.core;

import android.app.Application;
import android.content.Context;

import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tino.R;
import com.tino.core.net.OkHttpClientManager;
import com.tino.im.utils.Foreground;
import com.tino.im.utils.PushUtil;

import cn.sharesdk.framework.ShareSDK;
import okio.Buffer;

/**
 * Created by root on 17-7-24.
 */

public class TinoApplication extends Application {

    private static TinoApplication instance;
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }



        Foreground.init(instance);

        if(MsfSdkUtils.isMainProcess(instance)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });}
        PushUtil.getInstance();
        TIMManager.getInstance().init(instance);
        TIMManager.getInstance().disableAutoReport();


        OkHttpClientManager.getInstance()
                .setCertificates((new Buffer()
                        .writeUtf8(Cer)
                        .inputStream()));


    }




    public static Context getContext() {
        return instance;
    }
    public static TinoApplication getInstance() {
        return instance;
    }
    public final static String Cer="-----BEGIN CERTIFICATE-----\n" +
            "MIIDSTCCAjGgAwIBAgIEbuWaczANBgkqhkiG9w0BAQsFADBVMQowCAYDVQQGEwFo\n" +
            "MQowCAYDVQQIEwFoMQowCAYDVQQHEwFoMQowCAYDVQQKEwFoMQowCAYDVQQLEwFo\n" +
            "MRcwFQYDVQQDEw4xMTUuMTU5LjQzLjE3ODAeFw0xNzA1MTMwNTQzMTZaFw0xNzA4\n" +
            "MTEwNTQzMTZaMFUxCjAIBgNVBAYTAWgxCjAIBgNVBAgTAWgxCjAIBgNVBAcTAWgx\n" +
            "CjAIBgNVBAoTAWgxCjAIBgNVBAsTAWgxFzAVBgNVBAMTDjExNS4xNTkuNDMuMTc4\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi/LXOIf/rZI4D3E5UC/f\n" +
            "0UY3MmWTOnrZz0pClSMFwsAvFQVG5eq5brAnVHbAJwtgpfftewBDarZFQETobyZe\n" +
            "ozu4PrH8WSTi6xcB3ywQ0Ex+kx4DMaqcwzNkXHePZaZ0WB0H/SDNVSykLQpwzETp\n" +
            "jTDffeBiyVd+T4blvhDJfQWzcTZ6p3j4u0b3pt7bySGnogdCClMGtRF4hyfRTw73\n" +
            "u2XcGDhenJ0W7uJuSfzLc6lXKAQ3mMYsyv9uzelvjSHpf4hdbMs5NXRCsHwoJ7aT\n" +
            "4dkipWuACVbbcAKMwycuS1MqaJMqBhM8/Riv+3MQpzqU8OmiDsQNtrNQ9tYSoOyE\n" +
            "8wIDAQABoyEwHzAdBgNVHQ4EFgQU0GfdLBcxlJU33b6olemV7rEY+MUwDQYJKoZI\n" +
            "hvcNAQELBQADggEBAGUyi+JPysNAskavGD8fC/CsJmNDw3q2p9BqJYd00Jx4GFsy\n" +
            "X7kiq4nYnesv8PgpB7ddIAjnygS0QVLsogApVuWA2NWyMfr7mwlm2SFS+vtDlz+4\n" +
            "dMe7PD7FWoy2iOZUd2Al5o+IwHIbRL1acolzVnP89zOKflM4naTEoVqH+JbUzSWs\n" +
            "mgVv+6fuhiFmi8I5AUfQ715YyIo9q8nlJJG0j3K3haL4Er8TNj4lbIaxDgzA8O8H\n" +
            "BpdtOaWAUnvYcDpogzy4JIsVm7FKaM9N6fU27A8WBDg5YGkI7OU6tztsr4wPIxDL\n" +
            "SC7JXDpsLaWz9a0rObq+fzda1kY6fyMZV0TmCvg=\n" +
            "-----END CERTIFICATE-----" ;
}
