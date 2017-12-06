package com.hemaapp.tyyjsc;


/**
 * 该项目配置信息
 */
public class BaseConfig {
    /**
     * 是否打印信息开关
     */
    public static final boolean DEBUG = false;
    /**
     * 是否启用友盟统计l
     */
    public static final boolean UMENG_ENABLE = true;
    /**
     * 后台服务接口根路径
     */
    //测试的
//    public static final String SYS_ROOT = "http://124.128.23.74:8008/group13/hm_tyyjsc/";    //测试的

    //正式服务器地址
    public static final String SYS_ROOT = "http://139.129.109.220/hm_tyyjsc/";
    /**
     * 图片压缩的最大宽度.
     */
    public static final int IMAGE_WIDTH = 640;
    /**
     * 图片压缩的最大高度
     */
    public static final int IMAGE_HEIGHT = 3000;
    /**
     * 图片压缩的失真率
     */
    public static final int IMAGE_QUALITY = 100;
    /**
     * 银联支付环境--"00"生产环境,"01"测试环境
     */
    public static final String UNIONPAY_TESTMODE = "00";
    /**
     * 微信appid
     */
    public static final String APPID_WEIXIN = "wxb8224c75f984680b";
    //相册选择图片最大数
    public static int MAX_SIZE = 4;
    //广播消息
    public static final String BROADCAST_ACTION = "com.hemaapp.tyyjsc.login";
    public static final String BROADCAST_ADDRESS = "com.hemaapp.tyyjsc.address";
    public static final String BROADCAST_CART = "com.hemaapp.tyyjsc.cart";
    public static final String BROADCAST_PWD = "com.hemaapp.tyyjsc.pwd";
    public static final String BROADCAST_MONEY = "com.hemaapp.tyyjsc.money";
    public static final String BROADCAST_ORDER = "com.hemaapp.tyyjsc.order_status";
    public static final String BROADCAST_ORDER_NUM = "com.hemaapp.tyyjsc.order_num";
    public static final String BROADCAST_MSG_NUM = "com.hemaapp.tyyjsc.msg_num";
    public static final String BROADCAST_GETCITY = "com.hemaapp.tyyjsc.city";
}
