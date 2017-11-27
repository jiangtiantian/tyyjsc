package com.hemaapp.tyyjsc;


import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.tyyjsc.model.SysInitInfo;


/**
 * 网络请求信息枚举类
 */
public enum BaseHttpInformation implements HemaHttpInfomation {

    /**
     * 登录
     */
    CLIENT_LOGIN(HemaConfig.ID_LOGIN, "client_login", "登录", false),
    // 注意登录接口id必须为HemaConfig.ID_LOGIN
    /**
     * 第三方登录
     */
    THIRD_SAVE(HemaConfig.ID_THIRDSAVE, "third_save", "第三方登录", false),
    // 注意第三方登录接口id必须为HemaConfig.ID_THIRDSAVE

    /**
     * 后台服务接口根路径
     */
    SYS_ROOT(0, BaseConfig.SYS_ROOT, "后台服务接口根路径", true),
    /**
     * 系统初始化
     */
    /*INIT(1, "index.php/webservice/index/init", "系统初始化", false),  //正式的*/
    INIT(1, "index.php/Webservice/Index/init", "系统初始化", false),   //测试的


    /**
     * 验证用户名是否合法
     */
    CLIENT_VERIFY(2, "client_verify", "验证用户名是否合法", false),
    /**
     * 申请随机验证码
     */
    CODE_GET(3, "code_get", "申请随机验证码", false),
    /**
     * 验证随机码
     */
    CODE_VERIFY(4, "code_verify", "验证随机码", false),
    /**
     * 用户注册
     */
    CLIENT_ADD(5, "client_add", "用户注册", false),
    /**
     * 上传文件（图片，音频，视频）
     */
    FILE_UPLOAD(6, "file_upload", "上传文件（图片，音频，视频）", false),
    /**
     * 重设密码
     */
    PASSWORD_RESET(7, "password_reset", "重设密码", false),
    /**
     * 退出登录
     */
    CLIENT_LOGINOUT(8, "client_loginout", "退出登录", false),
    /**
     * 获取用户个人资料
     */
    CLIENT_GET(9, "client_get", "获取用户个人资料", false),
    /**
     * 保存用户资料
     */
    CLIENT_SAVE(10, "client_save", "保存用户资料", false),
    /**
     * 硬件注册保存
     */
    DEVICE_SAVE(11, "device_save", "硬件注册保存", false),
    /**
     * 阿里支付
     */
    ALIPAY(12, "OnlinePay/Alipay/alipaysign_get.php", "获取支付宝交易签名串", false),
    /**
     * 获取银联交易签名串
     */
    UNIONPAY(13, "OnlinePay/Unionpay/unionpay_get.php", "获取银联交易签名串", false),
    /**
     * 获取微信预支付交易会话标识
     */
    WEIXINPAY(14, "OnlinePay/Weixinpay/weixinpay_get.php", "获取微信预支付交易会话标识",
            false),
    /*
     * 首页banner列表
	 */
    BANNER_LIST(15, "ad_list", "Banner列表", false),
    /**
     * 类别列表
     */
    TYPE_LIST(16, "type_list", "类别列表", false),
    /**
     * 限时抢购商品列表
     */
    Limit_GOODS_LIST(17, "goods_list", "限时抢购商品列表", false),
    /**
     * 商品列表接口
     */
    GOODS_LIST(18, "goods_list", "商品列表接口", false),
    GOODSLIST(19, "goodslist", "商品首页列表", false),
    /**
     * 三级地址
     */
    DISTRICT_LIST(19, "addlistall_list", "三级地址", false),
    DISTRICT_LIST1(41, "district_list", "获取地区（城市）列表信息", false),
    /**
     * 用户签到
     */
    USER_SIGN(20, "check_add", "用户签到", false),
    /**
     * 标签列表
     */
    LABEL_LIST(21, "hotkeywords_get", "标签列表", false),
    /**
     * 签到可获取经验值接口
     */
    SIGN_VALUE_GET(22, "check_add", "签到可获取经验值接口", false),
    /**
     * 获取消息通知列表接口
     */
    NOTICE_LIST(23, "notice_list", "获取消息通知列表接口", false),
    /**
     * 保存用户通知操作接口
     */
    NOTICE_SAVEOPERATE(24, "notice_saveoperate", "保存用户通知操作接口", false),
    /**
     * 储值卡列表接口
     */
    CARD_LIST(25, "card_list", "储值卡列表接口", false),
    /**
     * 经典分类列表接口
     */
    TYPE_LIST_GET(26, "type_list", "经典分类列表接口", false),
    /**
     * 经典分类列表接口
     */
    TYPE_LIST_SUB_GET(26, "type_list_get", "经典分类子类列表接口", false),
    /**
     * 特色分类列表接口
     */
    CITY_TYPE_LIST(27, "city_type_list", "特色分类列表接口", false),
    /**
     * 抢购时间段列表接口
     */
    TIME_LIST(28, "time_list", "抢购时间段列表接口", false),
    /**
     * 地址列表接口
     */
    ADDRESS_LIST(29, "shipaddress_list", "地址列表接口", false),
    /**
     * 默认地址设置接口
     */
    ADDRESS_DEFAULT(30, "shipaddress_ope", "默认地址设置接口", false),
    /**
     * 删除相关接口
     */
    REMOVE(31, "remove", " 删除相关接口", false),
    /**
     * 地址添加接口
     */
    ADDRESS_SAVE(32, "shipaddress_save", " 地址保存", false),
    SHIPADDRESS_ADD(32, "shipaddress_add", "地址添加", false),
    /**
     * 商品详情接口
     */
    GOODS_GET(33, "goods_get", "商品详情接口", false),
    /**
     * 添加商品到购物车接口
     */
    CART_ADD(34, "cart_add", "添加商品到购物车接口", false),
    /**
     * 购物车列表接口
     */
    CART_LIST(35, "cart_list", "购物车列表接口", false),
    /**
     * 添加收藏
     */
    LOVE_ADD(36, "goods_ope", "添加收藏", false),
    /**
     * 取消收藏
     */
    LOVE_CANCEL(37, "collect_ope", "取消收藏", false),
    /**
     * 抢红包
     */
    REDBAG(38, "redbag", "抢红包", false),
    /**
     * 购物车操作
     */
    CART_OPERATE(39, "cart_ope", "购物车操作", false),
    /**
     * 加入购物清单
     */
    DETAILED_ADD(40, "detailed_add", "加入购物清单", false),
    /**
     * 清单列表
     */
    DETAILED_LIST(41, "detailed_list", "清单列表", false),
    /**
     * 抢购清单操作接口
     */
    DETAILED_OPERATE(42, "detailed_operate", "抢购清单操作接口", false),
    /**
     * 运费获取接口
     */
    GOODS_EXPRESSFEE_GET(43, "shipment_get", "费获取接口", false),
    /**
     * 我的代金券列表接口
     */
    VOUCHER_LIST(44, "vouchers_list", "我的代金券列表接口", false),
    /**
     * 订单生成
     */
    ORDER_ADD(45, "order_add", "购物车订单生成", false),
    FIRSTBUY(46, "firstbuy", "立即购买", false),
    /**
     * 订单列表接口
     */
    ORDER_LIST(46, "order_list", "订单列表接口", false),
    /**
     * 退款/售后 列表接口
     */
    RETURN_ORDER_LIST(47, "return_order_list", "退款/售后 列表接口", false),
    /**
     * \
     * 售后/退款详情接口
     */
    RETURN_ORDER_GET(48, "return_order_get", "售后/退款详情接口", false),
    /**
     * 订单详情接口
     */
    ORDER_GET(49, "order_get", "订单详情接口", false),
    /**
     * 客服留言
     */
    MESSAGE_ADD(50, "message_add", " 客服留言", false),
    /**
     * 意见反馈
     */
    ADVICE_ADD(51, "advice_add", "意见反馈", false),
    /**
     * 积分明细
     */
    SCORE_RECORD(52, "score_record", "积分明细", false),
    /**
     * 交易明细
     */
    RECORD_LIST(53, "record_list", "交易明细", false),
    /**
     * 我推荐的用户列表接口
     */
    RECOMMEND_LIST(54, "recommend_list", "我推荐的用户列表接口", false),
    /**
     * 银行卡列表
     */
    BANK_LIST(54, "bank_list", "银行卡列表", false),
    /**
     * 提现
     */
    CASH(54, "cash", "提现", false),
    /**
     * 重设支付密码
     */
    PAY_PASSWORD_ADD(55, "password_reset", "设置支付密码", false),
    /**
     * 密码修改
     */
    PASSWORD_SAVE(56, "password_save", "密码修改", false),
    /**
     * 余额支付
     */
    ORDER_PAY(57, "feeaccount_remove", "余额支付", false),
    /**
     * 储值卡充值
     */
    CARD_PAY(58, "card_pay", "储值卡充值", false),
    /**
     * 订单的操作
     */
    ORDER_OPE(59, "order_ope", "订单的操作", false),
    /**
     * 申请退款
     */
    ORDER_REFUND(60, "order_refund", "申请退款", false),
    /**
     * 原因列表
     */
    RETURN_REASON_LIST(61, "return_reason_list", "原因列表", false),
    /**
     * 评价商品
     */
    REPLY_ADD(62, "reply_add", "评价商品", false),
    /**
     * 评论列表
     */
    REPLY_LIST(63, "reply_list", "评价商品", false),
    /**
     * 红包详情接口
     */
    REDBAG_GET(64, "redbag_get", "红包详情接口", false),
    /**
     * 抢购订单
     */
    PACKAGEORDER_ADD(65, "packageorder_add", "抢购订单", false),
    /**
     * 直接购买订单生成接口
     */
    ORDER_DIRECTADD(66, "order_directadd", "直接购买订单生成接口", false),
    /**
     * 分享
     */
    SHARE(67, "share", "分享", false),
    /**
     * 提醒发货
     */
    REMINDER(68, "reminder", "提醒发货", false),
    /**
     * 积分兑换比例
     */
    PROPORTION_GET(69, "proportion_get", "积分兑换比例", false),
    /**
     * 银行卡信息接口接口
     */
    BANK_SAVE(70, "bank_save", "银行卡信息接口接口", false),
    /**
     * 支付宝信息接口接口
     */
    ALIPAY_SAVE(70, "alipay_save", "银行卡信息接口接口", false),
    /**
     * 第三方登录绑定手机号接口
     */
    MOBILE_SAVE(71, "mobile_save", "第三方登录绑定手机号接口", false),
    /**
     * 正在抢购商品列表
     */
    GOODS_LIST_LIMIT(72, "goods_list", "限时抢购", false),
    /**
     * 未读消息数接口
     */
    UNREAD_NOTICE(73, "unread_get", "未读消息数接口", false),
    /**
     * 邀请人手机号
     */
    CLIENT_CHECK(74, "client_check", "邀请人手机号", false),
    /**
     * 储值卡背景图获取接口
     */
    CARDIMG_GET(75, "cardimg_get", "储值卡背景图获取接口", false),
    /**
     * 充值卡
     */
    CARD_ADD(75, "card_add", "充值卡", false),
    /**
     * 收藏列表
     */
    MYCOLLECT_LIST(76, "mycollect_list", "收藏列表", false),
    /**
     * 储值卡列表
     */
    DEBITCARD_LIST(77, "debitcard_list", "储值卡列表", false),
    /**
     * 储值卡充值明细
     */
    DEBITCARD_RECORD(78, "debitcard_record", "储值卡充值明细", false),
    /**
     * 余额明细
     */
    FEEACCOUNT_LIST(79, "feeaccount_list", "余额明细", false),
    /**
     * 积分明细
     */
    SCORERECORD_LIST(80, "scorerecord_list", "积分明细", false),
    /**
     * 获取可用积分
     */
    SCORE_GET(81, "score_get", "获取可用积分", false),
    /**
     * 会员专区商品列表接口
     */
    MEMBERGOODS_LIST(82, "membergoods_list", "会员专区商品列表接口", false),
    /**
     * 评论列表接口
     */
    REPLY_LISTs(83, "reply_list", "评论列表接口", false),
    /**
     * 评论列表接口
     */
    ALLCOUPON_RECORD(84, "allcoupon_record", "评论列表接口", false),
    /**
     * 兑换代金券
     */
    COUPON_GET(84, "coupon_get", "兑换代金券", false),
    /**
     * 兑换商品生成订单接口
     */
    MEMBERORDER_ADD(85, "memberorder_add", "兑换商品生成订单接口", false),   //生成订单
    /**
     * 点券支付接口
     */
    ALLCOUPON_REMOVE(86, "allcoupon_remove", "点券支付接口", false),   //生成订单
    /**
     * 广告列表接口
     */
    AD_LIST(87, "ad_list", "广告列表接口", false),
    /**
     * 积分兑换金额接口
     */
    EXCHANGEMONEY(87, "exchangemoney", "积分兑换金额接口", false),
    /**
     * 积分兑换金额接口
     */
    CODE_ADD(88, "code_add", "生成二维码接口", false),
    /**
     * 余额提现接口
     */
    CASH_ADD(89, "cash_add", "余额提现接口", false),
    /**
     * 保存支付宝信息接口
     */
    ALI_SAVE(90, "alipay_save", "保存支付宝信息接口", false),
    /**
     * 下线用户的消费记录接口
     * */
    LINECLIENT_RECORD(91, "lineclient_record", "下线用户的消费记录接口", false),
    ;


    private int id;// 对应NetTask的id
    private String urlPath;// 请求地址
    private String description;// 请求描述
    private boolean isRootPath;// 是否是根路径

    BaseHttpInformation(int id, String urlPath, String description,
                        boolean isRootPath) {
        this.id = id;
        this.urlPath = urlPath;
        this.description = description;
        this.isRootPath = isRootPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrlPath() {
        if (isRootPath)
            return urlPath;

        String path = SYS_ROOT.urlPath + urlPath;

        if (this.equals(INIT))
            return path;

        BaseApplication application = BaseApplication.getInstance();
        SysInitInfo info = application.getSysInitInfo();
        path = info.getSys_web_service() + urlPath;

        if (this.equals(ALIPAY))
            path = info.getSys_plugins() + urlPath;

        if (this.equals(UNIONPAY))
            path = info.getSys_plugins() + urlPath;

        if (this.equals(WEIXINPAY))
            path = info.getSys_plugins() + urlPath;

        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

}
