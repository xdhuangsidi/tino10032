package com.tino.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.tencent.TIMConversationType;
import com.tino.R;
import com.tino.adapter.BlogViewAdapter;
import com.tino.adapter.PhotoAdapter;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.model.User;
import com.tino.core.net.CommonCallback;

import com.tino.ui.PhotoViewActivity;
import com.tino.ui.TestActivity;
import com.tino.ui.me.NetImageViewActivity;
import com.tino.ui.message.ChatActivity;
import com.tino.ui.message.ImageViewActivity;
import com.tino.utils.CommonHelper;
import com.tino.utils.CommonUtils;
import com.tino.utils.Constants;
import com.tino.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-19.
 */

public class UserDetialActivity extends FragmentActivity {

    User user;
    ViewPager viewPager;
    SlidingTabLayout slide_tab;
    CircleImageView circleImageView;
    ImageView imageView;
    ImageView ic_chat,ic_care,ic_info;
    TextView usernametext,institutetext,signaturetext;
    public boolean iscare=false;

    public static void navToDetail(Context context, final User user/*, String identify, TIMConversationType type, String senderfaceUrl*/){
        Intent intent = new Intent(context, UserDetialActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_detail);
        bindview();
        init();
    }

    private void bindview() {
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        slide_tab=(SlidingTabLayout)findViewById(R.id.slide_tab);
        circleImageView=(CircleImageView)findViewById(R.id.iv_avatar);
        imageView=(ImageView)findViewById(R.id.sex);
        usernametext=(TextView)findViewById(R.id.me_username);
        institutetext=(TextView)findViewById(R.id.me_institute);
        signaturetext=(TextView)findViewById(R.id.me_signature);
        ic_chat=(ImageView)findViewById(R.id.ic_user_chat);
        ic_care=(ImageView)findViewById(R.id.ic_user_care);
        ic_info=(ImageView)findViewById(R.id.me_setting);
    }
    private void init(){
        user=(User)getIntent().getSerializableExtra("user");
        List<Fragment> fragmentList = new ArrayList();
        fragmentList.add(new BlogCollectgement());
        fragmentList.add(new MyBlogfragement());
        fragmentList.add(new CareUsergement());
        fragmentList.add(new CareMegement());
        List<String> titles = new ArrayList();
        titles.add("Ta的收藏");
        titles.add("Ta的文章");
        titles.add("Ta的关注");
        titles.add("关注Ta的");
        AccountImpl.getCareUsers(CommonUtils.gettoken(), CommonUtils.getUid(), 0, new ActionCallback<List<User>>() {
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

            }

            @Override
            public void onFailure(String str, String str2) {

            }

            @Override
            public void onSuccess(List<User> users) {
                if(users==null||users.size()<=0){
                    iscare=false;
                    return;
                }
                   for(User s:users){
                       if(s.getUserId().equals(user.getUserId())){
                           iscare=true;
                           ic_care.setImageResource(R.drawable.icon_care);

                       }
                   }
            }
        });
        viewPager.setAdapter(new BlogViewAdapter(getSupportFragmentManager(), fragmentList, titles));
        this.slide_tab.setViewPager(this.viewPager/*, titles*/);

        user=(User)getIntent().getSerializableExtra("user");
        ic_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatActivity.navToChat(UserDetialActivity.this,user.getUserId(),TIMConversationType.C2C
                ,user.getAvatar(),user.getUserName());
            }
        });
        ic_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonHelper.toggleCare(ic_care, user.getUserId(), iscare, new CommonCallback() {
                    @Override
                    public void onSuccess(boolean i) {
                        iscare=i;
                        if(i){
                            ic_care.setImageResource(R.drawable.icon_care);
                        }
                        else  ic_care.setImageResource(R.drawable.icon_uncare);
                    }
                });
            }
        });
        ic_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.navToDetail(UserDetialActivity.this,user);
            }
        });
        Glide.with(this).load(user.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetialActivity.this, NetImageViewActivity.class);
                intent.putExtra("url", user.getAvatar());
                startActivity(intent);
            }
        });
        usernametext.setText(user.getUserName());
        if(user.getInstitute()!=null)
            institutetext.setText(Constants.xueyuan[Integer.parseInt(user.getInstitute())]);
        else institutetext.setText(Constants.xueyuan[0]);
        signaturetext.setText(user.getSignature());
        if ("男".equals(user.getGender())) {
            Glide.with(this).load(R.drawable.ic_boy).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else
            Glide.with(this).load(R.drawable.ic_girl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);


    }

}
