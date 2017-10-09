package com.tino.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tino.R;
import com.tino.core.model.Blog;
import com.tino.ui.TopicActivity;
import com.tino.ui.home.UserDetialActivity;
import com.tino.utils.CommonHelper;
import com.tino.utils.CommonUtils;
import com.tino.utils.StringFormatUtil;
import com.tino.utils.TimeUtils;
import com.tino.views.CircleImageView;
import com.tino.views.NoScrollGridView;

import java.util.List;

public class TopicBLogsAdapter extends BaseRecyclerAdapter<Blog> {

  private ItemClick click;
 private Context context;
    class CommentHolder extends CommonHolder<Blog> {
        CircleImageView avatar;
        TextView tv_title;
        TextView tv_content;
        TextView tv_name;
        ImageView iv_sub;
        TextView tv_time,tv_like_count,tv_comment_count,tv_subname,tv_topic;
        ImageButton ib_collect,ib_favorite;
        NoScrollGridView photo_gridview ;
        public CommentHolder(View root) {
            super(root);//, R.layout.item_comment);
            ib_collect=(ImageButton) root.findViewById(R.id.ib_add_collect);
            tv_name = (TextView)root.findViewById( R.id.tv_name);
            tv_time = (TextView) root.findViewById(R.id.tv_time);
            tv_content = (TextView) root.findViewById(R.id.tv_content);
            tv_title=(TextView)root.findViewById(R.id.tv_title);
            ib_favorite=(ImageButton)root.findViewById(R.id.ib_like_blog);
            tv_like_count=(TextView)root.findViewById(R.id.tv_status);
            avatar = (CircleImageView) root.findViewById(R.id.avatar);
            tv_subname=(TextView)root.findViewById(R.id.tv_subname);
            tv_comment_count=(TextView)root.findViewById(R.id.tv_comment_count);
            iv_sub=(ImageView)root.findViewById(R.id.image_sub);
            tv_topic=(TextView)root.findViewById(R.id.tv_topic);
            photo_gridview = (NoScrollGridView) root.findViewById( R.id.photo_gridview);

        }
        @Override
        public void bindData(final Blog blog) {
            if(blog.getOwner()==null)return;
            Glide.with(context)
                .load(blog.getOwner().getAvatar())
             //       .error(R.mipmap.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(this.avatar);
            this.tv_content.setText("");
            if(blog.getTopic()!=null&&blog.getTopic().getTheme().length()>=1){
                tv_topic.setVisibility(View.VISIBLE);
                String wholeStr ="#"+blog.getTopic().getTheme()+"#";

                if("2".equals(blog.getTopic().getTopicTime())){
                    StringFormatUtil spanStr = new StringFormatUtil(context, wholeStr,
                            wholeStr, R.color.first_blue).fillColor();
                    tv_topic.setText(spanStr.getResult());


                }else{
                    StringFormatUtil spanStr = new StringFormatUtil(context, wholeStr,
                            wholeStr, R.color.first_red).fillColor();
                    tv_topic.setText(spanStr.getResult());

                }


            }
            else{
                tv_topic.setText("");
                tv_topic.setVisibility(View.GONE);
            }

            if(blog.getTitle()==null||blog.getTitle().length()<=0)
                tv_title.setVisibility(View.GONE);
            this.tv_name.setText(blog.getOwner().getUserName());
            if(blog.getContent()!=null)this.tv_content.append(blog.getContent());
            this.tv_time.setText(TimeUtils.getListTime(Long.parseLong(blog.getBlogTime())));
            this.tv_title.setText(blog.getTitle());
            if(blog.getOwner().getBrief()!=null&&blog.getOwner().getBrief().length()!=0){
                tv_subname.setText(blog.getOwner().getBrief());
                tv_subname.setVisibility(View.VISIBLE);
                iv_sub.setVisibility(View.VISIBLE);
            }
            this.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(blog.getOwner().getUserId().equals(CommonUtils.getUid()))
                        return;
                    UserDetialActivity.navToDetail(context,blog.getOwner());
                    //ChatActivity.navToChat(context, blog.getUser().getUid(),  TIMConversationType.C2C, blog.getUser().getAvatar());
                }
            });

            tv_comment_count.setText(""+blog.getCommentCounts());
            if(blog.isIslike()){
                ib_favorite.setBackgroundResource(R.mipmap.ic_favorit);
            }
            else if(!blog.isIslike()){
                ib_favorite.setBackgroundResource(R.mipmap.ic_unfavorit);
            }
            ib_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonHelper.toggleFavorite(blog,ib_favorite,tv_like_count);
                }
            });

            if(blog.isIscollect()){
                ib_collect.setImageResource(R.mipmap.icon_collect);
            }
            else if(!blog.isIscollect()){
                ib_collect.setImageResource(R.mipmap.icon_uncollect);
            }
            ib_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonHelper.toggleCollect(blog,ib_collect);
                }
            });
            if(blog.getOwner().getUserId().equals(CommonUtils.getUid())){
                ib_collect.setImageResource(R.mipmap.ic_close);
                ib_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonHelper.deleteBlog(blog.getBlogTime(),context);
                    }
                });
            }
            tv_like_count.setText(""+blog.getLikeCounts());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(click!=null){
                        click.onItemClick(blog);
                    }
                }
            });
            List<String> list= JSON.parseObject(blog.getPictures(),new  TypeReference<List<String>>() {});
            if(list==null||list.size()==0){
                photo_gridview.setVisibility(View.GONE);
            }
            else{
                this.photo_gridview.setVisibility(View.VISIBLE);
                PhotoAdapter photoAdapter = new PhotoAdapter(this.itemView.getContext(),list);
                this.photo_gridview.setAdapter(photoAdapter);
            }

        }
    }
    public interface ItemClick {
        void onItemClick(Blog blog);
    }
    @Override
    public CommonHolder<Blog> setViewHolder(ViewGroup parent) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
    return new CommentHolder(v);
    }
    public void SetItemclick(ItemClick click){
        this.click=click;
    }
    public void SetContext(Context ct){
        this.context=ct;
    }
}
