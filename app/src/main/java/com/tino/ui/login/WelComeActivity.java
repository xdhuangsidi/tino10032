package com.tino.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.api.Constants;
import com.tino.core.model.User;
import com.tino.utils.CommonUtils;

/**
 * Created by root on 17-8-20.
 */

public class  WelComeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        String token = CommonUtils.gettoken(this);
        String uid = CommonUtils.getUid(this);
        if (token.length() > 4 && CommonUtils.isMobileNO(uid)) {

            AccountImpl.getinfo(uid, token, uid, new ActionCallback<User>() {
                @Override
                public void inProgress(float f) {

                }

                @Override
                public void onAfter() {

                }

                @Override
                public void onBefore() {

                }

                @Override
                public void onError() {
                    Toast.makeText(TinoApplication.getInstance(), "连接服务器失败,自动退出\n", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(String str, String str2) {

                }

                @Override
                public void onSuccess(User user) {

                    if(user==null){
                        startActivity(new Intent(WelComeActivity.this, LoginActivity.class));
                        CommonUtils.savetoken("",WelComeActivity.this);
                        CommonUtils.saveUid("",WelComeActivity.this);
                        finish();
                        return;
                    }
                    Constants.user=user;
                    MainActivity.NavtoMain(WelComeActivity.this, user);
                }
            });

        } else {
            try {
                Thread.sleep(1);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
