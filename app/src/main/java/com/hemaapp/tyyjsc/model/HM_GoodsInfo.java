package com.hemaapp.tyyjsc.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 商品详情
 */
public class HM_GoodsInfo extends XtomObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;//商品id
    private String coupon;//点券
    private String sid;
    private String name;//商品名字
    private String profiles;//子标题
    private String shopimgurl;//商品图片
    private String shopimgurlbig;//商品图片
    private String featuretype;
    private String sum_volume;//商品总销量（客户端显示该字段）
    private String keytype;//商品类型
    private String send_time;//配送时间
    private String shipment;//配送费
    private String vouchers_money;
    private String byprice;
    private String goodsreply;
    private String shopid;
    private String shopname;
    private String shopaddress;
    private String goods_status;
    private String fhimes;
    private String goodsnum;
    private String parameters;//商品参数
    private String star;// 	评论星总数 	1一星2两星3三星4四星5五星
    private String replycount;////评论总数
    private String score;//积分
    private String limit_num;//限时抢购特价预订
    private String full_fee;//满减限额
    private String collect;//是否添加收藏
    private String price;//商品售价
    private String originalprice;//商品原价
    private String displaysales;//展示销量
    private String realsale;//实际销量
    private String injectsale;//注入销量
    private String num;//套餐商品数量
    private String imgurl;//图片
    private String imgurlbig;//图片
    private String buytimememo;//抢购时间段
    private String begintime;//抢购开始时间
    private String endtime;//抢购结束时间
    private String stock;//库存
    private String limitnum;//限购数量
    private String shiptime;//配送时间
    private String waitday;//等待天数
    private String shipdays;//发货间隔
    private String fhtimes;//发货次数
    private String nowtime;//当前时间限时抢购列表用
    private String weight;

    public String getCollect() {
        return collect;
    }
    public void setCollect(String collect) {
        this.collect = collect;
    }
    private String img_list;//商品轮播图
    private ArrayList<HM_ImgInfo> imgs = new ArrayList<HM_ImgInfo>();
    private ArrayList<String> labes = new ArrayList<String>();
    private ArrayList<GoodsBriefIntroduction> packages = new ArrayList<>();
    private ArrayList<GoodsBriefIntroduction> goods = new ArrayList<>();
    private String attr_list;//属性组合
    private ArrayList<AttrInfo> attrs = new ArrayList<AttrInfo>();
    private ArrayList<AttrMixInfo> attrMixInfos = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();//评论
    private String send_price;//运费
    private String is_display = "";//是否显示库存 1 显示 2 隐藏



    public HM_GoodsInfo(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                coupon=get(jsonObject, "coupon");
                sid = get(jsonObject, "sid");
                name = get(jsonObject, "name");
                profiles = get(jsonObject, "profiles");
                price = get(jsonObject, "price");
                originalprice = get(jsonObject, "originalprice");
                imgurl=get(jsonObject,"imgurl");
                imgurlbig=get(jsonObject,"imgurlbig");
                shopimgurl = get(jsonObject, "shopimgurl");
                shopimgurlbig = get(jsonObject, "shopimgurlbig");
                displaysales = get(jsonObject, "displaysales");
                featuretype = get(jsonObject, "featuretype");
                sum_volume = get(jsonObject, "sum_volume");
                collect = get(jsonObject, "collect");
                keytype = get(jsonObject, "keytype");
                stock = get(jsonObject, "stock");
                begintime=get(jsonObject,"begintime");
                endtime=get(jsonObject,"endtime");
                send_time = get(jsonObject, "send_time");
                parameters = get(jsonObject, "parameters");
                replycount = get(jsonObject, "replycount");
                star = get(jsonObject, "star");
                score = get(jsonObject, "score");
                limit_num = get(jsonObject, "limit_num");
                full_fee = get(jsonObject, "full_fee");
                is_display = get(jsonObject, "is_display");
                img_list = get(jsonObject, "imgItems");
                send_price = get(jsonObject, "send_price");
                shipment = get(jsonObject, "shipment");
                vouchers_money = get(jsonObject, "vouchers_money");
                byprice = get(jsonObject, "byprice");
                goodsreply = get(jsonObject, "goodsreply");
                replycount = get(jsonObject, "replycount");
                shopid = get(jsonObject, "shopid");
                shopname = get(jsonObject, "shopname");
                shopaddress = get(jsonObject, "shopaddress");
                nowtime = get(jsonObject, "nowtime");
                limitnum = get(jsonObject, "limitnum");
                goods_status = get(jsonObject, "goods_status");
                shiptime = get(jsonObject, "shiptime");
                waitday = get(jsonObject, "waitday");
                shipdays = get(jsonObject, "shipdays");
                fhimes = get(jsonObject, "fhimes");
                goodsnum = get(jsonObject, "goodsnum");
                weight = get(jsonObject, "weight");

                if (!isNull(img_list)) {
                    imgs = new ArrayList<>();
                    JSONArray array = new JSONArray(img_list);
                    log_d("---" + img_list);
                    for (int i = 0; i < array.length(); i++) {
                        HM_ImgInfo imgInfo = new HM_ImgInfo(
                                array.getJSONObject(i));
                        imgs.add(imgInfo);
                    }
                }
                attr_list = get(jsonObject, "propertyinfo");
                if (!isNull(attr_list)) {
                    JSONArray array = new JSONArray(attr_list);
                    for (int i = 0; i < array.length(); i++) {
                        attrs.add(new AttrInfo(array.getJSONObject(i)));
                    }
                }
                log_d(toString());
            } catch (Exception e) {
                throw new DataParseException(e);
            }
        }
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }

    public String getShopimgurl() {
        return shopimgurl;
    }

    public void setShopimgurl(String shopimgurl) {
        this.shopimgurl = shopimgurl;
    }

    public String getShopimgurlbig() {
        return shopimgurlbig;
    }

    public void setShopimgurlbig(String shopimgurlbig) {
        this.shopimgurlbig = shopimgurlbig;
    }

    public String getFeaturetype() {
        return featuretype;
    }

    public void setFeaturetype(String featuretype) {
        this.featuretype = featuretype;
    }

    public String getSum_volume() {
        return sum_volume;
    }

    public String getWeight() {
        return weight;
    }

    @Override

    public String toString() {
        return "HM_GoodsInfo{" +
                "id='" + id + '\'' +
                ", coupon='" + coupon + '\'' +
                ", sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", profiles='" + profiles + '\'' +
                ", shopimgurl='" + shopimgurl + '\'' +
                ", shopimgurlbig='" + shopimgurlbig + '\'' +
                ", featuretype='" + featuretype + '\'' +
                ", sum_volume='" + sum_volume + '\'' +
                ", keytype='" + keytype + '\'' +
                ", send_time='" + send_time + '\'' +
                ", shipment='" + shipment + '\'' +
                ", vouchers_money='" + vouchers_money + '\'' +
                ", byprice='" + byprice + '\'' +
                ", goodsreply='" + goodsreply + '\'' +
                ", shopid='" + shopid + '\'' +
                ", shopname='" + shopname + '\'' +
                ", shopaddress='" + shopaddress + '\'' +
                ", goods_status='" + goods_status + '\'' +
                ", fhimes='" + fhimes + '\'' +
                ", goodsnum='" + goodsnum + '\'' +
                ", parameters='" + parameters + '\'' +
                ", star='" + star + '\'' +
                ", replycount='" + replycount + '\'' +
                ", score='" + score + '\'' +
                ", limit_num='" + limit_num + '\'' +
                ", full_fee='" + full_fee + '\'' +
                ", collect='" + collect + '\'' +
                ", price='" + price + '\'' +
                ", originalprice='" + originalprice + '\'' +
                ", displaysales='" + displaysales + '\'' +
                ", realsale='" + realsale + '\'' +
                ", injectsale='" + injectsale + '\'' +
                ", num='" + num + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", imgurlbig='" + imgurlbig + '\'' +
                ", buytimememo='" + buytimememo + '\'' +
                ", begintime='" + begintime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", stock='" + stock + '\'' +
                ", limitnum='" + limitnum + '\'' +
                ", shiptime='" + shiptime + '\'' +
                ", waitday='" + waitday + '\'' +
                ", shipdays='" + shipdays + '\'' +
                ", fhtimes='" + fhtimes + '\'' +
                ", nowtime='" + nowtime + '\'' +
                ", weight='" + weight + '\'' +
                ", img_list='" + img_list + '\'' +
                ", imgs=" + imgs +
                ", labes=" + labes +
                ", packages=" + packages +
                ", goods=" + goods +
                ", attr_list='" + attr_list + '\'' +
                ", attrs=" + attrs +
                ", attrMixInfos=" + attrMixInfos +
                ", comments=" + comments +
                ", send_price='" + send_price + '\'' +
                ", is_display='" + is_display + '\'' +
                '}';
    }

    public void setSum_volume(String sum_volume) {
        this.sum_volume = sum_volume;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getVouchers_money() {
        return vouchers_money;
    }

    public void setVouchers_money(String vouchers_money) {
        this.vouchers_money = vouchers_money;
    }

    public String getByprice() {
        return byprice;
    }

    public void setByprice(String byprice) {
        this.byprice = byprice;
    }

    public String getGoodsreply() {
        return goodsreply;
    }

    public void setGoodsreply(String goodsreply) {
        this.goodsreply = goodsreply;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getFhimes() {
        return fhimes;
    }

    public void setFhimes(String fhimes) {
        this.fhimes = fhimes;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getReplycount() {
        return replycount;
    }

    public void setReplycount(String replycount) {
        this.replycount = replycount;
    }

    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(String limit_num) {
        this.limit_num = limit_num;
    }

    public String getFull_fee() {
        return full_fee;
    }

    public void setFull_fee(String full_fee) {
        this.full_fee = full_fee;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurlbig() {
        return imgurlbig;
    }

    public void setImgurlbig(String imgurlbig) {
        this.imgurlbig = imgurlbig;
    }

    public String getBuytimememo() {
        return buytimememo;
    }

    public void setBuytimememo(String buytimememo) {
        this.buytimememo = buytimememo;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
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

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    public String getImg_list() {
        return img_list;
    }

    public void setImg_list(String img_list) {
        this.img_list = img_list;
    }

    public ArrayList<HM_ImgInfo> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<HM_ImgInfo> imgs) {
        this.imgs = imgs;
    }

    public ArrayList<String> getLabes() {
        return labes;
    }

    public void setLabes(ArrayList<String> labes) {
        this.labes = labes;
    }

    public ArrayList<GoodsBriefIntroduction> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<GoodsBriefIntroduction> packages) {
        this.packages = packages;
    }

    public ArrayList<GoodsBriefIntroduction> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<GoodsBriefIntroduction> goods) {
        this.goods = goods;
    }

    public String getAttr_list() {
        return attr_list;
    }

    public void setAttr_list(String attr_list) {
        this.attr_list = attr_list;
    }

    public ArrayList<AttrInfo> getAttrs() {
        return attrs;
    }

    public void setAttrs(ArrayList<AttrInfo> attrs) {
        this.attrs = attrs;
    }

    public ArrayList<AttrMixInfo> getAttrMixInfos() {
        return attrMixInfos;
    }

    public void setAttrMixInfos(ArrayList<AttrMixInfo> attrMixInfos) {
        this.attrMixInfos = attrMixInfos;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getSid() {
        return sid;
    }

    public String getSend_price() {
        return send_price;
    }

    public void setSend_price(String send_price) {
        this.send_price = send_price;
    }

    public String getIs_display() {
        return is_display;
    }

    public void setIs_display(String is_display) {
        this.is_display = is_display;
    }
}
