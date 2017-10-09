package com.tino.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tino.R;
import com.tino.core.ActionCallback;
import com.tino.core.TinoApplication;
import com.tino.core.model.Blog;
import com.tino.core.model.User;
import com.tino.core.net.UploadImp;
import com.tino.ui.home.UserDetialActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.TimeUtils;
import com.tino.views.CircleImageView;
import com.tino.views.NoScrollGridView;

import java.util.List;

public class UserListAdapter extends BaseRecyclerAdapter<User> {

    private ItemClick click;
    private Context context;
    class CommentHolder extends CommonHolder<User> {
        CircleImageView avatar;

        TextView tv_name;


        public CommentHolder(View root) {
            super(root);//, R.layout.item_comment);

            tv_name = (TextView)root.findViewById( R.id.tv_name);

            avatar = (CircleImageView) root.findViewById(R.id.avatar);


        }
        @Override
        public void bindData(final User blog) {
               if(blog.getUserId()==null)return;
            Glide.with(context)
                    .load(blog.getAvatar())
                    .error(R.mipmap.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(this.avatar);
            this.tv_name.setText(blog.getUserName());

            this.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!blog.getUserId().equals(CommonUtils.getUid()))
                    UserDetialActivity.navToDetail(context,blog);
                    //ChatActivity.navToChat(context, blog.getUser().getUid(),  TIMConversationType.C2C, blog.getUser().getAvatar());
                }
            });


        }
    }
    public interface ItemClick {
        void onItemClick(Blog blog);
    }
    @Override
    public CommonHolder<User> setViewHolder(ViewGroup parent) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
    return new CommentHolder(v);
    }
    public void SetItemclick(ItemClick click){
        this.click=click;
    }
    public void SetContext(Context ct){
        this.context=ct;
    }
}
