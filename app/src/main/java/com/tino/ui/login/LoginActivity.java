package com.tino.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.net.UploadImp;
import com.tino.utils.CommonUtils;

/**
 * Created by root on 17-7-31.
 */

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        init();
    }
    private void init(){
        PwdLoginFragment fragment1 = new PwdLoginFragment();
        addFragment(fragment1, "fragment1");
    }
    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            if(manager.popBackStackImmediate()){}
                else finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
