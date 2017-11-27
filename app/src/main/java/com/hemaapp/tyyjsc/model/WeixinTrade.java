package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 微信交易签名串
 */
public class WeixinTrade extends XtomObject implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6013096570500672617L;
	private String appid;// 公众账号ID 微信分配的公众账号ID
	private String partnerid;// 商户号 微信支付分配的商户号
	private String prepayid;// 预支付交易会话ID 微信返回的支付交易会话ID
	private String packageValue;// 扩展字段 暂填写固定值Sign=WXPay
	private String noncestr;// 随机字符串 随机字符串，不长于32位。
	private String timestamp;// 时间戳 时间戳
	private String sign;// 签名

	public WeixinTrade(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				appid = get(jsonObject, "appid");
				partnerid = get(jsonObject, "partnerid");
				prepayid = get(jsonObject, "prepayid");
				packageValue = get(jsonObject, "package");
				noncestr = get(jsonObject, "noncestr");
				timestamp = get(jsonObject, "timestamp");
				sign = get(jsonObject, "sign");

				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "WeixinTrade [appid=" + appid + ", partnerid=" + partnerid
				+ ", prepayid=" + prepayid + ", packageValue=" + packageValue
				+ ", noncestr=" + noncestr + ", timestamp=" + timestamp
				+ ", sign=" + sign + "]";
	}

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @return the partnerid
	 */
	public String getPartnerid() {
		return partnerid;
	}

	/**
	 * @return the prepayid
	 */
	public String getPrepayid() {
		return prepayid;
	}

	/**
	 * @return the packageValue
	 */
	public String getPackageValue() {
		return packageValue;
	}

	/**
	 * @return the noncestr
	 */
	public String getNoncestr() {
		return noncestr;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

}
