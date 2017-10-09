package com.tino.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.tino.R;
import com.tino.core.model.Comment;
import com.tino.views.spannable.CircleMovementMethod;
import com.tino.views.spannable.NameClickListener;
import com.tino.views.spannable.NameClickable;

import java.util.ArrayList;
import java.util.List;

public class ShareCommentAdapter extends BaseAdapter {
    private ICommentItemClickListener commentItemClickListener;
    private List<Comment> datasource = new ArrayList();
    private List<Comment> showingComments = new ArrayList();

    public interface ICommentItemClickListener {
        void onItemClick(int i);
    }

    class ViewHolder {
        Button bt_expand_comment;
        CircleMovementMethod circleMovementMethod;
        TextView commentTv;

        ViewHolder() {
        }
    }

    public void setCommentClickListener(ICommentItemClickListener commentItemClickListener) {
        this.commentItemClickListener = commentItemClickListener;
    }

    public ShareCommentAdapter(List<Comment> datasource) {
        setDatasource(datasource);
    }

    public void setDatasource(List<Comment> datasource) {
        if (datasource != null) {
            this.datasource.clear();
            this.showingComments.clear();
            this.datasource = datasource;
            if (datasource.size() > 2) {
                this.showingComments.add(datasource.get(0));
                this.showingComments.add(datasource.get(1));
                return;
            }
            this.showingComments.addAll(datasource);
        }
    }

    @SuppressLint({"InflateParams"})
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_comment_simple, null);
            holder.commentTv = (TextView) convertView.findViewById(R.id.commentTv);
            holder.bt_expand_comment = (Button) convertView.findViewById(R.id.bt_expand_comment);
            holder.circleMovementMethod = new CircleMovementMethod(R.color.line_gray, R.color.line_gray);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Comment bean = (Comment) this.datasource.get(position);
        String id = bean.getReplyer().getUserId();
        String name = bean.getReplyer().getUserName();

        String toReplyName="";
        if(bean.getReplyTo()!=null) toReplyName= bean.getReplyTo().getUserName();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, 0));
        if (!TextUtils.isEmpty(toReplyName)) {
            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, 1));
        }
        builder.append(": ");
        builder.append(bean.getContent());
        holder.commentTv.setText(builder);
        final CircleMovementMethod circleMovementMethod = holder.circleMovementMethod;
        holder.commentTv.setMovementMethod(circleMovementMethod);
        holder.commentTv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv() && ShareCommentAdapter.this.commentItemClickListener != null) {
                    ShareCommentAdapter.this.commentItemClickListener.onItemClick(position);
                }
            }
        });
        if (position != 1 || this.datasource.size() <=2 || this.showingComments.size() >2) {
            holder.bt_expand_comment.setVisibility(View.GONE);
        } else {
            holder.bt_expand_comment.setVisibility(View.VISIBLE);
        }
        holder.bt_expand_comment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShareCommentAdapter.this.showingComments = ShareCommentAdapter.this.datasource;
                ShareCommentAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, int position) {
        if(textStr==null) textStr="";
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new NameClickable(new NameClickListener(subjectSpanText, ""), position), 0, subjectSpanText.length(), 33);
        return subjectSpanText;
    }

    public int getCount() {
        return this.showingComments.size();
    }

    public Object getItem(int arg0) {
        return this.datasource.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public List<Comment> getDatasource() {
        return this.datasource;
    }
}
