package com.escience.weather.bean;

import java.util.HashMap;

/**
 * Created by Stark on 2017/11/28.
 */
public class WToday {
    public  String city;                                                    //"天津"
    public   String date_y;                                              //"2014年03月21日",
    public   String week;                                                 //"星期五",
    public    String temperature;                                   //"8℃~20℃"/*今日温度*/
    public  String weather;                                            //"晴转霾"/*今日天气*/
    public     HashMap<String,String> weather_id;   /*天气唯一标识*/
    public     String wind;                                                   //"西南风微风",
    public     String dressing_index;                               //"较冷", /*穿衣指数*/
    public     String dressing_advice;                             //"建议着大衣、呢外套加毛衣、卫衣等服装。",/*穿衣建议*/
    public     String uv_index;                                             //"中等"/*紫外线强度*/
    public     String comfort_index;                                   //"",/*舒适度指数*/
    public     String wash_index;                                        //"较适宜",/*洗车指数*/
    public     String travel_index;                                       //"适宜"/*旅游指数*/
    public     String exercise_index;                                  // "较适宜"/*晨练指数*/
    public    String drying_index;                                       ///*干燥指数*/
    public String getDate() {
        return date_y.substring(0,4)+date_y.substring(5,7)+date_y.substring(8,10);
    }
    public String getLow() {
        return temperature.substring(0,temperature.indexOf("℃"));
    }
    public String getHigh(){
        return temperature.substring(temperature.indexOf("~")+1,temperature.lastIndexOf("℃"));
    }
    public String getMid(){
        return (Integer.parseInt(getLow())+Integer.parseInt(getHigh()))/2+"°";
    }
}
