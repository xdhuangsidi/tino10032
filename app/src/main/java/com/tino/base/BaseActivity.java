package com.tino.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.tino.R;


public abstract class BaseActivity extends AppCompatActivity {
   /* public AccountAction accountAction;*/
    public Application application;
    public Context mContext;
    public abstract  void bindView();

    public abstract void init();

    public abstract int setInflateId();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitysManager.getInstanse().add(this);
        this.mContext = this;
        this.application = (Application) getApplication();//(BookApplication)
      /*  this.accountAction = new AccountImpl(this);*/

        setContentView(setInflateId());
        bindView();
        init();
        initAcition();
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivitysManager.getInstanse().killActivity(this);
    }

    public void initAcition() {
    }

    public void applyKitKatTranslucency(int color) {
        applyKitKatTranslucency(getResources().getColor(R.color.colorPrimary));
    }



    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= 67108864;
        } else {
            winParams.flags &= -67108865;
        }
        win.setAttributes(winParams);
    }

    public void setupBackToolbar(String title) {
        setupBackToolbar(R.id.toolbar, title);
    }

    public void setupBackToolbar(int toolbarId, String title) {
        Toolbar mToolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon((int) R.drawable.btn_back1);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BaseActivity.this.onBackPressed();
            }
        });
        setTitle(title);
    }

    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(this, cls);
        int i = 0;
        while (i < objs.length) {
            String str = objs[i];
            i++;
            intent.putExtra(str, objs[i]);
            i++;
        }
        startActivity(intent);
    }

    protected boolean filterException(Exception e) {
        if (e == null) {
            return true;
        }
        e.printStackTrace();
        showToast(e.getMessage());
        return false;
    }
}
