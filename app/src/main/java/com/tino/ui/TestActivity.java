package com.tino.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tino.R;
import com.tino.adapter.CircleViewAdapter;
import com.tino.core.model.User;
import com.tino.ui.home.UserDetialActivity;
import com.tino.ui.me.EditSetActivity;
import com.tino.utils.Constants;
import com.tino.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-17.
 */

public class TestActivity  extends Activity{


    CircleImageView circleImageView;
    AppCompatEditText tv_sig,tv_name,tv_gender,tv_institute,tv_school,tv_profession,tv_grade;
    public static void navToDetail(Context context, final User user/*, String identify, TIMConversationType type, String senderfaceUrl*/){
        Intent intent = new Intent(context, TestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        setContentView(R.layout.activity_user_info);
        bindView();
        init();



    }
    private  void bindView(){
        circleImageView=(CircleImageView) findViewById(R.id.avatar_user);
        tv_sig=(AppCompatEditText)findViewById(R.id.et_signature);
        tv_name=(AppCompatEditText)findViewById(R.id.et_username);
        tv_gender=(AppCompatEditText)findViewById(R.id.et_gender);
        tv_institute=(AppCompatEditText)findViewById(R.id.et_institute);

        tv_profession=(AppCompatEditText)findViewById(R.id.et_professional);
        tv_grade=(AppCompatEditText)findViewById(R.id.et_grade);


    }
    private void init(){
        User user=(User)getIntent().getSerializableExtra("user");
        Glide.with(this).load(user.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(circleImageView);
        tv_name.setText(user.getUserName());
        tv_grade.setText(Constants.garde[Integer.parseInt(user.getGrade())]);
        tv_gender.setText(user.getGender());
        tv_sig.setText(user.getSignature());
        tv_institute.setText(Constants.xueyuan[Integer.parseInt(user.getInstitute())]);
        tv_profession.setText(Constants.zhuanye[Integer.parseInt(user.getInstitute())][Integer.parseInt(user.getProfession())]);
    }
}
