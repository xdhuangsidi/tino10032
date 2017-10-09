package com.tino.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tino.R;


public abstract class BaseFragment extends Fragment implements OnTouchListener {
    private View rootView;

    public abstract void init();

    public abstract int setInflateId();
    public abstract void bindview(View view);
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(setInflateId(), container, false);
            bindview(rootView);
            this.rootView.setOnTouchListener(this);


        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return this.rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //ButterKnife.bind((Object) this, this.rootView);

        init();
    }

    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    protected void showToast(String str) {
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        }
    }

    public void startActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(getActivity(), cls);
        int i = 0;
        while (i < objs.length) {
            String str = objs[i];
            i++;
            intent.putExtra(str, objs[i]);
            i++;
        }
        startActivity(intent);
    }

    public void setupBackToolbar(String title) {
        //setupBackToolbar(R.id.toolbar, title);
    }

    public void setupBackToolbar(int toolbarId, String title) {
        Toolbar mToolbar = (Toolbar) this.rootView.findViewById(toolbarId);
        mToolbar.setNavigationIcon((int) R.drawable.btn_back1);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BaseFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mToolbar.setTitle((CharSequence) title);
    }

    public void checkAndSetTextView(TextView tv, String text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
    }

    protected boolean filterException(Exception e) {
        if (e == null) {
            return true;
        }
        e.printStackTrace();
        if (e.getMessage().contains("Connection Lost")) {
            showToast("网络连接失败");
        }
        return false;
    }
}
