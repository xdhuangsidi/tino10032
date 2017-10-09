package com.tino;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.api.Constants;
import com.tino.core.model.User;
import com.tino.core.net.UploadImp;
import com.tino.ui.find.Findragment;
import com.tino.ui.home.HomePageFragment;
import com.tino.ui.home.ShareDetailActivity;
import com.tino.ui.me.MeFragment;
import com.tino.ui.message.MessageFragment;
import com.tino.utils.CommonUtils;
import com.tino.views.FragmentTabHost;

public class MainActivity extends FragmentActivity {

    private LayoutInflater layoutInflater;
    public FragmentTabHost mTabHost;
    public TextView tv_count;
    private final Class fragmentArray[] = {/*Findragment.class,*/ MessageFragment.class,HomePageFragment.class,  MeFragment.class};
    private int mImageViewArray[] = {/* R.drawable.tab_find,*/  R.drawable.tab_message,R.drawable.tab_home, R.drawable.tab_me};
    private String mTextviewArray[] = {"一一"/*, "发现"*/, "二二", "三三"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public static void NavtoMain(final Context ct){

    }


    public static void NavtoMain(final Context ct,final User user){
        CommonUtils.savetoken(user.getToken(),ct);
        CommonUtils.saveUid(user.getUserId(),ct);
        Constants.user=user;
        CommonUtils.saveAvatar(user.getAvatar(),ct);
        UploadImp.getImSigFromServer(user.getToken(), user.getUserId(), new ActionCallback<String>() {
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
            public void onSuccess(String s) {
                final Intent intent = new Intent(ct,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                TIMUser timuser = new TIMUser();
                timuser.setAccountType("14602");
                timuser.setAppIdAt3rd("1400037236");
                timuser.setIdentifier(user.getUserId());
                TIMManager.getInstance().login(1400037236,
                        timuser, s, new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                System.out.println(" login the tecent sdk fail------->"+s+"\n 错误码"+i);
                                Toast.makeText(TinoApplication.getContext(), " login the tecent sdk fail------->"+s+"\n code"+i, Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onSuccess() {
                                ct.startActivity(intent);
                                ((Activity)ct).finish();
                                System.out.println(" login the tecent sdk successfully");

                            }
                        });
            }
        });







    }
    private void initView() {

        layoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
        int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; ++i) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }




    }
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.home_tab, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView title = (TextView) view.findViewById(R.id.tab_title);
        TextView a=(TextView)view.findViewById(R.id.tabUnread);
        a.setVisibility(View.GONE);
        title.setText(mTextviewArray[index]);
        if(index==0){
            tv_count=a;
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText("0");
        }
        return view;
    }
    public void setMsgUnread(int i){
        tv_count.setVisibility(View.VISIBLE);
       if(i==0)tv_count.setVisibility(View.GONE);
       else if(i!=0){

           tv_count.setText(""+i);
       }
    }

}
