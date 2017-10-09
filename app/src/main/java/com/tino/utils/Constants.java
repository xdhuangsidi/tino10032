package com.tino.utils;

public class Constants {
    public static final String ACTIVITY_TITLE = getPrefixConstant("activity_title");
    public static final String CONVERSATION_ID = getPrefixConstant("conversation_id");
    private static final String LEANMESSAGE_CONSTANTS_PREFIX = "leancloud.im.";
    public static final String MEMBER_ID = getPrefixConstant("member_id");
    public static final String OBJECT_ID = "objectId";
    public static final String gender[] = {"男","女"};
    public static final String topic[] = {"0","1","军训","约吧"};
    public static final String garde[]={"大一","大二","大三","大四","研一","研二","研三"};
    public static final String xueyuan[]={"轻工科学与工程学院","材料科学与工程学院","环境科学与工程学院",
                                          "食品与生物工程学院","机电工程学院","电气与信息工程学院",
                                            "经济与管理学院","科学与化工学院","设计与艺术学院",
                                           "文理学院","  职业教育师范学院"};
    public static final String zhuanye[][]={{"轻化工程","非织造材料与工程","包装工程","印刷工程"},
            {"无机非金属材料工程","材料物理","材料化学","纳米材料与技术"},
            {"环境科学与工程","环境工程"},
            {"食品科学与工程","食品质量与安全","乳品工程","药物制剂","制药工程"},
            {"机械设计制造及其自动化","机械电子工程","材料成型及控制工程","能源与动力工程","过程装备与控制工程","物流工程"," 工业工程"},
            {"自动化","电气工程及其自动化","测控技术与仪器","电子信息工程","电子科学与技术","光电信息科学与工程","电子信息科学与技术","计算机科学与技术","网络工程","物联网工程"},
            {"工商管理","会计学","市场营销","市场营销","行政管理","人力资源管理","国际经济与贸易"},
            {"化学工程与工艺","化学","高分子材料与工程","应用化学","石油工程"},
            {"动画","视觉传达设计","环境设计","产品设计","服装与服饰设计","工业设计","广播电视编导","播音与主持艺术","服装设计与工程"},
            {"信息与计算科学","数学与应用数学","应用物理学","英语"},
            {"电子信息工程","材料成型及控制工程","机械设计制造及其自动化","印刷工程"}
    };
    private static String getPrefixConstant(String str) {
        return LEANMESSAGE_CONSTANTS_PREFIX + str;
    }
}
