package com.tino.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ShareBean implements Parcelable {
    public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
    private String avatar;
    private int comment_count;
    private List<Comment> comments;
    private String content;
    private long create_time;
    private int favorite;
    private int favorite_count;
    private List<String> images;
    private String sid;
    private int type;
    private String uid;
    private String username;

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getName() {
        if (TextUtils.isEmpty(this.username)) {
            return this.uid;
        }
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getFavorite() {
        return this.favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getComment_count() {
        return this.comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getFavorite_count() {
        return this.favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public long getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getType() {
        return this.type;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sid);
        dest.writeString(this.uid);
        dest.writeString(this.username);
        dest.writeString(this.avatar);
        dest.writeString(this.content);
        dest.writeStringList(this.images);
        dest.writeList(this.comments);
        dest.writeInt(this.favorite);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.favorite_count);
        dest.writeLong(this.create_time);
        dest.writeInt(this.type);
    }

    protected ShareBean(Parcel in) {
        this.sid = in.readString();
        this.uid = in.readString();
        this.username = in.readString();
        this.avatar = in.readString();
        this.content = in.readString();
        this.images = in.createStringArrayList();
        this.comments = new ArrayList();
        in.readList(this.comments, Comment.class.getClassLoader());
        this.favorite = in.readInt();
        this.comment_count = in.readInt();
        this.favorite_count = in.readInt();
        this.create_time = in.readLong();
        this.type = in.readInt();
    }
}
