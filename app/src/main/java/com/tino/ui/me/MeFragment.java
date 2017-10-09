package com.tino.ui.me;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.tino.R;
import com.tino.adapter.BlogViewAdapter;
import com.tino.adapter.ViewsPageAdapter;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.model.Blog;
import com.tino.core.model.User;
import com.tino.ui.home.BloglistFragement;
import com.tino.ui.home.UserDetialActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.Constants;
import com.tino.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;


public class MeFragment extends Fragment {



    BroadcastReceiver bReceiver;
    IntentFilter iFilter;

    private View rootView;
    ViewPager viewPager;
    SlidingTabLayout slide_tab;
    CircleImageView circleImageView;
    ImageView imageView;
    ImageView setting,share;
    TextView usernametext,institutetext,signaturetext;


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.fragment_me_detail, container, false);

            viewPager=(ViewPager)rootView.findViewById(R.id.viewPager_me_detail);
            slide_tab=(SlidingTabLayout)rootView.findViewById(R.id.slide_tab);
            setting=(ImageView)rootView.findViewById(R.id.me_setting);
            init();

             //关注文章  我的文章 关注的人  关注我的
            List<Fragment> fragmentList = new ArrayList();
            fragmentList.add(new BlogCollectgement());
            fragmentList.add(new MyBlogfragement());
            fragmentList.add(new CareUsergement());
            fragmentList.add(new CareMegement());
            List<String> titles = new ArrayList();
            titles.add("收藏文章");
            titles.add("我的文章");
            titles.add("关注的人");
            titles.add("关注我的");

            viewPager.setAdapter(new BlogViewAdapter(getFragmentManager(), fragmentList, titles));
            this.slide_tab.setViewPager(this.viewPager/*, titles*/);

            share=(ImageView)rootView.findViewById(R.id.me_share) ;
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnekeyShare one=new OnekeyShare();
                    one.setText("我在使用tino，这是下载链接，欢迎下载使用");
                    one.setTitle("Tino");
                    one.setTitleUrl("http://tino-1254096761.cossh.myqcloud.com/tino.apk");
                    
                    one.setComment("这是我通过android程序里的代码分享的测试分享");
                    one.show(view.getContext());
                }
            });




        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
            getData();
        }
        return this.rootView;
    }

    private void init() {


        iFilter=new IntentFilter();
        iFilter.addAction("tino.reflash_userinfo");
        bReceiver=new UserinfoReceiver();
        getActivity().registerReceiver(bReceiver, iFilter);


        circleImageView=(CircleImageView)rootView.findViewById(R.id.iv_avatar);
        imageView=(ImageView)rootView.findViewById(R.id.sex);

        usernametext=(TextView)rootView.findViewById(R.id.me_username);
        institutetext=(TextView)rootView.findViewById(R.id.me_institute);
        signaturetext=(TextView)rootView.findViewById(R.id.me_signature);
        if(com.tino.core.api.Constants.user==null){
            getData();
            return;
        }


        final User user=(User)getActivity().getIntent().getSerializableExtra("user");
        Glide.with(getActivity()).load(user.getAvatar())
             //   .placeholder(R.mipmap.avatar_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NetImageViewActivity.class);
                intent.putExtra("url", user.getAvatar());
                getActivity().startActivity(intent);
            }
        });
        usernametext.setText(user.getUserName());
        if(user.getInstitute()!=null)
        institutetext.setText(Constants.xueyuan[Integer.parseInt(user.getInstitute())]);
        else institutetext.setText(Constants.xueyuan[0]);
        signaturetext.setText(user.getSignature());
        if ("男".equals(user.getGender())) {
            Glide.with(getActivity()).load(R.drawable.ic_boy).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else
            Glide.with(getActivity()).load(R.drawable.ic_girl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        setting.setClickable(true);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditSetActivity.navToActiivity(getActivity(), user.getGender(),user.getAvatar()
                        , user.getUserName(), user.getSignature());
            }
        });


    }

    private void getData() {
        AccountImpl.getinfo(CommonUtils.getUid(getActivity()), CommonUtils.gettoken(getActivity()),CommonUtils.getUid(getActivity()), new ActionCallback<User>() {
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
            public void onSuccess(final User meInfo) {
                setting.setClickable(true);
                setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditSetActivity.navToActiivity(getActivity(), meInfo.getGender(), meInfo.getAvatar()
                                , meInfo.getUserName(), meInfo.getSignature());
                    }
                });


                Glide.with(getActivity()).load(meInfo.getAvatar())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(circleImageView);
                if ("男".equals(meInfo.getGender())) {
                    Glide.with(getActivity()).load(R.drawable.ic_boy).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                } else
                    Glide.with(getActivity()).load(R.drawable.ic_girl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                signaturetext.setText(meInfo.getSignature());
                institutetext.setText(Constants.xueyuan[Integer.parseInt(meInfo.getInstitute())]);
                usernametext.setText(meInfo.getUserName());

                }});
            }

   public  class UserinfoReceiver extends BroadcastReceiver {
        public UserinfoReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
           getData();
        }

    }

}
