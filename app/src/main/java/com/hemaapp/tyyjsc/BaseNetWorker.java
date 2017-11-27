package com.hemaapp.tyyjsc;

import android.content.Context;

import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.tyyjsc.nettask.AdListTask;
import com.hemaapp.tyyjsc.nettask.AddressListTask;
import com.hemaapp.tyyjsc.nettask.AddressThirdInfoTask;
import com.hemaapp.tyyjsc.nettask.AlipayTradeTask;
import com.hemaapp.tyyjsc.nettask.AllcouponRecordTask;
import com.hemaapp.tyyjsc.nettask.AllcouponRemoveTask;
import com.hemaapp.tyyjsc.nettask.BankListTask;
import com.hemaapp.tyyjsc.nettask.CardInTask;
import com.hemaapp.tyyjsc.nettask.CartGoodsInfoListTask;
import com.hemaapp.tyyjsc.nettask.ClientAddTask;
import com.hemaapp.tyyjsc.nettask.ClientGetTask;
import com.hemaapp.tyyjsc.nettask.ClientLoginTask;
import com.hemaapp.tyyjsc.nettask.ClientLoginoutTask;
import com.hemaapp.tyyjsc.nettask.ClientSaveTask;
import com.hemaapp.tyyjsc.nettask.ClientVerifyTask;
import com.hemaapp.tyyjsc.nettask.CodeAddTask;
import com.hemaapp.tyyjsc.nettask.CodeGetTask;
import com.hemaapp.tyyjsc.nettask.CodeVerifyTask;
import com.hemaapp.tyyjsc.nettask.CommentListTask;
import com.hemaapp.tyyjsc.nettask.CoupongetTask;
import com.hemaapp.tyyjsc.nettask.DealItemListTask;
import com.hemaapp.tyyjsc.nettask.DeviceSaveTask;
import com.hemaapp.tyyjsc.nettask.DistrictListTask;
import com.hemaapp.tyyjsc.nettask.Exchangemoney;
import com.hemaapp.tyyjsc.nettask.FileUploadTask;
import com.hemaapp.tyyjsc.nettask.GoodsInfoTask;
import com.hemaapp.tyyjsc.nettask.GoodsListNoPageTask;
import com.hemaapp.tyyjsc.nettask.GoodsListTask;
import com.hemaapp.tyyjsc.nettask.GoodsListTask1;
import com.hemaapp.tyyjsc.nettask.ImgInfoGetTask;
import com.hemaapp.tyyjsc.nettask.ImgListTask;
import com.hemaapp.tyyjsc.nettask.InitTask;
import com.hemaapp.tyyjsc.nettask.LabelInfoListTask;
import com.hemaapp.tyyjsc.nettask.LastFeeTask;
import com.hemaapp.tyyjsc.nettask.LineClientRecordTask;
import com.hemaapp.tyyjsc.nettask.MembergoodsListTask;
import com.hemaapp.tyyjsc.nettask.MemberorderAddTask;
import com.hemaapp.tyyjsc.nettask.NoResultReturnTask;
import com.hemaapp.tyyjsc.nettask.NoticeListTask;
import com.hemaapp.tyyjsc.nettask.NoticeSaveoperateTask;
import com.hemaapp.tyyjsc.nettask.OrderAddTask;
import com.hemaapp.tyyjsc.nettask.OrderInfoTask;
import com.hemaapp.tyyjsc.nettask.OrderListTask;
import com.hemaapp.tyyjsc.nettask.PasswordResetTask;
import com.hemaapp.tyyjsc.nettask.ReasonListTask;
import com.hemaapp.tyyjsc.nettask.RecommendListTask;
import com.hemaapp.tyyjsc.nettask.RedBagTask;
import com.hemaapp.tyyjsc.nettask.ReplyIdTask;
import com.hemaapp.tyyjsc.nettask.ReplyListTask;
import com.hemaapp.tyyjsc.nettask.SaveCardInfoTask;
import com.hemaapp.tyyjsc.nettask.SignInfoTask;
import com.hemaapp.tyyjsc.nettask.SignValueInfoTask;
import com.hemaapp.tyyjsc.nettask.ThirdSaveTask;
import com.hemaapp.tyyjsc.nettask.TimeInfoTask;
import com.hemaapp.tyyjsc.nettask.TypeListTask;
import com.hemaapp.tyyjsc.nettask.UnionTradeTask;
import com.hemaapp.tyyjsc.nettask.VoucherInfoListTask;
import com.hemaapp.tyyjsc.nettask.WeixinTradeTask;

