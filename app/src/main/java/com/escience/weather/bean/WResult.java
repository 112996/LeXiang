package com.escience.weather.bean;

import android.util.Log;

import com.escience.weather.Network.JsonConvert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Stark on 2017/11/28.
 */
public class WResult {
    public  String resultcode;
    public   String reason;
    public   int error_code;
    public  HashMap<String,Object> result;
    public WSK getSK() throws IllegalAccessException{
        return (WSK)JsonConvert.MapToBean(new WSK(), (HashMap) result.get("sk"));
    }
    public WToday getToday() throws IllegalAccessException{
        return (WToday)JsonConvert.MapToBean(new WToday(), (HashMap) result.get("today"));
    }
    public WF getFuture(String str,int i) throws IllegalAccessException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date;
        try {
            date =  format.parse(str);
        }catch (Exception e){
            Log.e("date",e.toString());
            date=new Date();
        }
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        SimpleDateFormat format1=new SimpleDateFormat("yyyyMMdd");
        HashMap map=(HashMap) result.get("future");
        return (WF)JsonConvert.MapToBean(new WF(),(HashMap)map.get("day_"+format1.format(cal.getTime())));
    }
}
