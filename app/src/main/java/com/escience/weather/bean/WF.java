package com.escience.weather.bean;

import com.escience.weather.R;

import java.util.HashMap;

/**
 * Created by Stark on 2017/11/28.
 */
public class WF {
    public String temperature;                                     //"28℃~36℃"
    public String weather;                                             //"晴转多云"
    public HashMap<String,String> weather_id;
    public String wind;                                                    //"南风3-4级"
    public String week;                                                    //"星期一"
    public String date;                                                        //"20140804"
    public String getLow() {
        return temperature.substring(0,temperature.indexOf("℃"))+"°";
    }
    public String getHigh(){
        return temperature.substring(temperature.indexOf("~")+1,temperature.lastIndexOf("℃"))+"°";
    }
    public int getR(){
        int a=Integer.parseInt(weather_id.get("fa"));
        int b=Integer.parseInt(weather_id.get("fb"));
        a=a>b?a:b;
        if(a==0){
            return R.drawable.qing;
        }else if(a==1) {
            return R.drawable.duoyun;
        }else if(a==2) {
            return R.drawable.yin;
        }else if(a<12){
            return R.drawable.yu;
        }else if(a<17){
            return R.drawable.xue;
        }else{
            return R.drawable.yu;
        }
    }
}
