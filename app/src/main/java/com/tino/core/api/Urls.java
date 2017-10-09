package com.tino.core.api;

public class Urls {

    public static final String BASE_URL="https://139.199.21.36/tino/";

    public static final String LOGIN_PWD_URL = BASE_URL+"LoginAction_loginByPassword";
    public static final String GET_BLOG_URL = BASE_URL+"BlogAction_getBlogs";
    public static final String GET_TOPIC_BLOG_URL = BASE_URL+"BlogAction_getByTopic";
    public static final String GET_CARE_BLOG_URL = BASE_URL+"BlogAction_getCareBlogs";
    public static final String GET_COMMENTLIST_URL=BASE_URL+"BlogAction_getLists";
    public static final String GET_COLLECT_BLOG_URL = BASE_URL+ "BlogAction_getCollectBlogs";
    public static final String GET_MY_BLOG_URL = BASE_URL+"BlogAction_getMyBlogs";
    public static final String GET_CARE_ME_URL = BASE_URL+"UserAction_getWohCareMe";
    public static final String GET_CARE_USER_URL = BASE_URL+"UserAction_getCareUsers";
    public static final String ADD_REPLY_URL=BASE_URL+"CommentListAction_addCommentList";
    public static final String ADD_SubREPLY_URL=BASE_URL+"CommentAction_addComment";
    public static final String LOGIN_AUTH_URL = BASE_URL+"LoginAction_loginByCode";
    public static final String CHANGE_PASS = BASE_URL+"UserAction_alterPassword";
    public static final String CARE_USER_URL=BASE_URL+"UserAction_care";
    public static final String UNCARE_USER_URL=BASE_URL+"UserAction_deCare";
    public static final String GET_COS_SIG=BASE_URL+"UserAction_getCosSign";
    public static final String DISLIKE_BLOG_URL=BASE_URL+"BlogAction_disLike";
    public static final String COLLECT_BLOG_URL=BASE_URL+"UserAction_collect";
    public static final String DELETE_BLOG_URL=BASE_URL+"BlogAction_deleteBlog";
    public static final String DELETE_COMMENT_URL=BASE_URL+"CommentListAction_deleteCommentList";
    public static final String UNCOLLECT_BLOG_URL=BASE_URL+"UserAction_deCollect";
    public static final String LIKE_BLOG_URL=BASE_URL+"BlogAction_like";
    public static final String GET_IM_SIG=BASE_URL+"UserAction_getTimSign";
    public static final String ADD_INFO = BASE_URL+"UserAction_updateUserInfo";
    public static final String GET_INFO = BASE_URL+"UserAction_getUserMessage";
    public static final String GET_USER_INFO = BASE_URL+"UserAction_getUserMessage";
    public static final String UP_LOAD_BLOG=BASE_URL+"BlogAction_addBlog";

}
