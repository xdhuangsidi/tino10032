package com.tino.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.tino.R;
import com.tino.core.model.Comment;
import com.tino.utils.SoftInputUtils;


public class CommentEditorController {
    private static final String TAG = CommentEditorController.class.getSimpleName();
    CommentListener listener;
    private int mCirclePosition;
    private int mCommentPosition;
    private int mCommentType;
    private Context mContext;
    public EditText mEditText;
    public View mEditTextBody;
    private RecyclerView mListView;
    private String mReplyLid;
    private String mReplyUid;
    private int mSelectCircleItemH;
    private int mSelectCommentItemBottom;
    public View mSendBt;
    public interface CommentListener {
        void onSuccess(String str);
    }

    public RecyclerView getListView() {
        return this.mListView;
    }

    public void setListView(RecyclerView mListView) {
        this.mListView = mListView;
    }

    public CommentEditorController(Context context, View editTextBody, EditText editText, View sendBt) {
        this.mContext = context;
        this.mEditTextBody = editTextBody;
        this.mEditText = editText;




        this.mSendBt = sendBt;
        this.mSendBt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final String text = CommentEditorController.this.mEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                  /*  BookApplication.getInstance().getAccountAction().sendComment(CommentEditorController.this.mCommentType, CommentEditorController.this.mReplyLid, CommentEditorController.this.mReplyUid, text, new ActionCBImpl<Void>(CommentEditorController.this.mContext) {
                        public void onSuccess(Void data) {
                            Toast.makeText(CommentEditorController.this.mContext, "评论成功", Toast.LENGTH_SHORT).show();
                            if (CommentEditorController.this.listener != null) {
                                CommentEditorController.this.listener.onSuccess(text);
                            }
                        }
                    });*/
                    CommentEditorController.this.editTextBodyVisible(View.GONE);
                }
                CommentEditorController.this.editTextBodyVisible(View.GONE);
            }
        });
    }

    public void editTextBodyVisible(int mCirclePosition, int commentType, String mReplyLid, String replyUid, int commentPosition) {
        this.mCirclePosition = mCirclePosition;
        this.mReplyLid = mReplyLid;
        this.mCommentType = commentType;
        this.mReplyUid = replyUid;
        this.mCommentPosition = commentPosition;
        editTextBodyVisible(View.VISIBLE);
    }

    private void measure(int mCirclePosition, int commentType) {
        if (this.mListView != null) {
            View selectCircleItem = this.mListView.getChildAt(mCirclePosition - ((LinearLayoutManager) this.mListView.getLayoutManager()).findFirstVisibleItemPosition());
            this.mSelectCircleItemH = selectCircleItem.getHeight();
            AppNoScrollerListView commentLv = (AppNoScrollerListView) selectCircleItem.findViewById(R.id.common_web_browser_layout);
            if (commentLv != null) {
                View selectCommentItem = commentLv.getChildAt(this.mCommentPosition - commentLv.getFirstVisiblePosition());
                if (selectCommentItem != null) {
                    this.mSelectCommentItemBottom = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            this.mSelectCommentItemBottom += parentView.getHeight() - subItemBottom;
                        }
                        if (parentView == null) {
                            return;
                        }
                    } while (parentView != selectCircleItem);
                }
            }
        }
    }

    public void handleListViewScroll() {
        if (this.mListView != null) {
            this.mListView.addOnScrollListener(new OnScrollListener() {
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (Math.abs(dx) > 10) {
                        CommentEditorController.this.editTextBodyVisible(8);
                    }
                }
            });
        }
    }

    public void editTextBodyVisible(int visibility) {
        if (this.mEditTextBody != null) {
            this.mEditTextBody.setVisibility(visibility);
            if (visibility == View.VISIBLE) {
                this.mEditText.requestFocus();
                SoftInputUtils.showSoftInput(this.mEditText.getContext(), this.mEditText);
            } else if (View.GONE == visibility) {
                SoftInputUtils.hideSoftInput(this.mEditText.getContext(), this.mEditText);
            }
        }
    }

    public String getEditTextString() {
        String result = "";
        if (this.mEditText != null) {
            return this.mEditText.getText().toString();
        }
        return result;
    }

    public void clearEditText() {
        if (this.mEditText != null) {
            this.mEditText.setText("");
        }
    }

    public void setCommentListener(CommentListener listener) {
        this.listener = listener;
    }
}
