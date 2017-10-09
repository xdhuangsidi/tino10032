package com.tino.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tino.R;
import com.tino.core.model.Comment;


public class ShareCommentsAdapter extends BaseRecyclerAdapter<Comment> {

    class CommentHolder extends CommonHolder<Comment> {
       // @Bind({2131558721})
        TextView tv_name;

        public CommentHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.item_comment);
            tv_name = (TextView)root.findViewById(R.id.tv_name);
        }

        public void bindData(Comment comment) {
            this.tv_name.setText(comment.getReplyer().getUserName());
        }
    }

    public CommonHolder<Comment> setViewHolder(ViewGroup parent) {
        return new CommentHolder(parent.getContext(), parent);
    }
}
