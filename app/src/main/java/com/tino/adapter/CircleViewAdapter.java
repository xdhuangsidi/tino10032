package com.tino.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tino.R;
import com.tino.core.model.User;
import com.tino.ui.PhotoViewActivity;
import com.tino.ui.home.UserDetialActivity;
import com.tino.utils.CommonUtils;
import com.tino.utils.DensityUtil;
import com.tino.views.SquaredFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class CircleViewAdapter extends BaseAdapter {
    private final int cellSize;
    private Context mContext;
    private ArrayList<User> pics = new ArrayList();

    class PhotoHolder {
        SquaredFrameLayout fl_Root;
        ImageView iv_Photo;

        PhotoHolder() {
        }
    }

    public CircleViewAdapter(Context context, List<User> picUrls) {
        this.pics = (ArrayList) picUrls;
        this.mContext = context;
        this.cellSize = DensityUtil.getScreenWidth(context) / 3;
    }

    public int getCount() {
        return this.pics.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
      final   PhotoHolder photoHolder;
        if (convertView == null) {
            photoHolder = new PhotoHolder();
            convertView = View.inflate(this.mContext, R.layout.item_circleview, null);
            photoHolder.fl_Root = (SquaredFrameLayout) convertView.findViewById(R.id.flRoot);
            photoHolder.iv_Photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            convertView.setTag(photoHolder);
            String picUrl =  this.pics.get(position).getAvatar();
          /*  if()*/
            Glide.with(this.mContext)
                    .load(picUrl)
                    //.placeholder(R.mipmap.avatar_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(photoHolder.iv_Photo);
            long animationDelay = (long) ((position * 30) + 600);
            photoHolder.fl_Root.setScaleY(0.0f);
            photoHolder.fl_Root.setScaleX(0.0f);
            photoHolder.fl_Root.animate().scaleY(1.0f).scaleX(1.0f).setDuration(200).setInterpolator(new DecelerateInterpolator()).setStartDelay(animationDelay).start();
        } else {
            photoHolder = (PhotoHolder) convertView.getTag();
        }
        photoHolder.fl_Root.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(CommonUtils.getUid().equals(pics.get(position).getUserId()))return;
                int[] location = new int[2];
                photoHolder.fl_Root.getLocationOnScreen(location);
                UserDetialActivity.navToDetail(mContext,pics.get(position));
            }
        });
        return convertView;
    }
}
