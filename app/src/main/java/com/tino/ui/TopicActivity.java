package com.tino.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.tencent.TIMConversationType;
import com.tino.R;
import com.tino.adapter.BlogViewAdapter;
import com.tino.ui.home.BloglistFragement;
import com.tino.ui.home.TopicBloglistFragement;
import com.tino.ui.message.ChatActivity;
import com.tino.ui.upload.UpTopActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-29.
 */

public class TopicActivity extends FragmentActivity {


    ViewPager viewPager;
    SlidingTabLayout slide_tab;
    ImageView iv_bg;
    ImageButton iv_back;
    public String url,topicId;
    Button bt_up;

    public static void navToChat(Context context, String url, String type){
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("topicId", type);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_topic);
        url=getIntent().getStringExtra("url");
        topicId=getIntent().getStringExtra("topicId");
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        slide_tab=(SlidingTabLayout)findViewById(R.id.slide_tab);
        bt_up=(Button)findViewById(R.id.bt_add_top);
        bt_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpTopActivity.navToChat(TopicActivity.this,topicId);
            }
        });
        iv_back=(ImageButton) findViewById(R.id.ic_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_bg=(ImageView)findViewById(R.id.bg_topic);
        if(this.url!=null&&this.url.startsWith("http")){
            Glide.with(this)
                    .load(url)
                    //       .error(R.mipmap.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_bg);
        }
        List<Fragment> fragmentList = new ArrayList();
        fragmentList.add(new TopicBloglistFragement());

        List<String> titles = new ArrayList();
        titles.add("#话题#");

        viewPager.setAdapter(new BlogViewAdapter(getSupportFragmentManager(), fragmentList, titles));
        this.slide_tab.setViewPager(this.viewPager/*, titles*/);
    }
}
