package com.tino.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.imcore.CustomElem;
import com.tino.R;
import com.tino.adapter.BaseRecyclerAdapter;
import com.tino.adapter.CircleViewAdapter;
import com.tino.adapter.PhotoAdapter;
import com.tino.adapter.ReplyAdapter;
import com.tino.adapter.ShareListAdapter;
import com.tino.base.BaseActivity;
import com.tino.base.BaseRecyclerFragment;
import com.tino.core.AccountImpl;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.model.Blog;
import com.tino.core.model.CommentList;
import com.tino.core.model.CustomMessage;
import com.tino.core.model.Topic;
import com.tino.core.model.User;
import com.tino.core.net.UploadImp;
import com.tino.utils.CommonHelper;
import com.tino.utils.CommonUtils;
import com.tino.utils.TimeUtils;
import com.tino.views.CircleImageView;
import com.tino.views.CommentEditorController;
import com.tino.views.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;


public class ShareDetailActivity extends BaseActivity {


    public static Blog blog;
    static EditText et_input;
    static ImageButton ib_send;
    static View input_bar;
    View ll_root;

    static ShareListAdapter.CommentReflashCallback cb;
    static CommentEditorController  editorController;
    static CommentEditorController editorController1;

@Override
public void bindView()
{
    ll_root = (View)findViewById( R.id.ll_root);
    input_bar = (View) findViewById(  R.id.input_bottom_bar_layout);
    et_input = (EditText) findViewById(  R.id.input_bottom_bar_et_content);
    ib_send = (ImageButton)findViewById(  R.id.input_bottom_bar_btn_send);




    editorController1 = new CommentEditorController(this, this.input_bar, this.et_input, this.ib_send);

    ib_send=(ImageButton)findViewById(R.id.input_bottom_bar_btn_send);
    et_input=(EditText)findViewById(R.id.input_bottom_bar_et_content);
    Intent intent = getIntent();
    blog=(Blog)intent.getSerializableExtra("blog");
    this.editorController = new CommentEditorController(this, this.input_bar, this.et_input, this.ib_send);

}

    public  static class CommentListFragment extends BaseRecyclerFragment<CommentList> {
      ShareListAdapter adapter;
      CircleImageView avatar;
      ImageButton  ib_comment;
        TextView tv_title,tv_content,tv_time,tv_name,tv_status;
        NoScrollGridView photo_gridview ,avatar_gridview;
        public BaseRecyclerAdapter<CommentList> setAdapter() {
            this.adapter = new  ShareListAdapter();
            this.adapter.blog=blog;
            this.adapter.setCommentCallback(new ShareListAdapter.CommentReflashCallback(){

                @Override
                public void onReflash() {
                    toRefresh();
                }
            });
            cb=new ShareListAdapter.CommentReflashCallback() {
                @Override
                public void onReflash() {
                    toRefresh();
                }
            };
            this.adapter.setEditorController(editorController);


            /*-----start--------*/
            View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_blog_firstdetail, this.recyclerView, false);
            tv_name = (TextView)header.findViewById( R.id.tv_name);
            tv_time = (TextView) header.findViewById(R.id.tv_time);
            tv_status=(TextView)header.findViewById(R.id.tv_status_like_count);
            tv_content = (TextView)header.findViewById(R.id.tv_content);
            tv_title=(TextView)header.findViewById(R.id.tv_title);
            avatar = (CircleImageView)header. findViewById(R.id.avatar);
            photo_gridview = (NoScrollGridView)header. findViewById( R.id.photo_gridview);
            avatar_gridview=(NoScrollGridView)header.findViewById(R.id.avatar_gridview);
            ib_comment=(ImageButton)header.findViewById(R.id.ib_add);

            ib_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editorController1 = new CommentEditorController(getActivity(), input_bar, et_input, ib_send);
                    editorController1.editTextBodyVisible(0,1, blog.getBlogTime(), blog.getOwner().getUserId(), 0);
                    editorController1.setCommentListener(new CommentEditorController.CommentListener() {
                        public void onSuccess(String content) {

                        }
                    });

                    editorController1.mSendBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            final String content = editorController.mEditText.getText().toString().trim();
                            editorController.clearEditText();
                            editorController.editTextBodyVisible(View.GONE);

