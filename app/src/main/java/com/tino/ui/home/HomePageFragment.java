package com.tino.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tino.MainActivity;
import com.tino.R;
import com.tino.adapter.BLogsAdapter;
import com.tino.adapter.BlogViewAdapter;
import com.tino.adapter.ViewsPageAdapter;
import com.tino.base.BaseFragment;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.model.Blog;
import com.tino.core.net.OkHttpClientManager;
import com.tino.ui.TopicActivity;
import com.tino.ui.upload.UpShareActivity;
import com.tino.utils.CommonUtils;
import com.tino.views.haoRefresh.HaoRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class  HomePageFragment  extends Fragment {
    private View rootView;
    private RollPagerView mLoopViewPager;
    private TestLoopAdapter mLoopAdapter;
    ViewPager viewPager;
    SlidingTabLayout slide_tab;

    OkHttpClient client = OkHttpClientManager.getInstance().getOkHttpClient();

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    final String[] imgs ={"https://139.199.21.36/H23/topic1.jpg",
            "https://139.199.21.36/H23/topic2.jpg"};
    final String[] topicid={"3","2"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.fragment_home, container, false);
            viewPager=(ViewPager)rootView.findViewById(R.id.viewPager);
            slide_tab=(SlidingTabLayout)rootView.findViewById(R.id.slide_tab);

            mLoopViewPager= (RollPagerView)rootView.findViewById(R.id.loop_view_pager);
            mLoopViewPager.setPlayDelay(2500);
            mLoopViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mLoopViewPager,imgs));
            mLoopViewPager.setHintView(new ColorPointHintView(getActivity(),Color.BLACK,Color.WHITE));
            mLoopViewPager.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //Toast.makeText(getActivity(), ""+topicid[position], Toast.LENGTH_SHORT).show();
                    TopicActivity.navToChat(getActivity(),imgs[position],topicid[position]);
                }
            });

           // mLoopAdapter.setImgs(imgs);
           // getData(mPage);

            List<Fragment> fragmentList = new ArrayList();
            fragmentList.add(new BloglistFragement());
            fragmentList.add(new CarerBloglistFragement());

            List<String> titles = new ArrayList();
            titles.add("实时");
            titles.add("关注");
             mLoopAdapter.notifyDataSetChanged();
            viewPager.setAdapter(new BlogViewAdapter(getFragmentManager(), fragmentList, titles));
            this.slide_tab.setViewPager(this.viewPager/*, titles*/);

          Button fab = (Button)rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().startActivity(new Intent(getActivity(), UpShareActivity.class));
                }
            });


        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return this.rootView;
    }

    public void getData(final int page){
        Request request = new Request.Builder()
                .url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/"+5+"/"+page)
                .get()
                .build();
        Call call =  client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.i("NetImageActivity","error:"+e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"网络请求失败，error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String content = response.body().string();
                    System.out.println("NetImageActivity   raw data:"+content);

                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray strArr = jsonObject.getJSONArray("results");
                    final String[] imgs  = new String[strArr.length()];
                    for (int i = 0; i < strArr.length(); i++) {
                        JSONObject obj = strArr.getJSONObject(i);
                        imgs[i] = obj.getString("url");
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoopAdapter.setImgs(imgs);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
            }






    private class TestLoopAdapter extends  StaticPagerAdapter {
        String[] imgs; //= new String[2];

        public void setImgs(String[] imgs){
            this.imgs = imgs;
            notifyDataSetChanged();
        }


        public TestLoopAdapter(RollPagerView viewPager,String[] imgs)
        {
            //super(viewPager);
            this.imgs=imgs;
         //   notifyDataSetChanged();
        }

        @Override
        public View getView(ViewGroup container, int position) {
            Log.i("RollViewPager","getView:"+imgs[position]);

            ImageView view = new ImageView(TinoApplication.getInstance());
            Glide.with(TinoApplication.getInstance())
                    .load(imgs[position])
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    //.placeholder(R.drawable.loading)
                    .into(view);
            view.setScaleType(ImageView.ScaleType.FIT_START);
            //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            return view;
        }

        @Override
        public int getCount() {
            return 2;
        }



    }


}
