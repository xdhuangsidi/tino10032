package com.tino.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tino.R;
import com.tino.core.model.Blog;
import com.tino.core.model.Comment;
import com.tino.core.model.CommentList;
import com.tino.views.CommentEditorController;


public class ShareListAdapter extends BaseRecyclerAdapter<CommentList> {
    private ItemClick itemClick;
    private CommentEditorController editorController;
    private CommentReflashCallback callback;
    public Blog blog;
    public void setCommentCallback(CommentReflashCallback listener){
        this.callback=listener;
    }
    public void setEditorController(CommentEditorController editorController){
        this.editorController=editorController;
    }
    public interface ItemClick {
        void onItemClick(CommentList commentList);
    }
    public interface CommentReflashCallback {
        void onReflash();
    }
    @Override
    public CommonHolder<CommentList > setViewHolder(ViewGroup root) {
        this.animationsLocked = true;
        View v = LayoutInflater.from(root.getContext()).inflate( R.layout.item_share, root, false);
        return new ShareItemHolder(v,root.getContext(),this.itemClick,this.editorController,this.callback,blog);
    }


    public void setOnItemClickListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }
}
