package com.hemaapp.tyyjsc.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;

/**
 * 时间段
 */

public class TimeInfo extends XtomObject implements Serializable{

    private String id;//时间段编号
    private String name;//名字
    private String start_time;//开始时间
    private String end_time;//结束时间
    private String nowtime;//当前服务器时间
    private String nowTime;//当前服务器时间
    private String sort;//类别

    private int status;//0 抢购中；  -1 即将开始; 1 已结束
    private int pos = 0;//位置

    public TimeInfo(JSONObject jsonObject){
        if(jsonObject != null){
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                start_time = get(jsonObject, "start_time");
                end_time = get(jsonObject, "end_time");
                sort = get(jsonObject, "sort");
                nowtime = get(jsonObject, "nowtime");
                nowTime = get(jsonObject, "nowTime");

                log_d(toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getSort() {
        return sort;
    }


    public String getNowtime() {
        return nowtime;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "TimeInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", nowtime='" + nowtime + '\'' +
                ", nowTime='" + nowTime + '\'' +
                ", sort='" + sort + '\'' +
                ", status=" + status +
                ", pos=" + pos +
                '}';
    }
}
