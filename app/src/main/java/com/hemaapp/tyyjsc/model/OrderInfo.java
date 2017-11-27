package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 订单实体类
 */

public class OrderInfo extends XtomObject implements Serializable {
    private String id;//订单id
    private String ordernum;//订单编号
    private String status;//订单状态1：待付款2：待发货3：待收货4：待评价5：已完成6：已关闭7：退款申请中8：退款成功9：退款失败
    private String money;//订单总付款
    private String ordertime;//下单时间
    private String goodsprice;
    private String shipment;
    private String demo;
    private String keytype; // 	1：商城订单 2：抢购订单 3：预定订单 4：套餐订单 5：兑换订单
    private String clientname;
    private String tel;//收货人电话
    private String address;//收货地址
    private String fktime;//付款时间
    private String deliveryname;//快递名
    private String deliverynum;//快递单号
    private String regdate;//收货时间
    private String fhtime;//发货时间
    private String scoremoney;//积分抵现金额
    private String goodsnum;//订单中商品个数
    private String packageid;//套餐id
    private String packagename;//套餐名字
    private String packageimgurl;//套餐图片
    private String packageprice;
    private String shopname;
    private String allcoupon;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPackageprice() {
        return packageprice;
    }

    public void setPackageprice(String packageprice) {
        this.packageprice = packageprice;
    }

    private String packageimgurlbig;//套餐图片
    private String  packageshipdays;//发货间隔
    private String packagefhimes;//发货次数
    private String goodsItems;//订单商品详情
    private String goodsItem;
    private String allprice;

    public String getAllprice() {
        return allprice;
    }

    public void setAllprice(String allprice) {
        this.allprice = allprice;
    }

    private ArrayList<CartGoodsInfo> goods = new ArrayList<>();

    public String getScoremoney() {
        return scoremoney;
    }

    public void setScoremoney(String scoremoney) {
        this.scoremoney = scoremoney;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFktime() {
        return fktime;
    }

    public void setFktime(String fktime) {
        this.fktime = fktime;
    }

    public String getDeliveryname() {
        return deliveryname;
    }

    public void setDeliveryname(String deliveryname) {
        this.deliveryname = deliveryname;
    }

    public String getDeliverynum() {
        return deliverynum;
    }

    public void setDeliverynum(String deliverynum) {
        this.deliverynum = deliverynum;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getFhtime() {
        return fhtime;
    }

    public void setFhtime(String fhtime) {
        this.fhtime = fhtime;
    }

    public OrderInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                ordernum = get(jsonObject, "ordernum");
                status = get(jsonObject, "status");
                money = get(jsonObject, "money");
                ordertime = get(jsonObject, "ordertime");
                status = get(jsonObject, "status");
                goodsnum = get(jsonObject, "goodsnum");
                packageid = get(jsonObject, "packageid");
                packagename = get(jsonObject, "packagename");
                packageimgurl = get(jsonObject, "packageimgurl");
                packageimgurlbig = get(jsonObject, "packageimgurlbig");
                ordertime = get(jsonObject, "ordertime");
                packageshipdays = get(jsonObject, "packageshipdays");
                packagefhimes = get(jsonObject, "packagefhimes");
                goodsprice = get(jsonObject, "goodsprice");
                shipment = get(jsonObject, "shipment");
                demo = get(jsonObject, "demo");
                clientname = get(jsonObject, "clientname");
                tel = get(jsonObject, "tel");
                packageprice=get(jsonObject,"packageprice");
                address = get(jsonObject, "address");
                fktime = get(jsonObject, "fktime");
                deliveryname = get(jsonObject, "deliveryname");
                regdate = get(jsonObject, "regdate");
                fhtime = get(jsonObject, "fhtime");
                allprice=get(jsonObject,"allprice");
                keytype=get(jsonObject,"keytype");
                shopname=get(jsonObject,"shopname");
                scoremoney = get(jsonObject, "scoremoney");
                goodsItems = get(jsonObject, "goodsItems");
                goodsItem = get(jsonObject, "goodsItem");
                deliveryname=get(jsonObject,"deliveryname");
                deliverynum=get(jsonObject,"deliverynum");

                allcoupon = get(jsonObject, "allcoupon");

                goods.clear();
                if (!isNull(goodsItems)) {
                    JSONArray array = new JSONArray(goodsItems);
                    for (int i = 0; i < array.length(); i++) {
                        CartGoodsInfo introduction = new CartGoodsInfo(array.getJSONObject(i));
                        goods.add(introduction);
                    }
                }
                if (!isNull(goodsItem)) {
                    JSONArray array = new JSONArray(goodsItem);
                    for (int i = 0; i < array.length(); i++) {
                        CartGoodsInfo introduction = new CartGoodsInfo(array.getJSONObject(i));
                        goods.add(introduction);
                    }
                }
                log_d(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public String getAllcoupon() {
        return allcoupon;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }

    public String getPackageimgurl() {
        return packageimgurl;
    }

    public void setPackageimgurl(String packageimgurl) {
        this.packageimgurl = packageimgurl;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getPackageimgurlbig() {
        return packageimgurlbig;
    }

    public void setPackageimgurlbig(String packageimgurlbig) {
        this.packageimgurlbig = packageimgurlbig;
    }

    public String getPackageshipdays() {
        return packageshipdays;
    }

    public void setPackageshipdays(String packageshipdays) {
        this.packageshipdays = packageshipdays;
    }

    public String getPackagefhimes() {
        return packagefhimes;
    }

    public void setPackagefhimes(String packagefhimes) {
        this.packagefhimes = packagefhimes;
    }

    public String getGoodsItems() {
        return goodsItems;
    }

    public void setGoodsItems(String goodsItems) {
        this.goodsItems = goodsItems;
    }

    public ArrayList<CartGoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<CartGoodsInfo> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id='" + id + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", status='" + status + '\'' +
                ", money='" + money + '\'' +
                ", ordertime='" + ordertime + '\'' +
                ", goodsnum='" + goodsnum + '\'' +
                ", packageid='" + packageid + '\'' +
                ", packagename='" + packagename + '\'' +
                ", packageimgurl='" + packageimgurl + '\'' +
                ", packageimgurlbig='" + packageimgurlbig + '\'' +
                ", packageshipdays='" + packageshipdays + '\'' +
                ", packagefhimes='" + packagefhimes + '\'' +
                ", goodsItems='" + goodsItems + '\'' +
                ", goods=" + goods +
                '}';
    }
}
