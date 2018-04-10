package com.escience.weather.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Stark on 2017/8/31.
 */
public class NetBuilder {
    public Map<String,Object> Config;
    public Map<String,Object> Package;
    public List<Object> Data;
    public NetBuilder(){
        Config=new HashMap<String, Object>() ;
        Package=new HashMap<String,Object>();
        Data=new ArrayList<Object>();
    }
    public NetBuilder(String json)throws IllegalAccessException,JSONException{
        Log.e("json",json);
        NetBuilder temp=(NetBuilder)JsonConvert.DeserializeObject(json,new NetBuilder());
        Config=temp.Config;
        Package=temp.Package;
        Data=temp.Data;
    }
    public String get(String key){
        return get(key, null);
    }
    public String get(String key,String Default){
        if(Config.containsKey(key)){
            return Config.get(key).toString();
        }else if(Package.containsKey(key)){
            return Package.get(key).toString();
        }
        return Default;
    }
    public int getInt(String key,int Default){
        String temp=get(key);
        if(temp==null){
            return Default;
        }else{
            return Integer.parseInt(temp);
        }
    }
    public Long getLong(String key,Long Default){
        String temp=get(key);
        if(temp==null){
            return Default;
        }else{
            return Long.parseLong(temp);
        }
    }
    public boolean getBool(String key,boolean Default){
        String temp=get(key);
        if(temp==null){
            return Default;
        }else{
            return Boolean.parseBoolean(temp);
        }
    }
    public Object getList(int i){
        return Data.get(i);
    }
    public int ListSize(){
        return Data.size();
    }
    public NetBuilder add(String key,Object value){
        Config.put(key,value);
        return this;
    }
    public NetBuilder put(String key,Object value){
        Package.put(key, value);
        return this;
    }
    public NetBuilder in(Map pkg){
        Data.add(pkg);
        return this;
    }
    public String build(Context context) throws IllegalAccessException,JSONException{
        SharedPreferences sp=context.getSharedPreferences("action",Context.MODE_PRIVATE);
        String uuid=sp.getString("uuid",null);
        if(uuid==null||uuid.equals("")){
            uuid=new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA).format(new Date());
            sp.edit().putString("uuid",uuid).apply();
        }
        add("sponsor", uuid);
        return build();
    }
    public String build() throws IllegalAccessException,JSONException{
        return JsonConvert.SerializeObject(this);
    }
    public String getMsgcode(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA).format(new Date());
    }
}