import java.util.HashMap;

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomDeviceUuidFactory;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * 实例化网络请求工具类
 * 凡是手工输入的都需要进行MD5加密处理
 */
public class BaseNetWorker extends HemaNetWorker {
    /**
     * 实例化网络请求工具类
     *
     * @param mContext
     */
    private Context mContext;

    public BaseNetWorker(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public void clientLogin() {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<>();
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        params.put("username", username);// 用户登录名 手机号或邮箱
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        params.put("password", password); // 登陆密码
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

        BaseNetTask task = new ClientLoginTask(information, params);
        executeTask(task);
    }

    @Override
    public boolean thirdSave() {
        if (HemaUtil.isThirdSave(mContext)) {
            BaseHttpInformation information = BaseHttpInformation.THIRD_SAVE;
            HashMap<String, String> params = new HashMap<>();
            params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
            String version = HemaUtil.getAppVersionForSever(mContext);
            params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
            String thirdtype = XtomSharedPreferencesUtil.get(mContext,
                    "thirdtype");
            params.put("thirdtype", thirdtype);// 平台类型 1：微信 2：QQ 3：微博
            String thirduid = XtomSharedPreferencesUtil.get(mContext,
                    "thirduid");
            params.put("thirduid", thirduid);// 平台用户id 该平台唯一的id
            String avatar = XtomSharedPreferencesUtil.get(mContext, "avatar");
            params.put("avatar", avatar);// 平台用户头像 图片地址
            String nickname = XtomSharedPreferencesUtil.get(mContext,
                    "nickname");
            params.put("nickname", nickname);// 平台用户昵称
            String sex = XtomSharedPreferencesUtil.get(mContext, "sex");
            params.put("sex", sex);// 姓名 "男"或"女"
            String age = XtomSharedPreferencesUtil.get(mContext, "age");
            params.put("age", age);// 年龄

            BaseNetTask task = new ThirdSaveTask(information, params);
            executeTask(task);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取地区（城市）列表信息
     */
    public void districtList(String parentid) {
        BaseHttpInformation information = BaseHttpInformation.DISTRICT_LIST1;
        HashMap<String, String> params = new HashMap<>();
        params.put("parentid", parentid);
        BaseNetTask task = new DistrictListTask(information, params);
        executeTask(task);
    }

    /**
     * 第三方登录
     */
    public void thirdSave(String thirdtype, String thirduid, String avatar,
                          String nickname, String sex, String age) {
        BaseHttpInformation information = BaseHttpInformation.THIRD_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        params.put("thirdtype", thirdtype);// 平台类型 1：微信 2：QQ
        params.put("thirduid", thirduid);// 平台用户id 该平台唯一的id
        params.put("avatar", avatar);// 平台用户头像 图片地址
        params.put("nickname", nickname);// 平台用户昵称
        params.put("sex", sex);// 姓名 "男"或"女"
        params.put("age", age);// 年龄

        BaseNetTask task = new ThirdSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 用户登录
     */
    public void clientLogin(String username, String password) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGIN;

        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号
        params.put("keytype", "1");// 用户登录名 手机号或邮箱
        params.put("password", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(password))); // 登陆密码 服务器端存储的是32位的MD5加密串 Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(password))
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        BaseNetTask task = new ClientLoginTask(information, params);
        executeTask(task);
    }

    /**
     * 系统初始化
     */
    public void init() {
        BaseHttpInformation information = BaseHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<>();
        params.put("lastloginversion", HemaUtil.getAppVersionForSever(mContext));// 版本号码(默认：1.0.0)
        params.put("device_sn", XtomDeviceUuidFactory.get(mContext));// 客户端硬件串号
        params.put("device_mac", "");// 客户端硬件串号
        BaseNetTask task = new InitTask(information, params);
        executeTask(task);
    }

    /**
     * 验证用户名是否合法
     */
    public void clientVerify(String username) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        BaseNetTask task = new ClientVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * 获取随机码
     */
    public void codeGet(String username) {
        BaseHttpInformation information = BaseHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱

        BaseNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 验证随机码
     */
    public void codeVerify(String username, String code) {
        BaseHttpInformation information = BaseHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);// 用户登录名 手机号或邮箱
        params.put("code", code);// 6位随机号码 测试阶段固定向服务器提交“123456”
        BaseNetTask task = new CodeVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * 用户注册
     */
    public void clientAdd(String temp_token, String username, String password,
                          String nickname, String sex) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_token", temp_token);// 临时令牌 可以有效防止机器人恶意注册（该值从验证随机码接口获取）
        params.put("username", username);// 用户注册名
        params.put("password", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(password)));// 登陆密码
        params.put("nickname", nickname);// 用户昵称
        params.put("sex", sex);// 性别 男或女
        BaseNetTask task = new ClientAddTask(information, params);
        executeTask(task);
    }

    /**
     * 上传文件（图片，音频，视频）
     */
    public void fileUpload(String token, String keytype, String keyid,
                           String duration, String orderby, String content, String temp_file) {
        BaseHttpInformation information = BaseHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);//
        params.put("keytype", keytype); //
        params.put("keyid", keyid); //
        params.put("duration", duration); //
        params.put("orderby", orderby); //
        params.put("content", content);// 内容描述 有的项目中，展示性图片需要附属一段文字说明信息。默认传"无"
        HashMap<String, String> files = new HashMap<>();
        files.put("temp_file", temp_file); //

        BaseNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }

    /**
     * 重设密码
     */
    public void passwordReset(String temp_token, String new_password,
                              String keytype) {
        BaseHttpInformation information = BaseHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_token", temp_token);// 临时令牌
        params.put("new_password", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(new_password)));// 新密码
        params.put("keytype", keytype);// 密码类型 1：登陆密码 2：支付密码
        BaseNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
    }

    /**
     * 退出登录
     */
    public void clientLoginout(String token) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌

        BaseNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 获取用户个人资料
     */
    public void clientGet(String token, String id) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id); // 用户id

        BaseNetTask task = new ClientGetTask(information, params);
        executeTask(task);
    }

    /**
     * 保存用户资料
     */
    public void clientSave(String token, String nickname, String sex) {
        BaseHttpInformation information = BaseHttpInformation.CLIENT_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("nickname", nickname);// 用户昵称
        params.put("sex", sex);// 性别

        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 硬件注册保存
     */
    public void deviceSave(String token, String deviceid, String devicetype,
                           String channelid) {
        BaseHttpInformation information = BaseHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("deviceid", deviceid);// 登陆手机硬件码 对应百度推送userid
        params.put("devicetype", devicetype);// 登陆手机类型 1:苹果 2:安卓
        params.put("channelid", channelid);// 百度推送渠道id 方便直接从百度后台进行推送测试
        BaseNetTask task = new DeviceSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 图片列表
     */
    public void imgList(String keytype) {
        BaseHttpInformation information = BaseHttpInformation.BANNER_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        BaseNetTask task = new ImgListTask(information, params);
        executeTask(task);
    }

    /**
     * 商品列表
     */
    public void getGoodsList(String keytype, String id, String keyword, String page, String orderby) {
        BaseHttpInformation information = BaseHttpInformation.GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("id", id);
        params.put("keyword", keyword);
        params.put("orderby", orderby);
        params.put("page", page);
        BaseNetTask task = new GoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 套餐详情内，套餐内的商品列表
     */
    public void getGoodsList1(String keytype, String id, String keyword, String page, String orderby) {
        BaseHttpInformation information = BaseHttpInformation.GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("id", id);
        params.put("keyword", keyword);
        params.put("orderby", orderby);
        params.put("page", page);
        BaseNetTask task = new GoodsListTask1(information, params);
        executeTask(task);
    }

    //首页商品列表
    public void getIndexGoodsList(String keytype) {
        BaseHttpInformation information = BaseHttpInformation.GOODSLIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        BaseNetTask task = new GoodsListNoPageTask(information, params);
        executeTask(task);
    }

    //不分页
    public void getGoodsListNoPage(String keytype, String id, String keyword, String page) {
        BaseHttpInformation information = BaseHttpInformation.GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("id", id);
        params.put("keyword", keyword);
        params.put("page", page);
        BaseNetTask task = new GoodsListNoPageTask(information, params);
        executeTask(task);
    }

    /**
     * 三级地址
     */
    public void getAddress(String keytype, String parentid) {
        BaseHttpInformation information = BaseHttpInformation.DISTRICT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("parentid", parentid);
        BaseNetTask task = new AddressThirdInfoTask(information, params);
        executeTask(task);
    }

    /**
     * 热门搜索
     */
    public void getLabelList() {
        BaseHttpInformation information = BaseHttpInformation.LABEL_LIST;
        HashMap<String, String> params = new HashMap<>();
        BaseNetTask task = new LabelInfoListTask(information, params);
        executeTask(task);
    }

    /**
     * 签到可获取经验值接口
     *
     * @param token
     */
    public void getSignValue(String token) {
        BaseHttpInformation information = BaseHttpInformation.SIGN_VALUE_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        BaseNetTask task = new SignValueInfoTask(information, params);
        executeTask(task);
    }

    /**
     * 获取通知列表
     */
    public void getNoticeList(String token, String noticetype, String page) {
        BaseHttpInformation information = BaseHttpInformation.NOTICE_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("noticetype", noticetype);
        params.put("page", page);
        BaseNetTask task = new NoticeListTask(information, params);
        executeTask(task);
    }

    /**
     * 通知操作
     */
    public void noticeSaveoperate(String token, String id, String keytype, String keyid, String operatetype) {
        BaseHttpInformation information = BaseHttpInformation.NOTICE_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("operatetype", operatetype);
        BaseNetTask task = new NoticeSaveoperateTask(information, params);
        executeTask(task);
    }

    /**
     * @param parentid 传0：表示获取第一级别; 对应分类下parentid为第一级分类主键id
     */
    public void getTypeList(String parentid) {
        BaseHttpInformation information = BaseHttpInformation.TYPE_LIST_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", parentid);
        BaseNetTask task = new TypeListTask(information, params);
        executeTask(task);
    }

    /**
     * 地址列表
     */
    public void getAddressList(String token) {
        BaseHttpInformation information = BaseHttpInformation.ADDRESS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        BaseNetTask task = new AddressListTask(information, params);
        executeTask(task);
    }

    /**
     * 设置默认地址
     */
    public void setDefAddress(String token, String keytype, String address_id) {
        BaseHttpInformation information = BaseHttpInformation.ADDRESS_DEFAULT;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", address_id);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 删除相关接口
     */
    public void remove(String token, String keytype, String keyid) {
        BaseHttpInformation information = BaseHttpInformation.REMOVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 地址添加或保存接口
     */

    public void addressSave(String token, String id, String name, String tel, String zip_code, String provinces, String address) {
        BaseHttpInformation information = BaseHttpInformation.ADDRESS_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("name", name);
        params.put("tel", tel);
        params.put("zipcode", zip_code);
        params.put("provinces", provinces);
        params.put("address", address);
        BaseNetTask task = new SignValueInfoTask(information, params);
        executeTask(task);
    }

    //添加收货地址
    public void addressAdd(String token, String name, String tel, String zip_code, String provinces, String address) {
        BaseHttpInformation information = BaseHttpInformation.SHIPADDRESS_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("name", name);
        params.put("tel", tel);
        params.put("zipcode", zip_code);
        params.put("provinces", provinces);
        params.put("address", address);
        BaseNetTask task = new SignValueInfoTask(information, params);
        executeTask(task);
    }

    /**
     * 商品详情
     */
    public void getGoodsInfo(String id, String keytype) {
        BaseHttpInformation information = BaseHttpInformation.GOODS_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("keytype", keytype);
        BaseNetTask task = new GoodsInfoTask(information, params);
        executeTask(task);
    }

    /**
     * 加入购物车
     */
    public void cartAdd(String token, String keytype, String goodsid, String specs_id, String buycount) {
        BaseHttpInformation information = BaseHttpInformation.CART_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("goodsid", goodsid);
        params.put("propertyid", specs_id);
        params.put("buycount", buycount);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 添加收藏
     */
    public void loveAdd(String token, String keytype, String type, String keyid) {
        BaseHttpInformation information = BaseHttpInformation.LOVE_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("type", type);
        params.put("id", keyid);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    //收藏列表
    public void mycollect_list(String token, String page) {
        BaseHttpInformation information = BaseHttpInformation.MYCOLLECT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        BaseNetTask task = new GoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 取消收藏
     */
    public void loveCancel(String token, String keytype, String keyid) {
        BaseHttpInformation information = BaseHttpInformation.LOVE_CANCEL;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", keyid);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车列表
     */

    public void getCartGoodsList(String token, String keytype) {
        BaseHttpInformation information = BaseHttpInformation.CART_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        BaseNetTask task = new CartGoodsInfoListTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车操作
     */
    public void cartOperate(String token, String keytype, String cart_id, String buycount) {
        BaseHttpInformation information = BaseHttpInformation.CART_OPERATE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("cartid", cart_id);
        params.put("buycount", buycount);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 获取运费
     */
    public void getExpressFee(String token, String address, String shopid, String allweight) {
        BaseHttpInformation information = BaseHttpInformation.GOODS_EXPRESSFEE_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("address", address);
        params.put("shopid", shopid);
        params.put("allweight", allweight);

        BaseNetTask task = new SignValueInfoTask(information, params);
        executeTask(task);
    }

    /**
     * 代金券列表
     */
    public void getVourcher(String token, String keytype, String page) {
        BaseHttpInformation information = BaseHttpInformation.VOUCHER_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("page", page);
        BaseNetTask task = new VoucherInfoListTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车-提交订单
     */
    public void orderAdd(String token, String keytype, String cart_id, String address_id, String voucher_id, String money, String type, String remark) {
        BaseHttpInformation information = BaseHttpInformation.ORDER_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("cartid", cart_id);
        params.put("addressid", address_id);
        params.put("vouchersid", voucher_id);
        params.put("money", money);
        params.put("type", type);
        params.put("memo", remark);
        BaseNetTask task = new OrderAddTask(information, params);
        executeTask(task);
    }

    //立即购买
    public void firstbuy(String token, String keytype, String cart_id, String propertyid, String buycount, String address_id, String voucher_id, String type, String remark) {
        BaseHttpInformation information = BaseHttpInformation.FIRSTBUY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("goodsid", cart_id);
        params.put("propertyid", propertyid);
        params.put("buycount", buycount);
        params.put("addressid", address_id);
        params.put("vouchersid", voucher_id);
        params.put("type", type);
        params.put("memo", remark);
        BaseNetTask task = new OrderAddTask(information, params);
        executeTask(task);
    }

    /**
     * 套餐清单-提交订单
     */
    public void packageorderAdd(String token, String id, String addressid, String type, String memo, String vouchers) {
        BaseHttpInformation information = BaseHttpInformation.PACKAGEORDER_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("addressid", addressid);
        params.put("type", type);
        params.put("memo", memo);
        params.put("vouchers", vouchers);
        BaseNetTask task = new OrderAddTask(information, params);
        executeTask(task);
    }

    /**
     * @param token  登录令牌
     * @param status 获取全部订单列表的话该字段可不传    1:未付款    2:已付款(未消费)    3:待评价    4:已完成    5:已过期    6:已关闭
     * @param page   页码 	从0开始
     */
    public void getOrderList(String token, String keytype, String status, String page) {
        BaseHttpInformation information = BaseHttpInformation.ORDER_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("status", status);
        params.put("page", page);
        BaseNetTask task = new OrderListTask(information, params);
        executeTask(task);
    }

    /**
     * 订单详情
     */
    public void getOrderInfo(String token, String id) {
        BaseHttpInformation information = BaseHttpInformation.ORDER_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("orderid", id);
        BaseNetTask task = new OrderInfoTask(information, params);
        executeTask(task);
    }

    /**
     * 意见反馈
     */
    public void adviceAdd(String token, String device, String version, String brand, String system, String content) {
        BaseHttpInformation information = BaseHttpInformation.ADVICE_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("device", device);
        params.put("version", version);
        params.put("brand", brand);
        params.put("system", system);
        params.put("content", content);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    //充值卡充值明细
    public void debitcard_record(String token, String page) {
        BaseHttpInformation information = BaseHttpInformation.DEBITCARD_RECORD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        BaseNetTask task = new DealItemListTask(information, params);
        executeTask(task);
    }

    //余额明细
    public void feeaccount_list(String token, String page) {
        BaseHttpInformation information = BaseHttpInformation.FEEACCOUNT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        BaseNetTask task = new LastFeeTask(information, params);
        executeTask(task);
    }

    public void scorerecord_list(String token, String page) {
        BaseHttpInformation information = BaseHttpInformation.SCORERECORD_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        BaseNetTask task = new DealItemListTask(information, params);
        executeTask(task);
    }

    /**
     * 银行卡列表
     */
    public void getBankList() {
        BaseHttpInformation information = BaseHttpInformation.BANK_LIST;
        HashMap<String, String> params = new HashMap<>();

        BaseNetTask task = new BankListTask(information, params);
        executeTask(task);
    }

    /**
     * 设置支付密码
     */
    public void payPasswordAdd(String token, String keytype, String pay_password) {
        BaseHttpInformation information = BaseHttpInformation.PAY_PASSWORD_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_token", token);
        params.put("keytype", keytype);
        params.put("new_password", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(pay_password)));
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 修改密码
     */
    public void passwordSave(String token, String keytype, String old_password, String new_password) {
        BaseHttpInformation information = BaseHttpInformation.PASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("old_password", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(old_password)));
        params.put("new_password", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(new_password)));
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 推荐用户
     */
    public void getRecommentList(String token, String page) {
        BaseHttpInformation information = BaseHttpInformation.RECOMMEND_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", page);
        BaseNetTask task = new RecommendListTask(information, params);
        executeTask(task);
    }

    /**
     * 余额支付
     */
    public void orderPay(String token, String pay_password, String order_id, String pay_type) {
        BaseHttpInformation information = BaseHttpInformation.ORDER_PAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("paypassword", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(pay_password)));
        params.put("orderid", order_id);
        params.put("keytype", pay_type);


        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 确认收货
     */
    public void OrderOperation(String token, String keytype, String order_id) {
        BaseHttpInformation information = BaseHttpInformation.ORDER_OPE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("orderid", order_id);
        BaseNetTask task = new ReplyIdTask(information, params);
        executeTask(task);
    }

    /**
     * 获取支付宝交易签名串
     */
    public void alipay(String token, String paytype, String keytype,
                       String keyid, String total_fee) {
        BaseHttpInformation information = BaseHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", paytype);// 支付类型 固定传1
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)

        BaseNetTask task = new AlipayTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取银联交易签名串
     */
    public void unionpay(String token, String paytype, String keytype,
                         String keyid, String total_fee) {
        BaseHttpInformation information = BaseHttpInformation.UNIONPAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", paytype);// 支付类型 固定传2
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)

        BaseNetTask task = new UnionTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 获取微信预支付交易会话标识
     */
    public void weixinpay(String token, String paytype, String keytype,
                          String keyid, String total_fee) {
        BaseHttpInformation information = BaseHttpInformation.WEIXINPAY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);// 登陆令牌
        params.put("paytype", paytype);// 支付类型 固定传3
        params.put("keytype", keytype);// 业务类型,1：账户余额充值2：商品立即购买
        params.put("keyid", keyid);// 业务相关,id当keytype=1时,keyid=0当keytype=2时,keyid=blog_id
        params.put("total_fee", total_fee);// 支付交易金额,单位：元(测试时统一传递0.01元)

        BaseNetTask task = new WeixinTradeTask(information, params);
        executeTask(task);
    }

    /**
     * 商品评价
     */
    public void replyAdd(String token, String keytype, String keyid, String orderid, String reply_star, String content, String parentid,
                         String is_incognito) {
        BaseHttpInformation information = BaseHttpInformation.REPLY_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("orderid", orderid);
        params.put("replytype", reply_star);
        params.put("content", content);
        params.put("parentid", parentid);
        params.put("is_incognito", is_incognito);
        BaseNetTask task = new ReplyIdTask(information, params);
        executeTask(task);
    }

    public void score_get(String token, String cartid) {
        BaseHttpInformation information = BaseHttpInformation.SCORE_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("cartid", cartid);
        BaseNetTask task = new ReplyIdTask(information, params);
        executeTask(task);
    }

    /**
     * 获取评论列表
     */
    public void getCommentList(String keytype, String keyid, String page) {
        BaseHttpInformation information = BaseHttpInformation.REPLY_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("page", page);
        BaseNetTask task = new CommentListTask(information, params);
        executeTask(task);
    }

    /**
     * 分享
     */
    public void share(String token, String keyid) {
        BaseHttpInformation information = BaseHttpInformation.SHARE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keyid", keyid);
        BaseNetTask task = new OrderAddTask(information, params);
        executeTask(task);
    }

    /**
     * 银行卡信息接口接口
     */
    public void bankSave(String token, String bank, String bankuser, String bankname, String bankcard) {
        BaseHttpInformation information = BaseHttpInformation.BANK_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("bank", bank);
        params.put("bankuser", bankuser);
        params.put("bankname", bankname);
        params.put("bankcard", bankcard);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    /**
     * 未读消息数接口
     */
    public void getUnReadNoticeNum(String token) {
        BaseHttpInformation information = BaseHttpInformation.UNREAD_NOTICE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        BaseNetTask task = new ReplyIdTask(information, params);
        executeTask(task);
    }

    /**
     * 充值卡充值接口
     */
    public void cardAdd(String token, String card, String verification_code) {
        BaseHttpInformation information = BaseHttpInformation.CARD_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("card", card);
        params.put("verification_code", verification_code);
        BaseNetTask task = new ClientSaveTask(information, params);
        executeTask(task);
    }

    public void debitcard_list(String page) {
        BaseHttpInformation information = BaseHttpInformation.DEBITCARD_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        BaseNetTask task = new CardInTask(information, params);
        executeTask(task);
    }

    /**
     * 会员专区商品列表接口
     */
    public void membergoodsList(String keytype, String id, String page) {
        BaseHttpInformation information = BaseHttpInformation.MEMBERGOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        params.put("id", id);
        params.put("page", page);

        BaseNetTask task = new MembergoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 点券记录
     */
    public void allcouponecord(String token) {
        BaseHttpInformation information = BaseHttpInformation.ALLCOUPON_RECORD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        BaseNetTask task = new AllcouponRecordTask(information, params);
        executeTask(task);
    }

    /**
     * 兑换代金券接口
     */
    public void Couponget(String token, String num) {
        BaseHttpInformation information = BaseHttpInformation.COUPON_GET;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("num", num);
        BaseNetTask task = new CoupongetTask(information, params);
        executeTask(task);
    }

    /**
     * 兑换商品生成订单接口
     */
    public void memberorderAdd(String token, String id, String propertyid, String addressid, String memo, String buycount) {
        BaseHttpInformation information = BaseHttpInformation.MEMBERORDER_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("propertyid", propertyid);
        params.put("addressid", addressid);
        params.put("memo", memo);
        params.put("buycount", buycount);
        BaseNetTask task = new MemberorderAddTask(information, params);
        executeTask(task);
    }

    /**
     * 点券支付接口
     */

    public void allcouponRemove(String token, String id, String paypassword) {
        BaseHttpInformation information = BaseHttpInformation.ALLCOUPON_REMOVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("id", id);
        params.put("paypassword", Md5Util.getMd5(XtomConfig.DATAKEY + Md5Util.getMd5(paypassword)));

        BaseNetTask task = new AllcouponRemoveTask(information, params);
        executeTask(task);
    }

    /**
     * 点券支付接口
     */

    public void adList(String keytype) {
        BaseHttpInformation information = BaseHttpInformation.AD_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("keytype", keytype);
        BaseNetTask task = new AdListTask(information, params);
        executeTask(task);
    }

    /**
     * 积分兑换金额接口
     */

    public void exchangemoney(String token) {
        BaseHttpInformation information = BaseHttpInformation.EXCHANGEMONEY;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        BaseNetTask task = new Exchangemoney(information, params);
        executeTask(task);
    }

    /**
     * 生成二维码接口
     */

    public void codeAdd(String token) {
        BaseHttpInformation information = BaseHttpInformation.CODE_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        BaseNetTask task = new CodeAddTask(information, params);
        executeTask(task);
    }

    /**
     * 余额申请提现
     */
    public void cashAdd(String token, String applyfee, String keytype, String paypassword) {
        BaseHttpInformation information = BaseHttpInformation.CASH_ADD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("applyfee", applyfee);
        params.put("paypassword", Md5Util.getMd5(XtomConfig.DATAKEY
                + Md5Util.getMd5(paypassword)));

        BaseNetTask task = new NoResultReturnTask(information, params);
        executeTask(task);
    }

    /**
     * 保存支付宝信息接口
     */
    public void aliSave(String token, String alipayname) {
        BaseHttpInformation information = BaseHttpInformation.ALI_SAVE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("alipayname", alipayname);

        BaseNetTask task = new NoResultReturnTask(information, params);
        executeTask(task);
    }

    /**
     * 下线用户的消费记录接口
     */
    public void lineClientRecord(String token, int page) {
        BaseHttpInformation information = BaseHttpInformation.LINECLIENT_RECORD;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("page", String.valueOf(page));

        BaseNetTask task = new LineClientRecordTask(information, params);
        executeTask(task);
    }
}