                            if(!blog.getOwner().getUserId().equals(CommonUtils.getUid())) {
                                Gson gson = new Gson();
                                Blog blog1 = new Blog();
                                blog1.setTitle(blog.getTitle());
                                blog1.setBlogTime(blog.getBlogTime());
                                blog1.setContent(blog.getContent());
                                blog1.setLikeUserInfo(blog.getLikeUserInfo());
                                blog1.setOwner(blog.getOwner());
                                blog1.setPictures(blog.getPictures());
                                blog1.setLikeCounts(blog.getLikeCounts());
                                String js = gson.toJson(blog1);

                                TIMMessage message = new TIMMessage();
                                TIMCustomElem elem = new TIMCustomElem();
                                elem.setDesc(content);
                                elem.setData(js.getBytes());


                                elem.setExt(Long.toString(System.currentTimeMillis()).getBytes());

                                message.addElement(elem);
                                CustomMessage msg = new CustomMessage(message);
                                TIMManager.getInstance().getConversation(TIMConversationType.C2C
                                        , blog.getOwner().getUserId()).sendMessage(msg.getMessage(), new TIMValueCallBack<TIMMessage>() {
                                    @Override
                                    public void onError(int i, String s) {

                                    }

                                    @Override
                                    public void onSuccess(TIMMessage timMessage) {
                                        //Toast.makeText(TinoApplication.getInstance(), "send custom successu", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            UploadImp.addReply(CommonUtils.gettoken()
                                    , CommonUtils.getUid()
                                    , blog.getBlogTime(),content
                                    , ""
                                    , new ActionCallback<String>() {
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
                                        public void onSuccess(String s){
                                            if(cb!=null)cb.onReflash();
                                        }
                                    });
                        }

                    });


                }
            });

            Glide.with(TinoApplication.getContext()).load(blog.getOwner().getAvatar()).error(R.mipmap.avatar_default).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(this.avatar);
            this.tv_name.setText(blog.getOwner().getUserName());
            this.tv_content.setText(blog.getContent());
            this.tv_time.setText(TimeUtils.getListTime(Long.parseLong(blog.getBlogTime())));
            if(blog.getTitle().length()<=0)
                this.tv_title.setVisibility(View.GONE);
            tv_title.setText(blog.getTitle());
            tv_status.setText(blog.getLikeCounts()+"点赞");
            List<String> list= JSON.parseObject(blog.getPictures(),new  TypeReference<List<String>>() {});
            if(list.size()==0){
                photo_gridview.setVisibility(View.GONE);
            }
            else{
                this.photo_gridview.setVisibility(View.VISIBLE);
                PhotoAdapter photoAdapter = new PhotoAdapter(getActivity(),list);
                this.photo_gridview.setAdapter(photoAdapter);
            }
            this.avatar_gridview.setVisibility(View.VISIBLE);
            List<User> list1=new ArrayList<>();
            int i=0;
            for(User user:blog.getLikeUserInfo()){
                list1.add(user);
                i++;
                if(i==8)break;
            }
            CircleViewAdapter adapter=new CircleViewAdapter(getActivity(),list1);
            avatar_gridview.setAdapter(adapter);
            this.adapter.setHeadHolder(header);
            /*------end---------*/

            return this.adapter;
        }

        public boolean canLoadmore() {
            return true;
        }

        public void initAction() {


        }

        public void toRefresh() {
            super.toRefresh();
            AccountImpl.getComeentList(blog.getBlogTime(),this);


        }

        public void toLoadMore() {
            super.toLoadMore();
            if(currentPage>=1)return;
            List<CommentList> list=new ArrayList<>();
            for(CommentList c:blog.getCommentList()){
                list.add(c);
            }
            this.onSuccess(list);

        }

        @Override
        public void onSuccess(List<CommentList> datas) {

            if (datas == null || datas.size() <=0) {
                this.recyclerView.setCanloadMore(false);
            } else {
                this.recyclerView.setCanloadMore(true);
            }

            if (state == 0) {
                this.currentPage = 1;
                if (datas == null || datas.size() == 0) {
                    hideDefaultView();
                } else {
                    hideDefaultView();
                }

                this.adapter.setDataList(datas);
                return;
            }
            this.currentPage++;
            this.adapter.addItems(datas);

        }
    }

        public static void launch(Activity context, Blog blog) {
            Intent intent = new Intent(context, ShareDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("blog", blog);
            intent.putExtras(bundle);
            context.startActivity(intent);
    }

    public int setInflateId() {
        return R.layout.activity_share_detail;
    }

    public void init() {

        setupBackToolbar("帖子详情");

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CommentListFragment()).commit();

    }


}
