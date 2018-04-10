package com.escience.weather.bean;

import android.database.Cursor;

import com.escience.weather.R;
import com.escience.weather.Util.DateUtil;

import java.util.HashMap;

/**

 * Created by Stark on 2017/3/7.
 */
public class ItemMood extends BaseItem{
    private String sponsor;
    private String msg;
    private String head;
    private String msgCode;
    private String msgCode2;
    private String place;
    private String nick;
    private String sex;
    private String anum;
    private String time;
    public ItemMood(int itemType, HashMap msg){
        super(itemType);
        this.place=(String)msg.get("place");
        this.sponsor=(String)msg.get("sponsor");
        this.head=(String)msg.get("head");
        this.nick=(String)msg.get("nick");
        this.msg=(String)msg.get("msg");
        this.msgCode=(String)msg.get("msgcode");
        this.msgCode2=(String)msg.get("msgcode2");
        this.anum=(String)msg.get("anum");
        this.time=msgCode==null? DateUtil.MtoNT(msgCode2): DateUtil.MtoNT(msgCode);
        this.sex=(String)msg.get("sex");
    }
    public ItemMood(int itemType, Cursor msg){
        super(itemType);
        this.place=msg.getString(msg.getColumnIndex("place"));
        this.sponsor=msg.getString(msg.getColumnIndex("sponsor"));
        this.nick=msg.getString(msg.getColumnIndex("nick"));
        this.msg=msg.getString(msg.getColumnIndex("msg"));
        this.msgCode=msg.getString(msg.getColumnIndex("msgcode"));
        this.msgCode2=msg.getString(msg.getColumnIndex("msgcode2"));
        this.anum=msg.getString(msg.getColumnIndex("anum"));
        this.time=msgCode==null? DateUtil.MtoNT(msgCode2): DateUtil.MtoNT(msgCode);
    }
    public String getPlace(){return this.place;}
    public String getSponsor(){
        return this.sponsor;
    }
    public String getHead(){
        return "http://kwall.cn/weather/head/"+head;
    }
    public String getNick(){
        if(this.nick==null){
            return this.place+"用户";
        }
        return this.nick;
    }
    public String getMsg() {
        return this.msg;
    }
    public String getMsgCode() {
        return this.msgCode;
    }
    public String getMsgCode2() {
        return this.msgCode2;
    }
    public String getAnum(){
        return this.anum;
    }

    public int getSex(){
        if(sex!=null&&sex.equals("1")){
            return R.drawable.ic_boy;
        }else{
            return R.drawable.ic_girl;
        }
    }
    public String getTime(){
        return this.time;
    }

    public void setAnum(String anum) {
        this.anum = anum;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
