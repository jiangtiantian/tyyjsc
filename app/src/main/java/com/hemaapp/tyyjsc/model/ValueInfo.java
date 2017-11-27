package com.hemaapp.tyyjsc.model;


import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

public class ValueInfo extends XtomObject {

    private String v_two;
    private String v_three;

    private String rankOne = "1";
    private String rankTwo = "1";
    private String rankThree = "1";

    public ValueInfo(JSONObject jsonObject) throws DataParseException {
        if(jsonObject != null){
            try {
                v_two = get(jsonObject, "v_two");
                v_three = get(jsonObject, "v_three");
                if(!isNull(v_two)){
                    String [] ranks = v_two.split("~");
                    if(ranks.length == 2){
                        rankOne = ranks[0].trim();
                        rankTwo = ranks[1].trim();
                    }else{
                        rankOne = "1";
                        rankTwo = "1";
                    }
                }
                if(!isNull(v_three)){
                    String [] ranks = v_three.split("~");
                    if(ranks.length == 2){
                        rankTwo = ranks[0].trim();
                        rankThree = ranks[1].trim();
                    }else{
                        rankTwo = "1";
                        rankThree = "1";
                    }
                }

                log_d(toString());
            } catch (JSONException e) {
               throw new DataParseException(e);
            }
        }
    }

    public ValueInfo(String rankOne, String rankTwo, String rankThree){
        this.rankOne = rankOne;
        this.rankTwo = rankTwo;
        this.rankThree = rankThree;
    }

    public int getRankOne() {
        return (int)Double.parseDouble(rankOne) == 0 ? 1 : (int)Double.parseDouble(rankOne);
    }

    public int getRankTwo() {
        return (int)Double.parseDouble(rankTwo) == 0 ? 1 : (int)Double.parseDouble(rankTwo);
    }

    public int getRankThree() {
        return (int)Double.parseDouble(rankThree) == 0 ? 1 : (int)Double.parseDouble(rankThree);
    }

    @Override
    public String toString() {
        return "ValueInfo{" +
                "v_two='" + v_two + '\'' +
                ", v_three='" + v_three + '\'' +
                ", rankOne='" + rankOne + '\'' +
                ", rankTwo='" + rankTwo + '\'' +
                ", rankThree='" + rankThree + '\'' +
                '}';
    }
}
