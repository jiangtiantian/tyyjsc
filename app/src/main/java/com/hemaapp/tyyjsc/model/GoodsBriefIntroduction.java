package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 商品列表实体类
 */
public class GoodsBriefIntroduction extends XtomObject implements Serializable {

    private String id;//	商品主键id
    private String name;//	商品名称
    private String price;//	商品价格
    private String originalprice;//商品原价
    private String specs;//	规格
    private String stock;// 	库存量
    private String imgurl;//	商品图片
    private String displaysales;//展示销量
    private String realsale;//实际销量
    private String injectsale;//注入销量
    private String buytimememo;
    private String num;//套餐商品数量

    private String limitnum;
    private String shiptime;//配送时间
    private String waitday;//等待天数
    private String shipdays;//发货间隔
    private String fhtimes;//发货次数
    private String imgurlbig;//商品大图片
    private String sum_volume;//	商品总销数量
    private String sales_volume;//销量（猜你喜欢栏目）
    private String number;//套餐数量
    private String keytype;//商品类型

    //超值套餐中的商品列表
    private String goods_id;//	商品主键goods_id
    private String package_id;//
    private String property;
    //订单详情
    private String buycount;
    private int status = -2;//本地字段
    private String begintime;//开始时间
    private String endtime;//结束时间
    private String nowtime;//服务器时间
    private String is_display = "";//是否显示库存 1 显示 2 隐藏


    private String goodsid;


    public GoodsBriefIntroduction(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                price = get(jsonObject, "price");
                originalprice = get(jsonObject, "originalprice");
                specs = get(jsonObject, "specs");
                stock = get(jsonObject, "stock");
                imgurl = get(jsonObject, "imgurl");
                imgurlbig = get(jsonObject, "imgurlbig");
                sum_volume = get(jsonObject, "sum_volume");
                sales_volume = get(jsonObject, "sales_volume");
                goods_id = get(jsonObject, "goods_id");
                package_id = get(jsonObject, "package_id");
                buycount = get(jsonObject, "buycount");
                number = get(jsonObject, "number");
                keytype = get(jsonObject, "keytype");
                begintime = get(jsonObject, "begintime");
                endtime = get(jsonObject, "endtime");
                nowtime = get(jsonObject, "nowtime");
                is_display = get(jsonObject, "is_display");
                displaysales = get(jsonObject, "displaysales");
                realsale = get(jsonObject, "realsale");
                injectsale = get(jsonObject, "injectsale");
                buytimememo = get(jsonObject, "buytimememo");
                limitnum = get(jsonObject, "limitnum");
                num=get(jsonObject,"num");
                shiptime = get(jsonObject, "shiptime");
                shipdays = get(jsonObject, "shipdays");
                fhtimes=get(jsonObject,"fhtimes");
                property = get(jsonObject, "property");

                goodsid = get(jsonObject, "goodsid");

                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public GoodsBriefIntroduction(String id, String name, String imgurl, String price, String old_price, String sum_volume){
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.originalprice = old_price;
        this.sum_volume = sum_volume;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(String originalprice) {
        this.originalprice = originalprice;
    }
    public String getDisplaysales() {
        return displaysales;
    }

    public void setDisplaysales(String displaysales) {
        this.displaysales = displaysales;
    }

    public String getRealsale() {
        return realsale;
    }

    public void setRealsale(String realsale) {
        this.realsale = realsale;
    }

    public String getInjectsale() {
        return injectsale;
    }

    public void setInjectsale(String injectsale) {
        this.injectsale = injectsale;
    }

    public String getBuytimememo() {
        return buytimememo;
    }

    public void setBuytimememo(String buytimememo) {
        this.buytimememo = buytimememo;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public String getLimitnum() {
        return limitnum;
    }

    public void setLimitnum(String limitnum) {
        this.limitnum = limitnum;
    }

    public String getShiptime() {
        return shiptime;
    }

    public void setShiptime(String shiptime) {
        this.shiptime = shiptime;
    }

    public String getWaitday() {
        return waitday;
    }

    public void setWaitday(String waitday) {
        this.waitday = waitday;
    }

    public String getShipdays() {
        return shipdays;
    }

    public void setShipdays(String shipdays) {
        this.shipdays = shipdays;
    }

    public String getFhtimes() {
        return fhtimes;
    }

    public void setFhtimes(String fhtimes) {
        this.fhtimes = fhtimes;
    }

    public String getNowtime() {
        return nowtime;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProperty() {
        return property;
    }

    public void setOld_price(String old_price) {
        this.originalprice = old_price;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setSum_volume(String sum_volume) {
        this.sum_volume = sum_volume;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getSpecs() {
        return specs;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getStock() {
        return stock;
    }

    public String getImgurl() {
        return imgurl;
    }
    public String getImgurlbig() {
        return imgurlbig;
    }

    public String getSum_volume() {
        return sum_volume;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public String getOld_price() {
        return originalprice;
    }

    public int getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getPackage_id() {
        return package_id;
    }


    public String getBuycount() {
        return buycount;
    }

    public String getIs_display() {
        return is_display;
    }

    @Override
    public String toString() {
        return "GoodsBriefIntroduction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", old_price='" + originalprice + '\'' +
                ", specs='" + specs + '\'' +
                ", stock='" + stock + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", sum_volume='" + sum_volume + '\'' +
                ", status=" + status +
                '}';
    }
}
