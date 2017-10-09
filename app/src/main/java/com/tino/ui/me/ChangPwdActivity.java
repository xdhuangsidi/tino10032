package com.tino.ui.me;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tino.R;
import com.tino.ui.login.PwdLoginFragment;

/**
 * Created by root on 17-10-3.
 */

public class ChangPwdActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        init();
    }
    private void init(){
        ChangPwdFragment fragment1 = new ChangPwdFragment();
        addFragment(fragment1, "fragment1");
    }
    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }
}
