package com.tino.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.tino.R;
import com.tino.adapter.ViewsPageAdapter;
import com.tino.base.BaseActivity;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.model.User;
import com.tino.ui.home.ShareDetailActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.Constants;
import com.tino.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-11.
 */

public class UserDetail extends BaseActivity {
    public static String  uid;
    EditText et_input;
    ImageButton ib_send;
    View input_bar;
    View ll_root;
    @Override
    public void bindView() {
        ll_root = (View)findViewById( R.id.ll_root);
        input_bar = (View) findViewById(  R.id.input_bottom_bar_layout);
        et_input = (EditText) findViewById(  R.id.input_bottom_bar_et_content);
        ib_send = (ImageButton)findViewById(  R.id.input_bottom_bar_btn_send);
    }
    public static class MeFragment extends Fragment {



        private View rootView;
        ViewPager viewPager;
        SlidingTabLayout slide_tab;
        CircleImageView circleImageView;
        ImageView imageView,setting;
        TextView usernametext,institutetext,signaturetext;


        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (this.rootView == null) {
                this.rootView = inflater.inflate(R.layout.fragment_me_detail, container, false);

                viewPager=(ViewPager)rootView.findViewById(R.id.viewPager);
                slide_tab=(SlidingTabLayout)rootView.findViewById(R.id.slide_tab);

                String[] titles = new String[]{"关注文章", "关注的人","关注我的"};
                List<View> views = new ArrayList();
                View view_bookinfo = View.inflate(getActivity(), R.layout.inc_recycler_view, null);
                View view_comments = View.inflate(getActivity(), R.layout.inc_recycler_view, null);
                View view_comments1 = View.inflate(getActivity(), R.layout.inc_recycler_view, null);

                views.add(view_bookinfo);
                views.add(view_comments);
                views.add(view_comments1);
                viewPager.setAdapter(new ViewsPageAdapter(views, titles));
                this.slide_tab.setViewPager(this.viewPager, titles);
                init();

            }
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            return this.rootView;
        }

        private void init() {
            circleImageView=(CircleImageView)rootView.findViewById(R.id.iv_avatar);
            imageView=(ImageView)rootView.findViewById(R.id.sex);
            setting=(ImageView)rootView.findViewById(R.id.me_setting);
            usernametext=(TextView)rootView.findViewById(R.id.me_username);
            institutetext=(TextView)rootView.findViewById(R.id.me_institute);
            signaturetext=(TextView)rootView.findViewById(R.id.me_signature);
            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().startActivity(new Intent(getActivity(),EditMeActivity.class));
                }
            });
            getData();
        }

        private void getData() {
            AccountImpl.getinfo(uid, CommonUtils.gettoken(getActivity()),uid, new ActionCallback<User>() {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getActivity()).load(meInfo.getAvatar()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(circleImageView);
                            if(meInfo.getGender().equals("男")){
                                Glide.with(getActivity()).load(R.drawable.ic_boy).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                            }else Glide.with(getActivity()).load(R.drawable.ic_girl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                            signaturetext.setText(meInfo.getSignature());
                            institutetext.setText(Constants.xueyuan[Integer.parseInt(meInfo.getInstitute())]);
                            usernametext.setText(meInfo.getUserName());
                        }
                    });
                }
            });
        }

    }
    @Override
    public void init() {
        Intent intent = getIntent();
        uid=intent.getStringExtra("uid");
        setupBackToolbar("帖子详情");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MeFragment()).commit();
    }
    public static void launch(Activity context,String uid) {
        Intent intent = new Intent(context, ShareDetailActivity.class);

        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }
    @Override
    public int setInflateId() {
        return R.layout.activity_share_detail;
    }
}
